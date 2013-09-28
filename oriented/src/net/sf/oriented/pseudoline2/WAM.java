/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 * General back tracking design notes:
 * 
 * basic inspriation is the warren machine with four ports;
 * - call: takes next entry in call stack, records current trail ptr in call stack, does first option of call
 * - backto: undoes trail
 * - retry: takes next option of head of call stack
 * - fail: pop calls stack
 * with fifth port:
 * - expand: adds a new call to an empty call stack
 * 
 * this class keeps a call stack and a trail.
 * 
 * two methods {? and ?} are used for generating new entries for the call stack
 * 
 * currently the call stack is a bit naive forward looking, in that either there
 * are no entries or there is one.
 * 
 * if there are no entries then one of the two methods is used to generate the
 * next entry, if none is forthcoming then we have failed.
 * 
 * after every call or retry we check for success
 * 
 * it may be the case that the search finds two twisted graphs where the latter
 * is a proper subgraph of the former, this needs to be explicitly checked for
 * 
 * each call stack entry has a pointer to the trail before the call
 * 
 * when we backtrack to a call stack entry, we undo the trail
 * 
 * trail changes include changes to the twisted graph and the parent tension
 * graph
 * 
 * 
 * 
 * 
 * @author jeremycarroll
 * 
 */
public class WAM {

    private final class True extends Frame {
        @Override
        public String toString() {
            return "true";
        }

        @Override
        boolean call(int counter) {
            fail();
            return true;
        }
    }

    private abstract class Choice<T> extends Frame {
        private final T[] choices;

        private Choice(T[] opt) {
            this.choices = opt;
        }

        @Override
        public String toString() {
            return retryCount + "/" + (choices.length) + " "
                    + label(retryCount);
        }

        private String label(int ix) {
            if (ix < choices.length) {
                return choices[ix].toString();
            }
            return "";
        }

        @Override
        boolean call(int ix) {
            if (!removeChoice(ix - 1)) {
                fail();
                return false;
            }
            if (ix >= choices.length - 1) {
                fail();
            }
            return ix < choices.length && makeChoice(choices[ix]);
        }

        abstract boolean makeChoice(T a);

        abstract boolean decideAgainst(T a);

        /**
         * 
         * @param ix
         * @return true if operation omitted or successful, false to force
         *         backtracking
         */
        private boolean removeChoice(int ix) {
            if (ix == -1) {
                return true;
            }
            if (ix < choices.length) {
                boolean rslt = decideAgainst(choices[ix]);
                this.trailPtr = trail.size();
                return rslt;
            }
            return true;
        }
    }

    private final class ChoiceOfVertex extends Choice<TGVertex> {

        ChoiceOfVertex() {
            super(shrinking.getVertices().toArray(new TGVertex[0]));
        }

        @Override
        boolean makeChoice(TGVertex a) {
            return growing.addWithConsequences(a);
        }

        @Override
        boolean decideAgainst(TGVertex a) {
            boolean rslt = maybeRemove(a);
            if (!rslt) {
                fail();
            }
            return rslt;
        }

    }

    private final class ChoiceOfEdge extends Choice<TGEdge> {
        private final EdgeChoices opt;

        private ChoiceOfEdge(EdgeChoices opt) {
            super(opt.choices.toArray(new TGEdge[0]));
            this.opt = opt;
        }

        @Override
        boolean makeChoice(TGEdge a) {
            if (opt.alreadyDone()) {
                // this choice is now moot - as a result of choice removal
                fail(); // don't retry further
                return true;
            }
            return add(a);
        }

        @Override
        boolean decideAgainst(TGEdge a) {
            return maybeRemove(a);
        }
    }

    private final class Failure extends Frame {
        @Override
        boolean call(int counter) {
            fail();
            return false;
        }

        @Override
        public String toString() {
            return "fail";
        }
    }

    private enum Port {
        Call, Redo, Fail
    }

    private abstract class Frame {

        Port port = Port.Call;
        int trailPtr;
        int retryCount = 0;

        final boolean call() {
            trailPtr = trail.size();
            port = Port.Redo;
            return retry();
        }

        final boolean retry() {
            return call(retryCount++);
        }

        abstract boolean call(int counter);

        protected void fail() {
            port = Port.Fail;
        }
    }

    private abstract class Undoable {
        public abstract void undo();
    }

    private final TensionGraph base;
    /**
     * The current solution space is the set of graphs between growing and
     * shrinking. As the algorithm progresses these get closer together until we
     * hit success!
     */
    private final GrowingGraph growing;
    final ShrinkingGraph shrinking;
    private final Deque<Frame> stack = new ArrayDeque<Frame>();
    private final Deque<Undoable> trail = new ArrayDeque<Undoable>();
    private final List<Difficulty> results = new ArrayList<Difficulty>();
    final Deque<EdgeChoices> choices = new ArrayDeque<EdgeChoices>();

    // tracing and debug fields
    public int transitions = 0;
    private AbstractTGraph expected;
    public boolean debug;

    public WAM(TensionGraph b) {
        base = b;
        shrinking = new ShrinkingGraph(this, base);
        growing = shrinking.growing;
    }

    /**
     * 
     * @param tension
     * @return true if operation omitted or successful, false to force
     *         backtracking
     */
    boolean maybeRemove(TGEdge tension) {
        if (shrinking.containsEdge(tension)) {
            return remove(tension);
        }
        return true;
    }

    /**
     * 
     * @param v
     * @return true if operation omitted or successful, false to force
     *         backtracking
     */
    boolean maybeRemove(TGVertex v) {
        if (shrinking.containsVertex(v)) {
            return remove(v);
        }
        return true;
    }
    public List<Difficulty> search() {
        addChoiceOfInitialTGVertex();
        while (true) {
            Frame top = stack.peek();
            this.debugMsg(top.port.name());
            switch (top.port) {
            case Call:
                if (top.call()) {
                    if (success()) {
                        backTrack();
                    } else {
                        extend();
                    }
                } else {
                    backTrack();
                }
                break;
            case Redo:
                if (top.retry()) {
                    if (success()) {
                        backTrack();
                    } else {
                        extend();
                    }
                } else {
                    backTrack();
                }
                break;
            case Fail:
                stack.pop();
                if (stack.isEmpty()) return minimalResults();
                backTrack();
                break;
            }
        }
    }

    private List<Difficulty> minimalResults() {
        Difficulty r[] = new Difficulty[results.size()];
        results.toArray(r);
        int i = 0;
        int sz = r.length;
        
        while (i<sz-1) {
            Difficulty di = r[i++];
            int isz = di.rslt.getEdgeCount();
            int j = i;
            while (j<sz) {
                Difficulty dj = r[j++];
                int jsz = dj.rslt.getEdgeCount();
                if (jsz <= isz && this.subGraph(dj.rslt, di.rslt)) {
                    System.arraycopy(r, j, r, j-1, sz-j);
                    sz--;
                    continue;
                }
                if (subGraph(di.rslt,dj.rslt)) {
                    System.arraycopy(r, i, r, i-1, sz-i);
                    sz--;
                    break;
                }
            }
        }
        return Arrays.asList(r).subList(0, sz);
    }

    /**
     * Every solution has at least one vertex from the base. We choose that
     * vertex first, then we add edges until we have a twisted graph or failure.
     * This method adds the choicepoint for the first vertex. We go with larger
     * vertices first.
     */
    private void addChoiceOfInitialTGVertex() {
        stack.push(new ChoiceOfVertex());
    }

    void addChoice(final EdgeChoices opt) {
        choices.push(opt);

        trail.push(new Undoable() {
            @Override
            public void undo() {
                choices.remove(opt);
                opt.forgetChoiceInEdges();
            }
        });
    }

    public boolean hasOptions() {
        return !choices.isEmpty();
    }

    private boolean success() {
        boolean success = growing.isTwistedGraph();
        if (success) {
            results.add(new Difficulty(growing));
            // growing.dumpEdges();
        }
        return success;
    }

    private void extend() {
        if (hasOptions()) {
            pushChoiceFromEdge(selectNextEdgeChoice());
        } else {
            throw new IllegalStateException("Not possible");
            // final TGEdge t = findPossibleEdge();
            // if (t==null) {
            // stack.push(new Failure());
            // } else {
            // stack.push(new Frame(){
            // @Override
            // boolean call(int ix) {
            // switch(ix) {
            // case 0:
            // return add(t);
            // case 1:
            // fail();
            // return remove(t);
            // default:
            // throw new IllegalArgumentException("WAM call logic error");
            // }
            // }
            // @Override
            // public String toString() {
            // switch (this.retryCount) {
            // case 0:
            // return "add "+t.toString();
            // case 1:
            // return "[-add] "+t.toString();
            // case 2:
            // return "fail [add] "+t.toString();
            // default:
            // throw new IllegalArgumentException("WAM call logic error");
            // }
            // }
            // });
            // }
        }
    }

    private EdgeChoices selectNextEdgeChoice() {
        Iterator<EdgeChoices> it = choices.iterator();
        EdgeChoices best = it.next();
        int bestSize = best.size();
        while (it.hasNext()) {
            EdgeChoices n = it.next();
            if (n.size() < bestSize) {
                bestSize = n.size();
                best = n;
            }
        }
        return best;
    }

    private void pushChoiceFromEdge(final EdgeChoices opt) {
        choices.remove(opt);
        trail.push(new Undoable() {
            @Override
            public void undo() {
                choices.push(opt);
            }
        });
        // opt.prepareChoices(growing,shrinking);
        if (opt.impossible()) {
            stack.push(new Failure());
        }
        if (opt.alreadyDone()) {
            stack.push(new True());
        } else {
            stack.push(new ChoiceOfEdge(opt));
        }
    }

    private void debugMsg(String port) {
        transitions++;
        if (debug)
            System.err.println(port + "[" + stack.size() + "/" + trail.size()
                    + ":" + choices.size() + "] " + pad(stack) + pad(trail)
                    + pad(choices) + stack.peek().toString());
    }

    private String pad(Collection<?> c) {
        int size = c.size();
        return size < 10 ? "  " : (size < 100 ? " " : "");
    }

    boolean edgeRemovalFailed;

    /**
     * Remove an edge from the current solution space.
     * @param t
     * @return true if removal was ok, false to force backtracking
     */
    boolean remove(final TGEdge t) {
        edgeRemovalFailed = false;
        shrinking.removeEdge(t);
        growing.edgeHasBeenRemoved(t);
        Set<TGVertex> vv = new HashSet<TGVertex>();
        vv.add(t.source);
        vv.add(t.dest);
        shrinking.prune(vv, true);
        return !edgeRemovalFailed;
    }

    void trailRemove(final TGEdge t) {
        trail.push(new Undoable() {

            @Override
            public void undo() {
                shrinking.addEdge(t, t.source, t.dest);
            }
        });

    }

    /**
     * Add the edge to the current solution space.
     * @param tension
     * @return
     */
    protected boolean add(TGEdge tension) {
        return growing.containsEdge(tension)  // TODO remove first possibility - inefficient
                || ( shrinking.containsEdge(tension)
                && growing.addWithConsequences(tension));
    }

    private void backTrack() {
        debugMsg("back");
        while (trail.size() > stack.peek().trailPtr) {
            trail.pop().undo();
        }
    }

    // private TGEdge findPossibleEdge() {
    // Iterator<TGEdge> it =
    // Arrays.asList(shrinking.sortedEdges()).
    // // shrinking.getEdges().
    // iterator();
    // TGEdge t;
    // while (true) {
    // if (!it.hasNext()) {
    // return null;
    // }
    // t = it.next();
    // if (!growing.containsEdge(t)) {
    // return t;
    // }
    // }
    // }

    public void pushRemoveUndoingAdd(final GrowingGraph gg, final TGEdge t) {
        trail.push(new Undoable() {
            @Override
            public void undo() {
                TGVertex v1 = gg.getSource(t);
                TGVertex v2 = gg.getDest(t);

                if (!gg.removeEdge(t)) {
                    throw new IllegalStateException("Failed to remove edge");
                }
                if (gg.getNeighborCount(v1) == 0) {
                    gg.removeVertex(v1);
                }
                if (gg.getNeighborCount(v2) == 0) {
                    gg.removeVertex(v2);
                }
            }
        });
    }

    public void pushRemoveUndoingAdd(final GrowingGraph gg, final TGVertex v1) {
        trail.push(new Undoable() {
            @Override
            public void undo() {
                if (!gg.containsVertex(v1)) {
                    return;
                }
                if (gg.getNeighborCount(v1) != 0) {
                    throw new IllegalStateException("backtracking problem");
                }
                gg.removeVertex(v1);
            }
        });
    }

    public void setDebugExpected(AbstractTGraph expected) {
        this.expected = expected;
        this.debug = true;
    }

    boolean debugLookingGood() {
        return expected == null || subGraph(this.growing, expected)
                && subGraph(expected, shrinking);
    }

    private boolean subGraph(AbstractTGraph small, AbstractTGraph big) {
        for (TGEdge edge : small.getEdges()) {
            if (small.getSource(edge) != big.getSource(edge)) {
                return false;
            }
            if (small.getDest(edge) != big.getDest(edge)) {
                return false;
            }
        }
        return true;
    }

    public void pushAddUndoingRemove(final Set<TGEdge> allChoices,
            final TGEdge t) {
        trail.push(new Undoable() {
            @Override
            public void undo() {
                allChoices.add(t);
            }
        });
    }

    private boolean remove(TGVertex a) {
        List<TGEdge> es = new ArrayList<TGEdge>(shrinking.getIncidentEdges(a));
        for (TGEdge e : es) {
            if (!maybeRemove(e)) {
                return false;
            }
        }
        if (shrinking.containsVertex(a)) {
            throw new IllegalStateException("vertex removal logic");
        }
        return true;
    }

}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * The Java Oriented Matroid Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Java Oriented Matroid Library. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

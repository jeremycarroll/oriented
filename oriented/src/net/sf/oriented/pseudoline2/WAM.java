/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import edu.uci.ics.jung.graph.Graph;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.SetOfUnsignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;

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
    
    private static ObjectOutputStream TEST_SETS_FILE;
    static {
        try {
            TEST_SETS_FILE = new ObjectOutputStream(new FileOutputStream("/tmp/TestData.java"));
        }
        catch (IOException e) {
            throw new Error(e);
        }
    }
    
    private final static boolean DETERMINISTIC = false;
    /**
     * Some particular twisted graph is hard-coded as the topic of the debugging.
     */
    private final static boolean DEBUG = false;
    private final static int DEBUG_FREQUENCY = 1000000;

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
        @Override
        int optionCount() {
            return 1;
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
        int optionCount() {
            return choices.length;
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
            super(maybeSort(shrinking.getVertices().toArray(new TGVertex[0])));
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

        private ChoiceOfEdge(EdgeChoices opt, ShrinkingGraph sg) {
            super(maybeSort(sg.filter(opt.choices.toArray(new TGEdge[0]))));
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

        @Override
        int optionCount() {
            return 1;
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
        
        abstract int optionCount();
        
        
    }

    private static <T extends Comparable<T>> T[] maybeSort(T[] array) {
        if (DETERMINISTIC) Arrays.sort(array);
        return array;
    }
    public static abstract class Undoable {
        public abstract void undo();
    }

    final TensionGraph base;
    /**
     * The current solution space is the set of graphs between growing and
     * shrinking. As the algorithm progresses these get closer together until we
     * hit success!
     */
    private final GrowingGraph growing;
    final ShrinkingGraph shrinking;
    private final Deque<Frame> stack = new ArrayDeque<Frame>();
    final Deque<Undoable> trail = new ArrayDeque<Undoable>();
    private final List<Difficulty> results = new ArrayList<Difficulty>();
    final Deque<EdgeChoices> choices = new ArrayDeque<EdgeChoices>();

    // tracing and debug fields
    public long transitions = 0;
    private AbstractTGraph expected;
    public boolean debug = true;
    private Map<TGVertex,EdgeChoices> v2choice = new HashMap<TGVertex,EdgeChoices>();

    public WAM(TensionGraph b) {
        base = b;
        shrinking = new ShrinkingGraph(this, base);
        growing = shrinking.growing;
        OMasChirotope om = b.pseudolines.getEquivalentOM().getChirotope();
        for (Six sx: Sixes.get().analyze(om) ) {
            Difficulty d = sx.alignAndRegister(base, om);
            if (d != null) {
                results.add(d);
            }
        }
        sixDifficultyCount = results.size();
        if (DEBUG) {
            String findMe[][] = {
                    {"4","6","7"}, // a
                    {"3","5","7"}, // b
                    {"5","6","8"},
                    {"1","7","8"},
                    {"1","3","4"}, // c
                    
            };
            SetOfUnsignedSet findMe2 = ffactory().setsOfUnsignedSet().copyBackingCollection(
                    Iterables.transform(Arrays.asList(findMe), new Function<String[], UnsignedSet>(){
                        @Override
                        public UnsignedSet apply(String[] input) {
                            return ffactory().unsignedSets().copyBackingCollection(Iterables.transform(Arrays.asList(input),new Function<String,Label>(){
                                @Override
                                public Label apply(String l) {
                                    return ffactory().labels().parse(l);
                                }
                            }));
                        }

                    }));
            PrunableGraph debugTarget = new PrunableGraph();
            for (TGVertex v:b.getVertices()) {
                if (findMe2.contains(v.getId().support())) {
                    debugTarget.addVertex(v);
                }
            }
            for (TGVertex v:debugTarget.getVertices()) {
                for (TGVertex w:debugTarget.getVertices()) {
                    TGEdge e = b.findEdge(v, w);
                    if (e != null) {
                        debugTarget.addEdge(e,v,w);
                    }
                }
            }
            setDebugExpected(debugTarget);
            
        }
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
    public Difficulty[][] search() {
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
                        if (!extend()) {
                            backTrack();
                        }
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
                        if (!extend()) {
                            backTrack();
                        }
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

    private Difficulty[][] minimalResults() {
        System.err.println("Original difficulty count: "+results.size());
        Difficulty r[] = new Difficulty[results.size()];
//        int unnecessary = 0;
        results.toArray(r);
        
        int sz = r.length;
        int bad = 0;
        int done = 0;
        Arrays.sort(r, new Comparator<Difficulty>(){

            @Override
            public int compare(Difficulty o1, Difficulty o2) {
                return o1.bits.cardinality() - o2.bits.cardinality();
            }});
        for (int i = 0;i<sz-1; i++ ) {
            Difficulty di = r[i];
            if (di.bits.get(0)) {
                continue;
            }
            for (int j=i+1;j<sz;j++) {
                done++;
                if ((done % 10000000) == 0) {
                    System.err.println("Sorting: "+(done / 10000000)+ " " + i + ", "+ j + " / " + sz);
                }
                Difficulty dj = r[j];
                if (dj.bits.get(0)) {
                    continue;
                }
                if (!di.bits.intersects(dj.missingBits)) {
                    dj.bits.set(0);
                    bad++;
                }
            }
            
        }
                    
        Difficulty rr[][] = new Difficulty[][]{new Difficulty[sz-bad], 
//                                               new Difficulty[unnecessary],
                                               new Difficulty[bad]};
        
        int j=0;
        int k=0;
//        int l=0;
        for (int i=0;i<r.length;i++) {
            if (!r[i].bits.get(0)) {
//                if (r[i].unnecessary != null) {
//                    rr[1][l++] = r[i];
//                } else {
                   rr[0][j++] = r[i];
//                }
            } else {
                rr[1][k++] = r[i];
            }
        }
        if (rr[1].length > 0) {
            try {
                TEST_SETS_FILE.writeInt(r.length);
                for (int i=0;i<r.length;i++) {
                  TEST_SETS_FILE.writeObject(r[i].bits);
                }
                TEST_SETS_FILE.flush();
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
        }
        return rr;
        
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

    void addChoice(final TGVertex v, final EdgeChoices opt) {
        choices.push(opt);
        v2choice .put(v,opt);

        trail.push(new Undoable() {
            @Override
            public void undo() {
                choices.remove(opt);
                v2choice.remove(v);
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
//            if (growing.getEdgeCount()==6) {
//                System.err.println("!@#@");
//            }
//            if (this.isTheOne(growing)) {
//                System.err.println("FOUND!");
//            }
            results.add(new Difficulty(growing, base.totalBits()));
            this.foundDifficultyCount++;
        }
        return success;
    }

    private boolean extend() {
        if (hasOptions()) {
            pushChoiceFromEdge(selectNextEdgeChoice());
            return true;
        } else {
            return false;
        }
    }

    private EdgeChoices selectNextEdgeChoice() {
        Iterator<EdgeChoices> it = choices.iterator();
        EdgeChoices best = it.next();
        int bestSize = best.size();
        // this forced choice stuff gives only a marginal performance improvement.
        if (best.forcedChoice == 0) {
            return best;
        }
        if (best.forcedChoice == 1) {
            bestSize = 1;
        }
        while (it.hasNext()) {
            EdgeChoices n = it.next();
            if (n.forcedChoice == 0) {
                return n;
            }
            if (n.forcedChoice == 1) {
                bestSize = 1;
                best = n;
            }
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
            stack.push(new ChoiceOfEdge(opt, shrinking));
        }
    }

    private void debugMsg(String port) {
        transitions++;
        if (transitions%DEBUG_FREQUENCY != 0) {
            return;
        }
        if (debug) {
            int i=0;
            StringBuilder sb = new StringBuilder();
            Formatter fmt = new Formatter(sb);
            Iterator<Frame> it = stack.descendingIterator();
            while (i<4 && it.hasNext()) {
                Frame f = it.next();
                fmt.format("%3s/%-3s ", f.retryCount, f.optionCount());
                i++;
            }
            fmt.format("%s [S%-3dT%-5dC%-3dR%-7d] %s", 
                           port ,stack.size(), trail.size(), choices.size() , 
                           results.size(), stack.peek().toString());
            fmt.close();
            System.err.println(sb.toString());
        }
        if (stack.size()==6 && trail.size()==74) {
//            System.err.println("!!!");
        }
    }


    boolean edgeRemovalFailed;
    public int foundDifficultyCount = 0;
    public int sixDifficultyCount = 0;

    /**
     * Remove an edge from the current solution space.
     * @param t
     * @return true if removal was ok, false to force backtracking
     */
    boolean remove(final TGEdge t) {
        if (edgeRemovalFailed) {
            return false;
        }
        edgeRemovalFailed = false;
        try {
            shrinking.removeEdge(t);
            growing.edgeHasBeenRemoved(t);
//            if (!t.afterRemove(this)) {
//                return false;
//            }
            Set<TGVertex> vv = new HashSet<TGVertex>();
            vv.add(t.source);
            vv.add(t.dest);
            shrinking.prune(vv, true);
            return !edgeRemovalFailed;
        }
        finally {
            edgeRemovalFailed = false;
        }
    }

    void trailRemove(final TGEdge t) {
        trail.push(new Undoable() {

            @Override
            public void undo() {
                shrinking.addEdge(t, t.source, t.dest);
            }
        });
//        boolean ok = 
        t.afterRemove(this);
//        if (!ok) {
//            edgeRemovalFailed = true;
//        }
    }

    /**
     * Add the edge to the current solution space.
     * @param e
     * @return
     */
    protected boolean add(TGEdge e) {
        boolean rslt = shrinking.containsEdge(e)
        && growing.addWithConsequences(e);
        return //growing.containsEdge(tension)  // TODO remove first possibility - inefficient
                rslt;
    }

    private void backTrack() {
        debugMsg("back");
        while (trail.size() > stack.peek().trailPtr) {
            trail.pop().undo();
        }
    }

    public void pushRemoveUndoingAdd(final GrowingGraph gg, final TGEdge t) {
        trail.push(new Undoable() {
            @Override
            public void undo() {
                TGVertex v1 = gg.getSource(t);
                TGVertex v2 = gg.getDest(t);

                if (!gg.removeEdge(t)) {
                    throw new IllegalStateException("Failed to remove edge");
                }
                gg.bits.clear(t.bit);
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

    private <V,E> boolean subGraph(Graph<V,E> small, Graph<V,E> big) {
        for (E edge : small.getEdges()) {
            if (!small.getSource(edge).equals(big.getSource(edge))) {
                return false;
            }
            if (!small.getDest(edge).equals(big.getDest(edge))) {
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

    public void pushReenableChoice(final EdgeChoices edgeChoices) {
        trail.push(new Undoable(){

            @Override
            public void undo() {
                edgeChoices.unsetAlreadyDone();
                
            }});
    }

    public boolean containsChoiceFor(TGVertex v) {
        return v2choice.containsKey(v);
    }

    public EuclideanPseudoLines getEuclideanPseudoLines() {
        return base.getEuclideanPseudoLines();
    }
    
    public FactoryFactory ffactory() {
        return getEuclideanPseudoLines().ffactory();
    }

    public boolean isTheOne(GrowingGraph gg) {
        return expected != null && subGraph(expected, gg) && subGraph(gg, expected);
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import net.sf.oriented.omi.Face;

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
 * currently the call stack is a bit naive forward looking, in that either there are no entries
 * or there is one.
 * 
 * if there are no entries then one of the two methods is used to generate the next entry, if 
 * none is forthcoming then we have failed.
 * 
 * after every call or retry we check for success
 * 
 * it may be the case that the search finds two twisted graphs where the latter is a proper subgraph of the former, 
 * this needs to be explicitly checked for
 * 
 * each call stack entry has a pointer to the trail before the call
 * 
 * when we backtrack to a call stack entry, we undo the trail
 * 
 * trail changes include changes to the twisted graph and the parent tension graph
 * 
 * 
 * 
 * 
 * @author jeremycarroll
 *
 */
public class WAM {
    
    private final class Failure extends Frame {
        @Override
        boolean call(int counter) {
            fail();
            return false;
        }
    }
    private enum Port {
        Call, Retry, Fail
    }

    private abstract class Frame {

        Port port = Port.Call;
        int trailPtr;
        int retryCount = 0;

        final boolean call() {
            trailPtr = trail.size();
            port = Port.Retry;
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
        public abstract void undo() ;
    }

    private TensionGraph base;
    private GrowingGraph tg;
    private Deque<Frame> stack = new ArrayDeque<Frame>();
    private Deque<Undoable> trail = new ArrayDeque<Undoable>();
    
    public WAM(TensionGraph b) {
        base = b;
        tg = new GrowingGraph(b, this);
    }
    
    public void search() {
        extend();
        while (true) {
            Frame top = stack.peek();
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
            case Retry:
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
                if (stack.isEmpty())
                    return;
                backTrack();
                break;
            }
        }
    }

    private boolean success() {
        return tg.isTwistedGraph();
    }

    private void extend() {
        if ( tg.hasOptions() ) {
            final EdgeChoices opt = tg.choices.pop();
            trail.push(new Undoable(){
                @Override
                public void undo() {
                    tg.choices.push(opt);
                }});
            opt.prepareChoices(tg);
            if (opt.impossible()) {
                stack.push(new Failure());
            } if (opt.alreadyDone()) {
                stack.push(new Frame(){
                    @Override
                    boolean call(int counter) {
                        fail();
                        return true;
                    }});
            } else {
                final Tension singleChoices[] = opt.singleChoices();
                final Tension doubleChoices[] = opt.doubleChoices();
                stack.push(new Frame(){
                    @Override
                    boolean call(int ix) {
                        if (ix < singleChoices.length) {
                            return addWithTrail(singleChoices[ix]);
                        } else {
                            ix -= singleChoices.length;
                            if (ix*2 == doubleChoices.length) {
                                fail();
                                return false;
                            }
                            if (!  addWithTrail(doubleChoices[2*ix])  )
                                return false;
                            Tension t = doubleChoices[2*ix+1];
                            if (tg.containsEdge(t)) 
                                return true;
                            return addWithTrail(t);
                        }
                    }});
            }
        } else {
            final Tension t = findPossibleEdge();
            if (t==null) {
                stack.push(new Failure());
            } else {
            stack.push(new Frame(){
                @Override
                boolean call(int ix) {
                    switch(ix) {
                    case 0:
                        return addWithTrail(t);
                    case 1:
                        fail();
                        removeWithTrail(t);
                        return true;
                    default:
                        throw new IllegalArgumentException("WAM call logic error");
                    }
                }
            });
            }
        }
    }

    protected void removeWithTrail(final Tension t) {
        final Face source = base.getSource(t);
        final Face dest = base.getDest(t);
        base.removeEdge(t);
        trail.push(new Undoable(){

            @Override
            public void undo() {
                base.addEdge(t, source, dest );
            }});
    }

    protected boolean addWithTrail(Tension tension) {
        return tg.addWithTrail(tension);
    }

    private void backTrack() {
        while (trail.size() > stack.peek().trailPtr) {
            trail.pop().undo();
        }
    }

    private Tension findPossibleEdge() {
        Iterator<Tension> it = base.getEdges().iterator();
        Tension t;
        while (true) {
            if (!it.hasNext()) {
                return null;
            }
            t = it.next();
            if (!tg.containsEdge(t)) {
                return t;
            }
        }
    }

    public void pushUndoRemove(final GrowingGraph gg, final Tension t) {
        trail.push(new Undoable(){
            @Override
            public void undo() {
                gg.removeEdge(t);
            }});
    }

}


/************************************************************************
    This file is part of the Java Oriented Matroid Library.  

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/

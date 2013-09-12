/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.Stack;

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
public class TGSearch {
    
    private enum Port {
        Call, Retry, Fail
    }

    private abstract class Frame {

        Port port = Port.Call;
        int trailPtr;

        final boolean call() {
            trailPtr = trail.size();
            port = Port.Retry;
            init();
            return retry();
        }

        public abstract boolean retry();
        
        public abstract void init();

    }
    
    private abstract class Undoable {

        public abstract void undo() ;
        
    }

    private TensionGraph base;
    private TwistedGraph tg;
    private Deque<Frame> stack = new ArrayDeque<Frame>();
    private Deque<Undoable> trail = new ArrayDeque<Undoable>();
    
    
    
    public void search() {
        extend();
        while (!stack.isEmpty()) {
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
            pushOption();
        } else {
            pushAny();
        }
    }

    private void backTrack() {
        while (trail.size() > stack.peek().trailPtr) {
            trail.pop().undo();
        }
    }
    
    private boolean forwardByOption() {
        List<List<Tension>> opt = tg.getNextOption();
        if (opt == null) {
            return false;
        }
        
    }

    private boolean forwardByAny() {
        Iterator<Tension> it = base.getEdges().iterator();
        Tension t;
        while (true) {
            if (!it.hasNext()) {
                return false;
            }
            t = it.next();
            if (!tg.containsEdge(t)) {
                // found appropriate edge
                break;
            }
        }
        stack.push(new Frame(t,tg.trailPosition()));
        // choose to include t, on back-track choose to exclude t
        if (!tg.addWithConsequences(t)) {
            // failure back-track
            return false;
        }
        return true;
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

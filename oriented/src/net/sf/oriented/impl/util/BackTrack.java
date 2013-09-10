/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public abstract class BackTrack {
    private static enum State {
        Moot,
        New,
        True,
        False;
    };
    public abstract static class Choice  {
        State state;
        int trail;
        
        abstract boolean moot();
        abstract boolean chooseTrue();
        abstract boolean chooseFalse();
    }
    public abstract static class Undoable {
       abstract void doIt();
       abstract void unDoIt();
    }
    List<Choice> choices = new ArrayList<Choice>();
    Deque<Undoable> trail = new ArrayDeque<Undoable>();
    
    public void doIt(Undoable task) {
        trail.push(task);
        task.doIt();
    }
    
    public void go() {
        int pos = 0;
        while (pos>=0) {
            if (pos < choices.size()) {
                Choice choice = choices.get(pos);
                choice.trail = trail.size();
                switch (choice.state) {
                case New:
                    if (choice.moot()) {
                        pos++;
                        choice.state = State.Moot;
                        continue;
                    }
                    if (choice.chooseTrue()) {
                        pos++;
                        choice.state = State.True;
                        continue;
                    }
                case True:
                    if (choice.chooseFalse()) {
                        pos++;
                        choice.state = State.False;
                        continue;
                    }
                case False:
                    // need to back track.
                    break;
                case Moot:
                    // ignore while backtracking
                    break;
                }
                // backtrack below
                choice.state = State.New;
            } else {
                // success - all choices have been made
                // we want to exclude the case where unnecessary False choices have been made.
                success(); 
            }
            // back track
            pos--;
            if (pos<0) {
                break;
            }
            int trailMarker = choices.get(pos).trail;
            while (trail.size() > trailMarker) {
                trail.pop().unDoIt();
            }
            
        }
    }

    abstract void success();
    
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

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
    public abstract static class State implements Iterable<State> {
        Iterator<State> pos;     
        private void init() {
            pos = iterator();
        }
        abstract void fail();
    }
    Deque<State> stack = new ArrayDeque<State>();
    
    public void go(State start) {
        stack.push(start);
        start.init();
        while (!stack.isEmpty()) {
           State top = stack.peek();
           if (top.pos.hasNext()) {
               State next = top.pos.next();
               stack.push(next);
               next.init();
           } else {
               stack.pop().fail();
           }
        }
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

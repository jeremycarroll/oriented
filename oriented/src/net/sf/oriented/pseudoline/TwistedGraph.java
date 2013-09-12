/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import net.sf.oriented.omi.Face;

/**
 * This graph is created from a TensionGraph, which should
 * remain unchanged during the initialization of the twisted graph.
 * 
 * The twisted graph is built by adding edges from the TensionGraph until
 * the {@link #isTwistedGraph()} property returns true.
 * @author jeremycarroll
 *
 */
public class TwistedGraph extends AbstractTGraph {
    
    private final TensionGraph parent;
    public TwistedGraph(TensionGraph parent) {
       this.parent = parent; 
    }

    
    /**
     * 
     * @param t
     * @return true if added, false if it cannot be added.
     */
    public boolean addWithConsequences(Tension t) {
        rawAdd(t);
        boolean rslt = parent.consequences(getSource(t), t, this) && parent.consequences(getDest(t), t, this);
        return rslt;
    }
    

    private void rawAdd(Tension t) {
        addEdge(t, notNull(parent.getSource(t)), notNull(parent.getDest(t)));
    }

    private Face notNull(Face f) {
        if (f==null) {
            throw new NullPointerException("Face not found");
        }
        return f;
    }

    Deque<Options> options = new ArrayDeque<Options>();
    
    public void addOptions(Face face, List<List<Tension>> choices) {
        Options opt = new Options(face,choices);
        addOption(opt);
    }


    private void addOption(Options opt) {
        options.push(opt);
    }


    public boolean hasOptions() {
        return !options.isEmpty();
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

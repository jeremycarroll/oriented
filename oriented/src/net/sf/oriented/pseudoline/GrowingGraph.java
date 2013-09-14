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
public class GrowingGraph extends AbstractTGraph {
    
    private final ShrinkingGraph parent;
    private final WAM wam;
    public GrowingGraph(ShrinkingGraph parent, WAM wam) {
       this.parent = parent; 
       this.wam = wam;
    }

    
    /**
     * 
     * @param t
     * @return true if added, false if it cannot be added.
     */
    public boolean addWithConsequences(Tension t) {
        rawAdd(t);
        return parent.consequences(getSource(t), t, this) && parent.consequences(getDest(t), t, this);
    }
    

    private void rawAdd(Tension t) {
        if (this.containsEdge(t)) {
            throw new IllegalArgumentException("edge already present failed!" + t.hashCode());
        }
        if (!addEdge(t, notNull(parent.getSource(t)), notNull(parent.getDest(t)))) {
            
            throw new IllegalArgumentException("addEdge failed!");
        }
        wam.pushUndoRemove(this,t);
    }

    private Face notNull(Face f) {
        if (f==null) {
            throw new NullPointerException("Face not found");
        }
        return f;
    }

    
    public void addChoices(Face face, List<List<Tension>> choices) {
        EdgeChoices opt = new EdgeChoices(face,choices);
        wam.addChoice(opt);
    }


    public boolean addWithTrail(Tension t) {
        return addWithConsequences(t);
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

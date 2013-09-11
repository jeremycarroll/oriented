/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

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
     * During initialization, add an edge to the TensionGraph, and inherit edges
     * that must be included
     * @param t
     * @return null on failure, the empty list on success, or a list of choices: one of which must be added.
     */
    public List<Tension> add(Tension t) {
        rawAdd(t);
        return null;
    }
    
    private boolean addWithConsequences(Tension t) {
        rawAdd(t);
        boolean rslt = consequences(getSource(t), t) &&
               consequences(getDest(t), t);
        return rslt;
    }
    
    /**
     * 
     * @param dest
     * @param t
     * @return true if the consequences are satisfiable, false if unsatisfiable.
     */
    private boolean consequences(Face vertex, Tension t) {
//        this.findPlusMinusPlusX(vertex, in, out, ok);
        
        return true;
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import net.sf.oriented.pseudoline.EuclideanPseudoLines;

/**
 * This graph gets smaller as we move along the stack of the {@link WAM}
 * 
 * As we remove edges, operations to replace those edges on backtracking
 * get added to the trail of the 
 * associated {@link WAM}.
 * @author jeremycarroll
 *
 */
public class ShrinkingGraph extends PrunableGraph {
    
    final WAM wam;
    final GrowingGraph growing;
    ShrinkingGraph(WAM wam, TensionGraph orig) {
        this.wam = wam;
        copy(orig);
        growing = new GrowingGraph(wam, this);
    }
    
    @Override
    public boolean removeEdge(TGEdge t) {
        boolean rslt = super.removeEdge(t);
        if (!rslt) {
            throw new IllegalArgumentException("Logic error");
        }
        wam.trailRemove(t);
        return rslt;
    }
    
    /**
     * return true to indicate that we have successfully ensured that
     * vertex is not part of this graph.
     * @param vertex
     * @return
     */
    public boolean maybeRemove(TGVertex vertex) {
        // TODO Auto-generated method stub
        return false;
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

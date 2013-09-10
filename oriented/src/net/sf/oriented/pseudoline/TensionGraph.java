/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.oriented.omi.Face;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class TensionGraph extends AbstractTGraph {
    public void prune() {
        boolean pruned = true;
        while (pruned) {
            pruned = false;
            for (Face f:new ArrayList<Face>(getVertices())) {
                if (getNeighborCount(f)<3) {
                    removeVertex(f);
                    pruned = true;
                } else {
                    Collection<Tension> out = new ArrayList<Tension>(getOutEdges(f));
                    Collection<Tension> in = new ArrayList<Tension>(getInEdges(f));
                    Set<Tension> ok = new HashSet<Tension>();
                    findPlusMinusPlus(f, in, out, ok);
                    findPlusMinusPlus(f, out, in, ok);
                    int removedInfo = removeIfNotOk(out, ok)|removeIfNotOk(in, ok);
                    switch (removedInfo) {
                    case 0:
                        throw new IllegalArgumentException("Cannot happen (vertex logic)");
                    case 2:
                        removeVertex(f);
                        pruned = true;
                        break;
                    case 1:
                        break;
                    case 3:
                        pruned = true;
                        break;
                    default:
                        throw new IllegalArgumentException("Cannot happen");
                    }
                }
            }
        }
    }

    /**
     * 
     * @param out
     * @param ok
     * @return 0 if out is empty, 1 if no edges were removed, 2 if all edges were removed, 3 if some edges were removed.
     */
    private int removeIfNotOk(Collection<Tension> out, Set<Tension> ok) {
        int rslt = 0;
        for (Tension e:out) {
            if (ok.contains(e)) {
                rslt |= 1;
            } else {
                this.removeEdge(e);
                rslt |= 2;
            }
        }
        return rslt;
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

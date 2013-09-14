/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.oriented.omi.Face;

public class PrunableGraph extends AbstractTGraph {
    final class EdgePruner extends TwistedFace {
        private EdgePruner(Face f) {
            super(f);
        }

        private void removeIfNotOk(Collection<Tension> out, Collection<Tension> toBeRemoved) {
            for (Tension e:out) {
                if (!ok.contains(e)) {
                    toBeRemoved.add(e);
                }
            }
        }

        /**
         * 
         * @return true if an edge or vertex was removed
         */
        boolean prune() {
            List<Tension> toBeRemoved = new ArrayList<Tension>();
            removeIfNotOk(out, toBeRemoved);
            removeIfNotOk(in, toBeRemoved);
            for (Tension e:toBeRemoved) {
                removeEdge(e);
            }
            return !toBeRemoved.isEmpty();
        }
    }

    public void prune() {
        boolean pruned = true;
        while (pruned) {
            pruned = false;
            for (Face f:new ArrayList<Face>(getVertices())) {
                if (getNeighborCount(f)<3) {
                    removeVertex(f);
                    pruned = true;
                } else {
                    pruned = new EdgePruner(f).prune() || pruned;
                }
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

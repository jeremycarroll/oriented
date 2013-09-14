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
        List<Tension> prune() {
            List<Tension> toBeRemoved = new ArrayList<Tension>();
            removeIfNotOk(out, toBeRemoved);
            removeIfNotOk(in, toBeRemoved);
            for (Tension e:toBeRemoved) {
                removeEdge(e);
            }
            return toBeRemoved;
        }
    }

    public void prune() {
        Collection<Face> vv = getVertices();
        prune(vv, false);
    }

    void prune(Collection<Face> vv, boolean expand) {
        boolean pruned = true;
        while (pruned) {
            pruned = false;
            for (Face f:new ArrayList<Face>(vv)) {
                if (!this.containsVertex(f)) {
                    continue;
                }
                if (getNeighborCount(f)<3) {
                    if (expand) {
                        vv.addAll(this.getNeighbors(f));
                    }
                    removeVertex(f);
                    pruned = true;
                } else {
                    List<Tension> removedEdges  = new EdgePruner(f).prune();
                    boolean prunedThisFace = !removedEdges.isEmpty();
                    if (expand && prunedThisFace) {
                        for (Tension t:removedEdges) {
                            vv.add(t.source);
                            vv.add(t.dest);
                        }
                    }
                    pruned = prunedThisFace || pruned;
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

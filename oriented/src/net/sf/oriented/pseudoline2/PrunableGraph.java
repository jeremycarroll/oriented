/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PrunableGraph extends AbstractTGraph {
    public PrunableGraph() {
    }

    public PrunableGraph(AbstractTGraph gg) {
        copy(gg);
    }

    final class EdgePruner extends TwistedFace {
        private EdgePruner(TGVertex f) {
            super(f);
        }

        private void removeIfNotOk(Collection<TGEdge> out, Collection<TGEdge> toBeRemoved) {
            for (TGEdge e:out) {
                if (!ok.contains(e)) {
                    toBeRemoved.add(e);
                }
            }
        }

        /**
         * 
         * @return true if an edge or vertex was removed
         */
        List<TGEdge> prune() {
            List<TGEdge> toBeRemoved = new ArrayList<TGEdge>();
            removeIfNotOk(out, toBeRemoved);
            removeIfNotOk(in, toBeRemoved);
            for (TGEdge e:toBeRemoved) {
                removeEdge(e);
            }
            return toBeRemoved;
        }
    }

    public void prune() {
        Collection<TGVertex> vv = getVertices();
        prune(vv, false);
    }

    void prune(Collection<TGVertex> vv, boolean expand) {
        boolean pruned = true;
        while (pruned) {
            pruned = false;
            for (TGVertex vertex:new ArrayList<TGVertex>(vv)) {
                if (!this.containsVertex(vertex)) {
                    continue;
                }
                if (vertex.impossible(this)) {
                    if (expand) {
                        vv.addAll(this.getNeighbors(vertex));
                    }
                    removeVertex(vertex);
                    pruned = true;
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

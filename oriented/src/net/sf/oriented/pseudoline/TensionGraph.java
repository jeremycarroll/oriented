/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;


import net.sf.oriented.omi.Face;

public class TensionGraph extends AbstractTGraph {
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
    
    class EdgeSelector extends FaceAnalyzer {

        final Tension wanted;
        final GrowingGraph child;
        final int size;
        final List<List<Tension>> options = new ArrayList<List<Tension>>();
        final Collection<Tension> already;
        final int space;
        boolean justDoIt = false;
        EdgeSelector(Face f, Tension wanted, GrowingGraph child) {
            super(f);
            this.wanted = wanted;
            this.child = child;
            // how may edges are involved at this face?
            if (f.type() == Face.Type.Cocircuit ) {
                size = f.higher().size() / 2;
            } else {
                size = f.lower().size();
            }
            // what is already at this face concerning the child
            already = child.getIncidentEdges(f);
            space = size - already.size();
        }

        @Override
        boolean add(Tension f, Tension s, Tension t) {
            if (f==wanted) {
                add(s,t);
            } else  if (s==wanted) {
                add(f,t);
            } else if (t==wanted) {
                add(f,s);
            }
            return !justDoIt;
        }

        private void add(Tension s, Tension t) {
            if (already.contains(s)) {
                add(t);
            } else if (already.contains(t)) {
                add(s);
            } else {
                options.add(ImmutableList.of(s,t));
            }
        }

        private void add(Tension t) {
            if (already.contains(t)) {
                justDoIt = true;
            } else {
                options.add(ImmutableList.of(t));
            }
        }

        public boolean alreadyDone() {
            return already.contains(child);
        }

        public boolean impossible() {
            return space <= 0;
        }

        public void search() {
            this.findPlusMinusPlus();
        }
        
    }

    /**
     * 
     * @param face
     * @param t
     * @param tg
     * @return true if added, false if it cannot be added.
     */
    public boolean consequences(Face face, Tension t, GrowingGraph tg) {
        EdgeSelector selector = new EdgeSelector(face,t,tg);
        if (selector.alreadyDone()) {
            return true;
        }
        if (selector.impossible()) {
            return false;
        }
        selector.search();
        if (selector.justDoIt) {
            tg.addWithTrail(t);
            return true;
        }
        if (selector.options.isEmpty()) {
            return false;
        }
        tg.addWithTrail(t);
        if (selector.options.size() == 1) {
            for (Tension tt:selector.options.get(0)) {
                tg.addWithTrail(tt);
            }
        } else {
           tg.addChoices(face,selector.options);
        }
        return true;
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

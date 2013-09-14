/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;


public class TensionGraph extends PrunableGraph {
    
//    class EdgeSelector extends FaceAnalyzer {
//
//        final Tension wanted;
//        final GrowingGraph child;
//        final int size;
//        final List<List<Tension>> options = new ArrayList<List<Tension>>();
//        final Collection<Tension> already;
//        final int space;
//        boolean justDoIt = false;
//        EdgeSelector(Face f, Tension wanted, GrowingGraph child) {
//            super(f);
//            this.wanted = wanted;
//            this.child = child;
//            // how may edges are involved at this face?
//            if (f.type() == Face.Type.Cocircuit ) {
//                size = f.higher().size() / 2;
//            } else {
//                size = f.lower().size();
//            }
//            // what is already at this face concerning the child
//            already = child.getIncidentEdges(f);
//            space = size - already.size();
//        }
//
//        @Override
//        boolean add(Tension f, Tension s, Tension t) {
//            if (f==wanted) {
//                add(s,t);
//            } else  if (s==wanted) {
//                add(f,t);
//            } else if (t==wanted) {
//                add(f,s);
//            }
//            return !justDoIt;
//        }
//
//        private void add(Tension s, Tension t) {
//            if (already.contains(s)) {
//                add(t);
//            } else if (already.contains(t)) {
//                add(s);
//            } else {
//                if (space > 1) {
//                   options.add(ImmutableList.of(s,t));
//                }
//            }
//        }
//
//        private void add(Tension t) {
//            if (already.contains(t)) {
//                justDoIt = true;
//            } else {
//                if (space > 0) {
//                   options.add(ImmutableList.of(t));
//                }
//            }
//        }
//
//        public boolean impossible() {
//            return space < 0;
//        }
//
//        public void search() {
//            this.findPlusMinusPlus();
//            if (justDoIt) {
//                options.clear();
//                options.add(Arrays.asList(new Tension[0]));
//            }
//        }
//        
//    }
//
//    /**
//     * 
//     * @param face
//     * @param t Has already been added to tg
//     * @param tg
//     * @return true if added, false if it cannot be added.
//     */
//    public boolean consequences(Face face, Tension t, GrowingGraph tg) {
//        EdgeSelector selector = new EdgeSelector(face,t,tg);
//        if (selector.impossible()) {
//            return false;
//        }
//        selector.search();
//        if (selector.options.isEmpty()) {
//            return false;
//        }
//        if (selector.options.size() == 1) {
//            for (Tension tt:selector.options.get(0)) {
//                if (!(tg.containsEdge(tt) || tg.addWithTrail(tt))) {
//                    return false;
//                }
//            }
//        } else {
//           tg.addChoices(face,selector.options);
//        }
//        return true;
//    }

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

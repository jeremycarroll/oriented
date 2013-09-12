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
/**
 * This graph is a set of faces and vertices from the inside of euclidean pseudoline picture
 * with the edges reflecting how lines get crossed between them.
 * @author jeremycarroll
 *
 */
public class AbstractTGraph extends DirectedSparseMultigraph<Face, Tension> {
    public void dumpEdges() {
        System.err.println("====");
        for (Tension t:getEdges()) {
            Face from = this.getSource(t);
            Face to = this.getDest(t);
            System.err.println(t.label()+": "+from.covector()+" ==> "+to.covector());
        }
    }
    public void dumpVertices() {
        System.err.println("********");
        for (Face f:getVertices()) {
            System.err.print(f.covector()+": ");
            for (Tension t:getInEdges(f)) {
                System.err.print(t.label()+", ");
            }
            System.err.print(" /// ");
            for (Tension t:getOutEdges(f)) {
                System.err.print(t.label()+", ");
            }
            System.err.println();
        }
    }
    abstract class FaceAnalyzer  {
        Face face;
        protected Collection<Tension> out;
        protected Collection<Tension> in;
        FaceAnalyzer(Face f) {
            face = f;
            out = getOutEdges(face);
            in = getInEdges(face);
        }
        /**
         * 
         * @param f
         * @param s
         * @param t
         * @return True to keep looking, false to finish the search
         */
        abstract boolean add(Tension f, Tension s, Tension t);
        void findPlusMinusPlus(Collection<Tension> in, Collection<Tension> out) {
            for (Tension first:out) {
                Face firstV = getOpposite(face, first);
                for (Tension second:in) {
                    if (second.ordinal > first.ordinal) {
                        Face secondV = getOpposite(face,second);
                        if (firstV != secondV) {
                            for (Tension third: out) {
                                if (third.ordinal > second.ordinal) {
                                    Face thirdV = getOpposite(face,third);
                                    if (firstV != thirdV && secondV != thirdV) {
                                       if (!add(first, second, third)) {
                                           return;
                                       }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        protected void findPlusMinusPlus() {
            findPlusMinusPlus(in, out);
            findPlusMinusPlus(out, in);
        }
        
    }
    class TwistedFace extends FaceAnalyzer {
        final Set<Tension> ok = new HashSet<Tension>();

        TwistedFace(Face f) {
            super(f);
            findPlusMinusPlus();
        }

        @Override
        public boolean add(Tension first, Tension second, Tension third) {
            ok.add(first);
            ok.add(second);
            ok.add(third);
            return true;
        }

        boolean isTwisted() {
            if (!ok.containsAll(out)) {
                return false;
            }
            if (!ok.containsAll(in)) {
                return false;
            }
            return true;
        }
        
    }
    public boolean isTwistedGraph() {
        for (Face f:new ArrayList<Face>(getVertices())) {
            if (getNeighborCount(f)<3) {
                return false;
            } else {
                TwistedFace fa = new TwistedFace(f);
                if (!fa.isTwisted()) {
                    return false;
                }
            }
        }
        return true;
    }
//        private void findPlusMinusPlus(Face vertex, Collection<Tension> in,
//                Collection<Tension> out, Add3 add3, FaceAnalyzer fa) {
//                    fa.findPlusMinusPlus(vertex, in, out, add3, this);
//                }
//    protected void findPlusMinusPlusX(Face vertex, Collection<Tension> in,
//            Collection<Tension> out, final Set<Tension[]> ok) {
//        Add3 add3 = new Add3(){
//            @Override
//            public void add(Tension first, Tension second, Tension third) {
//                ok.add(new Tension[]{first,second,third});
//            }};
//        findPlusMinusPlus(vertex, in, out, add3, null);
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

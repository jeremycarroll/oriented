/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import net.sf.oriented.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.SignedSetFactory;
import net.sf.oriented.omi.UnsignedSet;

class Six {
    final private int pTriangles[][];
    final private int nTriangles[][];

    public Six(int[][] pTriangles, int[][] nTriangles) {
        if (pTriangles.length != 2) {
            System.err.println(pTriangles.length+" + "+nTriangles.length);
        }
        this.pTriangles = pTriangles;
        this.nTriangles = nTriangles;
    }

    Difficulty alignAndRegister(TensionGraph base, OMasChirotope om) {
        AbstractTGraph tg = new AbstractTGraph();
        if (hasParallelSides(pTriangles,om) || hasParallelSides(nTriangles,om)) {
            return null;
        }
        if (collectVertices(tg,base,1,pTriangles)
                && collectVertices(tg,base,-1,nTriangles) ) {
            copyEdges(tg, base);
            return new DifficultSix(tg, base.totalBits());
        } else {
            return null;
        }
    }

    private boolean hasParallelSides(int[][] triangles, OMasChirotope om) {
        for (int tri[] : triangles) {
            if (om.chi(0,tri[0],tri[1]) == 0 
                    ||om.chi(0,tri[0],tri[2]) == 0 
                    ||om.chi(0,tri[1],tri[2]) == 0 ) {
                return true;
            }
        }
        return false;
    }

    private void copyEdges(AbstractTGraph tg, TensionGraph base) {
        for (TGVertex v: tg.getVertices()) {
            for (TGEdge e : base.getOutEdges(v) ) {
                if (tg.containsVertex(e.dest)) {
                    tg.addEdge(e, e.source, e.dest);
                }
            }
        }
    }

    private boolean collectVertices(AbstractTGraph tg, TensionGraph base, int sign, 
            int[] ... triangles) {
        Label[] elements = base.pseudolines.getEquivalentOM().elements();
        FactoryFactory ffactory = base.pseudolines.ffactory();
        UnsignedSetFactory unsigned = ffactory.unsignedSets();
        SignedSetFactory signed = ffactory.signedSets();
        for (int[] tri:triangles) {
            UnsignedSet middle = unsigned.empty().union(elements[tri[1]]);
            UnsignedSet outer = unsigned.empty().union(elements[tri[0]]).union(elements[tri[2]]);
            SignedSet id;
            if (sign == 1) {
                id = signed.construct(outer, middle);
            } else {
                id = signed.construct(middle, outer);
            }
            final TGVertex v = base.id2vertex.get(id);
            if (v == null) {
                return false;
            }
            tg.addVertex(v);
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

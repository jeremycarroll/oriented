/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import net.sf.oriented.omi.Face;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class Difficulty {
    
    final BitSet bits = new BitSet();
    final BitSet missingBits;
//    public final TGEdge unnecessary;
    private Graph<Faces, DEdge> rslt;
    private List<TGEdge> saveEdges;
    Difficulty(AbstractTGraph gg, int sz) {
//        TGEdge unnecessary = null;
        for (TGEdge e:gg.getEdges()) {
//            if (e.unnecessary) {
//                unnecessary = e;
//            }
            bits.set(e.bit);
            if (e.saveInDifficulty) {
                if (saveEdges==null) {
                    saveEdges = new ArrayList<TGEdge>(4);
                }
                saveEdges.add(e);
            }
        }
        missingBits = (BitSet) bits.clone();
        missingBits.flip(1,sz+1);
//        this.unnecessary = unnecessary;
        
    }
    Graph<Faces, DEdge> getRslt(TensionGraph tg) {
        if (rslt != null) {
            return rslt;
        }
        rslt = new DirectedSparseGraph<Faces, DEdge>();
//        if (bits.get(0)) {
//            throw new IllegalArgumentException("Accessing deleted difficulty");
//        }
        // 0th bit is the deleted marker
        int bit = 1;
        while (true) {
            bit = bits.nextSetBit(bit);
            if (bit == -1) {
                return rslt;
            }
            DEdge d = tg.getDEdge(bit);
            TGEdge e = getSavedTGEdge(bit);
            if (e==null) {
                rslt.addEdge(d,  new Faces(d.source), new Faces(d.dest) );
            } else {
                rslt.addEdge(d,  new Faces(e.source.getSource(), d.source),
                                 new Faces(e.dest.getSource(), d.dest) );
            }
            bit++;
        }
    }
    public Graph<Face, DEdge> getSimplifiedRslt(TensionGraph tg) {
        Graph<Face, DEdge> resultGraph = new DirectedSparseGraph<Face, DEdge>();
        if (bits.get(0)) {
            throw new IllegalArgumentException("Accessing deleted difficulty");
        }
        // 0th bit is the deleted marker
        int bit = 1;
        while (true) {
            bit = bits.nextSetBit(bit);
            if (bit == -1) {
                return resultGraph;
            }
            DEdge d = tg.getDEdge(bit);
            TGEdge e = getSavedTGEdge(bit);
            if (e==null) {
                resultGraph.addEdge(d,  d.source, d.dest );
            } else {
                resultGraph.addEdge(d,  new Faces(e.source.getSource(), d.source).getSimplifiedVertex(),
                                 new Faces(e.dest.getSource(), d.dest).getSimplifiedVertex() );
            }
            bit++;
        }
    }
    private TGEdge getSavedTGEdge(int bit) {
        if (this.saveEdges==null)
        return null;
        for (TGEdge r:saveEdges) {
            if (r.bit == bit) {
                return r;
            }
        }
        return null;
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

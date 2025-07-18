/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.sines.Sines;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class Difficulty {
    
    final class DBitSet extends BitSet {
        Difficulty getDifficulty() {
            return Difficulty.this;
        }
    }
    final DBitSet bits = new DBitSet();
//    final BitSet missingBits;
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
                    saveEdges = new ArrayList<>(4);
                }
                saveEdges.add(e);
            }
        }
//        missingBits = (BitSet) bits.clone();
//        missingBits.flip(1,sz+1);
//        this.unnecessary = unnecessary;
        
    }
    public Sines getSines(TensionGraph tg) {
        Graph<Face, DEdge> g = getSimplifiedRslt(tg);
        SignedSet ss[] = new SignedSet[g.getVertexCount()];
        int i=0;
        FactoryFactory ff = tg.getEuclideanPseudoLines().ffactory();
        for (Face v:g.getVertices()) {
            ss[i++] = ff.signedSets().construct(labels(ff,g.getOutEdges(v)), labels(ff,g.getInEdges(v)));
        }
        return new Sines(tg.getEuclideanPseudoLines().getEquivalentOM(), ss);
    }
    private UnsignedSet labels(FactoryFactory ff, Collection<DEdge> edges) {
        Function<DEdge, Label> function = new Function<DEdge, Label>(){
            @Override
            public Label apply(DEdge e) {
                return e.label;
            }
        };
        return ff.unsignedSets().copyBackingCollection(Iterables.transform(edges, function));
    }
    Graph<Faces, DEdge> getRslt(TensionGraph tg) {
        if (rslt != null) {
            return rslt;
        }
        rslt = new DirectedSparseGraph<>();
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
        Graph<Face, DEdge> resultGraph = new DirectedSparseGraph<>();
//        if (bits.get(0)) {
//            throw new IllegalArgumentException("Accessing deleted difficulty");
//        }
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
/*
 * It is unclear to me what assumptions we can make.
 * 
 * We really only need 'minimal' difficulties, i.e. we don't want duplicates.
 * 
 * At the moment I am inclined to make some assumptions.
 * 
 * e.g. take a difficult A it is possible to modify it to make B by duplicating
 * some of the entries and making parallel lines.
 * This is pretty unclear.
 * 
 * Anyway: given A there is such a B - it doesn't quite mean that we can work with B
 * instead of A, but we will anyway.
 * 
 * 
 * 
 * 
 */

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

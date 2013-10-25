/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;


public class TensionGraph extends PrunableGraph {
    
    final EuclideanPseudoLines pseudolines;
    final Map<SignedSet, TGVertex> id2vertex = new HashMap<SignedSet, TGVertex>();

    public TensionGraph(EuclideanPseudoLines euclideanPseudoLines) {
        this.pseudolines = euclideanPseudoLines;
    }

    public EuclideanPseudoLines getEuclideanPseudoLines() {
        return pseudolines;
    }

    public void maybeAddVertex(TGVertex tgVertex) {
        SignedSet ss = tgVertex.getId();
        if (id2vertex.containsKey(ss)) {
            TGVertex old = id2vertex.get(ss);
            removeVertex(old);
            id2vertex.put(ss, new TGVertex(tgVertex, old));
        }
        id2vertex.put(ss, tgVertex);
        addVertex(tgVertex);
        
    }
    List<DEdge> allEdges = new ArrayList<DEdge>();
    Map<DEdge,Integer> edgeLookup = new HashMap<DEdge,Integer>();

    void saveEdgeAsBit(TGEdge edge) {
        DEdge d = new DEdge(edge, pseudolines.getFaceLattice());
        Integer bit = edgeLookup.get(d);
        if (bit == null) {
            allEdges.add(d);
            edge.bit = allEdges.size();
            edgeLookup.put(d, edge.bit);
        } else {
            edge.bit = bit;
        }
        if (! ( (d.source.equals(edge.source.getSource()))
                && (d.dest.equals(edge.dest.getSource())) )) {
            edge.saveInDifficulty = true;
        }
    }
    
    public int totalBits() {
        return allEdges.size();
    }

    DEdge getDEdge(int bit) {
        return allEdges.get(bit-1);
    }

    void storeEdgesByBit() {
        int counts[] =  new int[totalBits()+2];
        for (TGEdge e:getEdges()) {
            counts[e.bit]++;
        }
        TGEdge[][] tgEdges = new TGEdge[counts.length][];
        for (int i=0;i<counts.length;i++) {
            tgEdges[i] = new TGEdge[counts[i]];
        }
        Arrays.fill(counts, 0);
        for (TGEdge e:getEdges()) {
            tgEdges[e.bit][counts[e.bit]++] = e;
        }
        for (int i=0;i<tgEdges.length;i++) {
            TGEdge edges[] = tgEdges[i];
            if (edges.length != 0) {
                DEdge dEdge = getDEdge(i);
                for (TGEdge e:edges) {
                    e.dEdge = dEdge;
                    dEdge.tgEdges = edges;
                }
            }
        }
    }

    public TGEdge[] equivalentEdges(TGEdge t) {
        return t.dEdge.tgEdges;
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.BitSet;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.SignedSet;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class Difficulty {
    
    final BitSet bits = new BitSet();
    final BitSet missingBits;
    private Graph<Face, DEdge> rslt;
    Difficulty(GrowingGraph gg, int sz) {
        for (TGEdge e:gg.getEdges()) {
            bits.set(e.bit);
        }
        missingBits = (BitSet) bits.clone();
        missingBits.flip(1,sz+1);
    }
    Graph<Face, DEdge> getRslt(TensionGraph tg) {
        if (rslt != null) {
            return rslt;
        }
        rslt = new DirectedSparseGraph<Face, DEdge>();
        if (bits.get(0)) {
            throw new IllegalArgumentException("Accessing deleted difficulty");
        }
        // 0th bit is the deleted marker
        int bit = 1;
        while (true) {
            bit = bits.nextSetBit(bit);
            if (bit == -1) {
                return rslt;
            }
            DEdge d = tg.getDEdge(bit);
            rslt.addEdge(d,  d.source, d.dest);
            bit++;
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.Collection;

import net.sf.oriented.omi.Face;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
/**
 * This graph is a set of faces and vertices from the inside of euclidean pseudoline picture
 * with the edges reflecting how lines get crossed between them.
 * @author jeremycarroll
 *
 */
public class AbstractTGraph extends DirectedSparseMultigraph<TGVertex, TGEdge> {
//    public void dumpEdges() {
//        System.err.println("====");
//        for (TGEdge t:getEdges()) {
//            Face from = this.getSource(t);
//            Face to = this.getDest(t);
//            System.err.println(t.label()+": "+from.covector()+" ==> "+to.covector());
//        }
//    }
//    public void dumpVertices() {
//        System.err.println("********");
//        for (Face f:getVertices()) {
//            System.err.print(f.covector()+": ");
//            for (TGEdge t:getInEdges(f)) {
//                System.err.print(t.label()+", ");
//            }
//            System.err.print(" /// ");
//            for (TGEdge t:getOutEdges(f)) {
//                System.err.print(t.label()+", ");
//            }
//            System.err.println();
//        }
//    }
    public boolean isTwistedGraph() {
        Collection<TGVertex> vv = getVertices();
        if (vv.isEmpty()) {
            return false;
        }
        for (TGVertex v:vv) {
            if (!v.checkEdgeCount(this)) {
                return false;
            }
        }
        for (TGVertex v:vv) {
            if (!v.checkEdgeLabels(this)) {
                return false;
            }
        }
        return true;
    }
 
    /**
     * Copy all the edges from the orig graph.
     * @param orig
     */
    protected void copy(AbstractTGraph orig) {
        for (TGEdge t:orig.getEdges()) {
            addEdge(t,t.source,t.dest);
        }
    }
    protected int faceSize(Face f) {
        int size;
        if (f.type() == Face.Type.Cocircuit ) {
            size = f.higher().size() / 2;
        } else {
            size = f.lower().size();
        }
        return size;
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

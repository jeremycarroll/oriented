/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.HashSet;
import java.util.Set;

import net.sf.oriented.util.graph.SimplePath;

public class TensionPath extends SimplePath<TGVertex> {
    class EdgeInfo {
        
        // the jth line is represented as the jth bit
        int possibleLines;
        private final Set<TGEdge> edges;
        final int index;

        public EdgeInfo(int index, ShrinkingGraph g, TGVertex from, TGVertex to) {
            edges = (Set<TGEdge>) g.getIncidentEdges(from);
            edges.retainAll(g.getIncidentEdges(to));
            for (TGEdge t:edges) {
                possibleLines |= 1 << t.ordinal;
            }
            this.index = index;
        }

        // Copy constructor.
        public EdgeInfo(int index, EdgeInfo edgeInfo) {
            this.possibleLines = edgeInfo.possibleLines;
            this.edges = new HashSet<TGEdge>(edgeInfo.edges);
            this.index = index;
        }
        
    }
    
    final EdgeInfo edges[];
//    final FaceConstraints faces[];
    // which lines appear in which edges
    // the ith edge is represented as the ith bit in each entry in the map
    // the jth line is the jth entry in the map
    final int lineMap[];
    
    // the jth bit is set if this path definitely uses the jth line
    int necessaryLines;

    protected TensionPath(ShrinkingGraph g, int lineCount, TGVertex from, TGVertex to) {
        super(TGVertex.class, from, to);
        edges = new EdgeInfo[1];
        edges[0] = new EdgeInfo(0,g, from,to);
//        faces = new FaceConstraints[]{g.faceConstaints(0,this,from),g.faceConstaints(1,this,to)};
//        faces[0].forward(edges[0]);
//        faces[1].backward(edges[0]);
        lineMap = new int[lineCount];
    }

    public TensionPath(TensionPath first, TensionPath andThen) {
        super(first,andThen);
        lineMap = first.lineMap.clone();
        for (int i=0;i<lineMap.length;i++) {
            lineMap[i] |= andThen.lineMap[i]<< first.edges.length;
        }
        edges = new EdgeInfo[first.edges.length+andThen.edges.length];
        cloneEdges(0,first.edges);
        cloneEdges(first.edges.length,andThen.edges);
//        faces = new FaceConstraints[this.path.length];
//        cloneFaces(0,first.faces);
//        cloneFaces(first.faces.length-1,andThen.faces);
//        faces[first.edges.length].backward(edges[first.edges.length-1]);
    }

//    private void cloneFaces(int pos, FaceConstraints[] faces) {
//        for (int i=0;i<faces.length;i++) {
//            this.faces[i+pos] = faces[i].copy(i+pos);
//        }
//    }

    private void cloneEdges(int pos, EdgeInfo[] edges) {
        for (int i=0;i<edges.length;i++) {
            this.edges[i+pos] = new EdgeInfo(i+pos,edges[i]);
        }
    }

    boolean canBeFollowedBy(TensionPath p) {
        return super.canBeFollowedBy(p);
    }

    public boolean isBad() {
        // TODO Auto-generated method stub
        return false;
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

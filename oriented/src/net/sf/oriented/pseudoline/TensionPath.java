/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import net.sf.oriented.omi.Face;
import net.sf.oriented.pseudoline.ShrinkingGraph.FaceConstraints;
import net.sf.oriented.util.graph.SimplePath;

public class TensionPath extends SimplePath<Face> {
    class EdgeInfo {

        public EdgeInfo(ShrinkingGraph g, Face from, Face to) {
            // TODO Auto-generated constructor stub
        }

        // Copy constructor.
        public EdgeInfo(EdgeInfo edgeInfo) {
            // TODO Auto-generated constructor stub
        }
        
    }
    
    final EdgeInfo edges[];
    final FaceConstraints faces[];

    protected TensionPath(ShrinkingGraph g, Face from, Face to) {
        super(Face.class, from, to);
        edges = new EdgeInfo[1];
        edges[0] = new EdgeInfo(g, from,to);
        faces = new FaceConstraints[]{g.faceConstaints(from),g.faceConstaints(to)};
        faces[0].forward(edges[0]);
        faces[1].backward(edges[0]);
    }

    public TensionPath(TensionPath first, TensionPath andThen) {
        super(first,andThen);
        edges = new EdgeInfo[first.edges.length+andThen.edges.length];
        cloneEdges(0,first.edges);
        cloneEdges(first.edges.length,andThen.edges);
        faces = new FaceConstraints[this.path.length];
        cloneFaces(0,first.faces);
        cloneFaces(first.faces.length-1,andThen.faces);
        faces[first.edges.length].backward(edges[first.edges.length-1]);
    }

    private void cloneFaces(int pos, FaceConstraints[] faces) {
        for (int i=0;i<faces.length;i++) {
            this.faces[i+pos] = faces[i].copy();
        }
    }

    private void cloneEdges(int pos, EdgeInfo[] edges) {
        for (int i=0;i<edges.length;i++) {
            this.edges[i+pos] = new EdgeInfo(edges[i]);
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

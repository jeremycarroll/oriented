/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.HashSet;
import java.util.Set;

import net.sf.oriented.util.graph.SimplePath;

public class TGPath extends SimplePath<TGVertex> {
    class EdgeInfo {
        
        // the jth line is represented as the jth bit
        final int line;
        private final TGEdge edge;
        final int index;

        public EdgeInfo(int index, TensionGraph g, TGVertex from, TGVertex to) {
            Set<TGEdge> edges = (Set<TGEdge>) g.getIncidentEdges(from);
            edges.retainAll(g.getIncidentEdges(to));
            if (edges.size()!=1) {
                throw new IllegalArgumentException("Expecting one edge");
            }
            edge = edges.iterator().next();
            line = 1 << edge.ordinal;
            this.index = index;
        }

        // Copy constructor.
        public EdgeInfo(int index, EdgeInfo edgeInfo) {
            this.line = edgeInfo.line;
            this.edge = edgeInfo.edge;
            this.index = index;
        }
        
    }
    
    final EdgeInfo edges[];
    
    final int necessaryLines;
    final boolean isBad;

    protected TGPath(TensionGraph g, int lineCount, TGVertex from, TGVertex to) {
        super(TGVertex.class, from, to);
        edges = new EdgeInfo[1];
        edges[0] = new EdgeInfo(0,g, from,to);
        necessaryLines = edges[0].line;
        isBad = false;
    }

    public TGPath(TGPath first, TGPath andThen) {
        super(first,andThen);
        edges = new EdgeInfo[first.edges.length+andThen.edges.length];
        cloneEdges(0,first.edges);
        cloneEdges(first.edges.length,andThen.edges);
        necessaryLines = first.necessaryLines | andThen.necessaryLines;
        isBad = (first.necessaryLines & andThen.necessaryLines) != 0;
    }
///
    

    private void cloneEdges(int pos, EdgeInfo[] edges) {
        for (int i=0;i<edges.length;i++) {
            this.edges[i+pos] = new EdgeInfo(i+pos,edges[i]);
        }
    }

    boolean canBeFollowedBy(TGPath p) {
        return super.canBeFollowedBy(p);
    }

    public boolean isBad() {
        return isBad;
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

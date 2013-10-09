/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import edu.uci.ics.jung.graph.Graph;
import net.sf.oriented.omi.Face;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.util.graph.PathFactory;

public class DPathFactory implements PathFactory<Face, DPath>{
    
    final Graph<Face, DEdge> graph;
    final EuclideanPseudoLines epl;

    DPathFactory(Graph<Face, DEdge> g, EuclideanPseudoLines epl) {
        graph = g;
        this.epl = epl;
    }

    @Override
    public DPath create(Face from, Face to) {
        return checkNotBad(new DPath(graph,epl,from,to));
    }

    @Override
    public DPath combine(DPath first, DPath andThen) {
        if (first.canBeFollowedBy(andThen)) {
            return checkNotBad(new DPath(first,andThen));
        } else {
           return null;
        }
    }

    private DPath checkNotBad(DPath rslt) {
        if (rslt.isBad()) {
            return null;
        } else {
            return rslt;
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

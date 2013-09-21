/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import net.sf.oriented.omi.Face;
import net.sf.oriented.util.graph.PathFactory;

public class TensionPathFactory implements PathFactory<Face, TensionPath>{
    
    final ShrinkingGraph graph;
    
    TensionPathFactory(ShrinkingGraph g) {
        graph = g;
    }

    @Override
    public TensionPath create(Face from, Face to) {
        return checkNotBad(new TensionPath(graph,from,to));
    }

    @Override
    public TensionPath combine(TensionPath first, TensionPath andThen) {
        if (first.canBeFollowedBy(andThen)) {
            return checkNotBad(new TensionPath(first,andThen));
        } else {
           return null;
        }
    }

    private TensionPath checkNotBad(TensionPath rslt) {
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import net.sf.oriented.omi.Face;
import net.sf.oriented.util.graph.Paths;
import net.sf.oriented.util.graph.SimplePath;
import edu.uci.ics.jung.graph.Graph;

final class TensionPaths extends Paths<Face, Tension, SimplePath<Face>> {
    /**
     * 
     */
    private final WAM wam;

    TensionPaths(WAM wam, Graph<Face, Tension> g) {
        super(g);
        this.wam = wam;
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

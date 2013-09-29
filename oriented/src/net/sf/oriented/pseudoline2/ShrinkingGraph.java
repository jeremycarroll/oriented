/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.List;


/**
 * This graph gets smaller as we move along the stack of the {@link WAM}
 * 
 * As we remove edges, operations to replace those edges on backtracking
 * get added to the trail of the 
 * associated {@link WAM}.
 * @author jeremycarroll
 *
 */
public class ShrinkingGraph extends PrunableGraph {
    
    final WAM wam;
    final GrowingGraph growing;
    ShrinkingGraph(WAM wam, TensionGraph orig) {
        this.wam = wam;
        copy(orig);
        growing = new GrowingGraph(wam,this);
    }
    
    @Override
    public boolean removeEdge(TGEdge t) {
        boolean rslt = super.removeEdge(t);
        if (!rslt) {
            throw new IllegalArgumentException("Logic error");
        }
        wam.trailRemove(t);
        return rslt;
    }

    public TGEdge[] filter(TGEdge[] array) {
//        return array;
        // following code makes no difference to runtime, but does make the number of transitions more
        // indicative of actual work-load.
        List<TGEdge> rslt = new ArrayList<TGEdge>(array.length);
        for (TGEdge e:array) {
            if (containsEdge(e)) {
                rslt.add(e);
            }
        }
        
        return rslt.toArray(new TGEdge[0]);
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

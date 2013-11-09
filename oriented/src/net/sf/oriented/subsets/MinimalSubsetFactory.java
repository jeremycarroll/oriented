/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.io.IOException;
import java.util.BitSet;
import java.util.Collection;


public class MinimalSubsetFactory {

    public static MinimalSubsets naive() {
        
        return new NaiveMinimalSubsets();
    }
    
    /**
     * Use an implementation based on:
     * <a href="http://stackoverflow.com/a/8996897/2276263">Aaron McDaid's comment</a>:
     * @return
     */
    public static MinimalSubsets mcdaid() {
        
        return new TransposeMinimalSubsets();
    }

    static int max(Collection<BitSet> full) {
        int m = 0;
        for (BitSet b:full) {
            int l =  b.length();
            if (l>m) {
                m = l;
            }
        }
        return m;
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

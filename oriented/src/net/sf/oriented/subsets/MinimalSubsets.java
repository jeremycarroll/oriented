/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.BitSet;
import java.util.Collection;
import java.util.List;

public interface MinimalSubsets {
    
    /**
     * Returns a list of minimal elements of full.
     * i.e. every element of full is either in the result
     * or is an improper superset of some element of the result
     * and no element of the result is an improper superset
     * of any other element of the result.
     * @param full
     * @return
     */
    List<BitSet> minimal(Collection<BitSet> full);

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

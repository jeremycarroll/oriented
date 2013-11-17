/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.BitSet;
import java.util.Collection;
import java.util.List;

public class FindMinimalSets {
    
    /**
     * Returns a list of bitsets being those elements
     * in the input that are smaller than any others.
     * Formally: every element in the output is an
     * element in the input; 
     * no two elements of the output are subsets of one
     * another; and every element of the input is a superset
     * of some element of the output.
     * 
     * This implementation switches between the various algorithms
     * available in this package, based on my guesses
     * based on the size of the input, and the experience
     * evaluating the algorithms. For serious usage you are advised
     * to evaluate the algorithms afresh, and also possibly
     * explore some that I did not.
     * @param in
     * @return A list of minimal sets
     */
    public static List<BitSet> minimal(Collection<BitSet> in) {
        if (in.size() < 500) {
            return OneShotFactory.naive().minimal(in, Preparation.Minimal);
        }
        if (in.size() < 35000) {
            return OneShotFactory.singleOccursList().minimal(in, Preparation.ReversePritchard);
        }
        if (in.size() < 300000) {
            return OneShotFactory.lexicographic().minimal(in, Preparation.Pritchard);
        }
        return OneShotFactory.parallel().minimal(in, Preparation.Pritchard);
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

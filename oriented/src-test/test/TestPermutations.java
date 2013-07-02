/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import junit.framework.Assert;

import net.sf.oriented.combinatorics.Permutation;

public class TestPermutations {
    
    private static void permutationTest(int n, int factorial) {
        Set<Permutation> sofar = new HashSet<Permutation>();
        for (Permutation p:Permutation.all(n)) {
            Assert.assertTrue(sofar.add(p));
        }
    }
    
    @Test
    public void test3() {
        permutationTest(3,6);
    }
    @Test
    public void test6() {
        permutationTest(6,720);
    }
    @Test
    public void test8() {
        permutationTest(8,40320);
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

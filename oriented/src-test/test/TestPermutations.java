/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
    
    private static class Group {
        final Map<String,Permutation> members = new HashMap<String,Permutation>();
        final Map<Permutation,String> names = new HashMap<Permutation,String>();

        public void store(String name, Permutation value) {
            members.put(name, value);
            names.put(value, name);
        }
        
        public void inverses() {
            for (String nm: members.keySet()) {
                System.err.println("1/"+nm+" = "+name(get(nm).inverse()));
            }
        }

        public void times() {
            for (String a: members.keySet()) {
                for (String b: members.keySet())
                   System.err.println(a+"."+b+" = "+name(get(a).and(get(b))));
            }
        }

        private String name(Permutation p) {
            return names.get(p);
        }

        private Permutation get(String nm) {
            return members.get(nm);
        }
        
    }
    @Test
    public void testS3() {
        Group g = new Group();
        g.store("i", new Permutation(0,1,2));
        g.store( "r", new Permutation(1,2,0));
        g.store( "s",  new Permutation(2,0,1));
        g.store( "a",  new Permutation(0,2,1));
        g.store( "b",  new Permutation(1,0,2));
        g.store( "c",  new Permutation(2,1,0));
        g.inverses();
        g.times();
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

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
    
    private static final int s3members[] = { 12, 102, 21, 210, 120, 201, };
    private static final int s3[][] = { 
            { 12, 102, 21, 210, 120, 201, },
            { 102, 12, 120, 201, 21, 210, },
            { 21, 201, 12, 120, 210, 102, },
            { 210, 120, 201, 12, 102, 21, },
            { 120, 210, 102, 21, 201, 12, },
            { 201, 21, 210, 102, 12, 120, } };
    
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
    
    private static class GroupS3 {
        final Map<Integer,Permutation> members = new HashMap<Integer,Permutation>();
        final Map<Permutation,Integer> names = new HashMap<Permutation,Integer>();

        public void store(int name, Permutation value) {
            members.put(name, value);
            names.put(value, name);
        }
        
        public int id(Integer perm) {
            for (int i=0;i<s3members.length;i++ ) {
                if (s3members[i]==perm) {
                    return i;
                }
            }
            throw new IllegalArgumentException();
        }
        
        
        
        public void inverses() {
            for (Integer nm: members.keySet()) {
                int x = id(nm);
                int inv = id(name(get(nm).inverse()));
                Assert.assertEquals(s3[x][inv],12);
                Assert.assertEquals(s3[inv][x],12);
            }
        }

        public void times() {
            for (Integer a: members.keySet()) {
                for (Integer b: members.keySet())
                    Assert.assertEquals(s3[id(a)][id(b)],(int)name(get(a).and(get(b))));
            }
        }

        private Integer name(Permutation p) {
            return names.get(p);
        }

        private Permutation get(Integer nm) {
            return members.get(nm);
        }
        
    }
    @Test
    public void testS3() {
        GroupS3 g = new GroupS3();
        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++) 
                if (i!=j) {
                    for (int k=0;k<3;k++) {
                        if (i!=k && j!=k) {
                            g.store(i*100+j*10+k, new Permutation(i,j,k));
                        }
                    }
                }
        }
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

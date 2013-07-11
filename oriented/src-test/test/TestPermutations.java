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
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.OM;

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

    
    @Test
    public void testExampleChapter1() {
        testPerms(Examples.chapter1);
    }

    private void testPerms(OM om) {
        for (Permutation p:Permutation.all(om.n())) {
            testPerm(om.getCircuits(),p);
            testPerm(om.getChirotope(),p);
        }
    }

    private void testPerm(OM om, Permutation p) {
        Assert.assertEquals(om, om.permuteGround(p));
    }

 
    @Test
    public void testCountSymmetriesChapter1() {
        countSymmetries(0,Examples.chapter1);
    }
    @Test
    public void testCountSymmetriesCircularSaw() {
        countSymmetries(0,Examples.circularSaw3);
    }

    private void countSymmetries(int expected, OM om) {
        Map<OM,Set<OM>> symmetries = new HashMap<OM,Set<OM>>();
        for (Permutation p: Permutation.all(om.n())) {
            OM permuted = om.permute(p);
            Set<OM> equivalent = symmetries.get(permuted);
            if (equivalent == null) {
                equivalent = new HashSet<OM>();
                symmetries.put(permuted, equivalent);
            }
            equivalent.add(permuted);
        }
        for (Set<OM> equivalent: symmetries.values()) {
            System.err.print(equivalent.size()+", ");
            for (OM a: equivalent) 
                for (OM b:equivalent)
                    Assert.assertEquals(a, b);
        }
        System.err.println();
        Assert.assertEquals(expected, symmetries.size());
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

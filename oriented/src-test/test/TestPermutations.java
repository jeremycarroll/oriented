/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import net.sf.oriented.combinatorics.Permutation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMS;
import net.sf.oriented.omi.Options;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.om.OMInternal;
import net.sf.oriented.omi.impl.set.SetOfSignedSetInternal;

import org.junit.Test;

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
        Assert.assertEquals(factorial, sofar.size());
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

    /**
     * Swapping 1 with 6 and 2 with 5 in the definition of chapter1 example
     * leaves it unchanged.
     */
    @Test
    public void testSym16_25_chap1() {
        // note off by one issue ... :(
        testSym(Examples.chapter1(),new Permutation(6,new int[][]{ { 0, 5 }, {1, 4} }),true);
        
    }

    private void testSym(OM om, Permutation p, boolean equalOrNot) {
        Assert.assertEquals(equalOrNot, om.equals(om.permute(p)));
        
    }

    /**
     * Swapping 1 with 6 in the definition of chapter1 example
     * makes a real changed.
     */
    @Test
    public void testNotSym16_chap1() {
        testSym(Examples.chapter1(),new Permutation(6,new int[][]{ { 0, 5 } }),false);
        
    }
    
    SetOfSignedSet setsWithZeroOrOneMinus(OMS om) {
        FactoryFactory f = ((OMInternal)om).ffactory();
        @SuppressWarnings("unchecked")
        JavaSet<SignedSet> rslt = (JavaSet<SignedSet>) f.signedSets().emptyCollectionOf();
        for (SignedSet ss:(SetOfSignedSetInternal)om) {
            if (ss.minus().size()<=1) {
                rslt.add(ss);
            }
        }
        return f.setsOfSignedSet().copyBackingCollection(rslt);
    }
    
    @Test
    public void testRingel() {
        OM ringel = Examples.ringel();
        checkCentralFaceBoundary(ringel,"1","6","7","0","9");
        FactoryFactory ffactory = ((OMInternal)ringel).ffactory();
        OM reoriented = ringel.reorient(strings2labels(ffactory,"1","6","3","5"));
        checkCentralFaceBoundary(reoriented,"2","5","7");
    }

    private void checkCentralFaceBoundary(OM om, String ... boundary) {
        FactoryFactory ffactory = ((OMInternal)om).ffactory();
        Assert.assertTrue(om.isAcyclic());
        SetOfSignedSet centralFaceAndAdjacents = setsWithZeroOrOneMinus(om.dual().getMaxVectors());
        containsAllPositive(centralFaceAndAdjacents);
        Assert.assertEquals(unionNegatives(centralFaceAndAdjacents),
                makeUnsignedSet(ffactory,boundary));
    }
    private UnsignedSet makeUnsignedSet(FactoryFactory ffactory, String ... labelStrings ) {
        Label[] labels = strings2labels(ffactory, labelStrings);
        return ffactory.unsignedSets().copyBackingCollection(Arrays.asList(labels));
    }

    private Label[] strings2labels(FactoryFactory ffactory, String ... labelStrings) {
        Label labels[] = new Label[labelStrings.length];
        for (int i=0;i<labels.length;i++) {
            labels[i] = ffactory.labels().parse(labelStrings[i]);
        }
        return labels;
    }

    private UnsignedSet unionNegatives(SetOfSignedSet setOfSS) {
        UnsignedSet rslt = null;
        for (SignedSet ss : (SetOfSignedSetInternal)setOfSS) {
            if (rslt == null) {
                rslt = ss.minus();
            } else {
                rslt = rslt.union(ss.minus());
            }
        }
        return rslt;
    }

    private void containsAllPositive(SetOfSignedSet setOfSS) {
        for (SignedSet ss : (SetOfSignedSetInternal)setOfSS) {
            if (ss.minus().isEmpty()) {
                return;
            }
        }
        Assert.fail("Did not find the all positive max vector");
    }

    @Test
    public void testAutomorphismsChap1() {
        Assert.assertEquals(2,automorphisms(Examples.chapter1()));
    }

 

    @Test
    public void testTopesAutomorphismsChap1Chirotope() {
        OM chapter1 = Examples.chapter1();
        Label g[] = chapter1.ground();
        OM reorientedB = chapter1.getChirotope().reorient(g[2],g[4]);
        Assert.assertFalse(chapter1.dual().getMaxVectors().sameSetAs(reorientedB.dual().getMaxVectors()));
    }
    
    @Test
    public void testTopesAutomorphismsChap1Circuits() {
        OM chapter1 = Examples.chapter1();

        Label g[] = chapter1.ground();
        OM reorientedB = chapter1.getCircuits().reorient(g[2],g[4]);
        
        Assert.assertFalse(chapter1.dual().getMaxVectors().sameSetAs(reorientedB.dual().getMaxVectors()));
    }
    @Test
    public void testAutomorphismsSaw3() {
        automorphisms(Examples.circularsaw3());
    }

    @Test
    public void testAutomorphismsUniform4() {
        Assert.assertEquals(8,automorphisms(Examples.uniform4()));
    }
    private int automorphisms(OM om) {
        int cnt = 0;
        List<OM> same = new ArrayList<OM>();
        Set<Permutation> auto1 = new HashSet<Permutation>();
        for (Permutation p: Permutation.all(om.n())) {
            OM permuted = om.permute(p);
            if (permuted.equals(om)) {
                same.add(permuted);
                auto1.add(p);
                cnt++;
            }
        }
        Assert.assertEquals(auto1.size(),om.automorphisms().order());
        
        for (Permutation p:om.automorphisms()) {
            Assert.assertTrue(auto1.contains(p));
        }
        return cnt;
        
    }
    
    @Test
    public void testReorientation() {
        testReorientation(Examples.circularsaw3(), 1, 60, 3, 4);
    }
    @Test
    public void testReorientationHash() {
        testReorientation(new FactoryFactory(new Options(Options.Impl.hash)).circuits().remake(Examples.circularsaw3().getCircuits()),
                1, 60, 3, 4);
    }
    
    @Test
    public void testReorientationChap1() {
        testReorientation(Examples.chapter1(), 2, 24, 3, 8);
    }
    private void testReorientation(OM om, int ... expected) {
        @SuppressWarnings("unused")
        long circuitsTime = 0;
        @SuppressWarnings("unused")
        long chiroTime = 0;
        @SuppressWarnings("unused")
        long autoTime = 0;
        Label g[] = om.ground();
        int counts[] = new int[250];
        int bitcnt[] = new int[]{ 
                 0,  // 000
                 1, 
                 1,  // 010
                 2,
                 1,  // 100
                 2, 
                 2,  // 110
                 3
        };
        int nn = om.n()-1;
        Assert.assertTrue(nn<12);
        for (int i=0;i<(1<<nn);i++) {
            int cnt = bitcnt[i&7] + bitcnt[(i>>3)&7] + bitcnt[(i>>6)&7] + bitcnt[(i>>9)&7];
            Label axes[] = new Label[cnt];
            int k = 0;
            for (int j=0;j<nn;j++) {
                if ( ( i& (1<<j) ) != 0 ) {
                    axes[k++] = g[j+1];
                }
            }
            circuitsTime -= System.nanoTime();
            OM reorientedA = om.getCircuits().reorient(axes);
            if (om.dual().getMaxVectors().sameSetAs(reorientedA.dual().getMaxVectors())) {
                System.err.print("Auto: " + Integer.toString(i, 2));
                for (Label l : axes) {
                    System.err.print(" "+l.label());
                }
                System.err.println();
            }
            Assert.assertFalse( i!=0 && om.dual().getMaxVectors().sameSetAs(reorientedA.dual().getMaxVectors()));
            circuitsTime += System.nanoTime();
            chiroTime -= System.nanoTime();
            OM reorientedB = om.getChirotope().reorient(axes);
            Assert.assertFalse( i!=0 && om.dual().getMaxVectors().sameSetAs(reorientedB.dual().getMaxVectors()));
            
            chiroTime += System.nanoTime();
            Assert.assertEquals(reorientedA, reorientedB);
 //           System.err.println(this.stabilizer(reoriented));
            autoTime -= System.nanoTime();
            int sz = (int) reorientedA.automorphisms().order();
            autoTime += System.nanoTime();
            counts[sz]++;
            
        }
        int k=0;
        for (int j=0;j<counts.length;j++)
            if (counts[j]!=0) {
//                System.err.println(j+" "+counts[j]);
                Assert.assertEquals(expected[k++], j);
                Assert.assertEquals(expected[k++], counts[j]);
            }
//        System.err.println("Circuits: "+circuitsTime);
//        System.err.println("Chiro:    "+chiroTime);
//        System.err.println("Auto:     "+autoTime);
    }

    @Test
    public void testPermuteGroundExampleChapter1() {
        testPerms(Examples.chapter1());
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

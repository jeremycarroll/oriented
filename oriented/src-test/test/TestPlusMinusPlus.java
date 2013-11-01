/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import junit.framework.Assert;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.SignedSetFactory;
import net.sf.oriented.pseudoline.PlusMinusPlus;

import org.junit.Test;

import com.google.common.collect.Iterables;

public class TestPlusMinusPlus {
    
    @Test
    public void test3() {
        test(3,2);
    }

    @Test
    public void test4() {
        test(4,8);
    }
    @Test
    public void test5() {
        test(5,22);
    }

    @Test
    public void test6() {
        test(6,52);
    }
    @Test
    public void test7() {
        test(7,114);
    }
    @Test
    public void test8() {
        test(8,240);
    }
    @Test
    public void test9() {
        test(9,494);
    }
    
    @Test
    public void checkExpectedLessThanArraySize() {
        for (int i=3;i<50;i++) {
            Assert.assertTrue(i + " and "+computeExpected(i),computeExpected(i)<(1l<<(i)));
        }
    }

    private void test(int sz, int cnt) {
        Assert.assertEquals(cnt, Iterables.size(PlusMinusPlus.get(sz)));
        int exp = (int)computeExpected(sz);
        Assert.assertEquals(cnt,exp);
    }

    private long computeExpected(int sz) {
        long exp = 2;
        for (int i=3;i<sz;i++) {
            exp = 2 * exp + (2*(i-1));
        }
        return exp;
    }
    
    @Test
    public void test_nA_B_nC_D() {
        int split[][] = PlusMinusPlus.testSplitIntoThrees(4, 5);
        Assert.assertEquals(1,split.length);
        Assert.assertEquals(2,split[0].length);
        Assert.assertEquals(7,split[0][0]);
        Assert.assertEquals(14,split[0][1]);
    }

    @Test
    public void test_nA_B_C_nD_E_F() {
        int split[][] = PlusMinusPlus.testSplitIntoThrees(6, 54);
        Assert.assertEquals(2,split.length);
        Assert.assertEquals(3,split[0].length);
        Assert.assertEquals(3,split[1].length);
        Assert.assertEquals(9,split[0][0] & ~ 6);
        Assert.assertEquals(9,split[1][0] & ~ 6);
    }
    @Test
    public void test_A_nB_C_nD() {
        int split[][] = PlusMinusPlus.testSplitIntoThrees(4, 10);
        Assert.assertEquals(1,split.length);
        Assert.assertEquals(2,split[0].length);
        Assert.assertEquals(7,split[0][0]);
        Assert.assertEquals(14,split[0][1]);
    }
    @Test
    public void testSplitIntoThrees() {
        int total = 0;
        OM ceva = Examples.ceva();
        Label infinity = ceva.elements()[0];
        SetOfSignedSet sSets = ceva.getVectors();
        SignedSetFactory factory = ceva.ffactory().signedSets();
        for (SignedSet ss:sSets) {
            if (ss.sign(infinity) == -1) {
                continue;
            }
            ss = factory.construct(ss.plus().minus(infinity), ss.minus().minus(infinity));
            SignedSet aXs[][] = PlusMinusPlus.splitIntoThrees(ceva, ss);
            total += aXs.length;
//            System.err.println(ss);
//            for (int i=0;i<aXs.length;i++) {
//                System.err.println("    "+i+":");
//                for (SignedSet t:aXs[i]) {
//                    System.err.println("       "+t);
//                }
//            }
        }
        Assert.assertEquals(657, total); // the exact number is not very useful: I don't really know if it is correct
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

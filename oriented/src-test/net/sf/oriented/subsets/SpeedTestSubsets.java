/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized;
import test.BetterParameterized.TestName;


@RunWith(value = BetterParameterized.class)
public class SpeedTestSubsets {
    @Parameters
    public static Collection<Object[]> data() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException {
        List<Object[]> rslt = new ArrayList<Object[]>();
        ObjectInputStream in = new ObjectInputStream(SpeedTestSubsets.class.getResourceAsStream("tests.ser"));
        int nAnswers[] = {287, 49, 262, 263, 51, 51, 7, 9037, 11420, 396095, 505652, 16 };
        int cnt = 0;
        while (true) {
            int sz;
            String name;
            try {
                name = in.readUTF();
            }
            catch (EOFException e) {
                break;
            }
            int numberOfAnswers = nAnswers[cnt++];
            int bits = in.readInt();
            sz = in.readInt();
            BitSet sets[] = new BitSet[sz];
            for (int i=0;i<sz;i++) {
                sets[i] = (BitSet) in.readObject();
            }
            for (String method: new String[]{
//                    "naive",
                    //"mcdaid", 
                    "amsCard", 
                    "parallel"
                    })
            for (Preparation prep: //Preparation.values()
                    new Preparation[]{Preparation.Pritchard
//            Preparation.Minimal       
            }
                    ) {
                 {
                     if (method.equals("naive") && name.contains("*B")) {
                         continue;
                     }
                    Method m = MinimalSubsetFactory.class.getMethod(method);
                    rslt.add(new Object[]{m,prep,name,bits,cnt,sets,numberOfAnswers});
                }
            }
        }
        return rslt;
    }

    private final BitSet[] data;
    private final String name;
    private final int expected;
    private final Preparation prep;
    public SpeedTestSubsets(Method m, Preparation prep, String nme, int bits, int n, BitSet sets[], int expected) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        algorithm = (MinimalSubsets) m.invoke(null);
        data = sets;
        name = name(m,prep, nme,bits,n,sets, expected);
        this.expected = expected;
        this.prep = prep;
    }

    final MinimalSubsets algorithm;
    @TestName
    static public String name(Method m, Preparation prep, String name, int bits, int n, BitSet sets[], int expected) {
        return m.getName()+"-"+prep+"-"+name+"-bits-"+bits+"-sets-"+n+"-"+sets.length;
    }

    @Test
    public void go() {

//        Assume.assumeTrue(name.contains("tsukamoto13.-1*A"));
        Assume.assumeTrue(!name.contains("naive-tsu"));
        
        Assume.assumeTrue(!(name.contains("amsCard-tsu")&&name.contains("B"))); 
               
        BitSet copyClearedBit[] = new BitSet[data.length];
        for (int i=0; i<data.length; i++) {
            copyClearedBit[i] = (BitSet) data[i].clone();
            copyClearedBit[i].clear(0);
        }
        long start = System.currentTimeMillis();
        List<BitSet> rslt = algorithm.minimal(Arrays.asList(copyClearedBit),prep);
        System.err.println(name+" "+rslt.size()+" answers");
        long test = System.currentTimeMillis() ;
        System.err.println(name+" "+(test-start));
        Assert.assertEquals(expected, rslt.size());
//        long expected[][] = new long[rslt.size()][];
//        int i = 0;
//        for (BitSet b:data) {
//            if (!b.get(0)) {
//                if (i==expected.length) {
//                    Assert.fail("Too few results");
//                }
//                expected[i++] = b.toLongArray();
//            }
//        }
//        if (i<expected.length) {
//            Assert.fail("Too many results");
//        }
//        long actual[][] = new long[rslt.size()][];
//        i = 0;
//        for (BitSet b:rslt) {
//            actual[i++] = b.toLongArray();
//        }
//
//        long reformat = System.currentTimeMillis();
//        Arrays.sort(expected, Longs.lexicographicalComparator());
//        Arrays.sort(actual, Longs.lexicographicalComparator());
//        long sort = System.currentTimeMillis() ;
//        for (i=0;i<actual.length;i++) {
//            Assert.assertArrayEquals(expected[i], actual[i]);
//        }
//        long comp = System.currentTimeMillis() ;
////        if (test-start>100) {
////            System.err.println("t "+(test-start)+" r "+(reformat-test)+" s "+(sort-reformat)+" c "+ (comp-sort));
////        }
    }

}
/* [287, 49, 262, 263, 51, 51, 7, 9037, 11420, 396095, 505652, 16 ]
 * naive-circularsaw5*0-bits-230-sets-1-423 287 answers
naive-circularsaw5*0-bits-230-sets-1-423 14
mcdaid-circularsaw5*0-bits-230-sets-1-423 287 answers
mcdaid-circularsaw5*0-bits-230-sets-1-423 72
naive-circularsaw5*A-bits-268-sets-2-61 49 answers
naive-circularsaw5*A-bits-268-sets-2-61 1
mcdaid-circularsaw5*A-bits-268-sets-2-61 49 answers
mcdaid-circularsaw5*A-bits-268-sets-2-61 2
naive-_saw5A*0-bits-265-sets-3-315 262 answers
naive-_saw5A*0-bits-265-sets-3-315 5
mcdaid-_saw5A*0-bits-265-sets-3-315 262 answers
mcdaid-_saw5A*0-bits-265-sets-3-315 8
naive-_deformSaw5*0-bits-235-sets-4-377 263 answers
naive-_deformSaw5*0-bits-235-sets-4-377 2
mcdaid-_deformSaw5*0-bits-235-sets-4-377 263 answers
mcdaid-_deformSaw5*0-bits-235-sets-4-377 8
naive-_deformSaw5*A-bits-276-sets-5-56 51 answers
naive-_deformSaw5*A-bits-276-sets-5-56 1
mcdaid-_deformSaw5*A-bits-276-sets-5-56 51 answers
mcdaid-_deformSaw5*A-bits-276-sets-5-56 1
naive-_deformSaw5*B-bits-258-sets-6-73 51 answers
naive-_deformSaw5*B-bits-258-sets-6-73 1
mcdaid-_deformSaw5*B-bits-258-sets-6-73 51 answers
mcdaid-_deformSaw5*B-bits-258-sets-6-73 1
naive-pappus*0-bits-74-sets-7-8 7 answers
naive-pappus*0-bits-74-sets-7-8 0
mcdaid-pappus*0-bits-74-sets-7-8 7 answers
mcdaid-pappus*0-bits-74-sets-7-8 0
mcdaid-tsukamoto13.+1*A-bits-360-sets-8-15362 9037 answers
mcdaid-tsukamoto13.+1*A-bits-360-sets-8-15362 434
mcdaid-tsukamoto13.-1*A-bits-364-sets-9-23369 11420 answers
mcdaid-tsukamoto13.-1*A-bits-364-sets-9-23369 506
mcdaid-tsukamoto13.+1*B-bits-488-sets-10-1145894 396095 answers
mcdaid-tsukamoto13.+1*B-bits-488-sets-10-1145894 182807
mcdaid-tsukamoto13.-1*B-bits-488-sets-11-1581601 505652 answers
mcdaid-tsukamoto13.-1*B-bits-488-sets-11-1581601 307483
naive-ringel*0-bits-90-sets-12-16 16 answers
naive-ringel*0-bits-90-sets-12-16 2
mcdaid-ringel*0-bits-90-sets-12-16 16 answers
mcdaid-ringel*0-bits-90-sets-12-16 0
 */

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

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.primitives.Longs;

import test.BetterParameterized;
import test.BetterParameterized.TestName;


@RunWith(value = BetterParameterized.class)
public class SpeedTestSubsets {
    @Parameters
    public static Collection<Object[]> data() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException {
        List<Object[]> rslt = new ArrayList<Object[]>();
        ObjectInputStream in = new ObjectInputStream(SpeedTestSubsets.class.getResourceAsStream("tests.ser"));
        int cnt = 0;
        while (true) {
            int sz;
            cnt++;
            try {
                sz = in.readInt();
            }
            catch (EOFException e) {
                break;
            }
            BitSet sets[] = new BitSet[sz];
            for (int i=0;i<sz;i++) {
                sets[i] = (BitSet) in.readObject();
            }
            for (String method: new String[]{"naive", "mcdaid"}) {
                Method m = MinimalSubsetFactory.class.getMethod(method);
                rslt.add(new Object[]{m,cnt,sets});
            }
        }
        return rslt;
    }

    private final BitSet[] data;
    private final String name;
    public SpeedTestSubsets(Method m, int n, BitSet sets[]) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        algorithm = (MinimalSubsets) m.invoke(null);
        data = sets;
        name = name(m,n,sets);
    }

    final MinimalSubsets algorithm;
    @TestName
    static public String name(Method m, int n, BitSet sets[]) {
        return m.getName()+"-sets-"+n+"-"+sets.length;
    }

    @Test
    public void go() {
        BitSet copyClearedBit[] = new BitSet[data.length];
        for (int i=0; i<data.length; i++) {
            copyClearedBit[i] = (BitSet) data[i].clone();
            copyClearedBit[i].clear(0);
        }
        long start = System.currentTimeMillis();
        List<BitSet> rslt = algorithm.minimal(Arrays.asList(copyClearedBit));
        long test = System.currentTimeMillis() ;
        System.err.println(name+" "+(test-start));
        long expected[][] = new long[rslt.size()][];
        int i = 0;
        for (BitSet b:data) {
            if (!b.get(0)) {
                if (i==expected.length) {
                    Assert.fail("Too few results");
                }
                expected[i++] = b.toLongArray();
            }
        }
        if (i<expected.length) {
            Assert.fail("Too many results");
        }
        long actual[][] = new long[rslt.size()][];
        i = 0;
        for (BitSet b:rslt) {
            actual[i++] = b.toLongArray();
        }

        long reformat = System.currentTimeMillis();
        Arrays.sort(expected, Longs.lexicographicalComparator());
        Arrays.sort(actual, Longs.lexicographicalComparator());
        long sort = System.currentTimeMillis() ;
        for (i=0;i<actual.length;i++) {
            Assert.assertArrayEquals(expected[i], actual[i]);
        }
        long comp = System.currentTimeMillis() ;
//        if (test-start>100) {
//            System.err.println("t "+(test-start)+" r "+(reformat-test)+" s "+(sort-reformat)+" c "+ (comp-sort));
//        }
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

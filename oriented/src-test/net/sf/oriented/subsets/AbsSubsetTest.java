/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.io.EOFException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

import test.BetterParameterized;
import test.BetterParameterized.TestName;

@RunWith(value = BetterParameterized.class)
public class AbsSubsetTest {
    public  AbsSubsetTest(Method m, Preparation prep, String nme, int bits, int n, BitSet sets[], int expected) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        algorithm = (MinimalSubsets) m.invoke(null);
        data = sets;
        name = name(m,prep, nme,bits,n,sets, expected);
        this.expected = expected;
        this.prep = prep;
    }
    
    static Method[] getMethods(Class<?> clazz, String ... names) throws NoSuchMethodException, SecurityException {
        Method rslt[] = new Method[names.length];
        for (int i=0;i<names.length;i++) {
            rslt[i] = clazz.getMethod(names[i]);
        }
        return rslt;
    }

    protected static Collection<Object[]> data(final String testData, final String copyFileName, final Method methods[],
            Preparation preps[]) throws IOException,
            ClassNotFoundException {
                @SuppressWarnings("resource")
                ObjectOutputStream copyFile = copyFileName==null ? null : new ObjectOutputStream(new FileOutputStream( copyFileName));
                List<Object[]> rslt = new ArrayList<>();
                ObjectInputStream in = new ObjectInputStream(SpeedTestSubsets.class.getResourceAsStream(testData));
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
                    int bits = in.readInt();
                    int numberOfAnswers = in.readInt();
                    sz = in.readInt();
                    BitSet sets[] = new BitSet[sz];
                    for (int i=0;i<sz;i++) {
                        sets[i] = (BitSet) in.readObject();
                    }
                    if (copyFile!=null) {
                        copyFile.writeUTF(name);
                        copyFile.writeInt(bits);
                        copyFile.writeInt(numberOfAnswers);
                        copyFile.writeInt(sz);
                        for (int i=0;i<sz;i++) {
                            copyFile.writeObject(sets[i]);
                        }
                    }
                    for (Method m: methods)
                    for (Preparation prep: preps ) {
                         {
                            rslt.add(new Object[]{m,prep,name,bits,cnt,sets,numberOfAnswers});
                        }
                    }
                }
                if (copyFile != null) {
                    copyFile.close();
                }
                return rslt;
            }

    protected final BitSet[] data;
    protected final String name;
    protected final int expected;
    protected final Preparation prep;
    protected final MinimalSubsets algorithm;

    @TestName
    public static String name(Method m, Preparation prep, String name,
            int bits, int n, BitSet sets[], int expected) {
                return m.getName()+"-"+prep+"-"+name+"-bits-"+bits+"-sets-"+n+"-"+sets.length;
            }

    @Test
    public void go() {
                   
            BitSet copyClearedBit[] = new BitSet[data.length];
            for (int i=0; i<data.length; i++) {
                copyClearedBit[i] = (BitSet) data[i].clone();
                copyClearedBit[i].clear(0);
            }
//            long start = System.currentTimeMillis();
            List<BitSet> rslt = algorithm.minimal(Arrays.asList(copyClearedBit),prep);
//            long test = System.currentTimeMillis() ;
            Assert.assertEquals(expected, rslt.size());
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

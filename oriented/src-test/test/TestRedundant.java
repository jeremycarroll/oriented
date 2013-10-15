/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import junit.framework.Assert;
import net.sf.oriented.pseudoline.PlusMinusPlus;

import org.junit.Test;

public class TestRedundant {
    
    @Test
    public void test3() {
        test(3);
    }

    @Test
    public void test4() {
        test(4);
    }
    @Test
    public void test5() {
        test(5);
    }
    private void test(int i) {
        for (boolean x[]: PlusMinusPlus.get(i)) {
            test(x);
        }
    }

    private void test(boolean[] x) {
//        if (x[0] != x[x.length-1]) {
//            return;
//        }
        boolean actual[] = PlusMinusPlus.redundant(x);
        int count = 0;
        int useCount[] = new int[x.length];
        for (int i=0;i<x.length;i++) {
            for (int j=i+1;j<x.length;j++) {
                if (x[j] == x[i]) continue;
                for (int k=j+1;k<x.length;k++) {
                    if (x[j] == x[k]) continue;
                    count++;
                    useCount[i]++;
                    useCount[j]++;
                    useCount[k]++;
                }
            }
        }
        for (int i=0;i<actual.length;i++) {
            Assert.assertEquals(dump(x,"+","-")+":"+dump(actual,"?","#"),useCount[i]!=count, actual[i]);
        }
        
    }
    


    private static String dump(boolean[] x,String tt, String ff) {
        StringBuffer rslt = new StringBuffer();
        for (boolean t:x) {
            rslt.append(t?tt:ff);
        }
        return rslt.toString();
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

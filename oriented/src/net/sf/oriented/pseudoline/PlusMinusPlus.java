/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.Arrays;
import java.util.Iterator;

import com.google.common.base.Preconditions;

public class PlusMinusPlus implements Iterable<boolean[]>{
    private static PlusMinusPlus[] instances = new PlusMinusPlus[32];
    private final int patterns[];
    private final int size;
    private final int n;
    
    
    private PlusMinusPlus(int n) {
        this.n = n;
        patterns = new int[computeSize(n)]; // a little less than 2^^n
        int ix = initialize();
        for (int i=0;i<ix;i++) {
            int next = rotate(patterns[i]);
            if (!found(ix, next)) {
                patterns[ix++] = next;
            }
        }
        size = ix;
        if (size != patterns.length) {
            throw new IllegalStateException("Maths was wrong");
        }
    }


    private static int computeSize(int sz) {
        int exp = 2;
        for (int i=3;i<sz;i++) {
            exp = 2 * exp + (2*(i-1));
        }
        return exp;
    }

    /**
     * takes the first bit, and moves it to be the nth bit, but flipped.
     * @param pattern
     * @return
     */
    private int rotate(int pattern) {
        int bit = pattern & 1;
        return (pattern >> 1) | ((1-bit)<<(n-1));
    }



    private boolean found(int ix,int next) {
        for (int j=0;j<ix;j++) {
            if (next == patterns[j]) {
                return true;
            }
        }
        return false;
    }



    private int initialize() {
        // Following lines are insufficient.
//        patterns[0] = 1 | (1<<n-1);
//        return 1;
        int ix=0;
        for (int i=2;i<(1<<(n-1));i+=2) {
            patterns[ix++] = i;
        }
        return ix;
    }



    @Override
    public Iterator<boolean[]> iterator() {
        final boolean rslt[] = new boolean[n];
        return new Iterator<boolean[]>(){
            int cnt = 0;

            @Override
            public boolean hasNext() {
                return cnt < size;
            }

            @Override
            public boolean[] next() {
                int pattern = patterns[cnt++];
                for (int j=0;j<rslt.length;j++) {
                    rslt[j] = ((pattern >> j)&1) == 1;
                }
                return rslt;
            }

            @Override
            public void remove() {
               throw new UnsupportedOperationException();
            }};
    }
    
    public static PlusMinusPlus get(int sz) {
        Preconditions.checkArgument(sz>=3);
        Preconditions.checkArgument(sz<32,"Size limitation");
        if (instances[sz]==null) {
            instances[sz] = new PlusMinusPlus(sz);
        }
        return instances[sz];
    }
    
    public static boolean[] redundant(boolean pmp[]) {
//        int firstFalse = -1;
//        int firstTrue = -1;
//        int lastFalse = -1;
//        int lastTrue = -1;
//        for (int i=0;i<pmp.length;i++) {
//            if (pmp[i]) {
//                lastTrue = i;
//                if (firstTrue == -1) {
//                    firstTrue = i;
//                }
//            } else {
//                lastFalse = i;
//                if (firstFalse == -1) {
//                    firstFalse = i;
//                }
//            }
//        }
//        if (lastTrue == -1 || lastFalse == -1) {
//            throw new IllegalArgumentException("Not pmp");
//        }
//        boolean rslt[] = new boolean[pmp.length];
//        Arrays.fill(rslt, true);
//        
//        if (firstTrue == 1 || firstFalse == 1) {
//            rslt[0] = false;
//        }
//        int penUltimate = pmp.length - 2;
//        if (lastTrue == penUltimate || lastFalse == penUltimate) {
//            rslt[penUltimate+1] = false;
//        }
//        if (firstTrue == lastTrue) {
//            rslt[firstTrue] = false;
//        }
//        if (firstFalse == lastFalse) {
//            rslt[firstFalse] = false;
//        }
//        
        boolean rslt[] = new boolean[pmp.length];
        int firstOpposite = 1;
        if (pmp[0] == pmp[1]) {
            rslt[0] = true;
            for ( ;firstOpposite<pmp.length;firstOpposite++) {
                if (pmp[firstOpposite] == pmp[0]) {
                    rslt[firstOpposite] = true;
                } else {
                    break;
                }
            }
        } else {
            // look for pmp starting at 1
            rslt[0] = hasPmpStartingAt(pmp,1);
        }
        
        
        return rslt;
    }


    protected static boolean hasPmpStartingAt(boolean[] pmp, int start) {
        int j;
        for (j=start+1;j<pmp.length;j++) {
            if (pmp[j] != pmp[start]) {
                break;
            }
        }
        if (j==pmp.length) {
            return false;
        }
        for (j++;j<pmp.length;j++) {
            if (pmp[j] == pmp[start]) {
                return true;
            }
        }
        return false;
    }

    public static void main(String args[]) {
        for (int i=3;i<7;i++) {
            int cnt = 0;
            System.out.println("== "+i+" ==");
            for (boolean x[]:new PlusMinusPlus(i)) {
                dump(x,"+","-");
                dump(PlusMinusPlus.redundant(x),"?","#");
                cnt++;
            }
            System.out.println("** "+i+" ** "+cnt);
            
        }
        
    }


    protected static void dump(boolean[] x,String tt, String ff) {
        for (boolean t:x) {
            System.out.print(t?tt:ff);
        }
        System.out.println();
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

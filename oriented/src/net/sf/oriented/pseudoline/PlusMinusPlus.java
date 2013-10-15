/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

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
    
    /**
     * The ith element in the result is true if the ith element of the argument
     * is used in every plus-minus-plus or minus-plus-minus subsequence.
     * @param pmp
     * @return
     */
    public static boolean[] required(boolean pmp[]) {
        int firstFalse = -1;
        int firstTrue = -1;
        int lastFalse = -1;
        int lastTrue = -1;
        int flips = 0;
        boolean last = pmp[0];
        for (int i=0;i<pmp.length;i++) {
            if (pmp[i]) {
                lastTrue = i;
                if (firstTrue == -1) {
                    firstTrue = i;
                }
            } else {
                lastFalse = i;
                if (firstFalse == -1) {
                    firstFalse = i;
                }
            }
            if (last != pmp[i]) {
                flips++;
                last = pmp[i];
            }
        }

        switch (flips) {
        case 0:
        case 1:
            throw new IllegalArgumentException("Not pmp");
        case 2:
            return required2(pmp, firstFalse, lastFalse, firstTrue, lastTrue);
        case 3:
            return required3(pmp, firstFalse, lastFalse, firstTrue, lastTrue);
        default:
            return new boolean[pmp.length];
        }
    }


    /**
     * 
     * @param pmp A sequence of true followed by a sequence of false followed by a sequence of true followed by a sequence of false,
     *    or flipped.
     * @param firstFalse
     * @param lastFalse
     * @param firstTrue
     * @param lastTrue
     * @return
     */
    private static boolean[] required3(boolean[] pmp, int firstFalse,
            int lastFalse, int firstTrue, int lastTrue) {
        if (pmp[0]) {
            return required3(pmp,firstFalse,lastTrue);
        } else {
            return required3(pmp,firstTrue,lastFalse);
        }
    }


    /**
     * 
     * @param pmp
     * @param second  The index of the beginning of the second sequence
     * @param penultimate The index of the last of the third sequence
     * @return
     */
    private static boolean[] required3(boolean[] pmp, int second,
            int penultimate) {
        boolean rslt[] = new boolean[pmp.length];
        
        if (pmp[second] != pmp[second+1]) {
            rslt[second] = true;
        }
        if (pmp[penultimate] != pmp[penultimate-1]) {
            rslt[penultimate] = true;
        }
        return rslt;
    }


    /**
     * 
     * @param pmp A sequence of true followed by a sequence of false followed by a sequence of true,
     *    or flipped.
     * @param firstFalse
     * @param lastFalse
     * @param firstTrue
     * @param lastTrue
     * @return
     */
    private static boolean[] required2(boolean[] pmp, int firstFalse,
            int lastFalse, int firstTrue, int lastTrue) {
        boolean rslt[] = new boolean[pmp.length];
        if (firstFalse == 1 || firstTrue == 1) {
            rslt[0] = true;
        }
        if (lastFalse == pmp.length-2 || lastTrue == pmp.length - 2) {
            rslt[pmp.length-1] = true;
        }
        if (firstFalse==lastFalse) {
            rslt[firstFalse]  = true;
        }
        if (firstTrue==lastTrue) {
            rslt[firstTrue]  = true;
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
                dump(PlusMinusPlus.required(x),"?","#");
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

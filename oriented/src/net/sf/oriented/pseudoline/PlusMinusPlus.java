/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

public class PlusMinusPlus implements Iterable<boolean[]>{
    private static PlusMinusPlus[] instances = new PlusMinusPlus[32];
    private final int patterns[];
    private final int splitIntoThrees[][][];
    private final int size;
    private final int n;
    
    
    private PlusMinusPlus(int n) {
        this.n = n;
        patterns = new int[computeSize(n)]; // a little less than 2^^n
        splitIntoThrees = new int[patterns.length][][];
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
        Arrays.sort(patterns);
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

    public static SignedSet[][] splitIntoThrees(OM om, SignedSet splitMe) {
        int plus[] = om.asInt(splitMe.plus());
        int minus[] = om.asInt(splitMe.minus());
        if (minus.length == 0) {
            return new SignedSet[0][];
        }
        if (minus[0] == 0) {
            throw new IllegalArgumentException("Not supported - first element of om must be infinity");
        }
        Integer all[] = new Integer[plus.length+minus.length];
        PlusMinusPlus pmp = get(all.length);
        for (int i=0;i<minus.length;i++) {
            all[i] = -minus[i];
        }
        for (int i=0;i<plus.length;i++) {
            all[i+minus.length] = plus[i];
        }
        Arrays.sort(all,new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                return Math.abs(o1) - Math.abs(o2);
            }});
        int pattern = 0;
        for (int i=0;i<all.length;i++) {
            if (all[i]>0) {
                pattern |= (1<<i);
            }
        }
        int split[][] = pmp.splitIntoThrees(pattern);
        SignedSet rslt[][] = new SignedSet[split.length][];
        for (int i=0;i<rslt.length;i++) {
            rslt[i] = new SignedSet[split[i].length];
            for (int j=0;j<rslt[i].length;j++) {
                rslt[i][j] = pmp.toSignedSet(split[i][j],pattern, all,om);
            }
        }
        return rslt;
    }
    private SignedSet toSignedSet(int mask, int pattern, Integer[] all, OM om) {
//        if (Integer.bitCount(mask) != 3) {
//            throw new IllegalArgumentException("Only signed sets of size 3 supported");
//        }
//        if (Integer.bitCount(mask & pattern) == 2) {
//            return toSignedSet(mask, (1<<n)-pattern, all, om).opposite();
//        }
//        if (Integer.bitCount(mask & pattern) != 1) {
//            throw new IllegalArgumentException("Only PMP or MPM signed sets supported");
//        }
        Label e[] = om.elements();
        UnsignedSetFactory unsigned = om.ffactory().unsignedSets();
        UnsignedSet p = unsigned.empty();
        UnsignedSet m = unsigned.empty();
        for (int i=0;i<n;i++) {
            int bit = 1<<i;
            if ((bit&mask)!=0) {
                int ix = all[i];
                Label l = e[Math.abs(ix)];
                if (ix<0) {
                    m = m.union(l);
                } else {
                    p = p.union(l);
                }
            }
        }
        return om.ffactory().signedSets().construct(p, m);
    }


    /**
     * The n-bits of pattern can be split into several
     * 3 bits segements, each being a PMP
     * @param pattern n-bits, 0 for - 1 for +
     * @return An array of arrays, where each memeber of each memeber is three bits
     * that is a PMP in pattern, and where the bitwise OR of the members of each member
     * is (1<<n)-1, and where no member of a member is redundant
     * 
     */
    private int[][] splitIntoThrees(int pattern) {
        int ix = Arrays.binarySearch(patterns, pattern);
        if (ix < 0) {
            // not a PMP
            return new int[0][];
        }
        if (splitIntoThrees[ix]!=null) {
            return splitIntoThrees[ix];
        }
        List<int[]> results = new ArrayList<int[]>();
        splitIntoThrees( pattern, 0, results, new int[n], 0 );
        final int[][] r = results.toArray(new int[0][]);
        Arrays.sort(r, Ints.lexicographicalComparator());
        int from = 1;
        int i = 0;
        while (from < r.length) {
            if (!Arrays.equals(r[from], r[i])) {
                i++;
                r[i] = r[from];
            }
            from++;
        }
        i++;
        int sz = i;
        i = 0;
        for (int j=0;j<sz;j++) {
            for (int k=j+1;k<sz;k++) {
                if (r[j].length < r[k].length) {
                    if (containsAll(r[k],r[j])) {
                        if (k!= sz-1) {
                           r[k] = r[sz-1];
                           k--;
                        }
                        sz--;
                    }
                } else if ( r[j].length == r[k].length) {
                    // nothing
                } else {
                    if (containsAll(r[j],r[k])) {
                        if ( j != sz-1 ) {
                            r[j] = r[sz-1];
                            j--;
                        }
                        sz--;
                        break;
                    }
                }
            }
        }
        int counts[][] = new int[sz][];
        for (i=0;i<sz;i++) {
            counts[i] = new int[n];
            for (int j:r[i]) {
                for (int k=0;k<n;k++) {
                    if (((1<<k)&j)!=0) {
                        counts[i][k]++;
                    }
                }
            }
        }
        for (i=0;i<sz;i++) {
            for (int j=i+1;j<sz;j++) {
                if (Arrays.equals(counts[i],counts[j])) {
                    if ( j != sz - 1) {
                        counts[j] = counts[sz-1];
                        r[j] = r[sz-1];
                        j--;
                    }
                    sz--;
                }
            }
        }
        
        int rr[][] = new int[sz][];
        System.arraycopy(r, 0, rr, 0, rr.length);
        splitIntoThrees[ix] = rr;
        return rr;
    }


    public static int[][] testSplitIntoThrees(int n, int pattern) {
       return  get(n).splitIntoThrees(pattern);
    }
    private boolean containsAll(int[] bigger, int[] smaller) {
        nextX:
            for (int x:smaller) {
                for (int y:bigger) {
                    if (x==y) {
                        continue nextX;
                    }
                }
                return false;
            }
    return true;

    }


    private void splitIntoThrees(int pattern, int done, List<int[]> results,
            int[] threes, int tIx) {
        int max = (1<<n) -1;
        if (done == max ) {
            int r[] = new int[tIx];
            System.arraycopy(threes, 0, r, 0, tIx);
            Arrays.sort(r);
            results.add(r);
            return;
        }
        int coverMe = Integer.lowestOneBit(max & ~done);
        if (coverMe == 0) {
            return;
        }
        for (int three=7;three<= max;three ++) {
            if ((three & coverMe) != 0 && Integer.bitCount(three) == 3) {
                if (isPMP(pattern, three)) {
                    threes[tIx] = three;
                    splitIntoThrees(pattern, done | three, results, threes, tIx+1);
                }
            }
        }
    }


    private boolean isPMP(int pattern, int three) {
        int firstBit = Integer.lowestOneBit(three);
        int secondBit = Integer.lowestOneBit(three & ~firstBit);
        if ( ((pattern & firstBit)==0) == ((pattern & secondBit)==0 ) ) {
            return false;
        }
        int thirdBit = Integer.lowestOneBit(three & ~firstBit & ~secondBit);
        return ((pattern & firstBit)==0) == ((pattern & thirdBit)==0 ) ;
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

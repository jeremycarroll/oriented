/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;

import com.google.common.primitives.UnsignedLongs;

public class AMSLex extends AbstractMinimalSubsets {

    private static final Comparator<BitSet> bitsetComparator = new Comparator<BitSet>(){
        @Override
        public int compare(BitSet o1, BitSet o2) {
            int i1 = -1;
            int i2 = -1;
            while ( true ) {
                i1 = o1.nextSetBit(i1+1);
                i2 = o2.nextSetBit(i2+1);
                if (i1 == -1) {
                    if (i2 == -1) {
                        return 0;
                    } else {
                        return -1;
                    }
                } else {
                    if (i2 == -1) {
                        return 1;
                    } else if ( i1 == i2) {
                        continue;
                    } else {
                        return i1 - i2;
                    }
                }
            }
        }};


    @Override
    void markNonMinimal() {
        // TODO Auto-generated method stub

    }
    

    @Override
    void sort() {
        Comparator<Entry> comparator = new Comparator<Entry>(){
            @Override
            public int compare(Entry o1, Entry o2) {
                return  bitsetComparator.compare(o1.bs, o2.bs);
            }};
        Arrays.sort(sorted, comparator );
    }

    enum LexicographicalComparator implements Comparator<long[]> {
        INSTANCE;

        @Override
        public int compare(long[] left, long[] right) {
          int minLength = Math.min(left.length, right.length);
          for (int i = 0; i < minLength; i++) {
            if (left[i] != right[i]) {
              return UnsignedLongs.compare(~left[i], ~right[i]);
            }
          }
          return left.length - right.length;
        }
      }
    
    public static void main(String args[]) {
        BitSet a = BitSet.valueOf(new long[]{3,3});
        BitSet b = BitSet.valueOf(new long[]{1,3});
        BitSet c = BitSet.valueOf(new long[]{3,2});
        BitSet d = BitSet.valueOf(new long[]{1});
        BitSet e = BitSet.valueOf(new long[]{2});
        BitSet all[] = new BitSet[]{a,b,c,d,e};
        Arrays.sort(all, bitsetComparator );
        for (BitSet aa:all) {
            print("*",aa);
        }
        
    }


    private static void print(String name, BitSet original) {
        System.err.print(name+": { ");
        for (int i = original.nextSetBit(0); i >= 0; i = original.nextSetBit(i+1)) {
            System.err.print(i+", ");
        }
        System.err.println("}");
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

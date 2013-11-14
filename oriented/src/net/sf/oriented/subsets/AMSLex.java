/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;

import com.google.common.primitives.Ints;
import net.sf.oriented.subsets.AMSLex.LexEntry;

public class AMSLex extends AbstractMinimalSubsets<LexEntry> {
    
    static class LexEntry extends BitSetEntry  {

        final int bits[];
        LexEntry(BitSet bs) {
            super(bs);
            bits = new int[bs.cardinality()];
        }

        @Override
        void remap(int[] compressMapping) {
            super.remap(compressMapping);
            initBits();
        }
        @Override
        void noremap() {
            super.noremap();
            initBits();
        }

        private void initBits() {
            int j=0;
            for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
                bits[j++] = i;
            }
        }

        @Override
        public int compareTo(BitSetEntry o) {
            return Ints.lexicographicalComparator().compare(bits,((LexEntry)o).bits);
        }
    }

    @Override
    Class<LexEntry> getEntryClass() {
        return LexEntry.class;
    }

    @Override
    LexEntry create(BitSet b) {
        return new LexEntry(b);
    }
    @Override
    void markNonMinimal() {
        // By Pritchard, any subsets of an entry either immediately precede it
        // or follow it.
        
        // first look for the immediately preceding case
        int j;
        for (int i=0;i<sorted.length;i+=j) {
            j=1;
            while (i+j<sorted.length && sorted[i].isSubsetOf(sorted[i+j])) {
                sorted[i+j].deleted = true;
                j++;
            }
        }
        
        // now look for following subsets
        
        for (int i=0;i<sorted.length;i++) {
            if (isNonMinimal(sorted[i],i+1,sorted.length,0,0)) {
                sorted[i].deleted = true;
            }
        }

    }
    

    
    /**
     * Can we find a subset of searchItem in the range [from,to) in sorted,
     * knowing that the first matchedLength items of searchItem.bits match all these 
     * entries.
     * @param searchItem
     * @param from
     * @param to
     * @param matchedLength
     */
    private boolean isNonMinimal(LexEntry searchItem, int from, int to, int matchedLength, int matchedLength2) {
        
        if (from == to) {
            return false; // no match
        }
        
        if (matchedLength2 == sorted[from].bits.length) {
            return true;
        }
        int remaining = searchItem.bits.length - matchedLength;
        
        if (remaining == 0) {
            return false;
        }
        int matchNextBitStart = findIndex(sorted[from],matchedLength2,searchItem.bits[matchedLength],from,to);
        int matchNextBitEnd = findIndex(sorted[from],matchedLength2,searchItem.bits[matchedLength]+1,matchNextBitStart,to);
        if (isNonMinimal(searchItem,matchNextBitStart,matchNextBitEnd, matchedLength+1, matchedLength2+1 )) {
            return true;
        }
        if (remaining == 1) {
            return false;
        }
        int matchNextNextBitStart = findIndex(sorted[from],matchedLength2,searchItem.bits[matchedLength+1],matchNextBitEnd,to);
        return isNonMinimal(searchItem,matchNextNextBitStart,to,matchedLength+1,  matchedLength2);
        
    }




    /**
     * Find the first index in sorted of an entry
     * greater than or equal to the bit sequence formed
     * from taking matching bits from the beginning of  searchItem
     * and then next. The range to search is [from, to)
     * @param searchItem
     * @param matching
     * @param next
     * @param from
     * @param to
     * @return a number between from (incl) and to (excl).
     */
    private int findIndex(LexEntry searchItem, final int matching, final int next,
            int from, int to) {
        final int siBits[] = searchItem.bits;
        Comparator<LexEntry> comp = new Comparator<LexEntry>() {
            private boolean isSmaller(int[] entry) {
              int minLength = Math.min(entry.length, matching);
              for (int i = 0; i < minLength; i++) {
                int result = Ints.compare(entry[i], siBits[i]);
                if (result != 0) {
                  return result < 0;
                }
              }
              if (entry.length == matching) {
                  return true;
              }
              return Ints.compare(entry[matching],next) < 0;
            }

            @Override
            public int compare(LexEntry o1, LexEntry o2) {
                if (o1 == null) {
                    return isSmaller(o2.bits) ? 1 : -1;
                }
                if (o2 == null) {
                    return isSmaller(o1.bits) ? -1 : 1;
                }
                throw new IllegalArgumentException("Must have a null argument");
            }
            
        };
        final int r = -1 - Arrays.binarySearch(sorted, from, to, null, comp );
//        System.err./(r);
        return r;
    }
    public static void main(String args[]) {
        final int compressMappings[] = new int[128];
        for (int i=0;i<compressMappings.length;i++) {
            compressMappings[i] = i;
        }
        BitSet a = BitSet.valueOf(new long[]{3,3});
        BitSet b = BitSet.valueOf(new long[]{1,3});
        BitSet c = BitSet.valueOf(new long[]{3,2});
        BitSet d = BitSet.valueOf(new long[]{1});
        BitSet e = BitSet.valueOf(new long[]{2});
        BitSet all[] = new BitSet[]{a,b,c,d,e};
        Comparator<BitSet> bitsetComparator = new Comparator<BitSet>(){

            @Override
            public int compare(BitSet o1, BitSet o2) {
                final LexEntry l1 = new LexEntry(o1);
                final LexEntry l2 = new LexEntry(o2);
                l1.remap(compressMappings);
                l2.remap(compressMappings);
                return l1.compareTo(l2);
            }};
        Arrays.sort(all, bitsetComparator  );
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

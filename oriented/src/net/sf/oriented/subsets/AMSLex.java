/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;

import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedLongs;
import net.sf.oriented.subsets.AMSLex.LexEntry;

public class AMSLex extends AbstractMinimalSubsets<LexEntry> {
    
    static class LexEntry extends BitSetEntry  {

        private final int bits[];
        LexEntry(BitSet bs) {
            super(bs);
            bits = new int[bs.cardinality()];
        }

        @Override
        void compress(int[] compressMapping) {
            super.compress(compressMapping);
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
    void markNonMinimal() {
        // TODO Auto-generated method stub

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
                l1.compress(compressMappings);
                l2.compress(compressMappings);
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

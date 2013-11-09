/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;


abstract class AbstractMinimalSubsets implements MinimalSubsets {

    protected static class Entry implements Comparable<Entry> {
            final BitSet original;
            final BitSet bs;
            long[] bits;
            final int cardinality;
            boolean deleted;
            Entry(BitSet bs) {
                this.original = bs;
                this.bs = new BitSet();
                cardinality = bs.cardinality();
            }
            @Override
            public int compareTo(Entry o) {
                return cardinality - o.cardinality;
            }
            public boolean isSubsetOf(Entry ee) {
                if (ee.bits.length < bits.length) {
                    return false;
                }
                for (int i=0;i<bits.length;i++) {
                    if ((bits[i] & ~ee.bits[i])!=0) {
                        return false;
                    }
                }
                return true;
            }
            public void compress(int[] compressMapping) {
                for (int i = original.nextSetBit(0); i >= 0; i = original.nextSetBit(i+1)) {
                    bs.set(compressMapping[i]);
                }
                bits = bs.toLongArray();
            }
        }

    int max = 0;
    Entry sorted[];

    protected List<BitSet> gatherResults() {
        BitSet rslt[] = new BitSet[sorted.length];
        int i = 0;
        for (Entry b:sorted) {
            if (!b.deleted) {
                rslt[i++] = b.original;
            }
        }
        return Arrays.asList(rslt).subList(0,i);
    }

    void prepareData(Collection<BitSet> full) {
        sorted = new Entry[full.size()];
        int i = 0;
        int m = 0;
        BitSet any = new BitSet();
        for (BitSet b : full) {
            any.or(b);
            int l = b.length();
            if (l>m) {
                m = l;
            }
            sorted[i++] = new Entry(b);
        }
        max = any.cardinality();
        int compressMapping[] = new int[m];
        //int uncompressMapping[] = new int[max];
        int newBit = 0;

        for (i = any.nextSetBit(0); i >= 0; i = any.nextSetBit(i+1)) {
            compressMapping[i] = newBit ++;
        }
        for (Entry e:sorted) {
            e.compress(compressMapping);
        }
        Arrays.sort(sorted);
    }

    @Override
    public final List<BitSet> minimal(Collection<BitSet> full) {
        switch (full.size()) {
        case 0:
            return Arrays.asList();
        case 1:
            return Arrays.asList(full.iterator().next());
        }
        prepareData(full);
        markNonMinimal();
        return gatherResults();
    }

    abstract void markNonMinimal() ;

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

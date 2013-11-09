/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;


/**
 * The basic algorithm here is from
 * <a href="http://stackoverflow.com/a/8996897/2276263">Aaron McDaid's comment</a>,
 * modified by first sorting the BitSets in size of cardinality, and then
 * relying on only half of McDaid's algorithm (which covers the more general
 * case of augmenting a minimal set of sets).
 * @author jeremycarroll
 *
 */
final class TransposeMinimalSubsets implements MinimalSubsets {
    final class SortedOccursLists extends TreeSet<OccursList> {

        int lastPeek;
        SortedOccursLists() {
        }
            
        boolean initialize(int[][] occurs,  BitSet bs, int ix) {
            for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
                final OccursList entry = new OccursList(occurs[i]);
                if (!entry.advance(ix)) {
                   return false;
                }
                add(entry);
            }
            lastPeek = last().peek();
            return true;
        }


        boolean isAllEqual() {
            return lastPeek == first().peek();
        }

        int peek() {
            return lastPeek;
        }

        public boolean advance(boolean allMatched) {
            final OccursList first = first();
            remove(first);
            if (!first.advance(last().peek() + (allMatched?1:0))) {
                return false;
            }
            add(first);
            final int oldFirstPeek = first.peek();
            if (oldFirstPeek > lastPeek) {
                lastPeek = oldFirstPeek;
            }
            return true;
        }
        
    }
    private int ID_COUNTER = 0;
    final class OccursList implements Comparable<OccursList> {
        final int bitsets[];
        private int ix;
        final int id = ID_COUNTER++;
        OccursList(int bs[]) {
            bitsets = bs;
            ix = 0;
        }
        @Override
        public int compareTo(OccursList o) {
            int r = peek() - o.peek();
            return r!=0?r:(id - o.id);
        }
        private int peek() {
            return bitsets[ix];
        }
        public boolean advance(int i) {
            int pos = Arrays.binarySearch(bitsets, ix, bitsets.length, i);
            if (pos < 0) {
                ix = -1 - pos;
            } else {
                ix = pos;
            }
            while (ix < bitsets.length && sorted[bitsets[ix]]==null) {
                ix++;
            }
            return ix < bitsets.length;
        }
        
    }

    private static final Comparator<BitSet> BY_CARDINALITY = new Comparator<BitSet>(){
        @Override
        public int compare(BitSet o1, BitSet o2) {
            return o1.cardinality() - o2.cardinality();
        }};
    private BitSet[] sorted;
    @Override
    public List<BitSet> minimal(Collection<BitSet> full) {
        sorted = toSortedArray(full);
        int bad = 0;
        int firstMax = firstIxWithCardinalityFromIx(sorted.length-1, 0);
        int[][] occurs = createOccursIndex(full, sorted);
        int nextCardinalityIx = firstIxWithCardinalityFromIx(0, 1);
        outer:
        for (int ix=0;ix<firstMax;ix++) {
            BitSet bs = sorted[ix];
            if (bs == null) {
                continue;
            }
//            if (ix>=nextCardinalityIx ) {
//                nextCardinalityIx = firstIxWithCardinalityFromIx(ix, 1);
//            }
            SortedOccursLists ts = new SortedOccursLists();
            if (!ts.initialize(occurs, bs, //nextCardinalityIx
                    ix+1
                    )) {
                continue;
            }   
            
            while (true) {
                while (!ts.isAllEqual()) {
                    if (!ts.advance(false)) {
                        continue outer;
                    }
                }
                sorted[ts.peek()] = null;
                bad++;
                if (!ts.advance(true)) {
                    continue outer;
                }
            }
        }
        return gatherResults(sorted, bad);
    }
    private int firstIxWithCardinalityFromIx(int ixx, int step) {
        int nextCardinality = sorted[ixx].cardinality()+step;
        return findFirstWithCardinality(sorted, nextCardinality);
    }
    private List<BitSet> gatherResults(BitSet[] sorted, int bad) {
        BitSet rslt[] = new BitSet[sorted.length - bad];
        int i = 0;
        for (BitSet b:sorted) {
            if (b!=null) {
                rslt[i++] = b;
            }
        }
        return Arrays.asList(rslt);
    }
    private int[][] createOccursIndex(Collection<BitSet> full, BitSet[] sorted) {
        int[] counts = countBits(full);
        int max = counts.length;
        int cross[][] = new int[max][];
        for (int i=0;i<cross.length;i++) {
            cross[i] = new int[counts[i]];
        }
        Arrays.fill(counts,0);
        for (int j=0;j<sorted.length;j++) {
            BitSet bs = sorted[j];
            for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
                cross[i][counts[i]++] = j;
            }
        }
        return cross;
    }
    private static int[] countBits(Collection<BitSet> full) {
        int mx = MinimalSubsetFactory.max(full);
        int counts[] = new int[mx];
        for (BitSet bs:full) {
            for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
                counts[i]++;
            }
        }
        return counts;
    }
    private BitSet[] toSortedArray(Collection<BitSet> full) {
        BitSet sorted[] = new BitSet[full.size()];
        full.toArray(sorted);
        Arrays.sort(sorted, BY_CARDINALITY);
        return sorted;
    }
    private int findFirstWithCardinality(BitSet[] sorted,
            final int maxCardinality) {
        int firstMax = -(1 + Arrays.binarySearch(sorted, null, new Comparator<BitSet>(){

            @Override
            public int compare(BitSet o1, BitSet o2) {
                if (o1 == null) {
                    if (o2.cardinality() >= maxCardinality) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else if (o2 == null) {
                    if (o1.cardinality() >= maxCardinality) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    throw new IllegalArgumentException();
                }
            }}));
        return firstMax;
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

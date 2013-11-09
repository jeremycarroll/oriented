/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

/**
 * This class implements algorithm 1 from Bayardo and Panda,
 * inverted.
 * @author jeremycarroll
 *
 */
class AMSCard implements MinimalSubsets {
    
    private static class Entry implements Comparable<Entry> {
        final BitSet bs;
        final long[] bits;
        final int cardinality;
        boolean deleted;
        Entry(BitSet bs) {
            this.bs = bs;
            bits = bs.toLongArray();
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
    }

    @Override
    public List<BitSet> minimal(Collection<BitSet> full) {
        if (full.size() == 0) {
            return Arrays.asList(new BitSet[0]);
        }
        Entry all[] = new Entry[full.size()];
        int max = MinimalSubsetFactory.max(full);
        int i=0;
        for (BitSet bs:full) {
            all[i++] = new Entry(bs);
        }
        int bad = 0;
        Arrays.sort(all);
        @SuppressWarnings("unchecked")
        List<Entry> occurs[] = new List[max];
        int currentSize = all[0].cardinality;
        int lastSizeChange = 0;
        outer:
        for (i=0;i<all.length;i++) {
            Entry e = all[i];
            if (e.cardinality > currentSize) {
                for (int ii=lastSizeChange;ii<i;ii++) {
                    Entry ee = all[ii];
                    if (ee.deleted) {
                        continue;
                    }
                    int bit = ee.bs.nextSetBit(0);
                    if (occurs[bit]==null) {
                        occurs[bit] = new ArrayList<Entry>();
                    }
                    occurs[bit].add(ee);
                }
                currentSize = e.cardinality;
                lastSizeChange = i;
            }
            int bits = 0;
            for (int j = e.bs.nextSetBit(0); j >= 0; j = e.bs.nextSetBit(j+1)) {
                bits++;
                if (occurs[j]!=null) {
                    for (Entry ee:occurs[j]) {
                        // the optimization from line 5 of the algorithm in the paper
                        if (ee.cardinality > e.cardinality - bits + 1) {
                            break;
                        }
                        if ((!ee.deleted) && ee.isSubsetOf(e)) {
                            e.deleted = true;
                            bad ++;
                            continue outer;
                        }
                    }
                }
            }
        }
    
        return gatherResults(all, bad);
    }

    private List<BitSet> gatherResults(Entry[] all, int bad) {
        BitSet rslt[] = new BitSet[all.length - bad];
        int i = 0;
        for (Entry b:all) {
            if (!b.deleted) {
                rslt[i++] = b.bs;
            }
        }
        return Arrays.asList(rslt);
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

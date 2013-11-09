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

    int max = 0;

    protected List<BitSet> gatherResults(Entry[] all) {
        BitSet rslt[] = new BitSet[all.length];
        int i = 0;
        for (Entry b:all) {
            if (!b.deleted) {
                rslt[i++] = b.bs;
            }
        }
        return Arrays.asList(rslt).subList(0,i);
    }

    protected Entry[] prepareData(Collection<BitSet> full) {
        max = MinimalSubsetFactory.max(full);
        Entry r[] = new Entry[full.size()];
        int i = 0;
        for (BitSet b : full) {
            r[i++] = new Entry(b);
        }
        Arrays.sort(r);
        return r;
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

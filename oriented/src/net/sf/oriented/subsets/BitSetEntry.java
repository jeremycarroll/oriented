/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.BitSet;


class BitSetEntry implements Comparable<BitSetEntry> 
{
    final BitSet original;
    BitSet bs;
    private long[] bits;
    final int cardinality;
    boolean deleted;
    BitSetEntry(BitSet bs) {
        this.original = bs;
        cardinality = bs.cardinality();
    }
    @Override
    public int compareTo(BitSetEntry o) {
        return cardinality - o.cardinality;
    }
    boolean isSubsetOf(BitSetEntry ee) {
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
    void remap(int[] compressMapping) {
        bs = new BitSet();
        for (int i = original.previousSetBit(Integer.MAX_VALUE); i >= 0; i = original.previousSetBit(i-1)) {
            bs.set(compressMapping[i]);
        }
        bits = bs.toLongArray();
    }
    void noremap() {
        bs = original;
        bits = bs.toLongArray();
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

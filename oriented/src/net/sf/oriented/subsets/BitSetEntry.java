/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.BitSet;

import net.sf.oriented.subsets.AbstractMinimalSubsets.Entry;

class BitSetEntry implements Comparable<BitSetEntry> {
    final BitSet original;
    final BitSet bs;
    long[] bits;
    final int cardinality;
    boolean deleted;
    BitSetEntry(BitSet bs) {
        this.original = bs;
        this.bs = new BitSet();
        cardinality = bs.cardinality();
    }
    @Override
    public int compareTo(BitSetEntry o) {
        return cardinality - o.cardinality;
    }
    public boolean isSubsetOf(BitSetEntry ee) {
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

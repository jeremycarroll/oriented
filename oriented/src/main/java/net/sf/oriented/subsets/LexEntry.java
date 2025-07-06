/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.BitSet;


import com.google.common.primitives.Ints;

class LexEntry<U extends BitSet> extends BitSetEntry<U>  {

    final int bits[];
    LexEntry(U bs) {
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
    public int compareTo(BitSetEntry<U> o) {
        return Ints.lexicographicalComparator().compare(bits,((LexEntry<U>)o).bits);
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

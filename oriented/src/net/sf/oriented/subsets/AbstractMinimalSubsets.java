/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;


abstract class AbstractMinimalSubsets<T extends BitSetEntry> implements MinimalSubsets {

    int max = 0;
    T sorted[];

    protected List<BitSet> gatherResults() {
        BitSet rslt[] = new BitSet[sorted.length];
        int i = 0;
        for (BitSetEntry b:sorted) {
            if (!b.deleted) {
                rslt[i++] = b.original;
            }
        }
        return Arrays.asList(rslt).subList(0,i);
    }
    
    @SuppressWarnings("unchecked")
    Class<T> getEntryClass() {
        return (Class<T>) BitSetEntry.class;
    }

    @SuppressWarnings("unchecked")
    T create(BitSet b) {
        return (T) new BitSetEntry(b);
    }

    @Override
    public final List<BitSet> minimal(Collection<BitSet> full, Preparation prep) {
        switch (full.size()) {
        case 0:
            return Arrays.asList();
        case 1:
            return Arrays.asList(full.iterator().next());
        }
        prep.prepareData(full, this);
        markNonMinimal();
        return gatherResults();
    }

    abstract void markNonMinimal() ;

    protected int[] countBits(Function<BitSetEntry, BitSet> func, int max) {
        int counts[] = new int[max];
        for (BitSetEntry e:sorted) {
            BitSet bs = func.apply(e);
            for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
                counts[i]++;
            }
        }
        return counts;
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

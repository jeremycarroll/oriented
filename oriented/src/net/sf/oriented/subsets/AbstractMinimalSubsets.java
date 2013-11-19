/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Function;


abstract class AbstractMinimalSubsets<U extends BitSet, T extends BitSetEntry<U>> implements MinimalSubsets {

    int max = 0;
    T sorted[];

    protected List<U> gatherResults() {
        @SuppressWarnings("unchecked")
        U rslt[] = (U[])new BitSet[sorted.length];
        int i = 0;
        for (T b:sorted) {
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    T create(BitSet b) {
        return (T) new BitSetEntry(b);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <V extends BitSet> List<V> minimal(Collection<V> full, Preparation prep) {
        switch (full.size()) {
        case 0:
            return Arrays.asList();
        case 1:
            return Arrays.asList(full.iterator().next());
        }
        prep.prepareData((Collection<U>)full, this);
        markNonMinimal();
        return (List<V>)gatherResults();
    }

    abstract void markNonMinimal() ;

    protected int[] countBits(Function<T, BitSet> func, int max) {
        int counts[] = new int[max];
        for (T e:sorted) {
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

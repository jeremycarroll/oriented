/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;


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
    
    Class<T> getEntryClass() {
        return (Class<T>) BitSetEntry.class;
    }

    @SuppressWarnings("unchecked")
    void prepareData(Collection<BitSet> full) {
        sorted = (T[]) Array.newInstance(getEntryClass(), full.size());
        int i = 0;
        int m = 0;
        BitSet any = new BitSet();
        for (BitSet b : full) {
            any.or(b);
            int l = b.length();
            if (l>m) {
                m = l;
            }
            sorted[i++] = create(b);
        }
        max = any.cardinality();
        int compressMapping[] = new int[m];
        //int uncompressMapping[] = new int[max];
        int newBit = 0;

        for (i = any.nextSetBit(0); i >= 0; i = any.nextSetBit(i+1)) {
            compressMapping[i] = newBit ++;
        }
        for (BitSetEntry e:sorted) {
            e.compress(compressMapping);
        }
        sort();
    }

    @SuppressWarnings("unchecked")
    T create(BitSet b) {
        return (T) new BitSetEntry(b);
    }

    void sort() {
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

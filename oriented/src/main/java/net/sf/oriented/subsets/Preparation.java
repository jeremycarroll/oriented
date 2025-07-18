/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.primitives.Ints;

public enum Preparation {
    CompressBitMaps(0),
    Pritchard(1),
    ReversePritchard(1),
    Minimal(0) {
        @Override
        <U extends BitSet, T extends BitSetEntry<U>> T create(AbstractMinimalSubsets<U,T> algo, BitSet b) {
            final T rslt = algo.create(b);
            rslt.noremap();
            return rslt;
        }
        @Override
        <U extends BitSet, T extends BitSetEntry<U>> void remap(AbstractMinimalSubsets<U,T> algo,Data data) {
           // do nothing much
            algo.max = data.longest;
        }
    };
    
    private final int sign;
    Preparation(int s) {
        sign = s;
    }
    private static class Data {
        int longest;
        BitSet any = new BitSet();
        int counts[];
        public int[][] countsAsPairs(int sign) {
            int rslt[][] = new int[any.cardinality()][];
            int j = 0;
            for (int i=0;i<counts.length;i++) {
                if (counts[i]!=0) {
                    rslt[j++] = new int[]{counts[i]*sign,i};
                }
            }
            return rslt;
        }
    }

    <U extends BitSet, T extends BitSetEntry<U>> void prepareData(Collection<U> full, AbstractMinimalSubsets<U,T> algo) {
        algo.sorted = (T[]) Array.newInstance(algo.getEntryClass(), full.size());
        Data data = createEntries(full, algo);
        algo.max = data.any.cardinality();
        remap(algo, data);
        Arrays.sort(algo.sorted);
    }

    <U extends BitSet, T extends BitSetEntry<U>>  Data createEntries(Collection<U> full, AbstractMinimalSubsets<U,T> algo) {
        return sign == 0 ?
                createEntriesNoCounts(full, algo):
                createEntriesWithCounts(full, algo);
    }
    <U extends BitSet, T extends BitSetEntry<U>> Data createEntriesWithCounts(Collection<U> full, AbstractMinimalSubsets<U,T> algo) {
        Data data = createEntriesNoCounts(full, algo);
        Function<T, BitSet> func = new Function<T, BitSet>() {
            @Override
            public BitSet apply(T e) {
                return e.original;
            }
        };
        data.counts = algo.countBits(func, data.longest);
        return data;
    }
    <U extends BitSet, T extends BitSetEntry<U>> Data createEntriesNoCounts(Collection<U> full, AbstractMinimalSubsets<U,T> algo) {
        int i = 0;
        Data data = new Data();
        for (BitSet b : full) {
            data.any.or(b);
            int l = b.length();
            if (l>data.longest) {
                data.longest = l;
            }
            algo.sorted[i++] = create(algo, b);
        }
        return data;
    }

    <U extends BitSet, T extends BitSetEntry<U>> void remap(AbstractMinimalSubsets<U,T> algo,Data data) {
        int[] compressMapping = createBitMapping(data);
        for (T e:algo.sorted) {
            e.remap(compressMapping);
        }
    }

    int[] createBitMapping(Data data) {
        return sign==0 
                 ? createUnorderedBitMapping(data)
                 :createOrderedBitMapping(data);
    }

    int[] createUnorderedBitMapping(Data data) {
        int compressMapping[] = new int[data.longest];
        int newBit = 0;
        int i;
        for (i = data.any.nextSetBit(0); i >= 0; i = data.any.nextSetBit(i+1)) {
            compressMapping[i] = newBit ++;
        }
        return compressMapping;
    }

    int[] createOrderedBitMapping(Data data) {
        int mapping[] = new int[data.longest];
        int pairs[][] = data.countsAsPairs(sign);
        Arrays.sort(pairs,Ints.lexicographicalComparator());
        int newBit = 0;
        for (int p[]:pairs) {
            mapping[p[1]] = newBit ++;
        }
        return mapping;
    }
    <U extends BitSet, T extends BitSetEntry<U>> T create(AbstractMinimalSubsets<U,T> algo, BitSet b) {
        return algo.create(b);
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

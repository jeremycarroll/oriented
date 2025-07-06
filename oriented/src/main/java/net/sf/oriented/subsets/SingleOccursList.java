/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * This class implements algorithm 1 from Bayardo and Panda,
 * inverted.
 * @author jeremycarroll
 *
 */
class SingleOccursList<U extends BitSet, T extends BitSetEntry<U>> 
extends AbstractMinimalSubsets<U ,T>  {
    
    @Override
    public void markNonMinimal() {
        @SuppressWarnings("unchecked")
        List<T> occurs[] = new List[max];
        int currentSize = sorted[0].cardinality;
        int lastSizeChange = 0;
        outer:
        for (int i=0;i<sorted.length;i++) {
            T e = sorted[i];
            if (e.cardinality > currentSize) {
                addToOccursLists(sorted, lastSizeChange, i, occurs);
                currentSize = e.cardinality;
                lastSizeChange = i;
            }
            int bits = 0;
            for (int j = e.bs.nextSetBit(0); j >= 0; j = e.bs.nextSetBit(j+1)) {
                bits++;
                if (occurs[j]!=null) {
                    for (T ee:occurs[j]) {
                        // the optimization from line 5 of the algorithm in the paper
                        if (ee.cardinality > e.cardinality - bits + 1) {
                            break;
                        }
                        if ((!ee.deleted) && ee.isSubsetOf(e)) {
                            e.deleted = true;
                            continue outer;
                        }
                    }
                }
            }
        }
    }

    private void addToOccursLists(T[] all, int lastSizeChange, int i,
            List<T>[] occurs) {
        for (int ii=lastSizeChange;ii<i;ii++) {
            T ee = all[ii];
            if (ee.deleted) {
                continue;
            }
            int bit = ee.bs.nextSetBit(0);
            if (occurs[bit]==null) {
                occurs[bit] = new ArrayList<>();
            }
            occurs[bit].add(ee);
        }
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

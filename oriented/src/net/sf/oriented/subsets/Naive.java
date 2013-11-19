/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.BitSet;

final class Naive <U extends BitSet, T extends BitSetEntry<U>> 
extends AbstractMinimalSubsets<U ,T>  {

    @Override
    void markNonMinimal() {
        int sz = sorted.length;
        for (int i = 0; i < sz - 1; i++) {
            BitSetEntry<U> di = sorted[i];
            if (di.deleted) {
                continue;
            }
            for (int j = i + 1; j < sz; j++) {
                BitSetEntry<U> dj = sorted[j];
                if (dj.deleted) {
                    continue;
                }
                if (di.isSubsetOf(dj)) {
                    dj.deleted = true;
                }
            }

        }
    }

}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * The Java Oriented Matroid Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Java Oriented Matroid Library. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.combinatorics;

import java.util.Iterator;

import org.apache.commons.math3.util.ArithmeticUtils;

import com.google.common.math.LongMath;

class SymmetricGroup extends Group {

    public SymmetricGroup(int n) {
        super(n);
    }

    @Override
    public Iterator<Permutation> iterator() {
        return new Iterator<Permutation>() {
            final int[] value = Permutation.from0toN(n());
            private boolean finished = false;

            @Override
            public boolean hasNext() {
                return !finished;
            }

            @Override
            public Permutation next() {
                Permutation rslt = new Permutation(value);
                if (!increment()) {
                    finished = true;
                }
                return rslt;
            }

            // 2, 1, 0, 3 => 2,3,0,1

            // 3,1,2,0 => 3,0,1,2

            // 2,3,0,1 => 2,3,1,0

            private boolean increment() {
                int reorderFromHere;
                long reordering = 0l;
                int maxReordering = -1;
                for (reorderFromHere = n() - 1; reorderFromHere >= 0; reorderFromHere--) {
                    int v = value[reorderFromHere];
                    reordering |= (1l << v);
                    if (v < maxReordering) {
                        break;
                    } else if (v > maxReordering) {
                        maxReordering = v;
                    }
                }
                if (reorderFromHere < 0) {
                    return false;
                }
                int leastPermissable = value[reorderFromHere] + 1;
                for (int i = reorderFromHere; i < n(); i++) {
                    for (int j = leastPermissable; j < n(); j++) {
                        long bit = (1l << j);
                        if ((reordering & bit) != 0) {
                            value[i] = j;
                            reordering &= ~bit;
                            leastPermissable = 0;
                            break;
                        }
                    }
                }
                return true;
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public long order() {
        return LongMath.factorial(n());
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

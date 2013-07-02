/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.combinatorics;

import java.util.Arrays;
import java.util.Iterator;

public class Permutation {

    public static Iterable<Permutation> all(final int n) {
        final int value[] = new int[n];
        for (int i = 0; i < n; i++) {
            value[i] = i;
        }
        return new Iterable<Permutation>() {

            private boolean finished = false;

            @Override
            public Iterator<Permutation> iterator() {
                return new Iterator<Permutation>() {

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
                        for (reorderFromHere = n - 1; reorderFromHere >= 0; reorderFromHere--) {
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
                        for (int i = reorderFromHere; i < n; i++) {
                            for (int j = leastPermissable; j < n; j++) {
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

        };
    }

    final private int permutation[];

    public Permutation(int ... perm) {
        int n = perm.length;
        permutation = perm.clone();
        long mask = (1l << n) - 1;
        for (int v : perm) {
            if (v < 0 || v >= n) {
                throw new IllegalArgumentException(v
                        + " is not in range. 0 =< v < " + n);
            }
            long bit = (1l << v);
            if ((mask & bit) == 0) {
                throw new IllegalArgumentException(v + " occurs more than once");
            }
        }

    }

    public int get(int i) {
        return permutation[i];
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(permutation);
    }
    
    @Override
    public boolean equals(Object o) {
        return ( o instanceof Permutation) && Arrays.equals(permutation, ((Permutation)o).permutation);
    }
    @Override
    public String toString() {
        StringBuffer rslt = new StringBuffer();
        rslt.append("(");
        rslt.append(get(0));
        for (int i = 1; i < permutation.length; i++) {
            rslt.append(",");
            rslt.append(get(i));
        }
        rslt.append(")");
        return rslt.toString();
    }

    static public void main(String argv[]) {
        for (Permutation p : Permutation.all(Integer.parseInt(argv[0]))) {
            System.out.println(p.toString());
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

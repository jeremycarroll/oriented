/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util.combinatorics;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A permutation of integers
 * @author jeremycarroll
 *
 */
public class Permutation {

    /**
     * Return all permutations of the sequence <code>( 0, 1, ... n - 1)</code>
     * @param n
     * @return an iterator over all such permutations.
     */
    public static Iterable<Permutation> all(final int n) {
        return new Iterable<Permutation>() {
            @Override
            public Iterator<Permutation> iterator() {
                return new Iterator<Permutation>() {
                    final int[] value = from0toN(n);
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

    public static int[] from0toN(final int n) {
        final int value[] = new int[n];
        for (int i = 0; i < n; i++) {
            value[i] = i;
        }
        return value;
    }

    final private int permutation[];

    /**
     * Create a new permutations which maps
     * <code>i</code> to <code>map[i]</code>
     * for each <code>i</code>.
     * @param map The numbers 0 to <code>map.length - 1</code> in an arbitrary order.
     */
    public Permutation(int ... map) {
        int n = map.length;
        permutation = map.clone();
        long mask = (1l << n) - 1;
        for (int v : map) {
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
    /**
     * Use the notation <code>(0 1) (4 5 6)</code>to represent the permutation
     * <code>(1 0 2 3 5 6 4)</code>
     * @param n    <code>( 0, 1, ... n - 1)</code>
     * @param cycles A list of cycles in the permutation to be created.
     */
    public Permutation(int n, int[][] cycles) {
        this(computeCycles(n,cycles));
    }
    
    /**
     * The permutation as an array of integers.
     */
    public int[] toArray() {
        return permutation.clone();
    }
    

    

    private static int[] computeCycles(int n, int[][] cycles) {
        int rslt[] = new int[n];
        Arrays.fill(rslt, -1);
        for (int cycle[] : cycles ) {
            for (int ix=0;ix<cycle.length-1;ix++) {
                if (cycle[ix] < 0 || cycle[ix] >= n) {
                    throw new IllegalArgumentException("Each number once in constructor must be between 0 and n");
                }
                setOnce(rslt,cycle[ix],cycle[ix+1]);
            }
            setOnce(rslt,cycle[cycle.length-1],cycle[0]);
        }
        for (int i=0;i<n;i++) {
            if (rslt[i]==-1) {
                rslt[i] = i;
            }
        }
        return rslt;
    }

    private static void setOnce(int[] rslt, int i, int j) {
        if (rslt[i] != -1) {
            throw new IllegalArgumentException("Can only use each number once in constructor");
        }
        rslt[i] = j;
    }

    /**
     * The ith slot from the permutation
     * @param i An integer from <code>0</code> to <code>n - 1</code>
     * @return The value of the permutation at i
     */
    public int get(int i) {
        return permutation[i];
    }

    /**
     * This method returns the permuted arguments, which
     * must be of number {@link #n()}
     * @param args
     * @return the same arguments but in a permuted order
     */
    public <T> T[] permute(@SuppressWarnings("unchecked") T ... args) {
        if (args.length != n()) {
            throw new IllegalArgumentException("Wrong length");
        }
        T[] result = (T[]) Array.newInstance(args.getClass().getComponentType(), args.length);
        for (int i=0;i<args.length;i++)
            result[i] = args[get(i)];
        return result;
    }
    /**
     * For each of the arguments, which must be between 0 and {@link #n()}
     * map it to the new value under this permutation. {@link #get(int)} is the same as this method on one argument.
     * @param args
     * @return The values of the permutation at each of the arguments.
     */
    public int[] mapAll(int ... args) {
        int[] result = new int[args.length];
        for (int i=0;i<args.length;i++)
            result[i] = get(args[i]);
        return result;
        
    }
    /**
     * The inverse permutation
     */
    public Permutation inverse() {
        int inverse[] = new int[permutation.length];
        for (int i=0;i<inverse.length;i++) {
            inverse[get(i)] = i;
        }
        return new Permutation(inverse);
    }
    
    /**
     * Permutation composition
     * @param then Perform <code>this</code> permutation and then <code>then</code>
     * @return the composed permutation
     */
    public Permutation and(Permutation then) {
        return new Permutation(mapAll(then.permutation));
        
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

    /**
     * The number of items being permuted by this permutation.
     */
    public int n() {
        return permutation.length;
    }
    /**
     * simple test
     * @param argv the string representation of <code>n</code>
     */
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

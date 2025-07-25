/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.util.combinatorics;


import java.util.AbstractCollection;
import java.util.Iterator;

import com.google.common.math.IntMath;

/**
 * Iterate over lexicographic orderings of integers.
 * 
 * @author jeremycarroll
 *
 */
public class Lexicographic extends AbstractCollection<int[]> {
	final int n, r, sz;


    /**
     * Construct a lexicographic ordering or r items chosen from 0 through n &minus; 1.
     * 
     * @param n The number of things to choose from
     * @param r The number of things to choose: greater than 0 and less than n
     */
	public Lexicographic(int n, int r) {
		this.n = n;
		this.r = r;
		sz = IntMath.binomial(n, r);
	}

	@Override
	public Iterator<int[]> iterator() {
		return new Iterator<int[]>() {
			int cnt = 0;

			int pos = r - 1;

			int rslt[];

			@Override
			public boolean hasNext() {
				return cnt < sz;
			}

			@Override
			public int[] next() {
				cnt++;
				if (rslt == null) {
					rslt = new int[r];
					for (int i = 0; i < r; i++) {
						rslt[i] = i;
					}
				} else {
					while (rslt[pos] == n - (r - pos)) {
						pos--;
					}
					rslt[pos]++;
					for (pos++; pos < r; pos++) {
						rslt[pos] = rslt[pos - 1] + 1;
					}
					pos--;
				}
				return rslt.clone();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int size() {
		return sz;
	}
	
	/**
	 * simple test code
	 * @param args n and r
	 */
	public static void main(String args[]) {
	    Lexicographic lex = new Lexicographic(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
	    int i = 0;
	    for ( int[] vals : lex ) {
	        System.out.print(i++ +": ");
	        for (int v : vals) {
	            System.out.print(v+" ");
	        }
	        System.out.println();
	    }
	}
	
    private static int oldCodeForIndex(int n, int r, int p, int... i) {
        if (r == 0)
            return 0;
        if (i[p] < 0 || i[p] >= n)
            throw new IllegalArgumentException("value out of range.");
        for (int q = 1; q < r; q++)
            if (i[p + q] > i[p]) {
                i[p + q] -= (i[p] + 1);
            } else
                throw new IllegalArgumentException("sequence not monotonic");
        int rslt = 0;
        int nextN = n - 1 - i[p];
        while (i[p] > 0) {
            rslt += IntMath.binomial(n - i[p], r - 1);
            i[p]--;
        }
        return rslt + oldCodeForIndex(nextN, r - 1, p + 1, i);
    }

    /**
     * In a lexicographic ordering, what position is this index.
     * @param n the number of items being chosen from
     * @param ix The increasing list of integers in an ordering
     * @return The position in the iteration of this index.
     */
    public static int index(int n, int[] ix) {
        return oldCodeForIndex(n, ix.length, 0, ix);
    }
    
    /**
     * Convert a colexicographic chirotope string into a lexicographic one
     * @param n    The number of things to choose from
     * @param r    The number of things to choose
     * @param colex A colexicographically ordered string (one character per index)
     * @return The same string reordered to be lexicographically ordered.
     */
    public static String fromCoLexicographic(int n, int r, String colex) {
        char result[] = new char[colex.length()];
        int i = 0;
        for ( int index[]:new Lexicographic(n,r)) {
            result[i++] = colex.charAt(CoLexicographic.index(index));
        }
        return new String(result);
    }


}
/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * 
 * 
 * 
 * 
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

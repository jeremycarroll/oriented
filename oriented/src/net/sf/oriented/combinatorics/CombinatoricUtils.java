/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.combinatorics;

import java.util.Arrays;


public class CombinatoricUtils {
	/**
	 * 
	 * @param x
	 * @param y
	 *            Must be sorted.
	 * @return
	 */
	public static int sign(int[] x, int[] y) {
		// special cases:
		if (x.length == 0)
			return 1;

		for (int i = 1; i < y.length; i++) {
			if (y[i - 1] == y[i])
				return 0;
		}

		// sign is computed as -1^(n-m) where n = |x| and m is the number of
		// cycles.
		int sign = x.length % 2 == 0 ? 1 : -1;

		// normalize permutation
		int p[] = new int[x.length];

		for (int i = 0; i < x.length; i++) {
			p[i] = indexOf(x[i], y);
		}

		// count cycles
		for (int i = 0; i < p.length; i++) {
			if (p[i] == -1) {
				continue;
			}
			sign = -sign;
			int j = i;
			do {
				int nextj = p[j];
				p[j] = -1;
				j = nextj;
			} while (p[j] != -1);
		}
		return sign;
	}

	private static int indexOf(int i, int[] y) {
		for (int j = 0; j < y.length; j++)
			if (i == y[j])
				return j;
		throw new IllegalArgumentException(
				"The two arrays are not permutations.");
	}

	/**
	 * What is the sign of the permutation that sorts the values in x.
	 * 
	 * @param x
	 * @return 0 If the x are not all distinct, otherwise the sign of the
	 *         permutation.
	 */
	static public int sign(int... x) {
		int y[] = x.clone();
		Arrays.sort(y);
		return sign(x, y);
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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static net.sf.oriented.combinatorics.CombinatoricUtils.sign;
import static net.sf.oriented.omi.impl.om.ChirotopeImpl.dualBasis;
import static net.sf.oriented.omi.impl.om.ChirotopeImpl.pos;
import static net.sf.oriented.omi.impl.om.ChirotopeImpl.signDualBasis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.math.IntMath;

public class TestAlternating {

	@BeforeClass
	public static void setUpBeforeClass() {
	}

	@Test
	public void testSign() {

		assertEquals(1, sign(3, 5, 8, 9, 11));
		assertEquals(-1, sign(5, 3));
		assertEquals(1, sign(4, 6, 2));
		assertEquals(-1, sign(4, 6, 2, 3, 5));
	}

	@Test
	public void testChoose() {

		assertEquals(10, IntMath.binomial(5, 3));
		assertEquals(10 * 3 * 7, IntMath.binomial(10, 4));
		assertEquals(1, IntMath.binomial(8, 8));
		assertEquals(35, IntMath.binomial(7, 3));
	}

	@Test
	public void testPos1() {

		assertEquals(0, pos(1, 1, 0, 0));
		assertEquals(0, pos(2, 1, 0, 0));
		assertEquals(0, pos(3, 1, 0, 0));
		assertEquals(0, pos(3, 2, 0, 0, 1));
		assertEquals(0, pos(2, 1, 2, 10, 0, 0));
		assertEquals(0, pos(3, 2, 1, 10, 0, 1));
		assertEquals(0, pos(4, 2, 0, 0, 1));
		assertEquals(0, pos(5, 2, 0, 0, 1));
		assertEquals(0, pos(4, 3, 0, 0, 1, 2));
		assertEquals(0, pos(5, 3, 0, 0, 1, 2));

	}

	@Test
	public void testPos2() {

		assertEquals(0, pos(2, 0, 3, 1, 3, 4));
		assertEquals(0, pos(3, 1, 2, 1, 3, 0));
		assertEquals(0, pos(4, 2, 1, 1, 0, 1));
		assertEquals(1, pos(4, 2, 1, 1, 0, 2));
		assertEquals(2, pos(4, 2, 1, 1, 0, 3));
		assertEquals(3, pos(4, 2, 1, 1, 1, 2));
		assertEquals(8, pos(5, 3, 0, 1, 3, 4));

	}

	@Test
	public void testPos() {

		assertEquals(0, pos(5, 3, 0, 0, 1, 2)); // 3,4
		assertEquals(1, pos(5, 3, 0, 0, 1, 3)); // 2,4
		assertEquals(2, pos(5, 3, 0, 0, 1, 4)); // 2,3
		assertEquals(3, pos(5, 3, 0, 0, 2, 3)); // 1,4
		assertEquals(4, pos(5, 3, 0, 0, 2, 4)); // 1,3
		assertEquals(5, pos(5, 3, 0, 0, 3, 4)); // 1,2
		assertEquals(6, pos(5, 3, 0, 1, 2, 3)); // 0,4
		assertEquals(7, pos(5, 3, 0, 1, 2, 4)); // 0,3
		assertEquals(8, pos(5, 3, 0, 1, 3, 4)); // 0,2
		assertEquals(9, pos(5, 3, 0, 2, 3, 4)); // 0,1

	}

	@Test
	public void testSign2() {
		assertEquals(1, sign(0, 1, 2, 3, 4));
		assertEquals(-1, sign(0, 1, 3, 2, 4));
		assertEquals(1, sign(0, 1, 4, 2, 3));
		assertEquals(1, sign(0, 2, 3, 1, 4));
		assertEquals(-1, sign(0, 2, 4, 1, 3));
		assertEquals(1, sign(0, 3, 4, 1, 2));
		assertEquals(-1, sign(1, 2, 3, 0, 4));
		assertEquals(1, sign(1, 2, 4, 0, 3));
		assertEquals(-1, sign(1, 3, 4, 0, 2));
		assertEquals(1, sign(2, 3, 4, 0, 1));

	}

	public void tpos(int n) {
		int x = 0;
		for (int i0 = 0; i0 < n; i0++) {
			for (int i1 = i0 + 1; i1 < n; i1++) {
				for (int i2 = i1 + 1; i2 < n; i2++) {
					for (int i3 = i2 + 1; i3 < n; i3++) {
						assertEquals(x++, pos(n, 4, 0, i0, i1, i2, i3));
					}
				}
			}
		}

	}

	@Test
	public void testPos4() {
		tpos(7);
		tpos(20);
	}

	@Test
	public void dual() {
		int A[] = new int[] { 3, 4, 7 };
		int B[] = dualBasis(8, 5, A);
		int C[] = new int[] { 0, 1, 2, 5, 6 };
		assertEquals(C.length, B.length);
		for (int i = 0; i < C.length; i++) {
			assertEquals(C[i], B[i]);
		}
		assertFalse(0 == signDualBasis(A, B));
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

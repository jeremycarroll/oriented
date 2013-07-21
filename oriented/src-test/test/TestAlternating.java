/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static net.sf.oriented.omi.impl.om.ChirotopeImpl.dualBasis;
import static net.sf.oriented.omi.impl.om.ChirotopeImpl.signDualBasis;
import static net.sf.oriented.util.combinatorics.CombinatoricUtils.sign;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import net.sf.oriented.util.combinatorics.CoLexicographic;

import org.junit.Test;

import com.google.common.math.IntMath;

public class TestAlternating {

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
		int[] i = { 0 };
        assertEquals(0, CoLexicographic.index(i));
        int[] i1 = { 0 };
		assertEquals(0, CoLexicographic.index(i1));
        int[] i2 = { 0 };
		assertEquals(0, CoLexicographic.index(i2));
        int[] i3 = { 0, 1 };
		assertEquals(0, CoLexicographic.index(i3));
        int[] i4 = { 0, 1 };
		assertEquals(0, CoLexicographic.index(i4));
        int[] i5 = { 0, 1 };
		assertEquals(0, CoLexicographic.index(i5));
        int[] i6 = { 0, 1, 2 };
		assertEquals(0, CoLexicographic.index(i6));
        int[] i7 = { 0, 1, 2 };
		assertEquals(0, CoLexicographic.index(i7));
	}

	@Test
	public void testPos2() {
		int[] i = { 1, 3, 4 };
        assertEquals(8, CoLexicographic.index(i));
	}

	@Test
	public void testPos() {
		int[] i = { 0, 1, 2 };
        assertEquals(0, CoLexicographic.index(i));
        int[] i1 = { 0, 1, 3 }; // 3,4
		assertEquals(1, CoLexicographic.index(i1));
        int[] i2 = { 0, 1, 4 }; // 2,4
		assertEquals(4, CoLexicographic.index(i2));
        int[] i3 = { 0, 2, 3 }; // 2,3
		assertEquals(2, CoLexicographic.index(i3));
        int[] i4 = { 0, 2, 4 }; // 1,4
		assertEquals(5, CoLexicographic.index(i4));
        int[] i5 = { 0, 3, 4 }; // 1,3
		assertEquals(7, CoLexicographic.index(i5));
        int[] i6 = { 1, 2, 3 }; // 1,2
		assertEquals(3, CoLexicographic.index(i6));
        int[] i7 = { 1, 2, 4 }; // 0,4
		assertEquals(6, CoLexicographic.index(i7));
        int[] i8 = { 1, 3, 4 }; // 0,3
		assertEquals(8, CoLexicographic.index(i8));
        int[] i9 = { 2, 3, 4 }; // 0,2
		assertEquals(9, CoLexicographic.index(i9)); // 0,1
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

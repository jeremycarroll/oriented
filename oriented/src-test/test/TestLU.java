/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package test;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;
import net.sf.oriented.matrix.FieldLUDecomposition;
import net.sf.oriented.matrix.MatrixUtil;
import net.sf.oriented.matrix.PerisicFieldElement;
import net.sf.oriented.matrix.RationalMatrix;

import org.apache.commons.math3.linear.FieldMatrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized.TestName;

@RunWith(BetterParameterized.class)
public class TestLU {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays
				.asList(new Object[][] {
						{
								"chapter1",
								new int[][] { { 1, 1, 1, 0, 0, 0 },
										{ 0, 1, 1, 1, 1, 0 },
										{ 0, 0, 1, 0, 1, 1 } } },
						{ "singular", new int[][] { { 1, 2 }, { 2, 4 } } },
						{
								"chapter1up",
								new int[][] { { 0, 0, 1, 0, 1, 1 },
										{ 0, 1, 1, 1, 1, 0 },
										{ 1, 1, 1, 0, 0, 0 }, } },
						{ "wikipedia", new int[][] { { 4, 3, }, { 6, 3 } } },
						{
								"utdallas",
								new int[][] { { 1, 2, 3 }, { 2, -4, 6 },
										{ 3, -9, -3 } } },
						{
								"wikipediaX",
								new int[][] { { 4, 3 }, { 6, 3 }, { 1, 1 },
										{ 1, 0 } } },
						{
								"utdallasX",
								new int[][] { { 1, 2, 3, }, { 2, -4, 6 },
										{ 3, -9, -3 }, { 1, 0, 0 } } }, });
	}

	@TestName
	public static String name(String nm, int[][] d) {
		return nm;
	}

	private final int data[][];
	private final String name;

	public TestLU(String nm, int[][] d) {
		data = d;
		name = nm;
	}

	@Test
	public void testPlain() {
		test("plain", new RationalMatrix(data));

	}

	@Test
	public void testTranspose() {
		test("transpose", new RationalMatrix(new RationalMatrix(data)
				.getDelegate().transpose()));

	}

	@Test
	public void testRowExtended() {
		test("row",
				new RationalMatrix(MatrixUtil.rowExtended(new RationalMatrix(
						data).getDelegate())));
	}

	@Test
	public void testColExtended() {
		test("col",
				new RationalMatrix(MatrixUtil
						.columnExtended(new RationalMatrix(data).getDelegate())));
	}

	@Test
	public void testTransRowExtended() {
		test("trow",
				new RationalMatrix(MatrixUtil.rowExtended(new RationalMatrix(
						data).getDelegate().transpose())));
	}

	@Test
	public void testTransColExtended() {
		test("tcol",
				new RationalMatrix(MatrixUtil
						.columnExtended(new RationalMatrix(data).getDelegate()
								.transpose())));
	}

	private void test(String nm, RationalMatrix matrix) {
		FieldLUDecomposition<PerisicFieldElement> lu = new FieldLUDecomposition<PerisicFieldElement>(
				matrix.getDelegate());

		FieldMatrix<PerisicFieldElement> A = matrix.getDelegate();
		FieldMatrix<PerisicFieldElement> L = lu.getL();
		FieldMatrix<PerisicFieldElement> P = lu.getP();
		FieldMatrix<PerisicFieldElement> U = lu.getU();
//		System.err.println(nm + " " + name + " A: " + toString(A));
//		System.err.println(" L: " + toString(L));
//		System.err.println(" P: " + toString(P));
//		System.err.println(" U: " + toString(U));
		Assert.assertEquals(P.multiply(A), L.multiply(U));

	}

	@SuppressWarnings("unused")
    private String toString(FieldMatrix<PerisicFieldElement> u) {
		return new RationalMatrix(u).toString();
	}
}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * The Java Oriented Matroid Library is free software: you can
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

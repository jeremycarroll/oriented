/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package test;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.Assert;
import net.sf.oriented.matrix.GramSchmidt;
import net.sf.oriented.matrix.PerisicFieldElement;
import net.sf.oriented.matrix.RationalMatrix;

import org.apache.commons.math3.linear.FieldMatrix;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized.TestName;

@RunWith(BetterParameterized.class)
public class TestGramSchmidt {

	@Parameters
	public static Collection<Object[]> data() {
		return Arrays
				.asList(new Object[][] {
						{
								"chapter1",
								new int[][] { { 1, 1, 1, 0, 0, 0 },
										{ 0, 1, 1, 1, 1, 0 },
										{ 0, 0, 1, 0, 1, 1 } } },
						// { "singular", new int[][]{ { 1, 2 }, { 2, 4 } } },
						{
								"chapter1up",
								new int[][] { { 0, 0, 1, 0, 1, 1 },
										{ 0, 1, 1, 1, 1, 0 },
										{ 1, 1, 1, 0, 0, 0 }, } },
						{ "wikipedia", new int[][] { { 4, 3, }, { 6, 3 } } },
						{
								"utdallas",
								new int[][] { { 1, 2, 3 }, { 2, -4, 6 },
										{ 3, -9, -3 } } }, });
	}

	@TestName
	public static String name(String nm, int[][] d) {
		return nm;
	}

	private final int data[][];
	@SuppressWarnings("unused")
    private final String name;

	public TestGramSchmidt(String nm, int[][] d) {
		data = d;
		name = nm;
	}

	@Test
	public void testPlain() {
		test("plain", new RationalMatrix(data));

	}

	// @Test
	// public void testTranspose() {
	// test("transpose",new RationalMatrix(new
	// RationalMatrix(data).getDelegate().transpose()));
	//
	// }

	private void test(String nm, RationalMatrix matrix) {
		GramSchmidt<PerisicFieldElement> gs = new GramSchmidt<PerisicFieldElement>(
				matrix.getDelegate());

//		FieldMatrix<PerisicFieldElement> A = matrix.getDelegate();
//		FieldMatrix<PerisicFieldElement> B = gs.getBasis();
//		FieldMatrix<PerisicFieldElement> D = gs.getDual();
//		System.err.println(nm + " " + name + " A: " + toString(A));
//		System.err.println(" B: " + toString(B));
//		if (D != null) {
//			System.err.println(" D: " + toString(D));
//		}
		Assert.assertTrue(gs.verify());

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

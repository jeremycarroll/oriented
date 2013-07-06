/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import net.sf.oriented.matrix.FieldLUDecomposition;
import net.sf.oriented.matrix.PerisicFieldElement;
import net.sf.oriented.matrix.RationalMatrix;

import org.apache.commons.math3.linear.BlockFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
import org.junit.Test;

import Jama.LUDecomposition;
import Jama.Matrix;

public class TestLUDecomposition {
	static int chap1[][] = { { 1, 1, 1, 0, 0, 0 }, { 0, 1, 1, 1, 1, 0 },
			{ 0, 0, 1, 0, 1, 1 } };
	static double chap1D[][] = { { 1, 1, 1, 0, 0, 0 }, { 0, 1, 1, 1, 1, 0 },
			{ 0, 0, 1, 0, 1, 1 } };

	

	@Test
	public void testLUt() {
		RationalMatrix matrix = new RationalMatrix(new RationalMatrix(chap1)
				.getDelegate().transpose());
		FieldLUDecomposition<PerisicFieldElement> a = new FieldLUDecomposition<PerisicFieldElement>(
				matrix.getDelegate());

		System.err.println("L: " + new RationalMatrix(a.getL()).toString());
		System.err.println("P: " + new RationalMatrix(a.getP()).toString());
		System.err.println("U: " + new RationalMatrix(a.getU()).toString());
	}

	@Test
	public void testLU() {
		RationalMatrix matrix = new RationalMatrix(
				new RationalMatrix(chap1).getDelegate());
		FieldLUDecomposition<PerisicFieldElement> a = new FieldLUDecomposition<PerisicFieldElement>(
				matrix.getDelegate());

		System.err.println("L: " + new RationalMatrix(a.getL()).toString());
		System.err.println("P: " + new RationalMatrix(a.getP()).toString());
		System.err.println("U: " + new RationalMatrix(a.getU()).toString());
	}

	
	@Test
	public void testDual() {
		FieldMatrix<PerisicFieldElement> matrix = new RationalMatrix(chap1)
				.getDelegate().transpose();
		System.err.println("W: " + new RationalMatrix(matrix).toString());
		FieldMatrix<PerisicFieldElement> bigger = new BlockFieldMatrix<PerisicFieldElement>(
				matrix.getField(), matrix.getRowDimension(),
				matrix.getColumnDimension() + matrix.getRowDimension());
		PerisicFieldElement m[][] = matrix.getData();
		bigger.setSubMatrix(m, 0, 0);
		System.err.println("X: " + new RationalMatrix(bigger).toString());
		int mm = matrix.getColumnDimension();
		int n = matrix.getRowDimension();
		for (int i = 0; i < n; i++) {
			bigger.setEntry(i, i + mm, matrix.getField().getOne());
		}
		System.err.println("Y: " + new RationalMatrix(bigger).toString());
		RationalMatrix matrix2 = new RationalMatrix(bigger.transpose());
		FieldLUDecomposition<PerisicFieldElement> a = new FieldLUDecomposition<PerisicFieldElement>(
				matrix2.getDelegate());

		System.err.println("L: " + new RationalMatrix(a.getL()).toString());
		System.err.println("P: " + new RationalMatrix(a.getP()).toString());
		System.err.println("U: " + new RationalMatrix(a.getU()).toString());

	}

	@Test
	public void testLUJama() {
		Matrix matrix = new Matrix(chap1D);
		LUDecomposition a = new LUDecomposition(matrix.transpose());
		System.err.println("L: " + toString(a.getL()));
		System.err.println("P: " + toString(a.getPivot()));
		System.err.println("U: " + toString(a.getU()));
	}

	private String toString(Matrix l) {
		Writer w = new StringWriter();
		PrintWriter pw = new PrintWriter(w);
		l.print(pw, 5, 2);
		pw.close();
		return w.toString();
	}

	private String toString(int[] pivot) {
		final StringBuffer rslt = new StringBuffer();
		rslt.append("[");
		rslt.append(pivot[0]);
		for (int i = 1; i < pivot.length; i++) {
			rslt.append(",");
			rslt.append(pivot[i]);
		}
		rslt.append("]");
		return rslt.toString();
	}

	@SuppressWarnings("unused")
	private String toString(RealMatrix q) {
		final StringBuffer rslt = new StringBuffer();
		q.walkInColumnOrder(new RealMatrixPreservingVisitor() {

			int lastRow;

			@Override
			public void start(int rows, int columns, int startRow, int endRow,
					int startColumn, int endColumn) {
				rslt.append("[ ");
				lastRow = endRow;
			}

			@Override
			public void visit(int row, int column, double value) {
				if (row == 0) {
					if (column != 0) {
						rslt.append(" ");
					}
					rslt.append("[ ");
				} else {
					rslt.append(" ");
				}
				rslt.append(value);
				if (row == lastRow) {
					rslt.append(" ]");
				} else {
					rslt.append(" ");
				}
			}

			@Override
			public double end() {
				rslt.append(" ]");
				return 0.0d;
			}
		});
		return rslt.toString();
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

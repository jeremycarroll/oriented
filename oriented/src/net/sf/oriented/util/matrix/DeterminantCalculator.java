/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.util.matrix;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldLUDecomposition;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldMatrixChangingVisitor;
import org.apache.commons.math3.linear.FieldMatrixPreservingVisitor;

import com.perisic.ring.QuotientField;
import com.perisic.ring.RingElt;

class DeterminantCalculator {

	private final RingElt formula;
	private final int rank;

	static private ConcurrentMap<Integer, DeterminantCalculator> all = new ConcurrentHashMap<Integer, DeterminantCalculator>();

	private DeterminantCalculator(int h) {
		rank = h;
		FieldMatrix<PerisicFieldElement> m = new Array2DRowFieldMatrix<PerisicFieldElement>(
				PerisicField.QPolynomials, h, h);
		m.walkInOptimizedOrder(new FieldMatrixChangingVisitor<PerisicFieldElement>() {

			@Override
			public void start(int rows, int columns, int startRow, int endRow,
					int startColumn, int endColumn) {

			}

			@Override
			public PerisicFieldElement visit(int row, int column,
					PerisicFieldElement value) {
				return PerisicField.QPolynomials.map("x_" + row + "_" + column);
			}

			@Override
			public PerisicFieldElement end() {
				return null;
			}
		});

		RingElt quotient = new FieldLUDecomposition<PerisicFieldElement>(m)
				.getDeterminant().getDelegate();

		assert (((QuotientField) quotient.getRing()).denominator(quotient)
				.equals(PerisicField.U.one()));
		formula = ((QuotientField) quotient.getRing()).numerator(quotient);
//		System.err.println(formula);
	}

	public static DeterminantCalculator get(int h) {
		if (all.get(h) == null) {
			all.putIfAbsent(h, new DeterminantCalculator(h));
		}
		return all.get(h);
	}

	/**
	 * Compute the determinant of a submatrix with specified columns
	 * @param delegate  Is with rank rows, and more columns
	 * @param columns  This is a list of rank indices and we select the submatrix on these columns
	 * @return The determinant of the selected submatrix.
	 */
	PerisicFieldElement computeFromColumns(
			FieldMatrix<PerisicFieldElement> delegate, int[] columns) {
		final String vars[] = new String[rank * rank];
		final RingElt values[] = new RingElt[rank * rank];
		for (int i = 0; i < rank; i++) {
			final int ii = i;
			delegate.walkInOptimizedOrder(
					new FieldMatrixPreservingVisitor<PerisicFieldElement>() {

						@Override
						public void start(int rows, int columns, int startRow,
								int endRow, int startColumn, int endColumn) {
						}

						@Override
						public void visit(int row, int column,
								PerisicFieldElement value) {
							vars[rank * ii + row] = "x_" + ii + "_" + row;
							values[rank * ii + row] = value.getDelegate();
						}

						@Override
						public PerisicFieldElement end() {
							return null;
						}
					}, 0, rank - 1, columns[i], columns[i]);
		}

		RingElt det = PerisicField.U.evaluatePolynomial(formula, vars, values);
		return PerisicField.Q.create(det);
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

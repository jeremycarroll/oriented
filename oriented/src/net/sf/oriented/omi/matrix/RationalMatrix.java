/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.omi.matrix;

import java.util.List;

import org.apache.commons.math3.linear.BlockFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldMatrixChangingVisitor;
import org.apache.commons.math3.linear.FieldMatrixPreservingVisitor;

public class RationalMatrix {

    private final FieldMatrix<PerisicFieldElement> delegate;

    public RationalMatrix(final int[][] data) {
	delegate = new BlockFieldMatrix<PerisicFieldElement>(PerisicField.Q,
		data.length, data[0].length);
	delegate.walkInOptimizedOrder(new FieldMatrixChangingVisitor<PerisicFieldElement>() {

	    @Override
	    public void start(int rows, int columns, int startRow, int endRow,
		    int startColumn, int endColumn) {}

	    @Override
	    public PerisicFieldElement visit(int row, int column,
		    PerisicFieldElement value) {
		return PerisicField.Q.create(data[row][column]);
	    }

	    @Override
	    public PerisicFieldElement end() {
		return null;
	    }
	});
    }

    public RationalMatrix(FieldMatrix<PerisicFieldElement> d) {
	delegate = d;
    }

    public RationalMatrix(final List<List<PerisicFieldElement>> data) {
	delegate = new BlockFieldMatrix<PerisicFieldElement>(PerisicField.Q,
		data.get(0).size(), data.size());
	delegate.walkInOptimizedOrder(new FieldMatrixChangingVisitor<PerisicFieldElement>() {

	    @Override
	    public void start(int rows, int columns, int startRow, int endRow,
		    int startColumn, int endColumn) {}

	    @Override
	    public PerisicFieldElement visit(int row, int column,
		    PerisicFieldElement value) {
		return data.get(column).get(row);
	    }

	    @Override
	    public PerisicFieldElement end() {
		return null;
	    }
	});
    }

    public int height() {
	return delegate.getRowDimension();
    }

    public int width() {
	return delegate.getColumnDimension();
    }

    @Override
    public String toString() {
	final StringBuffer rslt = new StringBuffer();
	delegate.walkInColumnOrder(new FieldMatrixPreservingVisitor<PerisicFieldElement>() {

	    int lastRow;

	    @Override
	    public void start(int rows, int columns, int startRow, int endRow,
		    int startColumn, int endColumn) {
		rslt.append("[ ");
		lastRow = endRow;
	    }

	    @Override
	    public void visit(int row, int column, PerisicFieldElement value) {
		if (row == 0) {
		    if (column != 0) {
			rslt.append(" ");
		    }
		    rslt.append("[ ");
		} else {
		    rslt.append(" ");
		}
		rslt.append(value.toString());
		if (row == lastRow) {
		    rslt.append(" ]");
		} else {
		    rslt.append(" ");
		}
	    }

	    @Override
	    public PerisicFieldElement end() {
		rslt.append(" ]");
		return null;
	    }
	});
	return rslt.toString();
    }

    public PerisicFieldElement determinant(int[] indices) {
	return DeterminantCalculator.get(height()).compute(delegate, indices);
    }

    public FieldMatrix<PerisicFieldElement> getDelegate() {
	return delegate;
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

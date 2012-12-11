/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
************************************************************************/

package net.sf.oriented.matrix;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.BlockFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;

public class MatrixUtil {
    
    public static <T extends FieldElement<T>> FieldMatrix<T> unit(Field<T> f,int dim) {
	BlockFieldMatrix<T> rslt = new BlockFieldMatrix<T>(f,dim,dim);
	T one = f.getOne();
	for (int i=0;i<dim;i++) {
	    rslt.setEntry(i, i, one);
	}
	return rslt;
    }
    @SuppressWarnings("unchecked")
    public static <T extends FieldElement<T>> FieldMatrix<T> rowExtended(FieldMatrix<T> mat) {
	int n = mat.getColumnDimension();
	FieldMatrix<T> unit = unit(mat.getField(),n);
	return sideBySide(mat,unit);
    }
    public static <T extends FieldElement<T>> FieldMatrix<T> sideBySide(FieldMatrix<T> ...matrices ) {
	int n = matrices[0].getColumnDimension();
	Field<T> f = matrices[0].getField();
	int m = 0;
	for (FieldMatrix<T> mat : matrices ) {
	    m += mat.getRowDimension();
	    if (!mat.getField().equals(f))  {
		throw new IllegalArgumentException("Matrices with different fields");
	    }
	    if (mat.getColumnDimension()!=n) {
		throw new DimensionMismatchException( mat.getColumnDimension(), n);
	    }
	}
	BlockFieldMatrix<T> rslt = new BlockFieldMatrix<T>(f,m,n);
	int r = 0;
	for (FieldMatrix<T> mat : matrices ) {
	    rslt.setSubMatrix(mat.getData(), r, 0);
	    r += mat.getRowDimension();
	}
	return rslt;
    }
    public static <T extends FieldElement<T>> FieldMatrix<T> columnExtended(FieldMatrix<T> mat) {
	return rowExtended(mat.transpose()).transpose();
    }
}


/************************************************************************
    This file is part of the Java Oriented Matroid Library.

    The Java Oriented Matroid Library is free software: you can 
     
     
    

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/
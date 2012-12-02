/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/

package net.sf.oriented.omi.matrix;

import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldMatrixPreservingVisitor;

public class RationalMatrix {
    
    private FieldMatrix<Rational> delegate;

    public RationalMatrix(int[][] chap1) {
	// TODO Auto-generated constructor stub
    }


    public int width() {
	// TODO Auto-generated method stub
	return 0;
    }
    
    
    @Override
    public String toString() {
	final StringBuffer rslt = new StringBuffer();
	delegate.walkInColumnOrder(new FieldMatrixPreservingVisitor<Rational>(){

	    int lastRow;
	    @Override
	    public void start(int rows, int columns, int startRow, int endRow,
		    int startColumn, int endColumn) {
		rslt.append("{ ");
	    }

	    @Override
	    public void visit(int row, int column, Rational value) {
		if ( row == 0 ) {
		    if ( column != 0 ) {
			rslt.append(", ");
		    }
		    rslt.append("{ ");
		} else {
		    rslt.append( ", ");
		}
		rslt.append(value.toString());
		if ( row == lastRow ) {
		    rslt.append( " }");
		} else {
		    rslt.append(" ");
		}
	    }

	    @Override
	    public Rational end() {
		rslt.append(" }");
		return null;
	    }});
	return rslt.toString();
    }

}


/************************************************************************
    This file is part of the Java Oriented Matroid Library.

    The Java Oriented Matroid Library is free software: you can 
    redistribute it and/or modify it under the terms of the GNU General 
    Public License as published by the Free Software Foundation, either 
    version 3 of the License, or (at your option) any later version.

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/
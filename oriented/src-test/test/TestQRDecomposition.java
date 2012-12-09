/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/

package test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.FieldLUDecomposition;
import org.apache.commons.math3.linear.FieldMatrixPreservingVisitor;
import org.apache.commons.math3.linear.QRDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
import org.junit.Test;

import Jama.LUDecomposition;
import Jama.Matrix;

import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.matrix.FieldLUDecompositionX;
import net.sf.oriented.omi.matrix.FieldQRDecomposition;
import net.sf.oriented.omi.matrix.PerisicFieldElement;
import net.sf.oriented.omi.matrix.RationalMatrix;

public class TestQRDecomposition {    
    static int chap1[][] = {
	{ 1, 1, 1, 0, 0, 0 },
	{ 0, 1, 1, 1, 1, 0 },
	{ 0, 0, 1, 0, 1, 1 }
    };   
    static double chap1D[][] = {
	{ 1, 1, 1, 0, 0, 0 },
	{ 0, 1, 1, 1, 1, 0 },
	{ 0, 0, 1, 0, 1, 1 }
    };

    @Test
    public void testDatum() {
	RationalMatrix matrix = new RationalMatrix(chap1);
	FieldQRDecomposition<PerisicFieldElement> a = new FieldQRDecomposition<PerisicFieldElement>(matrix.getDelegate().transpose());
	
	System.err.println("Q: "+new RationalMatrix(a.getQ()).toString());
	System.err.println("QT: "+new RationalMatrix(a.getQT()).toString());
	System.err.println("H: "+new RationalMatrix(a.getH()).toString());
	System.err.println("R: "+new RationalMatrix(a.getR()).toString());
    }

    @Test
    public void testQR() {
	RealMatrix matrix = new Array2DRowRealMatrix(chap1D).transpose();
	QRDecomposition a = new QRDecomposition(matrix);
	
	System.err.println("Q: "+toString(a.getQ()));
	System.err.println("QT: "+toString(a.getQT()));
	System.err.println("H: "+toString(a.getH()));
	System.err.println("R: "+toString(a.getR()));
    }

    @Test
    public void testLU() {
	RationalMatrix matrix = new RationalMatrix(new RationalMatrix(chap1).getDelegate().transpose());
	FieldLUDecompositionX<PerisicFieldElement> a = new FieldLUDecompositionX<PerisicFieldElement>(matrix.getDelegate());
	
	System.err.println("L: "+new RationalMatrix(a.getL()).toString());
	System.err.println("P: "+new RationalMatrix(a.getP()).toString());
	System.err.println("U: "+new RationalMatrix(a.getU()).toString());
    }

    @Test
    public void testLUJama() {
	Matrix matrix = new Matrix(chap1D);
	LUDecomposition a = new LUDecomposition(matrix.transpose());
	
	System.err.println("L: "+a.getL().toString());
	System.err.println("P: "+a.getPivot());
	System.err.println("U: "+a.getU().toString());
    }

    private String toString(RealMatrix q) {
	final StringBuffer rslt = new StringBuffer();
	q.walkInColumnOrder(new RealMatrixPreservingVisitor(){

	    int lastRow;
	    @Override
	    public void start(int rows, int columns, int startRow, int endRow,
		    int startColumn, int endColumn) {
		rslt.append("[ ");
		lastRow = endRow;
	    }

	    @Override
	    public void visit(int row, int column, double value) {
		if ( row == 0 ) {
		    if ( column != 0 ) {
			rslt.append(" ");
		    }
		    rslt.append("[ ");
		} else {
		    rslt.append( " ");
		}
		rslt.append(value);
		if ( row == lastRow ) {
		    rslt.append( " ]");
		} else {
		    rslt.append(" ");
		}
	    }

	    @Override
	    public double end() {
		rslt.append(" ]");
		return 0.0d;
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
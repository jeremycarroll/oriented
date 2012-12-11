/**********************************************************************
 * PROMINENT NOTICE:
 * 
 * This file is a derivative work from the source file:
 * commons-math3-3.0-sources.jar/org/apache/commons/math3/linear/QRDecomposition.java
 * 
 * The original work was licensed under the Apache License, Version 2.0.
 * 
 * This file has been modified extensively with the following intents:
 * 1) Support FieldMatrix<T extends FieldElement<T>> rather than RealMatrix
 * 2) Do not support functionality that is not required in net.sf.oriented
 *    In particular, the Solver is not included.
 * 3) The package and class name are changed.
 *
 **/

/*************************************************************************
 Modifications are:
 (c) Copyright 2012 Jeremy J. Carroll
 
 ************************************************************************/

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sf.oriented.matrix;

import java.lang.reflect.Array;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.FastMath;

/**
 * Calculates the QR-decomposition of a matrix.
 * <p>
 * The QR-decomposition of a matrix A consists of two matrices Q and R that
 * satisfy: A = QR, Q is orthogonal (Q<sup>T</sup>Q = I), and R is upper
 * triangular. If A is m&times;n, Q is m&times;m and R m&times;n.
 * </p>
 * <p>
 * This class compute the decomposition using Householder reflectors.
 * </p>
 * <p>
 * For efficiency purposes, the decomposition in packed form is transposed. This
 * allows inner loop to iterate inside rows, which is much more cache-efficient
 * in Java.
 * </p>
 * <p>
 * This class is based on the class with similar name from the <a
 * href="http://math.nist.gov/javanumerics/jama/">JAMA</a> library, with the
 * following changes:
 * </p>
 * <ul>
 * <li>a {@link #getQT() getQT} method has been added,</li>
 * <li>the {@code solve} and {@code isFullRank} methods have been replaced by a
 * {@link #getSolver() getSolver} method and the equivalent methods provided by
 * the returned {@link DecompositionSolver}.</li>
 * </ul>
 * 
 * @see <a
 *      href="http://mathworld.wolfram.com/QRDecomposition.html">MathWorld</a>
 * @see <a href="http://en.wikipedia.org/wiki/QR_decomposition">Wikipedia</a>
 * 
 * @version $Id: QRDecomposition.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 1.2 (changed to concrete class in 3.0)
 */
public class FieldQRDecomposition<T extends FieldElement<T>> {
    /**
     * A packed TRANSPOSED representation of the QR decomposition.
     * <p>
     * The elements BELOW the diagonal are the elements of the UPPER triangular
     * matrix R, and the rows ABOVE the diagonal are the Householder reflector
     * vectors from which an explicit form of Q can be recomputed if desired.
     * </p>
     */
    private T[][] qrt;
    /** The diagonal elements of R. */
    private T[] rDiag;
    /** Cached value of Q. */
    private FieldMatrix<T> cachedQ;
    /** Cached value of QT. */
    private FieldMatrix<T> cachedQT;
    /** Cached value of R. */
    private FieldMatrix<T> cachedR;
    /** Cached value of H. */
    private FieldMatrix<T> cachedH;
    private final Field<T> field;

    /**
     * Calculates the QR-decomposition of the given matrix.
     * 
     * @param matrix
     *            The matrix to decompose.
     * @param threshold
     *            Singularity threshold.
     */
    public FieldQRDecomposition(FieldMatrix<T> matrix) {
	field = matrix.getField();

	final int m = matrix.getRowDimension();
	final int n = matrix.getColumnDimension();
	qrt = matrix.transpose().getData();
	rDiag = buildArray(FastMath.min(m, n));
	cachedQ = null;
	cachedQT = null;
	cachedR = null;
	cachedH = null;

	/*
	 * The QR decomposition of a matrix A is calculated using Householder
	 * reflectors by repeating the following operations to each minor
	 * A(minor,minor) of A:
	 */
	for (int minor = 0; minor < FastMath.min(m, n); minor++) {

	    final T[] qrtMinor = qrt[minor];

	    /*
	     * Let x be the first column of the minor, and a^2 = |x|^2. x will
	     * be in the positions qr[minor][minor] through qr[m][minor]. The
	     * first column of the transformed minor will be (a,0,0,..)' The
	     * sign of a is chosen to be opposite to the sign of the first
	     * component of x. Let's find a:
	     */
	    T xNormSqr = field.getZero();
	    for (int row = minor; row < m; row++) {
		final T c = qrtMinor[row];
		xNormSqr = xNormSqr.add(c.multiply(c));
	    }
	    final T a = field.getOne();
	    // (qrtMinor[minor] > 0) ? -FastMath.sqrt(xNormSqr) :
	    // FastMath.sqrt(xNormSqr);

	    if (xNormSqr.equals(field.getZero())) {
		rDiag[minor] = xNormSqr;
	    } else {
		rDiag[minor] = xNormSqr;

		/*
		 * Calculate the normalized reflection vector v and transform
		 * the first column. We know the norm of v beforehand: v = x-ae
		 * so |v|^2 = <x-ae,x-ae> = <x,x>-2a<x,e>+a^2<e,e> =
		 * a^2+a^2-2a<x,e> = 2a*(a - <x,e>). Here <x, e> is now
		 * qr[minor][minor]. v = x-ae is stored in the column at qr:
		 */
		qrtMinor[minor] = qrtMinor[minor].subtract(a); // now |v|^2 =
							       // -2a*(qr[minor][minor])

		/*
		 * Transform the rest of the columns of the minor: They will be
		 * transformed by the matrix H = I-2vv'/|v|^2. If x is a column
		 * vector of the minor, then Hx = (I-2vv'/|v|^2)x =
		 * x-2vv'x/|v|^2 = x - 2<x,v>/|v|^2 v. Therefore the
		 * transformation is easily calculated by subtracting the column
		 * vector (2<x,v>/|v|^2)v from x.
		 * 
		 * Let 2<x,v>/|v|^2 = alpha. From above we have |v|^2 =
		 * -2a*(qr[minor][minor]), so alpha =
		 * -<x,v>/(a*qr[minor][minor])
		 */
		for (int col = minor + 1; col < n; col++) {
		    final T[] qrtCol = qrt[col];
		    T alpha = field.getZero();
		    for (int row = minor; row < m; row++) {
			alpha = alpha.subtract(qrtCol[row]
				.multiply(qrtMinor[row]));
		    }
		    alpha = alpha.divide(a.multiply(qrtMinor[minor]));

		    // Subtract the column vector alpha*v from x.
		    for (int row = minor; row < m; row++) {
			qrtCol[row] = qrtCol[row].subtract(alpha
				.multiply(qrtMinor[row]));
		    }
		}
	    }
	}
    }

    @SuppressWarnings("unchecked")
    private T[] buildArray(int length) {
	return (T[]) Array.newInstance(field.getRuntimeClass(), length);
    }

    @SuppressWarnings("unchecked")
    private T[][] buildArray(int w, int h) {
	return (T[][]) Array.newInstance(field.getRuntimeClass(), w, h);
    }

    /**
     * Returns the matrix R of the decomposition.
     * <p>
     * R is an upper-triangular matrix
     * </p>
     * 
     * @return the R matrix
     */
    public FieldMatrix<T> getR() {

	if (cachedR == null) {

	    // R is supposed to be m x n
	    final int n = qrt.length;
	    final int m = qrt[0].length;
	    T[][] ra = buildArray(m, n);
	    // copy the diagonal from rDiag and the upper triangle of qr
	    for (int row = FastMath.min(m, n) - 1; row >= 0; row--) {
		ra[row][row] = rDiag[row];
		for (int col = row + 1; col < n; col++) {
		    ra[row][col] = qrt[col][row];
		}
	    }
	    cachedR = MatrixUtils.createFieldMatrix(ra);
	}

	// return the cached matrix
	return cachedR;
    }

    /**
     * Returns the matrix Q of the decomposition.
     * <p>
     * Q is an orthogonal matrix
     * </p>
     * 
     * @return the Q matrix
     */
    public FieldMatrix<T> getQ() {
	if (cachedQ == null) {
	    cachedQ = getQT().transpose();
	}
	return cachedQ;
    }

    /**
     * Returns the transpose of the matrix Q of the decomposition.
     * <p>
     * Q is an orthogonal matrix
     * </p>
     * 
     * @return the Q matrix
     */
    public FieldMatrix<T> getQT() {
	if (cachedQT == null) {

	    // QT is supposed to be m x m
	    final int n = qrt.length;
	    final int m = qrt[0].length;
	    T[][] qta = buildArray(m, m);

	    /*
	     * Q = Q1 Q2 ... Q_m, so Q is formed by first constructing Q_m and
	     * then applying the Householder transformations
	     * Q_(m-1),Q_(m-2),...,Q1 in succession to the result
	     */
	    for (int minor = m - 1; minor >= FastMath.min(m, n); minor--) {
		qta[minor][minor] = field.getOne();
	    }

	    for (int minor = FastMath.min(m, n) - 1; minor >= 0; minor--) {
		final T[] qrtMinor = qrt[minor];
		qta[minor][minor] = field.getOne();
		if (!qrtMinor[minor].equals(field.getOne())) {
		    for (int col = minor; col < m; col++) {
			T alpha = field.getZero();
			for (int row = minor; row < m; row++) {
			    alpha = alpha.subtract(qta[col][row]
				    .multiply(qrtMinor[row]));
			}
			alpha = alpha.divide(rDiag[minor]
				.multiply(qrtMinor[minor]));

			for (int row = minor; row < m; row++) {
			    qta[col][row] = qta[col][row].subtract(alpha
				    .multiply(qrtMinor[row]));
			}
		    }
		}
	    }
	    cachedQT = MatrixUtils.createFieldMatrix(qta);
	}

	// return the cached matrix
	return cachedQT;
    }

    /**
     * Returns the Householder reflector vectors.
     * <p>
     * H is a lower trapezoidal matrix whose columns represent each successive
     * Householder reflector vector. This matrix is used to compute Q.
     * </p>
     * 
     * @return a matrix containing the Householder reflector vectors
     */
    public FieldMatrix<T> getH() {
	if (cachedH == null) {

	    final int n = qrt.length;
	    final int m = qrt[0].length;
	    T[][] ha = buildArray(m, n);
	    for (int i = 0; i < m; ++i) {
		for (int j = 0; j < FastMath.min(i + 1, n); ++j) {
		    ha[i][j] = qrt[j][i].divide(rDiag[j].negate());
		}
	    }
	    cachedH = MatrixUtils.createFieldMatrix(ha);
	}

	// return the cached matrix
	return cachedH;
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

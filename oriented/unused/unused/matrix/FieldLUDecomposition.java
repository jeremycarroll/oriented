/**********************************************************************
 * PROMINENT NOTICE:
 * 
 * This file is a derivative work from the source file:
 * commons-math3-3.0-sources.jar/org/apache/commons/math3/linear/FieldLUDecomposition.java
 * 
 * The original work was licensed under the Apache License, Version 2.0.
 * 
 * This file has been modified with the following intents:
 * 1) Allow m by n matrices with m > n
 * 
 * The changes were made by comparing with the Jama file from which
 * the original was apparently derived.
 *
 **/

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

package unused.matrix;

import java.lang.reflect.Array;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.ArrayFieldVector;
import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.FieldDecompositionSolver;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.FieldVector;
import org.apache.commons.math3.linear.NonSquareMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;

/**
 * Calculates the LUP-decomposition of a square matrix.
 * <p>
 * The LUP-decomposition of a matrix A consists of three matrices L, U and P
 * that satisfy: PA = LU, L is lower triangular, and U is upper triangular and P
 * is a permutation matrix. All matrices are m&times;m.
 * </p>
 * <p>
 * For an m-by-n matrix A with m >= n, the LU decomposition is an m-by-n unit
 * lower triangular matrix L, an n-by-n upper triangular matrix U, and a
 * permutation vector piv of length m so that A(piv,:) = L*U. If m < n, then L
 * is m-by-m and U is m-by-n.
 * </p>
 * <p>
 * The LU decompostion with pivoting always exists, even if the matrix is
 * singular, so the constructor will never fail. The primary use of the LU
 * decomposition is in the solution of square systems of simultaneous linear
 * equations. This will fail if isNonsingular() returns false.
 * </p>
 * <p>
 * Since {@link FieldElement field elements} do not provide an ordering
 * operator, the permutation matrix is computed here only in order to avoid a
 * zero pivot element, no attempt is done to get the largest pivot element.
 * </p>
 * <p>
 * This class is based on the class with similar name from the <a
 * href="http://math.nist.gov/javanumerics/jama/">JAMA</a> library.
 * </p>
 * <ul>
 * <li>a {@link #getP() getP} method has been added,</li>
 * <li>the {@code det} method has been renamed as {@link #getDeterminant()
 * getDeterminant},</li>
 * <li>the {@code getDoublePivot} method has been removed (but the int based
 * {@link #getPivot() getPivot} method has been kept),</li>
 * <li>the {@code solve} and {@code isNonSingular} methods have been replaced by
 * a {@link #getSolver() getSolver} method and the equivalent methods provided
 * by the returned {@link DecompositionSolver}.</li>
 * </ul>
 * 
 * @param <T>
 *            the type of the field elements
 * @see <a
 *      href="http://mathworld.wolfram.com/LUDecomposition.html">MathWorld</a>
 * @see <a href="http://en.wikipedia.org/wiki/LU_decomposition">Wikipedia</a>
 * @version $Id: FieldLUDecomposition.java 1296611 2012-03-03 11:48:57Z sebb $
 * @since 2.0 (changed to concrete class in 3.0)
 */
public class FieldLUDecomposition<T extends FieldElement<T>> {

	/** Field to which the elements belong. */
	private final Field<T> field;

	/** Entries of LU decomposition. */
	private final T[][] lu;

	/**
	 * Row and column dimensions, and pivot sign.
	 * 
	 * @serial column dimension.
	 * @serial row dimension.
	 * @serial pivot sign.
	 */
	private final int m, n;

	/** Pivot permutation associated with LU decomposition. */
	private final int[] pivot;

	/** Parity of the permutation associated with the LU decomposition. */
	private boolean even;

	/** Cached value of L. */
	private FieldMatrix<T> cachedL;

	/** Cached value of U. */
	private FieldMatrix<T> cachedU;

	/** Cached value of P. */
	private FieldMatrix<T> cachedP;

	private final int min;

	/**
	 * Calculates the LU-decomposition of the given matrix.
	 * 
	 * @param matrix
	 *            The matrix to decompose.
	 * @throws NonSquareMatrixException
	 *             if matrix is not square
	 */
	public FieldLUDecomposition(FieldMatrix<T> matrix) {

		// final int m = matrix.getColumnDimension();
		field = matrix.getField();
		m = matrix.getRowDimension();
		n = matrix.getColumnDimension();
		min = Math.min(n, m);
		// if (m < n) {
		// throw new LUDimensionException("Too few rows");
		// }
		lu = matrix.getData();
		pivot = new int[m];
		cachedL = null;
		cachedU = null;
		cachedP = null;

		// Initialize permutation array and parity
		for (int row = 0; row < m; row++) {
			pivot[row] = row;
		}
		even = true;

		// Loop over columns
		for (int col = 0; col < n; col++) {

			T sum = field.getZero();

			// upper
			for (int row = 0; row < col && row < m; row++) {
				final T[] luRow = lu[row];
				sum = luRow[col];
				for (int i = 0; i < row; i++) {
					sum = sum.subtract(luRow[i].multiply(lu[i][col]));
				}
				luRow[col] = sum;
			}

			// lower
			int nonZero = col; // permutation row
			for (int row = col; row < m; row++) {
				final T[] luRow = lu[row];
				sum = luRow[col];
				for (int i = 0; i < col; i++) {
					sum = sum.subtract(luRow[i].multiply(lu[i][col]));
				}
				luRow[col] = sum;

				if (lu[nonZero][col].equals(field.getZero())) {
					// try to select a better permutation choice
					++nonZero;
				}
			}

			// // Singularity check
			// if (nonZero >= m) {
			// singular = true;
			// return;
			// }

			// Pivot if necessary
			if (nonZero != col && nonZero < m) {
				T tmp = field.getZero();
				for (int i = 0; i < n; i++) {
					tmp = lu[nonZero][i];
					lu[nonZero][i] = lu[col][i];
					lu[col][i] = tmp;
				}
				int temp = pivot[nonZero];
				pivot[nonZero] = pivot[col];
				pivot[col] = temp;
				even = !even;
			}

			// Divide the lower elements by the "winning" diagonal elt.
			if (col < m) {
				final T luDiag = lu[col][col];
				if (!luDiag.equals(field.getZero())) {
					for (int row = col + 1; row < m; row++) {
						final T[] luRow = lu[row];
						luRow[col] = luRow[col].divide(luDiag);
					}
				}
			}
		}

	}

	/**
	 * Returns the matrix L of the decomposition.
	 * <p>
	 * L is a lower-triangular matrix
	 * </p>
	 * <p>
	 * For an m-by-n matrix A with m >= n, an m-by-n unit lower triangular
	 * matrix L If m < n, then L is m-by-m
	 * </p>
	 * 
	 * @return the L matrix (or null if decomposed matrix is singular)
	 */
	public FieldMatrix<T> getL() {
		if ((cachedL == null)) {
			cachedL = new Array2DRowFieldMatrix<>(field, m, min);
			for (int i = 0; i < m; ++i) {
				final T[] luI = lu[i];
				for (int j = 0; j < min; j++) {
					if (j < i) {
						cachedL.setEntry(i, j, luI[j]);
					} else if (j == i) {
						cachedL.setEntry(i, i, field.getOne());
					}
				}
			}
		}
		return cachedL;
	}

	/**
	 * Returns the matrix U of the decomposition.
	 * <p>
	 * U is an upper-triangular matrix
	 * </p>
	 * 
	 * <p>
	 * For an m-by-n matrix A with m >= n, an n-by-n upper triangular matrix U,
	 * If m < n, then U is m-by-n.
	 * </p>
	 * 
	 * @return the U matrix (or null if decomposed matrix is singular)
	 */
	public FieldMatrix<T> getU() {
		if ((cachedU == null)) {
			cachedU = new Array2DRowFieldMatrix<>(field, min, n);
			for (int i = 0; i < min; ++i) {
				final T[] luI = lu[i];
				for (int j = i; j < n; ++j) {
					cachedU.setEntry(i, j, luI[j]);
				}
			}
		}
		return cachedU;
	}

	/**
	 * Returns the P rows permutation matrix.
	 * <p>
	 * P is a sparse matrix with exactly one element set to 1.0 in each row and
	 * each column, all other elements being set to 0.0.
	 * </p>
	 * <p>
	 * The positions of the 1 elements are given by the {@link #getPivot() pivot
	 * permutation vector}.
	 * </p>
	 * 
	 * @return the P rows permutation matrix (or null if decomposed matrix is
	 *         singular)
	 * @see #getPivot()
	 */
	public FieldMatrix<T> getP() {
		if ((cachedP == null)) {
			final int m = pivot.length;
			cachedP = new Array2DRowFieldMatrix<>(field, m, m);
			for (int i = 0; i < m; ++i) {
				cachedP.setEntry(i, pivot[i], field.getOne());
			}
		}
		return cachedP;
	}

	/**
	 * Returns the pivot permutation vector.
	 * 
	 * @return the pivot permutation vector
	 * @see #getP()
	 */
	public int[] getPivot() {
		return pivot.clone();
	}

	/**
	 * Return the determinant of the matrix.
	 * 
	 * @return determinant of the matrix
	 */
	public T getDeterminant() {
		if (isSingular())
			return field.getZero();
		else {
			final int m = pivot.length;
			T determinant = even ? field.getOne() : field.getZero().subtract(
					field.getOne());
			for (int i = 0; i < m; i++) {
				determinant = determinant.multiply(lu[i][i]);
			}
			return determinant;
		}
	}

	/**
	 * Get a solver for finding the A &times; X = B solution in exact linear
	 * sense.
	 * 
	 * @return a solver
	 */
	public FieldDecompositionSolver<T> getSolver() {
		return new Solver<>(field, lu, pivot, isSingular());
	}

	private boolean isSingular() {
		for (int j = 0; j < min; j++) {
			if (lu[j][j].equals(field.getZero()))
				return true;
		}
		return false;
	}

	/** Specialized solver. */
	private static class Solver<T extends FieldElement<T>> implements
			FieldDecompositionSolver<T> {

		/** Field to which the elements belong. */
		private final Field<T> field;

		/** Entries of LU decomposition. */
		private final T[][] lu;

		/** Pivot permutation associated with LU decomposition. */
		private final int[] pivot;

		/** Singularity indicator. */
		private final boolean singular;

		/**
		 * Build a solver from decomposed matrix.
		 * 
		 * @param field
		 *            field to which the matrix elements belong
		 * @param lu
		 *            entries of LU decomposition
		 * @param pivot
		 *            pivot permutation associated with LU decomposition
		 * @param singular
		 *            singularity indicator
		 */
		private Solver(final Field<T> field, final T[][] lu, final int[] pivot,
				final boolean singular) {
			this.field = field;
			this.lu = lu;
			this.pivot = pivot;
			this.singular = singular;
		}

		/** {@inheritDoc} */
		@Override
		public boolean isNonSingular() {
			return !singular;
		}

		/** {@inheritDoc} */
		@Override
		public FieldVector<T> solve(FieldVector<T> b) {
			try {
				return solve((ArrayFieldVector<T>) b);
			} catch (ClassCastException cce) {

				final int m = pivot.length;
				if (b.getDimension() != m)
					throw new DimensionMismatchException(b.getDimension(), m);
				if (singular)
					throw new SingularMatrixException();

				// field is of type T
				final T[] bp = (T[]) Array.newInstance(field.getRuntimeClass(),
						m);

				// Apply permutations to b
				for (int row = 0; row < m; row++) {
					bp[row] = b.getEntry(pivot[row]);
				}

				// Solve LY = b
				for (int col = 0; col < m; col++) {
					final T bpCol = bp[col];
					for (int i = col + 1; i < m; i++) {
						bp[i] = bp[i].subtract(bpCol.multiply(lu[i][col]));
					}
				}

				// Solve UX = Y
				for (int col = m - 1; col >= 0; col--) {
					bp[col] = bp[col].divide(lu[col][col]);
					final T bpCol = bp[col];
					for (int i = 0; i < col; i++) {
						bp[i] = bp[i].subtract(bpCol.multiply(lu[i][col]));
					}
				}

				return new ArrayFieldVector<>(field, bp, false);

			}
		}

		/**
		 * Solve the linear equation A &times; X = B.
		 * <p>
		 * The A matrix is implicit here. It is
		 * </p>
		 * 
		 * @param b
		 *            right-hand side of the equation A &times; X = B
		 * @return a vector X such that A &times; X = B
		 * @throws DimensionMismatchException
		 *             if the matrices dimensions do not match.
		 * @throws SingularMatrixException
		 *             if the decomposed matrix is singular.
		 */
		public ArrayFieldVector<T> solve(ArrayFieldVector<T> b) {
			final int m = pivot.length;
			final int length = b.getDimension();
			if (length != m)
				throw new DimensionMismatchException(length, m);
			if (singular)
				throw new SingularMatrixException();

			// field is of type T
			final T[] bp = (T[]) Array.newInstance(field.getRuntimeClass(), m);

			// Apply permutations to b
			for (int row = 0; row < m; row++) {
				bp[row] = b.getEntry(pivot[row]);
			}

			// Solve LY = b
			for (int col = 0; col < m; col++) {
				final T bpCol = bp[col];
				for (int i = col + 1; i < m; i++) {
					bp[i] = bp[i].subtract(bpCol.multiply(lu[i][col]));
				}
			}

			// Solve UX = Y
			for (int col = m - 1; col >= 0; col--) {
				bp[col] = bp[col].divide(lu[col][col]);
				final T bpCol = bp[col];
				for (int i = 0; i < col; i++) {
					bp[i] = bp[i].subtract(bpCol.multiply(lu[i][col]));
				}
			}

			return new ArrayFieldVector<>(bp, false);
		}

		/** {@inheritDoc} */
		@Override
		public FieldMatrix<T> solve(FieldMatrix<T> b) {
			final int m = pivot.length;
			if (b.getRowDimension() != m)
				throw new DimensionMismatchException(b.getRowDimension(), m);
			if (singular)
				throw new SingularMatrixException();

			final int nColB = b.getColumnDimension();

			// Apply permutations to b
			// field is of type T
			final T[][] bp = (T[][]) Array.newInstance(field.getRuntimeClass(),
					new int[] { m, nColB });
			for (int row = 0; row < m; row++) {
				final T[] bpRow = bp[row];
				final int pRow = pivot[row];
				for (int col = 0; col < nColB; col++) {
					bpRow[col] = b.getEntry(pRow, col);
				}
			}

			// Solve LY = b
			for (int col = 0; col < m; col++) {
				final T[] bpCol = bp[col];
				for (int i = col + 1; i < m; i++) {
					final T[] bpI = bp[i];
					final T luICol = lu[i][col];
					for (int j = 0; j < nColB; j++) {
						bpI[j] = bpI[j].subtract(bpCol[j].multiply(luICol));
					}
				}
			}

			// Solve UX = Y
			for (int col = m - 1; col >= 0; col--) {
				final T[] bpCol = bp[col];
				final T luDiag = lu[col][col];
				for (int j = 0; j < nColB; j++) {
					bpCol[j] = bpCol[j].divide(luDiag);
				}
				for (int i = 0; i < col; i++) {
					final T[] bpI = bp[i];
					final T luICol = lu[i][col];
					for (int j = 0; j < nColB; j++) {
						bpI[j] = bpI[j].subtract(bpCol[j].multiply(luICol));
					}
				}
			}

			return new Array2DRowFieldMatrix<>(field, bp, false);

		}

		/** {@inheritDoc} */
		@Override
		public FieldMatrix<T> getInverse() {
			final int m = pivot.length;
			final T one = field.getOne();
			FieldMatrix<T> identity = new Array2DRowFieldMatrix<>(field, m, m);
			for (int i = 0; i < m; ++i) {
				identity.setEntry(i, i, one);
			}
			return solve(identity);
		}
	}
}

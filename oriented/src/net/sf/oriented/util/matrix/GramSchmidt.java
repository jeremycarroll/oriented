/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.util.matrix;

import java.lang.reflect.Array;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Verify;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.linear.BlockFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;

/**
 * The Gram–Schmidt process, but without normalizing.
 * We omit normalization since we work over
 * the rationals.
 * @author jeremycarroll
 *
 * @param <T>
 */
public class GramSchmidt<T extends FieldElement<T>> implements Verify {

	private final Field<T> field;
	private final T[][] data;
	private final T[] uDotu;
	private final int n;
	private final int m;
	private boolean dualComputed = false;

	/**
	 * Initialize the GramSchmidt process over the given matrix.
	 * @param matrix A matrix with more columns than rows.
	 */
	public GramSchmidt(FieldMatrix<T> matrix) {
		field = matrix.getField();
		n = matrix.getRowDimension();
		m = matrix.getColumnDimension();
		if (n > m)
			throw new IllegalArgumentException();
		data = buildArray(m, m);
		uDotu = (T[]) Array.newInstance(field.getRuntimeClass(), m);
		matrix.copySubMatrix(0, n - 1, 0, m - 1, data);
		for (int i = 0; i < n; i++) {
			if (!gramSchmidt(i))
				throw new SingularMatrixException();
		}
	}

	private T[][] buildArray(int r, int c) {
		return (T[][]) Array.newInstance(field.getRuntimeClass(), r, c);
	}

	/**
	 * u_k = v_k - sum_{j=0}^{k-1} proj_{u_j}(v_k)
	 * 
	 * As side effect, sets uDotu[k]
	 * 
	 * @param k
	 * @return true if u_k as above is non-zero, false if zero.
	 */
	private boolean gramSchmidt(int k) {
		for (int j = 0; j < k; j++) {
			T uDotv = dot(data[j], data[k]);
			T ratio = uDotv.divide(uDotu[j]);
			for (int i = 0; i < m; i++) {
				data[k][i] = data[k][i].subtract(data[j][i].multiply(ratio));
			}
		}
		uDotu[k] = dot(data[k], data[k]);
		return !field.getZero().equals(uDotu[k]);
	}

	private T dot(T[] u, T[] v) {
		T rslt = field.getZero();
		for (int i = 0; i < m; i++) {
			rslt = rslt.add(u[i].multiply(v[i]));
		}
		return rslt;
	}

	/**
	 * An orthogonal basis spanning the same space as the original matrix.
	 */
	public FieldMatrix<T> getBasis() {
		T[][] subData;
		if (m == n) {
			subData = data;
		} else {
			subData = buildArray(n, m);
			System.arraycopy(data, 0, subData, 0, n);
		}
		FieldMatrix<T> r = new BlockFieldMatrix<>(field, n, m);
		r.setSubMatrix(subData, 0, 0);
		return r;
	}

	/**
	 * Continues the GramSchmidt process to compute a dual basis too.
	 * This is underdocumented.
	 */
	public FieldMatrix<T> getDual() {
		if (n == m)
			return null;
		lazyDual();
		T[][] subData = buildArray(m - n, m);
		System.arraycopy(data, n, subData, 0, m - n);
		FieldMatrix<T> r = new BlockFieldMatrix<>(field, m - n, m);
		r.setSubMatrix(subData, 0, 0);
		return r;

	}

	private synchronized void lazyDual() {
		if (dualComputed)
			return;
		int id = 0;
		T zero = field.getZero();
		T one = field.getOne();
		for (int j = n; j < m; j++) {
			boolean first = true;
			do {
				if (first) {
					for (int i = 0; i < m; i++) {
						data[j][i] = i == id ? one : zero;
					}
					first = false;
				} else {
					data[j][id - 1] = zero;
					data[j][id] = one;
				}
				id++;
			} while (!gramSchmidt(j));
		}
		dualComputed = true;
	}

	@Override
	public void verify() throws AxiomViolation {
		T zero = field.getZero();
		lazyDual();
		for (int i = 0; i < m; i++) {
			if (zero.equals(dot(data[i], data[i])))  {
                throw new AxiomViolation(this,"non-zero");
			}
			for (int j = i + 1; j < m; j++) {
				if (!zero.equals(dot(data[i], data[j]))) {
	                throw new AxiomViolation(this,"perpendicular");
	            }
			}
		}
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

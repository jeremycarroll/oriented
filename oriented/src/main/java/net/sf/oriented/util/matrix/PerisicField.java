/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.util.matrix;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

import com.perisic.ring.QuotientField;
import com.perisic.ring.RationalField;
import com.perisic.ring.Ring;
import com.perisic.ring.RingElt;
import com.perisic.ring.UniversalPolynomialRing;

/**
 * Combine the perisic library with the apache commons math library.
 * We implement {@link org.apache.commons.math3.Field} using
 * classes from {@link com.perisic.ring}
 * @author jeremycarroll
 *
 */
public abstract class PerisicField implements Field<PerisicFieldElement> {

	static final UniversalPolynomialRing U = new UniversalPolynomialRing(Ring.Q);

	private static final QuotientField QU = new QuotientField(U);

	final private PerisicFieldElement zero, one;
	final private Ring ring;

	/**
	 * Imports the rationals.
	 * @see Ring#Q
	 */
	public static final PerisicField Q = new PerisicField(Ring.Q) {
		@Override
		int hashCode(RingElt delegate) {
			return rationalHashCode(delegate);
		}
	};
	/**
	 * A universal polynomial field over the rationals.
	 */
	public static final PerisicField Polynomials = new PerisicField(U) {

		@Override
		int hashCode(RingElt delegate) {
			return polyHashCode(delegate);
		}
	};
    /**
     * A universal polynomial field, with quotient polynomials over the rationals.
     */
	public static final PerisicField QPolynomials = new PerisicField(QU) {

		@Override
		int hashCode(RingElt delegate) {
			return (polyHashCode(QU.numerator(delegate)) * 7)
					^ (polyHashCode(QU.denominator(delegate)) * 11);
		}
	};

	private PerisicField(Ring r) {
		zero = new PerisicFieldElement(r.zero());
		one = new PerisicFieldElement(r.one());
		ring = r;
	}

	protected static int polyHashCode(RingElt delegate) {
		throw new UnsupportedOperationException(
				"hashCode rather difficult to implement for polynomials");
	}

	private static int rationalHashCode(RingElt delegate) {
		return (RationalField.numeratorToBigInteger(delegate).hashCode() * 5)
				^ (RationalField.denominatorToBigInteger(delegate).hashCode() * 13);
	}

	@Override
	public PerisicFieldElement getZero() {
		return zero;
	}

	@Override
	public PerisicFieldElement getOne() {
		return one;
	}

	@Override
	public Class<? extends FieldElement<PerisicFieldElement>> getRuntimeClass() {
		return PerisicFieldElement.class;
	}

	static PerisicField getField(Ring ring) {
		if (ring == Ring.Q)
			return Q;
		if (ring == U)
			return Polynomials;
		throw new IllegalArgumentException();
	}

	/**
	 * Map an integer into this field.
	 */
	public PerisicFieldElement create(int i) {
		return new PerisicFieldElement(ring.map(i));
	}
	/**
	 * Parse some string, and map it into this field
	 * @param str a string representation recognized by the underlying {@link Ring}
	 * @return the field element corresponding to {@code str}
	 */
	public PerisicFieldElement map(String str) {
		return new PerisicFieldElement(ring.map(str));
	}

	PerisicFieldElement create(RingElt det) {
		return new PerisicFieldElement(det);
	}

    /**
     * Parse some string as a rational, and map it into this field
     * @param rational a string representation recognized by the underlying {@link Ring#Q}
     * @return the field element corresponding to {@code rational}
     */
	public static PerisicFieldElement rational(String rational) {
		return Q.map(rational);
	}

	abstract int hashCode(RingElt delegate);

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

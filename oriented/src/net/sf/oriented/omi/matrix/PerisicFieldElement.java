/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.omi.matrix;

import java.math.BigInteger;
import org.apache.commons.math3.FieldElement;

import com.perisic.ring.Ring;
import com.perisic.ring.RingElt;

public final class PerisicFieldElement implements
	FieldElement<PerisicFieldElement> {

    // static final RationalField RATIONAL_FIELD = new RationalField();
    //
    // private static final class RationalField implements
    // Field<PerisicFieldElement> {
    // @Override
    // public PerisicFieldElement getZero() {
    // return ZERO;
    // }
    //
    // @Override
    // public PerisicFieldElement getOne() {
    // return ONE;
    // }
    //
    // @Override
    // public Class<? extends FieldElement<PerisicFieldElement>>
    // getRuntimeClass() {
    // return PerisicFieldElement.class;
    // }
    // }

    private final RingElt delegate;

    // public static final PerisicFieldElement ONE = new
    // PerisicFieldElement(Q.one());
    // public static final PerisicFieldElement ZERO = new
    // PerisicFieldElement(Q.zero());

    PerisicFieldElement(RingElt d) {
	delegate = d;
    }

    // PerisicFieldElement(int n) {
    // this(Q.construct(BigInteger.valueOf(n), BigInteger.ONE));
    // }

    @Override
    public PerisicFieldElement add(PerisicFieldElement a) {
	return new PerisicFieldElement(getRing().add(delegate, a.delegate));
    }

    @Override
    public PerisicFieldElement subtract(PerisicFieldElement a) {
	return new PerisicFieldElement(getRing().sub(delegate, a.delegate));
    }

    @Override
    public PerisicFieldElement negate() {
	return new PerisicFieldElement(getRing().neg(delegate));
    }

    @Override
    public PerisicFieldElement multiply(int n) {
	return new PerisicFieldElement(getRing().mult(delegate,
		getRing().map(n)));
    }

    @Override
    public PerisicFieldElement multiply(PerisicFieldElement a) {
	return new PerisicFieldElement(getRing().mult(delegate, a.delegate));
    }

    @Override
    public PerisicFieldElement divide(PerisicFieldElement a) {
	return new PerisicFieldElement(getRing().div(delegate, a.delegate));
    }

    @Override
    public PerisicFieldElement reciprocal() {
	return new PerisicFieldElement(getRing().inv(delegate));
    }

    @Override
    public PerisicField getField() {
	return PerisicField.getField(getRing());
    }

    @Override
    public String toString() {
	return delegate.toString();
    }

    public int sign() {
	return com.perisic.ring.RationalField.numeratorToBigInteger(delegate)
		.compareTo(BigInteger.ZERO);
    }

    private Ring getRing() {
	return delegate.getRing();
    }

    RingElt getDelegate() {
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

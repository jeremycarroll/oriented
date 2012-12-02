/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/

package net.sf.oriented.omi.matrix;

import java.math.BigInteger;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

import static com.perisic.ring.Ring.Q;
import com.perisic.ring.RingElt;

public class Rational implements FieldElement<Rational> {
    
    static final RationalField RATIONAL_FIELD = new RationalField();

    private static final class RationalField implements Field<Rational> {
	@Override
	public Rational getZero() {
	   return ZERO;
	}

	@Override
	public Rational getOne() {
           return ONE;
	}

	@Override
	public Class<? extends FieldElement<Rational>> getRuntimeClass() {
	   return Rational.class;
	}
    }

    private final RingElt delegate;
    public static final Rational ONE = new Rational(Q.one());
    public static final Rational ZERO = new Rational(Q.zero());
    
    private Rational(RingElt d) {
	delegate = d;
	assert( d.getRing() == Q );
    }
    
    Rational(int n) {
	this(Q.construct(BigInteger.valueOf(n), BigInteger.ONE));
    }

    @Override
    public Rational add(Rational a) {
	return new Rational(Q.add(delegate,a.delegate));
    }

    @Override
    public Rational subtract(Rational a) {
	return new Rational(Q.sub(delegate,a.delegate));
    }

    @Override
    public Rational negate() {
	return new Rational(Q.neg(delegate));
    }

    @Override
    public Rational multiply(int n) {
	// horribly inefficient
	return new Rational(Q.mult(delegate,Q.construct(BigInteger.valueOf(n), BigInteger.ONE)));
    }

    @Override
    public Rational multiply(Rational a) {
	return new Rational(Q.mult(delegate,a.delegate));
    }

    @Override
    public Rational divide(Rational a) {
	return new Rational(Q.div(delegate,a.delegate));
    }

    @Override
    public Rational reciprocal() {
	return new Rational(Q.inv(delegate));
    }

    @Override
    public Field<Rational> getField() {
	return RATIONAL_FIELD;
    }
    
    @Override
    public String toString() {
	return delegate.toString();
    }

    public int sign() {
	return com.perisic.ring.RationalField.numeratorToBigInteger( delegate ).compareTo(BigInteger.ZERO);
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
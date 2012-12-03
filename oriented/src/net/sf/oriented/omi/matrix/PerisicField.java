/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/

package net.sf.oriented.omi.matrix;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;

import com.perisic.ring.Ring;
import com.perisic.ring.UniversalPolynomialRing;

public class PerisicField implements Field<PerisicFieldElement> {
    
    
    private static final UniversalPolynomialRing U = new UniversalPolynomialRing(Ring.Q);

    final private PerisicFieldElement zero, one;
    
    public static final PerisicField Q = new PerisicField(Ring.Q);
    public static final PerisicField Polynomials = new PerisicField(U);
    private PerisicField(Ring r) {
	zero = new PerisicFieldElement(r.zero());
	one = new PerisicFieldElement(r.one());
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
	if (ring == Ring.Q) return Q;
	if (ring == U ) return Polynomials;
	throw new IllegalArgumentException();
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
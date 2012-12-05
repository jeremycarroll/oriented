/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
 ************************************************************************/

package net.sf.oriented.omi.impl.om;

import static net.sf.oriented.omi.impl.om.Cryptomorphisms.REALIZED;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMRealized;
import net.sf.oriented.omi.RealizedFactory;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.matrix.RationalMatrix;

public class RealizedImpl extends AbsOM implements OMRealized {

    private final RationalMatrix matrix;

    public RealizedImpl(OMAll all, RationalMatrix mat) {
	super(all);
	matrix = mat;
	all.set(REALIZED, this);
    }

    @Override
    public boolean verify() {
	// TODO Auto-generated method stub
	return true;
    }

    @Override
    public Iterator<? extends SignedSet> iterator() {
	throw new UnsupportedOperationException();
    }

    @Override
    public RationalMatrix getMatrix() {
	return matrix;
    }

    @Override
    public String toString() {
	return toString(ffactory());
    }

    public String toString(FactoryFactory factory) {
	List<? extends Label> g = Arrays.asList(ground());
	UnsignedSetFactory sets = all.unsignedSets(factory);
	return "(" + sets.toString(g, sets.copyBackingCollection(g)) + ", "
		+ toMatrixString(factory.realized()) + " )";
    }

    public String toMatrixString(RealizedFactory realizedFactory) {
	return realizedFactory.toString(this);
    }

   
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if ( o == this ) {
		    return true;
		}
		return getChirotope().equals(o);
	}
}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * The Java Oriented Matroid Library is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
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

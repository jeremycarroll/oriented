/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.omi.impl.om;

import static net.sf.oriented.omi.impl.om.Cryptomorphisms.REALIZED;

import java.util.Iterator;

import net.sf.oriented.matrix.GramSchmidt;
import net.sf.oriented.matrix.PerisicFieldElement;
import net.sf.oriented.matrix.RationalMatrix;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OMRealized;
import net.sf.oriented.omi.RealizedFactory;
import net.sf.oriented.omi.SignedSet;

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
		return toMatrixString(factory.realized());
	}

	public String toMatrixString(RealizedFactory realizedFactory) {
		return realizedFactory.toString(this);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		return getChirotope().equals(o);
	}

	@Override
	public RationalMatrix getDualBasis() {
		GramSchmidt<PerisicFieldElement> gs = new GramSchmidt<PerisicFieldElement>(
				matrix.getDelegate());
		return new RationalMatrix(gs.getDual());
	}
}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
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

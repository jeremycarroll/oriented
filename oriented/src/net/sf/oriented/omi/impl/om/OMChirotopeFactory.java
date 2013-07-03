/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import static net.sf.oriented.omi.impl.om.ChirotopeImpl.pos;

import java.util.Collection;

import net.sf.oriented.omi.Chirotope;
import net.sf.oriented.omi.ChirotopeFactory;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMChirotope;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.items.ParseContext;

public class OMChirotopeFactory extends AbsMatroidFactory<OMChirotope, Pair>
		implements ChirotopeFactory {

	public OMChirotopeFactory(FactoryFactory factory) {
		super(factory);
	}

	@Override
	public OMChirotope construct(Chirotope chi) {
		final int n = chi.n();
		return construct(new SimpleLabels(n), chi);
	}

	@Override
	public String toString(OMChirotope t) {
		if (t instanceof ChirotopeImpl) {
			ChirotopeImpl x = (ChirotopeImpl) t;
			return x.toString(factory);
		}
		return t.toString();
	}

	@Override
	protected OMChirotope construct(Collection<? extends Label> ground,
			final Pair defn) {
		final int n = ground.size();
		return construct(ground, new Chirotope() {
			@Override
			public int chi(int... i) {
				char ch = defn.s.charAt(pos(n, defn.r, 0, i));
				switch (ch) {
				case '+':
					return 1;
				case '-':
					return -1;
				case '0':
					return 0;
				default:
					throw new IllegalArgumentException(
							"Syntax error in chirotope defn string: " + ch);
				}
			}

			@Override
			public int n() {

				return n;
			}

			@Override
			public int rank() {
				return defn.r;
			}
		});
	}

	@Override
	OMChirotope parse(ParseContext pc) {
		return parsePair(pc);
	}

	@Override
	Pair parseMatroid(ParseContext pc) {
		Pair rslt = new Pair();
		rslt.r = Integer.parseInt(uptoSeparator(pc));
		rslt.s = uptoSeparator(pc);
		return rslt;
	}

	@Override
	public OMChirotope construct(Collection<? extends Label> e, Chirotope chi) {
		// TODO: this is an unsafe cast:
		LabelImpl[] g = e.toArray(new LabelImpl[0]);
		OMAll all = new OMAll(g, factory);
		return new ChirotopeImpl(all, chi);
	}

	@Override
	OMChirotope construct(Pair p) {
		return construct(new SimpleLabels(p.r), p);
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

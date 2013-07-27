/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Collection;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.impl.items.ParseContext;
import net.sf.oriented.omi.Chirotope;
import net.sf.oriented.omi.ChirotopeFactory;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.util.combinatorics.CoLexicographic;

import com.google.common.math.IntMath;

public class OMChirotopeFactory extends AbsMatroidFactory<OMasChirotope, RankAndChirotope>
		implements ChirotopeFactory {

	public OMChirotopeFactory(FactoryFactory factory) {
		super(factory);
	}

	@Override
	public OMasChirotope construct(Chirotope chi) {
		final int n = chi.n();
		return construct(new SimpleLabels(n), chi);
	}

	@Override
	public String toString(OMasChirotope t) {
		if (t instanceof ChirotopeImpl) {
			ChirotopeImpl x = (ChirotopeImpl) t;
			return x.toString(factory);
		}
		return t.toString();
	}

	@Override
	protected OMasChirotope construct(Collection<? extends Label> ground,
			final RankAndChirotope defn) {
		final int n = ground.size();
		defn.checkSize(n);
		return construct(ground, new Chirotope() {
			@Override
			public int chi(int... i) {
				char ch = defn.chirotope.charAt(CoLexicographic.index(i));
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
				return defn.rank;
			}
		});
	}

	@Override
	OMasChirotope parse(ParseContext pc) {
		return parsePair(pc);
	}

	@Override
	RankAndChirotope parseMatroid(ParseContext pc) {
		return new RankAndChirotope(
		        Integer.parseInt(uptoSeparator(pc)),
		        uptoSeparator(pc));
	}

	@Override
	public OMasChirotope construct(Collection<? extends Label> e, Chirotope chi) {
		LabelImpl[] g = e.toArray(new LabelImpl[0]);
		OMAll all = new OMAll(g, factory);
		return new ChirotopeImpl(all, chi);
	}

	@Override
	OMasChirotope construct(RankAndChirotope p) {
	    return construct(new SimpleLabels(howManyElements(p.rank, p.chirotope.length())), p);
	}

    private int howManyElements(int rank, int l) {
        int nHigh = rank;
        int n;
        while (IntMath.binomial(nHigh, rank)<l) {
            nHigh *= 2;
        }
        int nLow = nHigh/2;
        // following should be divide and conquer, but lazily we use brute force.
        for (n = nLow; n<=nHigh; n++) {
            int b = IntMath.binomial(n, rank);
            if ( b==l ) {
                break;
            }
            if ( b > l ) {
                throw new IllegalArgumentException("Initialization string is of length "+l+
                        ". Nearby legal values are: "+IntMath.binomial(n-1, rank)+" and "+b);
            }
        }
        return n;
    }

    @Override
    public OMasChirotope fromCoLexicographic(int rank, String plusMinusZeros) {
        return construct(new RankAndChirotope(rank,plusMinusZeros));
    }

    @Override
    public OMasChirotope fromLexicographic(int rank, String plusMinusZeros) {
        // TODO Auto-generated method stub
        return null;
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

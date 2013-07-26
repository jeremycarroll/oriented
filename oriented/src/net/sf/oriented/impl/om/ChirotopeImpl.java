/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import static net.sf.oriented.impl.om.Cryptomorphisms.CHIROTOPE;
import static net.sf.oriented.util.combinatorics.CombinatoricUtils.sign;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.impl.set.SignedSetFactory;
import net.sf.oriented.impl.set.SignedSetInternal;
import net.sf.oriented.impl.set.UnsignedSetFactory;
import net.sf.oriented.impl.set.UnsignedSetInternal;
import net.sf.oriented.omi.Alternating;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Chirotope;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.FullChirotope;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.CoLexicographic;
import net.sf.oriented.util.combinatorics.CombinatoricUtils;
import net.sf.oriented.util.combinatorics.Permutation;
import net.sf.oriented.util.matrix.RationalMatrix;

import com.google.common.math.IntMath;

public class ChirotopeImpl extends AbsOM implements OMasChirotope {

	final private int rank;

	private final class Chi implements Chirotope {
		@Override
		public int chi(int... i) {
			int r = rank();
			if (i.length != r)
				throw new IllegalArgumentException("rank is " + r + ", not "
						+ i.length);
			return getNthEntry(CoLexicographic.index(i.clone()));
		}

		@Override
		public int rank() {
			return ChirotopeImpl.this.rank();
		}

		@Override
		public int n() {
			return ChirotopeImpl.this.n();
		}
	}

	/**
	 * Semantics - two bits per item in chirotope:
	 * 
	 * 00 - 0 01 - 1 10 - undefined 11 - -1
	 */
	final private int bits[];

	final Alternating alt;

	ChirotopeImpl(Circuits a) {
		super(a);
		rank = all.rank();
		bits = new int[(IntMath.binomial(all.ground.length, rank) + 15) / 16];
		alt = new Alternating(new Chi());
		initFromCircuits();
        ensureFirstSignIsPositive();
		all.set(CHIROTOPE, this);
	}

	ChirotopeImpl(ChirotopeImpl d) {
		super(d.dual());
		rank = elements().length - d.rank;
		bits = new int[d.bits.length];
		alt = new Alternating(new Chi());
		initFromDual(d);
		ensureFirstSignIsPositive();
		all.set(CHIROTOPE, this);
	}

	public ChirotopeImpl(OMAll all, Chirotope chi) {
		super(all);
		rank = chi.rank();
		if (all.elements().length != chi.n())
			throw new IllegalArgumentException("Size mismatch");
		bits = new int[(IntMath.binomial(chi.n(), chi.rank()) + 15) / 16];
		alt = new Alternating(new Chi());
		initFromChi(chi);
        ensureFirstSignIsPositive();
		all.set(CHIROTOPE, this);
	}


    public ChirotopeImpl(OMAll all, final RationalMatrix matrix) {
		this(all, new Chirotope() {

			@Override
			public int chi(int... indices) {
				return matrix.determinant(indices).sign();
			}

			@Override
			public int rank() {
				return matrix.height();
			}

			@Override
			public int n() {
				return matrix.width();
			}
		});
	}

	private void initFromChi(Chirotope chi) {
		CoLexicographic lex = new CoLexicographic(chi.n(), chi.rank());
		int i = 0;
		Iterator<int[]> it = lex.iterator();
		while (it.hasNext()) {
			setNthEntry(i++, chi.chi(it.next()));
		}
	}

	UnsignedSet convert(LabelImpl[] impls) {
		return ffactory().unsignedSets().copyBackingCollection(
				Arrays.asList(impls));
	}

	private void initFromDual(ChirotopeImpl d) {
		UnsignedSet g = convert(elements());
		int n = g.size();
		int r = rank();

		CoLexicographic lex = new CoLexicographic(n, d.rank());
		Iterator<int[]> it = lex.iterator();
		while (it.hasNext()) {
			int[] dBasis = it.next();
			int basis[] = dualBasis(n, r, dBasis);
			setNthEntry(CoLexicographic.index(basis.clone()),
					signDualBasis(basis, dBasis) * d.chi(dBasis));
		}

	}

	static public int[] dualBasis(int n, int r, int[] basis) {
		int rslt[] = new int[r];
		int j = 0;
		int v = 0;
		for (int i = 0; i < r; i++) {
			while (j < basis.length && basis[j] == v) {
				j++;
				v++;
			}
			rslt[i] = v++;
		}
		return rslt;
	}

	static public int signDualBasis(int[] bx, int[] ax) {
		int p[] = new int[ax.length + bx.length];
		System.arraycopy(ax, 0, p, 0, ax.length);
		System.arraycopy(bx, 0, p, ax.length, bx.length);
		int s = sign(p);

		return s;
	}

	private void initFromCircuits() {
		Bases bases = getMatroid().getBases();
		Iterator<UnsignedSetInternal> it = bases.iterator();
		UnsignedSetInternal theBasis = it.next();
		setNthEntry(indexFor(theBasis), 1);
		while (it.hasNext()) {
			UnsignedSetInternal nBasis = it.next();
			ensureSet(bases, theBasis, nBasis);
		}
	}

	private void ensureSet(Bases bases, UnsignedSetInternal theBasis,
			UnsignedSetInternal nBasis) {
		if (getNthEntry(indexFor(nBasis)) == 0) {
			findAndSetValue(bases, theBasis, nBasis);
		}
		if (getNthEntry(indexFor(nBasis)) == 0)
			throw new IllegalStateException("Chirotope initialization failed");
	}

	private void findAndSetValue(Bases bases, UnsignedSetInternal theBasis,
			UnsignedSetInternal nBasis) {
		UnsignedSetInternal exchanged = bases.basisExchange(nBasis, theBasis);
		ensureSet(bases, theBasis, exchanged);
		UnsignedSetInternal dependentSet = exchanged.union(nBasis);
		MatroidCircuits circuitsM = bases.getCircuits();
		UnsignedSetInternal p = findPivot(nBasis, exchanged, dependentSet,
				circuitsM.asCollection());
		if (p == null)
			throw new IllegalStateException(
					"No circuit found for pivoting rule.");
		pivot(nBasis, exchanged, circuitsM.signed(getCircuits(),p));
	}

	private UnsignedSetInternal findPivot(UnsignedSetInternal nBasis,
			UnsignedSetInternal exchanged, UnsignedSetInternal dependentSet,
			JavaSet<? extends UnsignedSet> circuitsJ) {
		for (int sz = dependentSet.size(); sz > 0; sz--) {
			JavaSet<UnsignedSetInternal> dependentSets = dependentSet
					.subsetsOfSize(sz);
			dependentSets.retainAll(circuitsJ);
			if (!dependentSets.isEmpty()) {
				Iterator<UnsignedSetInternal> it = dependentSets.iterator();
				UnsignedSetInternal circuit = it.next();
				if (it.hasNext())
					throw new IllegalStateException(
							"More than one circuit found during pivoting");
				return circuit;
			}
		}
		return null;
	}

	private void pivot(UnsignedSetInternal newBasis,
			UnsignedSetInternal oldBasis, SignedSetInternal signedCircuit) {
		UnsignedSetInternal inter = oldBasis.intersection(newBasis);
		LabelImpl e = oldBasis.minus(inter).theMember();
		LabelImpl f = newBasis.minus(inter).theMember();
		int sign = -1 * signFor(e, inter) * signFor(f, inter)
				* signedCircuit.sign(e) * signedCircuit.sign(f);
		if (sign == 0)
			throw new RuntimeException(
					"Chirotope initialization failed: internal logic error");
		setNthEntry(indexFor(newBasis), sign * getNthEntry(indexFor(oldBasis)));
	}

	private int indexFor(UnsignedSetInternal basis) {
		return CoLexicographic.index(asInt(basis));
	}

	private int signFor(LabelImpl e, UnsignedSetInternal inter) {
		int ix[] = asInt(inter);
		int ix2[] = new int[ix.length + 1];
		ix2[0] = asInt(e);
		System.arraycopy(ix, 0, ix2, 1, ix.length);
		return CombinatoricUtils.sign(ix2);
	}

	/**
	 * The chirotope is only unique up to multiplication by -1;
	 * make first sign positive.
	 */
    private void ensureFirstSignIsPositive() {
        int sz = IntMath.binomial(n(), rank());
        boolean flipping = false;
        for (int i=0;i<sz;i++) {
            switch (getNthEntry(i)) {
            case -1:
                flipping = true;
                setNthEntry(i,1);
                break;
            case 0:
                break;
            case 1:
                if (!flipping) {
                    return;
                }
                setNthEntry(i,-1);
                break;
            }
        }
        if (!flipping) {
            throw new IllegalArgumentException("Bad chirotope initialization: chirotope is zero");
        }
    }
	private int getNthEntry(int x) {
		int w = x / 16;
		int shift = (x % 16) * 2;
		int pairOfBits = (bits[w] >>> shift) & 3;
		switch (pairOfBits) {
		case 2:
			throw new IllegalStateException(x
					+ "th entry in chirotope has not been defined.");
		case 1:
			return 1;
		case 3:
			return -1;
		case 0:
			return 0;
		}
		throw new RuntimeException("Unreachable code - has been reached :(");
	}

	private void setNthEntry(int x, int v) {
		int w = x / 16;
		int shift = (x % 16) * 2;
		int pairOfBits;
		switch (v) {
		case 1:
			pairOfBits = 1;
			break;
		case 0:
			pairOfBits = 0;
			break;
		case -1:
			pairOfBits = 3;
			break;
		default:
			throw new IllegalArgumentException(
					"Trying to set a chirotope entry to: " + v);

		}
		bits[w] &= ~(3<<shift);
		bits[w] |= (pairOfBits << shift);

	}

	@Override
	public void verify()  throws AxiomViolation {
		 verifyNonZero();
		 verifyMatroid();
		 verify3TermGrassmannPlücker();
		
	}

    private void verify3TermGrassmannPlücker()  throws AxiomViolation {
        UnsignedSet g = this.setOfElements();  // need to reformulate
        JavaSet<? extends UnsignedSet> aa = g.subsetsOfSize(rank-2);
        JavaSet<? extends UnsignedSet> bb = g.subsetsOfSize(4);
        for (UnsignedSet AA:aa) {
            int indexes[] = new int[rank];
            int ix=2;
            for (Label l:(UnsignedSetInternal)AA) {
                indexes[ix++] = asInt(l);
            }
            for (UnsignedSet BB:bb) {
                if (AA.intersection(BB).isEmpty()) {
                    @SuppressWarnings("unchecked")
                    JavaSet<UnsignedSet> done = (JavaSet<UnsignedSet>) ffactory().unsignedSets().emptyCollectionOf();
                    // check condition splitting BB three times
                    for (UnsignedSet X1X2:BB.subsetsOfSize(2) ) {
                        if (done.contains(X1X2)) continue;
                        UnsignedSet Y1Y2 = BB.minus(X1X2);
                        done.add(Y1Y2);
                        Label X1 = amember(X1X2);
                        Label X2 = amember(X1X2.minus(X1));
                        Label Y1 = amember(Y1Y2);
                        Label Y2 = amember(Y1Y2.minus(Y1));
                        int e = chiTimesChi(X1,X2,Y1,Y2,indexes);
                        if (e == 0) {
                            continue;
                        }
                        if (e != chiTimesChi(Y1,X2,X1,Y2,indexes)
                            && e != chiTimesChi(Y2,X2,Y1,X1,indexes) ) {
                            throw new AxiomViolation(this,"3TermGrassmannPlücker");
                        }
                        
                    }
                }
            }
        }
    }

    private int chiTimesChi(Label x1, Label x2, Label y1, Label y2,
            int[] indexes) {
        return chi(x1,x2,indexes) * chi(y1,y2,indexes);
    }

    private int chi(Label x1, Label x2, int[] indexes) {
        indexes[0] = asInt(x1);
        indexes[1] = asInt(x2);
        return chi(indexes);
    }

    private Label amember(UnsignedSet x1x2) {
        return x1x2.iterator().next();
    }

    private void verifyMatroid()  throws AxiomViolation  {
        getMatroid().getBases().verify();
        if (!getMatroid().getBases().sameSetAs(getBases())) {
            throw new AxiomViolation(this,"Chirotope defines matroid bases");
        }
    }

    private void verifyNonZero()  throws AxiomViolation  {
        for (int bit : bits)
			if (bit != 0)
				return;
        throw new AxiomViolation(this,"Non-Zero");
    }

	@Override
	public int chi(int... i) {
		return alt.chi(i);
	}

	public String toString(FactoryFactory factory) {
		List<? extends Label> g = Arrays.asList(elements());
		UnsignedSetFactory sets = all.unsignedSets(factory);
		return "( " + sets.toString(g, sets.copyBackingCollection(g)) + ", "
				+ rank() + ", " + toShortString() + " )";
	}

	@Override
    public String toShortString() {
		int sz = IntMath.binomial(elements().length, rank());
		char r[] = new char[sz];
		for (int i = 0; i < sz; i++) {
			r[i] = "-0+!".charAt(getNthEntry(i) + 1);
		}
		return new String(r);
	}

	@Override
	public ChirotopeImpl getChirotope() {
		return this;
	}

	@Override
	public String toString() {
		return toString(ffactory());
	}

	@Override
	public int rank() {
		return rank;
	}

	public SetOfUnsignedSetInternal getBases() {
		CoLexicographic lex = new CoLexicographic(n(), rank());
		Iterator<int[]> it = lex.iterator();
		SetOfUnsignedSetFactory f = (SetOfUnsignedSetFactory) ffactory()
				.setsOfUnsignedSet();
		JavaSet<UnsignedSetInternal> r = all.unsignedSets().emptyCollectionOf();
		while (it.hasNext()) {
			int b[] = it.next();
			if (chi(b) == 0) {
				continue;
			}
			r.add(all.asUnsignedSet(b));

		}
		return f.fromBackingCollection(r);
	}

	SetOfSignedSetInternal circuits() {
		MatroidCircuits mc = getMatroid().getCircuits();
		Iterator<UnsignedSetInternal> it = mc.iterator();
		SignedSetInternal rslt[] = new SignedSetInternal[2 * mc.size()];
		int i = 0;
		while (it.hasNext()) {
			UnsignedSetInternal unsigned = it.next();
			SignedSetInternal signed = assign(unsigned);
			rslt[i++] = signed;
			rslt[i++] = signed.opposite();
		}
		return (SetOfSignedSetInternal) ffactory().symmetricSetsOfSignedSet()
				.copyBackingCollection(Arrays.asList(rslt));
	}

	private SignedSetInternal assign(UnsignedSetInternal unsigned) {
		JavaSet<LabelImpl> plus = all.unsignedSets().itemFactory()
				.emptyCollectionOf();
		JavaSet<LabelImpl> minus = all.unsignedSets().itemFactory()
				.emptyCollectionOf();

		Iterator<LabelImpl> it = unsigned.iterator();
		LabelImpl e = it.next();
		UnsignedSetInternal A = findBasis(unsigned.minus(e));
		int ix[] = asInt(A);
		int Ne = asInt(e);
		// for (int i=1;i<rank;i++)
		// if (ix[i]==fe) {
		// ix[i]=ix[0];
		// ix[0]=fe;
		// }
		// if (ix[0]!=fe )
		// throw new IllegalStateException("basis error");
		add(plus, minus, chi(ix), e);
		while (it.hasNext()) {
			LabelImpl f = it.next();
			int Nf = asInt(f);
			int i = indexOf(Nf, ix);
			ix[i] = Ne;
			add(plus, minus, -chi(ix), f);
			ix[i] = Nf;
		}
		return ((SignedSetFactory) ffactory().signedSets()).create(all
				.unsignedSets().copyBackingCollection(plus), all.unsignedSets()
				.copyBackingCollection(minus));
	}

	private int indexOf(int nf, int[] ix) {
		for (int i = 0; i < ix.length; i++)
			if (ix[i] == nf)
				return i;
		throw new IllegalStateException("basis error");
	}

	private UnsignedSetInternal findBasis(UnsignedSetInternal independent) {
		AbsMatroid bases = getMatroid().getBases();
		if (independent.size() == rank()) {
			if (bases.contains(independent))
				return independent;
		} else {
			Iterator<UnsignedSetInternal> it = bases.iterator();
			while (it.hasNext()) {
				UnsignedSetInternal base = it.next();
				if (base.isSupersetOf(independent))
					return base;
			}
		}
		throw new IllegalArgumentException("did not find a basis");
	}

	private void add(JavaSet<LabelImpl> plus, JavaSet<LabelImpl> minus,
			int chi, LabelImpl e) {
		switch (chi) {
		case -1:
			minus.add(e);
			break;
		case 1:
			plus.add(e);
			break;
		default:
			throw new IllegalArgumentException("basis error");
		}
	}

	@Override
	public Iterator<? extends SignedSet> iterator() {
		throw new UnsupportedOperationException(
				"Chirotopes don't have iterators.");
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof ChirotopeImpl) {
			if (o == this)
				return true;
			ChirotopeImpl c = (ChirotopeImpl) o;
			if (rank != c.rank)
				return false;
			if (Arrays.equals(elements(), c.elements()))
				return Arrays.equals(bits, c.bits);
			return all.equalsByCircuits((OMInternal) o);
		}
		if (o instanceof RealizedImpl)
			return equals(((RealizedImpl) o).getChirotope());
		return all.equals(o);
	}

    @Override
    public OM permuteGround(Permutation p) {
        return create(p.permute(elements()),new PermutedChirotope(p, alt));
    }

    @Override
    public OM permute(Permutation p) {
        return create(p.permute(elements()),alt);
    }

    private OMasChirotope create(LabelImpl[] labelImpls, FullChirotope chi) {
        return ffactory().chirotope().construct(Arrays.asList(labelImpls),chi);
    }

    @Override
    public OMInternal reorientRaw(Label ... axes) {
        final boolean reoriented[] = new boolean[n()];
        for (Label lbl:axes) {
            reoriented[asInt(lbl)] = true;
        }
        return (OMInternal) create(elements(),new FullChirotope(){

            @Override
            public int rank() {
                return rank;
            }

            @Override
            public int n() {
                return ChirotopeImpl.this.n();
            }

            @Override
            public int chi(int ... indexes) {
                int sign = 1;
                for (int ix:indexes) {
                    if (reoriented[ix]) {
                        sign = sign * -1;
                    }
                }
                return sign*alt.chi(indexes);
            }});
    }

    @Override
    public OMasChirotope mutate(int newSign, Label ... basis) {
        if (basis.length!= rank()) {
            throw new IllegalArgumentException("Incorrect number of elements in basis, expecting: "+rank());
        }
        final int basis2[] = asInt(basis);
        
        final int sign = newSign * CombinatoricUtils.sign(basis2);
        Arrays.sort(basis2);

        OMAll all = new OMAll(elements(), ffactory());
        return new ChirotopeImpl(all, new Chirotope(){

            @Override
            public int chi(int ... i) {
                if (Arrays.equals(basis2, i)) {
                    return sign;
                }
                return alt.chi(i);
            }

            @Override
            public int rank() {
                return ChirotopeImpl.this.rank();
            }

            @Override
            public int n() {
                return ChirotopeImpl.this.n();
            }
            
        });
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

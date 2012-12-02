/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.om;

import static net.sf.oriented.combinatorics.CombinatoricUtils.choose;
import static net.sf.oriented.combinatorics.CombinatoricUtils.sign;
import static net.sf.oriented.omi.impl.om.Cryptomorphisms.CHIROTOPE;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.combinatorics.CombinatoricUtils;
import net.sf.oriented.combinatorics.Lexicographic;
import net.sf.oriented.omi.Alternating;
import net.sf.oriented.omi.Chirotope;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMChirotope;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.SignedSetFactory;
import net.sf.oriented.omi.impl.set.SignedSetInternal;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;
import net.sf.oriented.omi.matrix.Rational;
import net.sf.oriented.omi.matrix.RationalMatrix;



public class ChirotopeImpl extends AbsOM implements OMChirotope {

	final private int rank;

	private final class Chi implements Chirotope {
		@Override
		public int chi(int... i) {
			int r = rank();
			if (i.length != r)
				throw new IllegalArgumentException("rank is " + r + ", not "
						+ i.length);
			return getNthEntry(pos(ground().length, r, 0, i.clone()));
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
	 * Return the index in the lexicographic sequence of r elements from n of
	 * the sequence i. p is how far we have got in the sequence.
	 * 
	 * @param n
	 * @param r
	 * @param p
	 * @param i
	 * @return
	 */
	static public int pos(int n, int r, int p, int... i) {
		if (r == 0)
			return 0;
		if (i[p] < 0 || i[p] >= n)
			throw new IllegalArgumentException("value out of range.");
		for (int q = 1; q < r; q++)
			if (i[p + q] > i[p])
				i[p + q] -= (i[p] + 1);
			else
				throw new IllegalArgumentException("sequence not monotonic");
		int rslt = 0;
		int nextN = n - 1 - i[p];
		while (i[p] > 0) {
			rslt += choose(n - i[p], r - 1);
			i[p]--;
		}
		return rslt + pos(nextN, r - 1, p + 1, i);
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
		bits = new int[(choose(all.ground.length, rank) + 15) / 16];
		alt = new Alternating(new Chi());
		initFromCircuits();
		all.set(CHIROTOPE, this);
	}

	ChirotopeImpl(ChirotopeImpl d) {
		super(d.dual());
		rank = ground().length - d.rank;
		bits = new int[d.bits.length];
		alt = new Alternating(new Chi());
		initFromDual(d);
		all.set(CHIROTOPE, this);
	}

	public ChirotopeImpl(OMAll all, Chirotope chi) {
		super(all);
		rank = chi.rank();
		if (all.ground().length != chi.n())
			throw new IllegalArgumentException("Size mismatch");
		bits = new int[(choose(chi.n(), chi.rank()) + 15) / 16];
		alt = new Alternating(new Chi());
		initFromChi(chi);
		all.set(CHIROTOPE, this);
	}

	public ChirotopeImpl(OMAll all, final RationalMatrix matrix) {
	    this(all, new Chirotope(){
		

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
		}});
	}

	private void initFromChi(Chirotope chi) {
		Lexicographic lex = new Lexicographic(chi.n(), chi.rank());
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
		UnsignedSet g = convert(ground());
		int n = g.size();
		int r = rank();

		Lexicographic lex = new Lexicographic(n, d.rank());
		Iterator<int[]> it = lex.iterator();
		while (it.hasNext()) {
			int[] dBasis = it.next();
			int basis[] = dualBasis(n, r, dBasis);
			setNthEntry(pos(n, r, 0, basis.clone()), signDualBasis(basis, dBasis)
					* d.chi(dBasis));
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
		if (getNthEntry(indexFor(nBasis)) == 0)
			findAndSetValue(bases, theBasis, nBasis);
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
		pivot(nBasis, exchanged, circuitsM.signed(p));
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

	private void pivot(UnsignedSetInternal newBasis, UnsignedSetInternal oldBasis,
			SignedSetInternal signedCircuit) {
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
		return pos(ground().length, rank(), 0, asInt(basis));
	}

	private int signFor(LabelImpl e, UnsignedSetInternal inter) {
		int ix[] = asInt(inter);
		int ix2[] = new int[ix.length + 1];
		ix2[0] = asInt(e);
		System.arraycopy(ix, 0, ix2, 1, ix.length);
		return CombinatoricUtils.sign(ix2);
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
		bits[w] |= (pairOfBits << shift);

	}

	@Override
	public boolean verify() {
		for (int i=0;i<bits.length;i++)
			if (bits[i]!=0)
				return true;
		// TODO chi verify
		return false;
	}

	@Override
	public int chi(int... i) {
		return alt.chi(i);
	}

	public String toString(FactoryFactory factory) {
		List<? extends Label> g = Arrays.asList(ground());
		UnsignedSetFactory sets = all.unsignedSets(factory);
		return "(" + sets.toString(g, sets.copyBackingCollection(g)) + ", "
				+ rank() + ", " + toShortString() + " )";
	}

	public String toShortString() {
		int sz = choose(ground().length, rank());
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

	// TODO: generalize - put on all matroid inferaces
	@Override
	public int n() {
		return ground().length;
	}

	public SetOfUnsignedSetInternal getBases() {
		Lexicographic lex = new Lexicographic(n(), rank());
		Iterator<int[]> it = lex.iterator();
		SetOfUnsignedSetFactory f = (SetOfUnsignedSetFactory) ffactory()
				.setsOfUnsignedSet();
		JavaSet<UnsignedSetInternal> r = all.unsignedSets().emptyCollectionOf();
		while (it.hasNext()) {
			int b[] = it.next();
			if (chi(b) == 0)
				continue;
			r.add(all.asUnsignedSet(b));

		}
		return f.fromBackingCollection(r);
	}

	SetOfSignedSetInternal circuits() {
		MatroidCircuits mc = getMatroid().getCircuits();
		Iterator<UnsignedSetInternal> it = mc.iterator();
		SignedSetInternal rslt[] = new SignedSetInternal[2*mc.size()];
		int i =0;
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
			if (ix[i] == nf) {
				return i;
			}
		throw new IllegalStateException("basis error");
	}

	private UnsignedSetInternal findBasis(UnsignedSetInternal independent) {
		Bases bases = getMatroid().getBases();
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
		throw new UnsupportedOperationException("Chirotopes don't really have iterators.");
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o instanceof ChirotopeImpl) {
			ChirotopeImpl c = (ChirotopeImpl)o;
			if (rank != c.rank)
				return false;
			if (Arrays.equals(ground(), c.ground()) )
				return Arrays.equals(bits, c.bits);
			return all.equalsByCircuits((OMInternal)o);
		}
		return all.equals(o);
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

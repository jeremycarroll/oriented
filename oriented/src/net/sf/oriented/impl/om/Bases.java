/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Iterator;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.impl.set.UnsignedSetInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;

public class Bases extends AbsMatroid {

	Bases(SetOfUnsignedSetInternal s, MatroidInternal m) {
		super(s, m);
		m.asAll().setBases(this);
	}

	Bases(Bases bases, MatroidInternal all) {
		this(bases.dualBases(), all);
	}

	Bases(MatroidCircuits circuits) {
		this(circuits.computeBases(), circuits);
	}

	private SetOfUnsignedSetInternal dualBases() {
		UnsignedSetInternal s = convert(ground());
		JavaSet<UnsignedSetInternal> db = emptyCollectionOf();
		Iterator<UnsignedSetInternal> it = iterator();
		while (it.hasNext()) {
			db.add(s.minus(it.next()));
		}
		return useCollection(db);
	}

	@Override
	public Bases getBases() {
		return this;
	}

	@Override
	public int rank() {
		return iterator().next().size();
	}

	@Override
	public void verify() throws AxiomViolation {

        verifyNonEmpty();
        verifyBasesExchange();
	}

    private void verifyBasesExchange() throws AxiomViolation {
        new ForAllForAllExists<Label,Label>() {

            @Override
            boolean check(Label a, UnsignedSet A, UnsignedSet B, Label b) {
                return set.contains(A.minus(a).union(b));
            }

            @Override
            Iterator<? extends Label> suchThatForAll(UnsignedSet A, UnsignedSet B) {
                return A.equals(B) ? null : A.minus(B).iterator();
            }
            @Override
            protected Iterator<? extends Label> innerIterator(UnsignedSet A, UnsignedSet B) {
                return B.minus(A).iterator();
            }
        }.verify();
    }

    SetOfUnsignedSetInternal computeCircuits() {
		/*
		 * 0) compute independent sets 1) For i = 1 to r + 1 a) Compute set of
		 * size i which - is not independent - does not have smaller depedendent
		 * set as subset b) Add it
		 */
		UnsignedSetInternal s = convert(ground());
		JavaSet<UnsignedSetInternal> independent = independentSets();
		JavaSet<UnsignedSetInternal> circuits = emptyCollectionOf();
		JavaSet<UnsignedSetInternal> c0 = emptyCollectionOf();
		int r = rank();
		for (int i = 1; i <= r + 1; i++) {
			Iterator<UnsignedSetInternal> it = s.subsetsOfSize(i).iterator();
			loopc: while (it.hasNext()) {
				UnsignedSetInternal c = it.next();
				if (independent.contains(c)) {
					continue;
				}
				Iterator<UnsignedSetInternal> i2 = circuits.iterator();
				while (i2.hasNext())
					if (i2.next().isSubsetOf(c)) {
						continue loopc;
					}
				c0.add(c);
			}
			circuits.addAll(c0);
			c0.clear();
		}
		return useCollection(circuits);
	}

	private JavaSet<UnsignedSetInternal> independentSets() {
		JavaSet<UnsignedSetInternal> rslt = emptyCollectionOf();
		Iterator<UnsignedSetInternal> it = iterator();
		while (it.hasNext()) {
			rslt.addAll(it.next().powerSet());
		}
		return rslt;
	}

	/**
	 * Return a basis of the form A\{a} U {b}, with a in A\B and b in B\A.
	 * 
	 * @param theBasis
	 * @param fromBasis
	 * @return
	 */
	UnsignedSetInternal basisExchange(UnsignedSetInternal A,
			UnsignedSetInternal B) {
		// TODO: API for exchange
		return basisExchange(A, A.minus(B).iterator().next(), B);
	}

	UnsignedSetInternal basisExchange(UnsignedSetInternal A, LabelImpl a,
			UnsignedSetInternal B) {
		UnsignedSetInternal big = A.minus(a);
		Iterator<LabelImpl> it = B.minus(A).iterator();
		while (it.hasNext()) {
			LabelImpl b = it.next();
			UnsignedSetInternal newBasis = big.union(b);
			if (contains(newBasis))
				return newBasis;
		}
		throw new IllegalArgumentException("No basis found.");
	}

	@Override
	public String toString() {
		return ffactory().bases().toString(this);
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

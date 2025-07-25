/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import static net.sf.oriented.impl.om.Cryptomorphisms.CIRCUITS;

import java.util.Iterator;

import net.sf.oriented.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.impl.set.SignedSetInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMSFactory;
import net.sf.oriented.omi.OMasFaceLattice;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Group;
import net.sf.oriented.util.combinatorics.Permutation;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public class Circuits extends AbsVectorsOM {

	// private static final String CIRCUITS = null;

	Circuits(SetOfSignedSetInternal c, OMInternal a) {
		super(c, CIRCUITS, a);
	}

	Circuits(Vectors vectors) {
		super(vectors.minimal(), CIRCUITS, vectors);
	}

	Circuits(ChirotopeImpl chi) {
		super(chi.circuits(), CIRCUITS, chi);
	}

	public Circuits(OMasFaceLattice lattice, OMAll a) {
	    this(LatticeUtil.cocircuits(lattice),a);
    }

    @Override
	public void verify() throws AxiomViolation {
		super.verify();
		verifyIncomparability();
		verifyWeakElimination();

	}

	private void verifyWeakElimination() throws AxiomViolation {
		new ForAllForAllExists<Label,SignedSet>() {
			UnsignedSet plus;
			UnsignedSet minus;

			@Override
			public boolean check(Label e, SignedSet x, SignedSet y, SignedSet z) {
				return z.sign(e) == 0 && z.plus().isSubsetOf(plus)
						&& z.minus().isSubsetOf(minus);
			}

			@Override
			public Iterator<? extends Label> suchThatForAll(SignedSet a, SignedSet b) {
				if (a.equalsOpposite(b))
					return null;
				UnsignedSet result = a.plus().intersection(b.minus());
				if (result.isEmpty())
					return null;
				plus = a.plus().union(b.plus());
				minus = a.minus().union(b.minus());
				return result.iterator();
			}

		}.verify();
	}

	private void verifyIncomparability() throws AxiomViolation {
       new ForAllForAll() {
			@Override
			public boolean check(SignedSet a, SignedSet b) {
				return (!a.support().isSubsetOf(b.support()))
						|| a.equalsIgnoreSign(b);
			}
		}.verify();
	}

	SetOfSignedSetInternal allCompositions() {
		JavaSet<SignedSetInternal> r = emptyCollectionOf();

		r.add(factory().itemFactory().empty());

		JavaSet<SignedSetInternal> x = emptyCollectionOf();
		Iterator<SignedSetInternal> ic = iterator2();
		while (ic.hasNext()) {
			SignedSetInternal c = ic.next();
			Iterator<SignedSetInternal> iv = r.iterator();
			while (iv.hasNext()) {
				SignedSetInternal v = iv.next();
				if (c.conformsWith(v)) {
					x.add(c.compose(v));
				}
			}
			r.addAll(x);
			x.clear();
		}

		return factory().fromBackingCollection(r);
	}

	private JavaSet<SignedSetInternal> emptyCollectionOf() {
		return vectors.factory().itemFactory().emptyCollectionOf();
	}

	SetOfUnsignedSetInternal unsigned() {
		return unsignedSets();
	}

	@Override
	public int hashCode() {
		return vectors.hashCode();
	}

    

    @Override
    OMSFactory omsFactory() {
        return ffactory().circuits();
    }


    @Override
    public Group automorphisms() {
        return getMatroid().automorphisms().filter(new Predicate<Permutation>(){
            @Override
            public boolean apply(Permutation p) {
                Function<SignedSet,SignedSet> map = signedSetPermuter(p);
                for (SignedSet s:vectors) {
                    if (!vectors.contains(map.apply(s))) {
                        return false;
                    }
                }
                return true;
            }
        });
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

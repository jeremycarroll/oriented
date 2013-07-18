/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import static net.sf.oriented.omi.impl.om.Cryptomorphisms.CIRCUITS;

import java.util.Iterator;

import net.sf.oriented.combinatorics.Group;
import net.sf.oriented.combinatorics.Permutation;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMSFactory;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.SignedSetInternal;

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

	@Override
	public boolean verify() {
		return super.verify() && verifyIncomparability()
				&& verifyWeakElimination();

	}

	private boolean verifyWeakElimination() {
		return new ForAllForAllExists<Label,SignedSet>() {
			UnsignedSet plus;
			UnsignedSet minus;

			@Override
			public boolean check(Label e, SignedSet x, SignedSet y, SignedSet z) {
				return z.sign(e) == 0 && z.plus().isSubsetOf(plus)
						&& z.minus().isSubsetOf(minus);
			}

			@Override
			public Iterator<? extends Label> check(SignedSet a, SignedSet b) {
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

	private boolean verifyIncomparability() {

		return new ForAllForAll() {
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
		Iterator<SignedSetInternal> ic = iterator();
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

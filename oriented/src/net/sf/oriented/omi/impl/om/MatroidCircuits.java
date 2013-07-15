/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import java.util.Iterator;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import net.sf.oriented.combinatorics.Group;
import net.sf.oriented.combinatorics.Permutation;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.SignedSetInternal;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;

public class MatroidCircuits extends AbsMatroid {

	private Group automorphisms;

    MatroidCircuits(SetOfUnsignedSetInternal c, MatroidInternal a) {
		super(c, a);
		a.asAll().setCircuits(this);
	}

	MatroidCircuits(Bases bases) {
		this(bases.computeCircuits(), bases);
	}

	MatroidCircuits(Circuits circuits, MatroidAll matroid) {
		this(circuits.unsigned(), matroid);
	}

	MatroidCircuits(Vectors vectors, MatroidAll matroid) {
		this(vectors.circuits(), matroid);
	}

	@Override
	public MatroidCircuits getCircuits() {
		return this;
	}

	@Override
	public boolean verify() {
		return (!isEmpty()) && verifyIncomparability()
				&& verifyWeakElimination();

	}

	private boolean verifyWeakElimination() {
		return new ForAllForAllExists<Label>() {

			private UnsignedSet union;

			@Override
			public boolean check(Label e, UnsignedSet x, UnsignedSet y,
					UnsignedSet z) {
				return (!z.contains(e)) && z.isSubsetOf(union);
			}

			@Override
			public Iterator<? extends Label> check(UnsignedSet a, UnsignedSet b) {
				if (a == b)
					return null;
				union = a.union(b);
				return a.intersection(b).iterator();
			}

		}.verify();
	}

	private boolean verifyIncomparability() {

		return new ForAllForAll() {
			@Override
			public boolean check(UnsignedSet a, UnsignedSet b) {
				return (!a.isSubsetOf(b)) || a == b;
			}
		}.verify();
	}

	SetOfUnsignedSetInternal computeBases() {
		Iterator<UnsignedSetInternal> it = iterator();
		int max = 0;
		while (it.hasNext()) {
			int sz = it.next().size();
			if (sz > max) {
				max = sz;
			}
		}
		UnsignedSetInternal support = union();
		max += ground().length - support.size();

		JavaSet<UnsignedSetInternal> bases = convert(ground()).subsetsOfSize(
				max - 1);
		// System.out.println(support().size() + " " + bases.size());
		it = iterator();
		while (it.hasNext()) {
			UnsignedSet n = it.next();
			if (n.size() == max - 1) {
				bases.remove(n);
			} else if (n.size() < max - 1) {
				Iterator<? extends UnsignedSet> i2 = bases.iterator();
				while (i2.hasNext()) {
					UnsignedSet b = i2.next();
					if (n.isSubsetOf(b)) {
						i2.remove();
					}
				}
			}
		}
		return useCollection(bases);
	}

	public SignedSetInternal signed(UnsignedSetInternal circuit) {
		OMInternal om = getOM();
		if (om == null)
			throw new IllegalStateException("No associated Oriented Matroid");
		Circuits c = om.getCircuits();
		return c.withSupport(circuit).asCollection().iterator().next();

	}

	@Override
	public String toString() {
		return ffactory().unsignedCircuits().toString(this);
	}

    @Override
    public Group automorphisms() {
        if (automorphisms == null) {
            automorphisms = Group.symmetric(ground().length).filter(new Predicate<Permutation>(){
                @Override
                public boolean apply(Permutation p) {
                    Function<UnsignedSet,UnsignedSet> map = setPermuter(p);
                    for (UnsignedSet s:set) {
                        if (!set.contains(map.apply(s))) {
                            return false;
                        }
                    }
                    return true;
                }
            });
//            System.err.println(automorphisms.order()+" matroid automorphisms");
        }
        return automorphisms;
    }

    protected Function<UnsignedSet, UnsignedSet> setPermuter(Permutation p) {
        final Permutation universePermuter = ffactory().labels().permuteUniverse(ground(), p);
        return new Function<UnsignedSet, UnsignedSet>() {
            @Override
            public UnsignedSet apply(UnsignedSet input) {
                return ((UnsignedSetInternal)input).permuteUniverse(universePermuter);
            }
            
        };
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

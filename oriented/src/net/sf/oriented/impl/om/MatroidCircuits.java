/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Iterator;

import net.sf.oriented.impl.items.LabelFactoryImpl;
import net.sf.oriented.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.impl.set.SignedSetInternal;
import net.sf.oriented.impl.set.UnsignedSetInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Group;
import net.sf.oriented.util.combinatorics.Permutation;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

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
	public void verify() throws AxiomViolation {
	    verifyNonEmpty();
	    verifyIncomparability();
	    verifyWeakElimination();

	}

	private void verifyWeakElimination() throws AxiomViolation {
		new ForAllForAllExists<Label,UnsignedSet>() {

			private UnsignedSet union;

			@Override
			public boolean check(Label e, UnsignedSet x, UnsignedSet y,
					UnsignedSet z) {
				return (!z.contains(e)) && z.isSubsetOf(union);
			}

			@Override
			public Iterator<Label> suchThatForAll(UnsignedSet a, UnsignedSet b) {
				if (a.equals(b))
					return null;
				union = a.union(b);
				return a.intersection(b).iterator();
			}

		}.verify();
	}

	private void verifyIncomparability() throws AxiomViolation {
		new ForAllForAll() {
			@Override
			public boolean check(UnsignedSet a, UnsignedSet b) {
				return (!a.isSubsetOf(b)) || a.equals(b);
			}
		}.verify();
	}

	SetOfUnsignedSetInternal computeBases() {
		Iterator<UnsignedSetInternal> it = iterator2();
		int max = 0;
		while (it.hasNext()) {
			int sz = it.next().size();
			if (sz > max) {
				max = sz;
			}
		}
		UnsignedSetInternal support = union();
		max += elements().length - support.size();

		JavaSet<UnsignedSetInternal> bases = convert(elements()).subsetsOfSize(
				max - 1);
		// System.out.println(support().size() + " " + bases.size());
		it = iterator2();
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

	public SignedSetInternal signed(OMInternal om, UnsignedSetInternal circuit) {
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
            automorphisms = Group.symmetric(elements().length).filter(new Predicate<Permutation>(){
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
        final Permutation universePermuter = ((LabelFactoryImpl)ffactory().labels()).permuteUniverse(elements(), p);
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

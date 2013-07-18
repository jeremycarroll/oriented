/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import static net.sf.oriented.omi.impl.om.Cryptomorphisms.VECTORS;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.OMSFactory;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.SignedSetInternal;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;

public class Vectors extends AbsVectorsOM {

	// public static Vectors parse(String s) {
	// OMAll all = new OMAll();
	// Vectors rslt = new Vectors(SymmetricSetOfSignedSet.parse(s),all);
	// return rslt;
	// }
	Vectors(SetOfSignedSetInternal c, OMInternal a) {
		super(c, VECTORS, a);
	}

	Vectors(Circuits circuits) {
		super(circuits.allCompositions(), VECTORS, circuits);
	}

	public Vectors(MaxVectors maxVectors) {
		super(maxVectors.vectors(), VECTORS, maxVectors);
	}

	@Override
	public boolean verify() {
		return super.verify() && verifyVO() && verifyV2p() && verifyYapproxX();

	}

	static private List<Object> singleton = Arrays.asList(new Object[] { 0 });

	/**
	 * Bible page 145, prop 3.7.10, axiom V3''
	 * 
	 * @return
	 */
	private boolean verifyYapproxX() {
		return new ForAllForAllExists<Object,SignedSet>() {

			@Override
			boolean check(Object e, SignedSet x, SignedSet y, SignedSet z) {
				return z.isRestrictionOf(x) && (!z.equals(x))
						&& x.plus().minus(y.minus()).isSubsetOf(z.plus())
						&& x.minus().minus(y.plus()).isSubsetOf(z.minus());
			}

			@Override
			Iterator<Object> suchThatForAll(SignedSet x, SignedSet y) {
				if (x.plus().intersection(y.minus()).isEmpty())
					return null;
				if (y.support().isSubsetOf(x.support()))
					return singleton.iterator();
				return null;
			}

		}.verify();
	}

	/**
	 * Bible page 144, Corollary 3.7.7
	 * 
	 * @return
	 */
	private boolean verifyV2p() {
		return new ForAllForAll() {

			@Override
			boolean suchThatForAll(SignedSet a, SignedSet b) {
				if (!a.conformsWith(b))
					return true;
				return vectors.contains(a.compose(b));
			}

		}.verify();
	}

	/**
	 * Bible page 143
	 * 
	 * @return
	 */
	private boolean verifyVO() {
		return vectors.contains(factory().itemFactory().empty());
	}

	SetOfSignedSetInternal minimal() {
		// SetOfUnsignedSetInternal vvv = vectors.unsignedSets();
		// Map<UnsignedSetInternal,SetOfSignedSetInternal> map
		// = vectors.withSupport(vvv);

		MatroidCircuits mc = getMatroid().getCircuits();
		JavaSet<SignedSetInternal> r = factory().itemFactory()
				.emptyCollectionOf();
		Iterator<UnsignedSetInternal> it = mc.iterator();
		while (it.hasNext()) {
			UnsignedSetInternal circuit = it.next();
			r.addAll(withSupport(circuit).asCollection());
		}
		return factory().fromBackingCollection(r);
	}

	SetOfSignedSetInternal maximal() {
		return withSupport(support());
	}

	@SuppressWarnings("unchecked")
	public SetOfUnsignedSetInternal circuits() {
		JavaSet<UnsignedSetInternal> dependent[] = new JavaSet[ground().length + 1];
		for (int i = 0; i < dependent.length; i++) {
			dependent[i] = all.unsignedSets().emptyCollectionOf();
		}
		Iterator<SignedSetInternal> it = iterator();
		while (it.hasNext()) {
			UnsignedSetInternal dep = it.next().support();
			dependent[dep.size()].add(dep);
		}
		JavaSet<UnsignedSetInternal> circuits = all.unsignedSets()
				.emptyCollectionOf();
		if (dependent.length > 60)
			throw new UnsupportedOperationException("unimplemented - too big");
		long present = 0;
		for (int i = 1; i < dependent.length; i++)
			if (addIfNoSubsets(circuits, dependent[i], present)) {
				present |= (1l << i);
			}
		return ((SetOfUnsignedSetFactory) ffactory().setsOfUnsignedSet())
				.fromBackingCollection(circuits);
	}

	private boolean addIfNoSubsets(JavaSet<UnsignedSetInternal> circuits,
			JavaSet<UnsignedSetInternal> v, long present) {
		Iterator<UnsignedSetInternal> it = v.iterator();
		JavaSet<UnsignedSetInternal> c = all.unsignedSets().emptyCollectionOf();
		outer: while (it.hasNext()) {
			UnsignedSetInternal vv = it.next();
			int sz = vv.size();
			for (int i = 0; i <= sz; i++)
				if (((1l << i) & present) != 0) {
					JavaSet<UnsignedSetInternal> subs = vv.subsetsOfSize(i);
					subs.retainAll(circuits);
					if (!subs.isEmpty()) {
						continue outer;
					}
				}
			c.add(vv);
		}
		circuits.addAll(c);
		return !c.isEmpty();
	}
    @Override
    OMSFactory omsFactory() {
        return ffactory().vectors();
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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
/**
 * Copyright 2007, Jeremy J. Carroll
 */
package net.sf.oriented.omi.impl.set.hash;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.set.SetOfSignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.SignedSetFactory;
import net.sf.oriented.omi.impl.set.SignedSetInternal;
import net.sf.oriented.omi.impl.set.Test;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;

/**
 * @author jeremy
 * 
 */
public class SetOfSignedSetImpl
		extends
		SetImpl<SignedSetInternal, SetOfSignedSetInternal, SignedSet, SetOfSignedSet, SignedSetInternal, SetOfSignedSetInternal>
		implements SetOfSignedSetInternal {

	@Override
	public SetOfSignedSetFactory factory() {
		return (SetOfSignedSetFactory) super.factory();
	}

	private UnsignedSetInternal support;

	private UnsignedSetInternal computeSupport() {
		Iterator<SignedSetInternal> it = iterator();
		UnsignedSetInternal r = factory().setOfUnsignedSetFactory()
				.itemFactory().empty();

		while (it.hasNext()) {
			r = r.union(it.next().support());
		}
		return r;
	}

	/**
	 * @param a
	 * @param ig
	 */
	public SetOfSignedSetImpl(JavaSet<SignedSetInternal> a,
			SetOfSignedSetFactory f) {
		super(a, f);

	}

	public SetOfSignedSetImpl deletion(final Label s) {
		return (SetOfSignedSetImpl) only(new Test<SignedSet>() {
			@Override
			public boolean test(SignedSet e) {
				return e.sign(s) == 0;
			}
		});
	}

	public SetOfSignedSet contraction(Label l) {
		JavaSet<SignedSetInternal> r = emptyCollectionOf();
		Iterator<SignedSetInternal> it = iterator();
		while (it.hasNext()) {
			SignedSetInternal s = it.next();
			switch (s.sign(l)) {
			case -1:
				r.add(new SignedSetImpl(s.plus(), s.minus().minus(l), factory()
						.itemFactory()));
				break;
			case 0:
				r.add(s);
				break;
			case 1:
				r.add(new SignedSetImpl(s.plus().minus(l), s.minus(), factory()
						.itemFactory()));
				break;
			}
		}
		return useCollection(r);

	}

	@Override
	public UnsignedSetInternal support() {
		if (support == null) {
			support = computeSupport();
		}
		return support;
	}

	Map<UnsignedSet, SignedSetInternal> indexes;

	@Override
	public SetOfUnsignedSetInternal unsignedSets() {
		indexes = new HashMap<UnsignedSet, SignedSetInternal>();
		SetOfUnsignedSetFactory setOfUnsignedSetFactory = factory()
				.setOfUnsignedSetFactory();
		JavaSet<UnsignedSetInternal> s = setOfUnsignedSetFactory.itemFactory()
				.emptyCollectionOf();
		Iterator<SignedSetInternal> it = iterator();
		while (it.hasNext()) {
			SignedSetInternal signed = it.next();
			UnsignedSetInternal unsigned = signed.support();
			s.add(unsigned);
			indexes.put(unsigned, signed);
		}
		return setOfUnsignedSetFactory.fromBackingCollection(s);
	}

	@Override
	public SetOfSignedSetInternal withSupport(final UnsignedSetInternal u) {
		return only(new Test<SignedSet>() {
			@Override
			public boolean test(SignedSet e) {
				return u.sameSetAs(e.support());
			}
		});
	}

	@Override
	public SetOfSignedSetInternal conformingWith(final SignedSetInternal x) {
		return only(new Test<SignedSet>() {
			@Override
			public boolean test(SignedSet e) {
				return x.conformsWith(e);
			}
		});
	}

	@Override
	public SetOfSignedSetInternal restriction(UnsignedSet x0) {
		JavaSet<SignedSetInternal> tx = factory().itemFactory()
				.emptyCollectionOf();
		Iterator<SignedSetInternal> topes = iterator();
		while (topes.hasNext()) {
			tx.add(topes.next().restriction(x0));
		}
		return useCollection(tx);
	}

    @Override
    public SetOfSignedSet reorientRaw(Label ... axes) {
        SignedSetFactory signedSetFactory = factory().itemFactory();
        UnsignedSetImpl changed = (UnsignedSetImpl) signedSetFactory.unsignedF.copyBackingCollection(Arrays.asList(axes));
        UnsignedSetImpl unchanged = (UnsignedSetImpl) support().minus(changed);
        JavaSet<SignedSetInternal> reoriented = signedSetFactory.emptyCollectionOf();
        for (SignedSetInternal ss:this ) {
            UnsignedSetInternal plus = ss.plus().intersection(unchanged)
                                         .union(ss.minus().intersection(changed));
            UnsignedSetInternal minus = ss.plus().intersection(changed)
                    .union(ss.minus().intersection(unchanged));
            reoriented.add(  new SignedSetImpl(plus,minus,signedSetFactory) );
        }
        return new SetOfSignedSetImpl(reoriented, factory());
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

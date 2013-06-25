/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
/**
 * Copyright 2007, Jeremy J. Carroll
 */
package net.sf.oriented.omi.impl.set.hash;

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

	// public String toString(UnsignedSet e, boolean usePlusMinus,
	// boolean symmetric) {
	// StringBuffer rslt = new StringBuffer();
	// String sep = "";
	// rslt.append("{");
	// Iterator<SignedSetI> it = iterator();
	// while (it.hasNext()) {
	// String v;
	// if (usePlusMinus) {
	// v = it.next().toPlusMinus(e);
	// if (symmetric) {
	// int p = v.indexOf('+');
	// int m = v.indexOf('-');
	// if (m < p && m >= 0)
	// continue;
	// }
	// } else {
	// v = it.next().toString(e);
	// if (symmetric && v.length() > 1 && v.charAt(1) == '\'')
	// continue;
	// }
	// rslt.append(sep);
	// rslt.append(v);
	// sep = ",";
	// }
	// rslt.append("}");
	// return rslt.toString();
	// }
	//
	// public String toString(UnsignedSet e) {
	// return toString(e,false,false);
	// }
	// public String toPlusMinus(UnsignedSet e) {
	// return toString(e,true,false);
	// }

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

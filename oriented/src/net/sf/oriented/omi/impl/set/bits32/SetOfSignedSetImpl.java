/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
/**
 * Copyright 2007, Jeremy J. Carroll
 */
package net.sf.oriented.omi.impl.set.bits32;

import static net.sf.oriented.omi.impl.set.SignedSetFactory.plus;
import static net.sf.oriented.omi.impl.set.SignedSetFactory.toLong;

import java.util.Arrays;
import java.util.Iterator;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelFactory;
import net.sf.oriented.omi.impl.set.SetFactoryInternal;
import net.sf.oriented.omi.impl.set.SetOfSignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.SignedSetFactory;
import net.sf.oriented.omi.impl.set.SignedSetInternal;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;

/**
 * @author jeremy
 * 
 */
final public class SetOfSignedSetImpl
		extends
		SetImpl<SignedSetInternal, SetOfSignedSetInternal, SignedSet, SetOfSignedSet, SignedSetInternal, SetOfSignedSetInternal>
		implements SetOfSignedSetInternal {

	@Override
	public SetOfSignedSetFactory factory() {
		return (SetOfSignedSetFactory) super.factory();
	}

	private UnsignedSetInternal support;
	final private long members[];

	private UnsignedSetInternal computeSupport() {
		int s = 0;
		for (long member : members) {
			s |= plus(member) | minus(member);
		}
		return make(s);
	}

	private UnsignedSetInternal make(int s) {
		return new UnsignedSetImpl(s, factory().itemFactory().unsignedF);
	}

	static int minus(long l) {
		return SignedSetFactory.minus(l);
	}

	/**
	 * @param m
	 * @param ig
	 */
	public SetOfSignedSetImpl(
			JavaSet<SignedSetInternal> a,
			SetFactoryInternal<SignedSetInternal, SetOfSignedSetInternal, SignedSet, SetOfSignedSet, SignedSetInternal, SetOfSignedSetInternal> setFactoryInternal) {
		super(setFactoryInternal);
		members = new long[a.size()];
		int i = 0;
		for (SignedSetInternal ss : a) {
			SignedSetImpl sss = (SignedSetImpl) ss;
			members[i++] = toLong(sss.plus, sss.minus);
		}
		Arrays.sort(members);
	}

	private SetOfSignedSetImpl(
			long[] m,
			SetFactoryInternal<SignedSetInternal, SetOfSignedSetInternal, SignedSet, SetOfSignedSet, SignedSetInternal, SetOfSignedSetInternal> factory) {
		super(factory);
		members = m;
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

	// public SetOfSignedSetImpl deletion(final Label s) {
	// return (SetOfSignedSetImpl) only(new Test<SignedSet>(){
	// public boolean test(SignedSet e) {
	// return e.sign(s)==0;
	// }});
	// }
	// public SetOfSignedSet contraction(Label l) {
	// JavaSet<SignedSetInternal> r = emptyCollectionOf();
	// Iterator<SignedSetInternal> it = iterator();
	// while (it.hasNext()) {
	// SignedSetInternal s = it.next();
	// switch (s.sign(l)) {
	// case -1:
	// r.add(new
	// SignedSetImpl(s.plus(),s.minus().minus(l),factory().itemFactory()));
	// break;
	// case 0:
	// r.add(s);
	// break;
	// case 1:
	// r.add(new
	// SignedSetImpl(s.plus().minus(l),s.minus(),factory().itemFactory()));
	// break;
	// }
	// }
	// return useCollection(r);
	//
	// }

	@Override
	public UnsignedSetInternal support() {
		if (support == null) {
			support = computeSupport();
		}
		return support;
	}

	@Override
	public boolean contains(SignedSet b) {
		long bb = remakex(b);
		return Arrays.binarySearch(members, bb) >= 0;
	}

	@Override
	public SetOfSignedSetInternal intersection(SetOfSignedSet b) {
		SetOfSignedSetImpl bb = remakex(b);
		long inter[] = new long[MIN(members.length, bb.members.length)];
		int i = 0, j = 0, k = 0;
		while (i < members.length && j < bb.members.length) {
			long cmp = members[i] - bb.members[j];
			if (cmp == 0) {
				inter[k++] = members[i++];
				j++;
			} else if (cmp < 0) {
				i++;
			} else {
				j++;
			}
		}
		return make(k, inter);
	}

	private SetOfSignedSetInternal make(int k, long[] inter) {
		long m[] = new long[k];
		System.arraycopy(inter, 0, m, 0, k);
		return new SetOfSignedSetImpl(m, factory());
	}

	private SetOfUnsignedSetInternal make(int k, int[] inter) {
		int m[] = new int[k];
		System.arraycopy(inter, 0, m, 0, k);
		return new SetOfUnsignedSetImpl(m, factory().setOfUnsignedSetFactory());
	}

	static int MIN(int a, int b) {
		if (a < b)
			return a;
		return b;
	}

	private SetOfSignedSetImpl remakex(SetOfSignedSet b) {
		return (SetOfSignedSetImpl) remake(b);
	}

	private int remakex(UnsignedSet a) {
		return ((UnsignedSetImpl) factory().setOfUnsignedSetFactory()
				.itemFactory().remake(a)).members;
	}

	@Override
	public boolean isEmpty() {
		return members.length == 0;
	}

	@Override
	public boolean isSubsetOf(SetOfSignedSet b) {
		return isSubSetOfX(remakex(b));
	}

	private boolean isSubSetOfX(SetOfSignedSetImpl bb) {
		int i = 0, j = 0;
		while (i < members.length && j < bb.members.length) {
			long cmp = members[i] - bb.members[j];
			if (cmp == 0) {
				i++;
				j++;
			} else if (cmp < 0)
				return false;
			else {
				j++;
			}
		}
		return i == members.length;
	}

	@Override
	public boolean isSupersetOf(SetOfSignedSet b) {
		return remakex(b).isSubSetOfX(this);
	}

	@Override
	public Iterator<SignedSetInternal> iterator() {
		return new Iterator<SignedSetInternal>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < members.length;
			}

			@Override
			public SignedSetInternal next() {
				long l = members[i++];
				return SignedSetImpl.make(l, factory().itemFactory());
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("immutable");
			}
		};
	}

	@Override
	public int size() {
		return members.length;
	}

	@Override
	public SetOfSignedSetInternal minus(SetOfSignedSet b) {
		SetOfSignedSetImpl bb = remakex(b);
		long inter[] = new long[members.length];
		int i = 0, j = 0, k = 0;
		while (i < members.length && j < bb.members.length) {
			long cmp = members[i] - bb.members[j];
			if (cmp == 0) {
				i++;
				j++;
			} else if (cmp < 0) {
				inter[k++] = members[i++];
			} else {
				j++;
			}
		}
		while (i < members.length) {
			inter[k++] = members[i++];
		}
		return make(k, inter);
	}

	@Override
	public SetOfSignedSetInternal minus(SignedSet b) {
		long bb = remakex(b);
		int pos = Arrays.binarySearch(members, bb);
		if (pos < 0)
			return this;
		long m[] = new long[members.length - 1];
		System.arraycopy(members, 0, m, 0, pos);
		System.arraycopy(members, pos + 1, m, pos, members.length - pos - 1);
		return new SetOfSignedSetImpl(m, factory());
	}

	@Override
	public SetOfSignedSetInternal union(SetOfSignedSet b) {
		SetOfSignedSetImpl bb = remakex(b);
		long inter[] = new long[members.length + bb.members.length];
		int i = 0, j = 0, k = 0;
		while (i < members.length && j < bb.members.length) {
			long cmp = members[i] - bb.members[j];
			if (cmp == 0) {
				inter[k++] = members[i++];
				j++;
			} else if (cmp < 0) {
				inter[k++] = members[i++];
			} else {
				inter[k++] = bb.members[j++];
			}
		}
		while (i < members.length) {
			inter[k++] = members[i++];
		}
		while (j < bb.members.length) {
			inter[k++] = bb.members[j++];
		}
		return make(k, inter);
	}

	@Override
	public SetOfSignedSetInternal union(SignedSet b) {
		long bb = remakex(b);
		int pos = Arrays.binarySearch(members, bb);
		if (pos >= 0)
			return this;
		long m[] = new long[members.length + 1];
		System.arraycopy(members, 0, m, 0, -pos - 1);
		m[-pos - 1] = bb;
		System.arraycopy(members, -pos - 1, m, -pos, members.length + pos + 1);
		return new SetOfSignedSetImpl(m, factory());
	}

	private long remakex(SignedSet b) {
		SignedSetImpl bb = (SignedSetImpl) remake(b);
		return toLong(bb.plus, bb.minus);
	}

	// TODO issue about empty set and equality etc....
	@Override
	public boolean sameSetAs(SetOfSignedSet b) {
		SetOfSignedSetImpl bb = remakex(b);
		if (members.length != bb.members.length)
			return false;
		if (hashCode != MARKER && bb.hashCode != MARKER
				&& hashCode != bb.hashCode)
			return false;
		for (int i = 0; i < members.length; i++)
			if (members[i] != bb.members[i])
				return false;
		return true;

	}

	static final int MARKER = 0x34561234;
	private int hashCode = MARKER;

	@Override
	public int hashCode() {
		if (hashCode == MARKER) {
			LabelFactory lf = factory().itemFactory().unsignedF.itemFactory();
			hashCode = 0;
			for (long member : members) {
				hashCode += lf.hashCode(plus(member)) + 35
						* lf.hashCode(SignedSetFactory.minus(member));
			}
		}
		return hashCode;
	}

	@Override
	public SetOfUnsignedSetInternal unsignedSets() {
		int m[] = new int[members.length];
		for (int i = 0; i < members.length; i++) {
			m[i] = plus(members[i]) | minus(members[i]);
		}
		return removeDuplicates(m);
	}

	private SetOfUnsignedSetInternal removeDuplicates(int[] m) {
		Arrays.sort(m);
		int ok = 0;
		for (int i = 1; i < m.length; i++)
			if (m[i] != m[ok]) {
				m[++ok] = m[i];
			}
		ok++;
		return make(ok, m);
	}

	private SetOfSignedSetInternal removeDuplicates(long[] m) {
		Arrays.sort(m);
		int ok = 0;
		for (int i = 1; i < m.length; i++)
			if (m[i] != m[ok]) {
				m[++ok] = m[i];
			}
		ok++;
		return make(ok, m);
	}

	@Override
	public SetOfSignedSetInternal withSupport(UnsignedSetInternal u) {
		int want = remakex(u);
		int ok = 0;
		long m[] = new long[members.length];
		for (long member : members) {
			if (want == (plus(member) | minus(member))) {
				m[ok++] = member;
			}
		}
		return make(ok, m);
	}

	@Override
	public SetOfSignedSetInternal conformingWith(SignedSetInternal x) {
		long want = remakex(x);
		int p = plus(want);
		int m = minus(want);
		int ok = 0;
		long mems[] = new long[members.length];
		for (long member : members) {
			if (conforming(plus(member), minus(member), p, m)) {
				mems[ok++] = member;
			}
		}
		return make(ok, mems);
	}

	static private boolean conforming(int p0, int m0, int p1, int m1) {
		return ((p0 & m1) | (m0 & p1)) == 0;
	}

	@Override
	public SetOfSignedSetInternal restriction(UnsignedSet x) {
		int want = remakex(x);
		long m[] = new long[members.length];
		for (int i = 0; i < members.length; i++) {
			m[i] = toLong(plus(members[i]) & want, minus(members[i]) & want);
		}
		return removeDuplicates(m);
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

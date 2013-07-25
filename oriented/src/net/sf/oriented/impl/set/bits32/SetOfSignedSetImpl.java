/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
/**
 * Copyright 2007, Jeremy J. Carroll
 */
package net.sf.oriented.impl.set.bits32;

import static net.sf.oriented.impl.set.SignedSetFactory.plus;
import static net.sf.oriented.impl.set.SignedSetFactory.toLong;

import java.util.Arrays;
import java.util.Iterator;

import net.sf.oriented.impl.set.SetFactoryInternal;
import net.sf.oriented.impl.set.SetOfSignedSetFactory;
import net.sf.oriented.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.impl.set.SignedSetFactory;
import net.sf.oriented.impl.set.SignedSetInternal;
import net.sf.oriented.impl.set.UnsignedSetInternal;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.LabelFactory;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

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

	@Override
	public UnsignedSetInternal setOfElements() {
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
		return (SetOfSignedSetImpl) remake(b.respectingEquals());
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

    @Override
    public SetOfSignedSet reorientRaw(Label ... axes) {
        UnsignedSetImpl changed = (UnsignedSetImpl) factory().itemFactory().unsignedF.copyBackingCollection(Arrays.asList(axes));
        UnsignedSetImpl unchanged = (UnsignedSetImpl) setOfElements().minus(changed);
        int ch = changed.members;
        int un = unchanged.members;
        long reoriented[] = new long[members.length];
        for (int i = 0; i < members.length ; i++ ) {
            int p = plus(members[i]);
            int m = minus(members[i]);
            reoriented[i] = toLong((p&un)|(m&ch),(m&un)|(p&ch));
        }
        Arrays.sort(reoriented);
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

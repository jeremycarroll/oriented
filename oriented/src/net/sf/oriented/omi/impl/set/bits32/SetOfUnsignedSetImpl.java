/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
/**
 * Copyright 2007, Jeremy J. Carroll
 */
package net.sf.oriented.omi.impl.set.bits32;

import java.util.Arrays;
import java.util.Iterator;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOfUnsignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelFactory;
import net.sf.oriented.omi.impl.set.SetFactoryInternal;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.Test;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;

/**
 * @author jeremy
 * 
 */
public class SetOfUnsignedSetImpl
		extends
		SetImpl<UnsignedSetInternal, SetOfUnsignedSetInternal, 
		        UnsignedSet,         SetOfUnsignedSet, 
		        UnsignedSetInternal, SetOfUnsignedSetInternal>
       implements SetOfUnsignedSetInternal {

	@Override
	public SetOfUnsignedSetFactory factory() {
		return (SetOfUnsignedSetFactory) super.factory();
	}

	final private int members[];

	public SetOfUnsignedSetImpl(
			JavaSet<UnsignedSetInternal> a,
			SetFactoryInternal<UnsignedSetInternal, SetOfUnsignedSetInternal, UnsignedSet, SetOfUnsignedSet, UnsignedSetInternal, SetOfUnsignedSetInternal> factory) {
		super(factory);
		members = new int[a.size()];
		int i = 0;
		for (UnsignedSetInternal ss : a) {
			UnsignedSetImpl sss = (UnsignedSetImpl) ss;
			members[i++] = sss.members;
		}
		Arrays.sort(members);
	}

	SetOfUnsignedSetImpl(
			int[] m,
			SetFactoryInternal<UnsignedSetInternal, SetOfUnsignedSetInternal, UnsignedSet, SetOfUnsignedSet, UnsignedSetInternal, SetOfUnsignedSetInternal> factory) {
		super(factory);
		members = m;
	}

	private UnsignedSetInternal computeSupport() {
		int s = 0;
		for (int member : members) {
			s |= member;
		}
		return make(s);
	}

	private UnsignedSetInternal make(int s) {
		return new UnsignedSetImpl(s, factory().itemFactory());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.impl.set.hash.SetOfUnsignedSetInternal#deletion(omi.Label)
	 */
	public SetOfUnsignedSetInternal deletion(final Label s) {
		return only(new Test<UnsignedSet>() {
			@Override
			public boolean test(UnsignedSet e) {
				return !e.contains(s);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.impl.set.hash.SetOfUnsignedSetInternal#contraction(omi.Label)
	 */
	public SetOfUnsignedSetInternal contraction(Label l) {
		JavaSet<UnsignedSetInternal> r = emptyCollectionOf();
		Iterator<UnsignedSetInternal> it = iterator();
		while (it.hasNext()) {
			UnsignedSetInternal s = it.next();
			if (s.contains(l)) {
				r.add(s.minus(l));

			}
		}
		return useCollection(r);

	}

	private UnsignedSetInternal support;

	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.impl.set.hash.SetOfUnsignedSetInternal#union()
	 */
	@Override
	public UnsignedSetInternal union() {
		if (support == null) {
			support = computeSupport();
		}
		return support;
	}

	@Override
	public boolean contains(UnsignedSet a) {
		int bb = remakex(a);
		return Arrays.binarySearch(members, bb) >= 0;
	}

	private int remakex(UnsignedSet a) {
		return ((UnsignedSetImpl) remake(a)).members;
	}

	@Override
	public SetOfUnsignedSetInternal intersection(SetOfUnsignedSet b) {
		SetOfUnsignedSetImpl bb = remakex(b);
		int inter[] = new int[SetOfSignedSetImpl.MIN(members.length,
				bb.members.length)];
		int i = 0, j = 0, k = 0;
		while (i < members.length && j < bb.members.length) {
			int cmp = members[i] - bb.members[j];
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

	private SetOfUnsignedSetInternal make(int k, int[] inter) {
		int m[] = new int[k];
		System.arraycopy(inter, 0, m, 0, k);
		return new SetOfUnsignedSetImpl(m, factory());
	}

	private SetOfUnsignedSetImpl remakex(SetOfUnsignedSet b) {
		return (SetOfUnsignedSetImpl) remake(b);
	}

	@Override
	public boolean isEmpty() {
		return members.length == 0;
	}

	@Override
	public boolean isSubsetOf(SetOfUnsignedSet b) {
		return isSubSetOfX(remakex(b));
	}

	private boolean isSubSetOfX(SetOfUnsignedSetImpl bb) {
		int i = 0, j = 0;
		while (i < members.length && j < bb.members.length) {
			int cmp = members[i] - bb.members[j];
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
	public boolean isSupersetOf(SetOfUnsignedSet b) {
		return remakex(b).isSubSetOfX(this);
	}

	@Override
	public Iterator<UnsignedSetInternal> iterator() {
		return new Iterator<UnsignedSetInternal>() {
			int i = 0;

			@Override
			public boolean hasNext() {
				return i < members.length;
			}

			@Override
			public UnsignedSetInternal next() {
				return make(members[i++]);
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
	public SetOfUnsignedSetInternal minus(SetOfUnsignedSet b) {
		SetOfUnsignedSetImpl bb = remakex(b);
		int inter[] = new int[members.length];
		int i = 0, j = 0, k = 0;
		while (i < members.length && j < bb.members.length) {
			int cmp = members[i] - bb.members[j];
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
	public SetOfUnsignedSetInternal minus(UnsignedSet b) {
		int bb = remakex(b);
		int pos = Arrays.binarySearch(members, bb);
		if (pos < 0)
			return this;
		int m[] = new int[members.length - 1];
		System.arraycopy(members, 0, m, 0, pos);
		System.arraycopy(members, pos + 1, m, pos, members.length - pos - 1);
		return new SetOfUnsignedSetImpl(m, factory());
	}

	@Override
	public SetOfUnsignedSetInternal union(SetOfUnsignedSet b) {
		SetOfUnsignedSetImpl bb = remakex(b);
		int inter[] = new int[members.length + bb.members.length];
		int i = 0, j = 0, k = 0;
		while (i < members.length && j < bb.members.length) {
			int cmp = members[i] - bb.members[j];
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
	public SetOfUnsignedSetInternal union(UnsignedSet b) {
		int bb = remakex(b);
		int pos = Arrays.binarySearch(members, bb);
		if (pos >= 0)
			return this;
		int m[] = new int[members.length + 1];
		System.arraycopy(members, 0, m, 0, -pos - 1);
		m[-pos - 1] = bb;
		System.arraycopy(members, -pos - 1, m, -pos, members.length + pos + 1);
		return new SetOfUnsignedSetImpl(m, factory());
	}

	@Override
	public boolean sameSetAs(SetOfUnsignedSet b) {
		SetOfUnsignedSetImpl bb = remakex(b);
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
			LabelFactory lf = factory().itemFactory().itemFactory();
			hashCode = 0;
			for (int member : members) {
				hashCode += lf.hashCode(member);
			}
		}
		return hashCode;
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

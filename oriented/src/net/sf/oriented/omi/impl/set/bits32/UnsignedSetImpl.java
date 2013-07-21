/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.set.bits32;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.set.SetFactoryInternal;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;
import net.sf.oriented.util.combinatorics.Permutation;

final public class UnsignedSetImpl
		extends
		SetImpl<LabelImpl, UnsignedSetInternal, Label, UnsignedSet, LabelImpl, UnsignedSetInternal>
		implements UnsignedSetInternal {

	final int members;

	@Override
	public UnsignedSetFactory factory() {
		return (UnsignedSetFactory) super.factory();
	}

	public UnsignedSetImpl(
			JavaSet<LabelImpl> a,
			SetFactoryInternal<LabelImpl, UnsignedSetInternal, Label, UnsignedSet, LabelImpl, UnsignedSetInternal> factory) {
		super(factory);
		int m = toInt(a);
		members = m;
	}

	private int toInt(JavaSet<LabelImpl> a) {
		int v = 0;
		for (LabelImpl l : a) {
			v |= (1 << l.ordinal());
		}
		return v;
	}

	UnsignedSetImpl(
			int v,
			SetFactoryInternal<LabelImpl, UnsignedSetInternal, Label, UnsignedSet, LabelImpl, UnsignedSetInternal> factory) {
		super(factory);
		members = v;
	}

	@Override
	public boolean contains(Label a) {
		return (members & member(a)) != 0;
	}

	@Override
	public UnsignedSetInternal intersection(UnsignedSet b) {
		return new UnsignedSetImpl(members & members(b), factory());
	}

	private int members(UnsignedSet b) {
		return ((UnsignedSetImpl) remake(b)).members;
	}

	private int member(Label a) {
		return 1 << remake(a).ordinal();
	}

	@Override
	public boolean isEmpty() {
		return members == 0;
	}

	@Override
	public boolean isSubsetOf(UnsignedSet b) {
		return (members & ~members(b)) == 0;
	}

	@Override
	public boolean isSupersetOf(UnsignedSet b) {
		return (~members & members(b)) == 0;
	}

	@Override
	public int hashCode() {
		int h = factory().itemFactory().hashCode(members);
		// if (h!=super.hashCode())
		// throw new RuntimeException("hashCode wrong");
		return h;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<LabelImpl> iterator() {
		return (Iterator<LabelImpl>) (Iterator<? extends Label>) new Iterator<Label>() {

			int nValue = 0;

			@Override
			public boolean hasNext() {
				while (nValue < 32 && ((members >> nValue) & 1) == 0) {
					nValue++;
				}
				return nValue < 32;
			}

			@Override
			public Label next() {
				if (!hasNext())
					throw new NoSuchElementException();
				return factory.getOptions().getLabel(nValue++);
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Immutable");
			}

		};
	}

	@Override
	public UnsignedSetInternal minus(UnsignedSet b) {
		return new UnsignedSetImpl(members & ~members(b), factory());
	}

	@Override
	public UnsignedSetInternal minus(Label b) {
		return new UnsignedSetImpl(members & ~member(b), factory());
	}

	@Override
	public int size() {
		return sizex(members);
	}

	static public int size8[] = { 0, 1, 1, 2, 1, 2, 2, 3 };

	static int sizex(int m) {
		int r = 0;
		for (int i = 0; i < 11; i++) {
			r += size8[(m >>> (i * 3)) & 7];
		}
		return r;
	}

	@Override
	public UnsignedSetInternal union(UnsignedSet b) {
		return new UnsignedSetImpl(members | members(b), factory());
	}

	@Override
	public UnsignedSetInternal union(Label b) {
		return new UnsignedSetImpl(members | member(b), factory());
	}

    @Override
    public UnsignedSetInternal permuteUniverse(Permutation u) {
        return new UnsignedSetImpl(((SmartPermutation)u).mapAll(members),factory());
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

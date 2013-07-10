/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.set.hash;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetOf;
import net.sf.oriented.omi.impl.items.HasFactory;
import net.sf.oriented.omi.impl.set.AbsSetImpl;
import net.sf.oriented.omi.impl.set.SetFactoryInternal;
import net.sf.oriented.omi.impl.set.SetOfInternal;
/**
 * Sets built around java hash sets
 * @author jeremycarroll
 *
 * @param <ITEM_INTERNAL> The internal API or implementation for members.
 * @param <SET_INTERNAL> The internal API or implementation for sets.
 * @param <ITEM> The external API for members.
 * @param <SET> The external API for sets.
 * @param <ITEM_INTERNAL2> See {@link net.sf.oriented.util.TypeChecker}
 * @param <SET_INTERNAL2> See {@link net.sf.oriented.util.TypeChecker}
 */
//@formatter:off
abstract public class SetImpl<
                  ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
                  SET_INTERNAL extends SetOfInternal<ITEM_INTERNAL, SET_INTERNAL, 
                                                     ITEM, SET, 
                                                     ITEM_INTERNAL2, SET_INTERNAL2>, 
                  ITEM, 
                  SET extends SetOf<ITEM, SET>, 
                  ITEM_INTERNAL2 extends ITEM, 
                  SET_INTERNAL2 extends SET
                  >
        extends AbsSetImpl<
              ITEM_INTERNAL, 
              SET_INTERNAL, 
              ITEM, 
              SET, 
              ITEM_INTERNAL2, 
              SET_INTERNAL2>  {
//@formatter:on
	private final class PowerJavaSet extends AbstractCollection<SET_INTERNAL2> implements
			JavaSet<SET_INTERNAL2> {

		final int size;

		private PowerJavaSet(int sz) {
			size = 1 << sz;
		}

		@Override
		public Iterator<SET_INTERNAL2> iterator() {
			return new Iterator<SET_INTERNAL2>() {
				int i = 0;

				@Override
				public boolean hasNext() {
					return i < size;
				}

				@Override
				public SET_INTERNAL2 next() {
					JavaSet<ITEM_INTERNAL2> m = emptyCollectionOf();
					Iterator<ITEM_INTERNAL2> it = members.iterator();
					int j = 0;
					while (it.hasNext()) {
						ITEM_INTERNAL2 n = it.next();
						if (((1 << j) & i) != 0) {
							m.add(n);
						}
						j++;
					}
					i++;
					return useCollection(m);
				}

				@Override
				public void remove() {
					throw new UnsupportedOperationException();
				}
			};
		}

		@Override
		public int size() {
			return size;
		}
	}

	private final JavaSet<ITEM_INTERNAL2> members;

	public SetImpl(JavaSet<ITEM_INTERNAL2> a, SetFactoryInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2> f) {
		super(f);
		members = a;
	}

	@Override
	public JavaSet<ITEM_INTERNAL2> asCollection() {
		return members;
	}

	@Override
	public SET_INTERNAL2 union(SET b) {
		JavaSet<ITEM_INTERNAL2> mm = emptyCollectionOf();
		mm.addAll(members);
		mm.addAll(factory().remake(b).asCollection());
		return useCollection(mm);
	}

	@Override
	public SET_INTERNAL2 intersection(SET b) {
		JavaSet<ITEM_INTERNAL2> mm = emptyCollectionOf();
		mm.addAll(members);
		mm.retainAll(b.asCollection());
		return useCollection(mm);
	}

	@Override
	public SET_INTERNAL2 minus(SET b) {
		JavaSet<ITEM_INTERNAL2> mm = emptyCollectionOf();
		mm.addAll(members);
		mm.removeAll(b.asCollection());
		return useCollection(mm);
	}

	@Override
	public boolean contains(ITEM a) {
		return members.contains(a);
	}

	@Override
	public Iterator<ITEM_INTERNAL2> iterator() {
		return new Iterator<ITEM_INTERNAL2>() {
			Iterator<ITEM_INTERNAL2> underlying = members.iterator();

			@Override
			public boolean hasNext() {
				return underlying.hasNext();
			}

			@Override
			public ITEM_INTERNAL2 next() {
				return underlying.next();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("S is immutable");
			}
		};
	}

	@Override
	public int size() {
		return members.size();
	}

	@Override
	public boolean isSubsetOf(SET b) {
		return b.asCollection().containsAll(members);
	}

	@Override
	public boolean isSupersetOf(SET b) {
		return members.containsAll(b.asCollection());
	}

	@Override
	public boolean isEmpty() {
		return members.isEmpty();
	}

	@Override
	public SET_INTERNAL2 minus(ITEM b) {
		JavaSet<ITEM_INTERNAL2> r = emptyCollectionOf();
		r.addAll(members);
		r.remove(b);
		return useCollection(r);
	}

	@Override
	@SuppressWarnings("unchecked")
	public SET_INTERNAL2 union(ITEM b) {
		JavaSet<ITEM_INTERNAL2> r = emptyCollectionOf();
		r.addAll(members);
		r.add((ITEM_INTERNAL2) factory().itemFactory().remake(b));
		return useCollection(r);
	}

	@Override
	public JavaSet<SET_INTERNAL2> powerSet() {
		final int sz = members.size();
		if (sz > 20)
			throw new IllegalArgumentException("unimplemented powerset sz > 20");
		return new PowerJavaSet(sz);
	}

	@Override
	public JavaSet<SET_INTERNAL2> subsetsOfSize(int i) {
		return convert(subsetsOfSize(0, i));
	}

	private JavaSet<SET_INTERNAL2> convert(Collection<JavaSet<ITEM_INTERNAL2>> c) {
		JavaSet<SET_INTERNAL2> r = factory.emptyCollectionOf();
		Iterator<JavaSet<ITEM_INTERNAL2>> it = c.iterator();
		while (it.hasNext()) {
			SET_INTERNAL2 useCollection = useCollection(it.next());
			r.add(useCollection);
		}
		return r;
	}

	private Set<JavaSet<ITEM_INTERNAL2>> subsetsOfSize(int x, int sz) {

		// System.err.println(x + "/"+ sz + "/" + size());

		if (sz == 0) {
			Set<JavaSet<ITEM_INTERNAL2>> r = new HashSet<JavaSet<ITEM_INTERNAL2>>();
			r.add(emptyCollectionOf());
			return r;
		}
		if (x == size())
			return new HashSet<JavaSet<ITEM_INTERNAL2>>();
		ITEM_INTERNAL2 xth = ith(x);
		Set<JavaSet<ITEM_INTERNAL2>> rslt = subsetsOfSize(x + 1, sz);
		Iterator<JavaSet<ITEM_INTERNAL2>> it = subsetsOfSize(x + 1, sz - 1).iterator();
		while (it.hasNext()) {
			JavaSet<ITEM_INTERNAL2> n = it.next();
			n.add(xth);
			rslt.add(n);
		}
		return rslt;
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

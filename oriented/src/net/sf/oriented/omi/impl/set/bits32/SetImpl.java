package net.sf.oriented.omi.impl.set.bits32;

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
 * Sets built around 32 bit representations of up to 32 different items.
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
					Iterator<ITEM_INTERNAL2> it = SetImpl.this.iterator();
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

	SET_INTERNAL remake(SET x) {
		return factory.remake(x);
	}

	ITEM_INTERNAL remake(ITEM x) {
		return factory().itemFactory().remake(x);
	}

	// @SuppressWarnings("unchecked")
	// SetImpl(E a, SF f) {
	// super(f);
	// members = (List<E>)Arrays.asList(new Object[]{a});
	// }

	// public SetImpl(Collection<E> a, SF f) {
	// super(f);
	// members = new HashSet<E>(a);
	// }

	public SetImpl(SetFactoryInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2> f) {
		super(f);
	}

	@Override
	public JavaSet<ITEM_INTERNAL2> asCollection() {
		JavaSet<ITEM_INTERNAL2> r = factory().itemFactory().emptyCollectionOf();
		Iterator<ITEM_INTERNAL2> it = iterator();
		while (it.hasNext()) {
			r.add(it.next());
		}
		return r;
	}

	@Override
	public JavaSet<SET_INTERNAL2> powerSet() {
		final int sz = size();
		if (sz > 30)
			throw new IllegalArgumentException("unimplemented powerset sz > 30");
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

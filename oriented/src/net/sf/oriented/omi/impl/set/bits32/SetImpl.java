package net.sf.oriented.omi.impl.set.bits32;


import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetOf;
import net.sf.oriented.omi.impl.items.FactoryInternal;
import net.sf.oriented.omi.impl.items.HasFactory;
import net.sf.oriented.omi.impl.set.HasSetFactoryImpl;
import net.sf.oriented.omi.impl.set.SetFactoryInternal;
import net.sf.oriented.omi.impl.set.SetOfInternal;
import net.sf.oriented.omi.impl.set.Test;




abstract public class SetImpl<E extends HasFactory<E,EX,ER>, 
S extends SetOfInternal<E,S,EX,SX,EF,SF,ER,SS>,
EX,
SX extends SetOf<EX,SX>,
EF extends FactoryInternal<E,EX,ER>,
SF extends SetFactoryInternal<E,S,EX,SX,EF,SF,ER,SS>,
ER extends EX,
SS extends SX >
    extends HasSetFactoryImpl<E,S,EX,SX,EF,SF,ER,SS>
    implements SetOfInternal<E,S,EX,SX,EF,SF,ER,SS> {
	private final class PowerJavaSet extends AbstractCollection<SS> implements JavaSet<SS> {
		
		final int size;

		private PowerJavaSet(int sz) {
			size = 1<<sz;
		}

		@Override
		public Iterator<SS> iterator() {
			return new Iterator<SS>(){
		        int i = 0;
				@Override
				public boolean hasNext() {
					return i<size;
				}
		
				@Override
				public SS next() {
					JavaSet<ER> m = emptyCollectionOf();
					Iterator<ER> it = SetImpl.this.iterator();
					int j=0;
					while (it.hasNext()) {
						ER n = it.next();
						if (((1<<j)&i)!=0) {
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
				}};
		}

		@Override
		public int size() {
			return size;
		}
	}

	S remake(SX x) {
		return factory.remake(x);
	}
	
	E remake(EX x) {
		return factory().itemFactory().remake(x);
	}

	

	
//	@SuppressWarnings("unchecked")
//	SetImpl(E a, SF f) {
//		super(f);
//		members = (List<E>)Arrays.asList(new Object[]{a});
//	}

	
//	public SetImpl(Collection<E> a, SF f) {
//		super(f);
//		members = new HashSet<E>(a);
//	}
	
	public SetImpl(SetFactoryInternal<E,S,EX,SX,EF,SF,ER,SS> f){
		super(f);
	}

	@Override
	public JavaSet<ER> asCollection() {
		JavaSet<ER> r = factory().itemFactory().emptyCollectionOf();
		Iterator<ER> it = iterator();
		while (it.hasNext()) {
			r.add(it.next());
		}
		return r;
	}




	@Override
	public int hashCode() {
		int rslt = 0;
		Iterator<ER> it = iterator();
		while (it.hasNext()){
			rslt += it.next().hashCode();
		}
		return rslt;
	}


	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object o) {
		if (o==null || (!(o instanceof SetOf)))
			return false;
		SX sx = (SX)o;
		return sx.equalsIsSameSetAs() && sameSetAs(sx);
	}


	@Override
	public boolean sameSetAs(SX a) {
		if (size()!=a.size())
			return false;
		return isSubsetOf(a) && isSupersetOf(a);
	}
	


	public SS only(Test<EX> t) {
		JavaSet<ER> r = emptyCollectionOf();
		Iterator<ER> i = iterator();
		while (i.hasNext()) {
			ER n = i.next();
			if (t.test(n))
				r.add(n);
		}
		return useCollection(r);
	}

/**
 * r is dedicated to being the backing collection
 * for this set. It is *not* copied, and must not
 * be modified after this call.
 * @param bases
 * @return
 */
	 @Override
	public SS useCollection(JavaSet<ER> bases) {
		return factory().fromBackingCollection(bases);
	}
	
	public SS excluding(Test<EX> t) {
		JavaSet<ER> r = emptyCollectionOf();
		Iterator<ER> i = iterator();
		while (i.hasNext()) {
			ER n = i.next();
			if (!t.test(n))
				r.add(n);
		}
		return useCollection(r);
	}

	protected JavaSet<ER> emptyCollectionOf() {
		return factory().itemFactory().emptyCollectionOf();
	}



	@Override
	public JavaSet<SS> powerSet() {
		final int sz = size();
		if (sz>30) {
			throw new IllegalArgumentException("unimplemented powerset sz > 30");
		}
		return new PowerJavaSet(sz);
	}



	@Override
	public JavaSet<SS> subsetsOfSize(int i) {
		return convert(subsetsOfSize(0, i));
	}
	
	private JavaSet<SS> convert(Collection<JavaSet<ER>> c) {
		JavaSet<SS> r = factory.emptyCollectionOf();
		Iterator<JavaSet<ER>> it = c.iterator();
		while (it.hasNext()) {
			SS useCollection = useCollection(it.next());
			r.add(useCollection);
		}
		return r;
	}

	private Set<JavaSet<ER>> subsetsOfSize(int x, int sz) {

//		System.err.println(x + "/"+ sz + "/" + size());

		if (sz==0 ) {
			Set<JavaSet<ER>> r =  new HashSet<JavaSet<ER>>();
			r.add(emptyCollectionOf());
			return r;
		}
		if ( x==size()) {
			return new HashSet<JavaSet<ER>>();
		}
		ER xth = ith(x);
		Set<JavaSet<ER>> rslt = subsetsOfSize(x+1, sz);
		Iterator<JavaSet<ER>> it = subsetsOfSize(x+1,sz-1).iterator();
		while (it.hasNext()) {
			JavaSet<ER> n = it.next();
			n.add(xth);
			rslt.add(n);
		}
		return rslt;
	}


	private ER ith(int x) {
		Iterator<ER> it = iterator();
		while (x > 0) {
			x--;
			it.next();
		}
		return it.next();
	}

	@Override
	public ER theMember() {
		Iterator<ER> it = iterator();
		ER r = it.next();
		if (it.hasNext())
			throw new IllegalStateException("More than one member.");
		return r;
	}

	@Override
	public boolean equalsIsSameSetAs() {
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public SS respectingEquals() {
		return (SS)this;
	}


}

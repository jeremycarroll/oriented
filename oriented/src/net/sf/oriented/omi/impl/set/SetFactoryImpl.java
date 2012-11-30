/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.set;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOf;
import net.sf.oriented.omi.impl.items.FactoryImpl;
import net.sf.oriented.omi.impl.items.FactoryInternal;
import net.sf.oriented.omi.impl.items.HasFactory;
import net.sf.oriented.omi.impl.items.ParseContext;



abstract public class SetFactoryImpl<E extends HasFactory<E,EX,ER>, 
S extends SetOfInternal<E,S,EX,SX,ER,T>,
EX,
SX extends SetOf<EX,SX>,
ER extends EX,
T extends SX>
extends FactoryImpl<S,SX,T>
implements SetFactoryInternal<E,S,EX,SX,ER,T>
{
	protected SetFactoryImpl(FactoryInternal<E,EX,ER> f) {
		super(f.getOptions());
		itemFactory = f;
		e = fromBackingCollection(itemFactory.emptyCollectionOf());
	}
    final protected FactoryInternal<E,EX,ER> itemFactory;

	@Override
	final public FactoryInternal<E,EX,ER> itemFactory() {
		return itemFactory;
	}

	@Override
	final public T parse(ParseContext pc) {
        JavaSet<ER> rslt = itemFactory.emptyCollectionOf();
			parseItems(pc, rslt);
		return fromBackingCollection(rslt);
	}

	protected void parseItems(ParseContext pc, Collection<ER> rslt) {
		expect(pc,'{');
		while (pc.index < pc.string.length() 
				&& pc.string.charAt(pc.index)!='}') {
			ER ee = itemFactory.parse(pc);
			add(rslt,ee);
		}
		expect(pc,'}');
	}
	@Override
	final public List<ER> orderedParse(ParseContext pc) {
		List<ER> ll = new ArrayList<ER>();
		parseItems(pc, ll);
		return ll;
	}
	final private T e;
    @Override
	public T empty() {
	   return e;
    }
    

	@Override
	final public String toString(SX s) {
	   return toString(getOptions().getUniverse(),s);
	}
	
	@Override
	public String toString(List<? extends Label> u, SX s) {
		StringBuffer rslt = new StringBuffer();
		String sep = "";
		rslt.append("{");
		Iterator<? extends EX> it = s.iterator();
		while (it.hasNext()) {
			rslt.append(sep);
			rslt.append(itemFactory.toString(u,it.next()));
			sep = ",";
		}
		rslt.append("}");
		return rslt.toString();
	}
	
	@Override
	public T copyBackingCollection(Collection<? extends EX> c) {
		JavaSet<ER> r = itemFactory.emptyCollectionOf();
		Iterator<? extends EX> it = c.iterator();
		while (it.hasNext()) {
			add(r, it.next());
		}
		return fromBackingCollection(r);
	}

	@SuppressWarnings("unchecked")
	protected void add(Collection<ER> r, EX next) {
		r.add((ER) itemFactory.remake(next));
	}
	

	@Override
	@SuppressWarnings("unchecked")
	final public T fromBackingCollection(JavaSet<ER> c) {
		return construct(c,(SetFactoryInternal<E,S,EX,SX,ER,T>) this);
	}



	@SuppressWarnings("unchecked")
	@Override
	protected S fallbackRemake(SX t) {
		return (S) copyBackingCollection(t.asCollection());
	}

	protected T construct(JavaSet<ER> c, SetFactoryInternal<E,S,EX,SX,ER,T> f) {
	try {
			return constructor.newInstance(new Object[]{c,f});
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("internal problem",e);
		} catch (InstantiationException e) {
			throw new RuntimeException("internal problem",e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("internal problem",e);
		} catch (InvocationTargetException e) {
			Throwable rte = e.getCause();
			if (rte instanceof RuntimeException)
				throw (RuntimeException)rte;
			throw new RuntimeException("internal problem",e);
		}
	}
}
/************************************************************************
    This file is part of the Java Oriented Matroid Library.

    The Java Oriented Matroid Library is free software: you can 
    redistribute it and/or modify it under the terms of the GNU General 
    Public License as published by the Free Software Foundation, either 
    version 3 of the License, or (at your option) any later version.

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/

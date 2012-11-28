/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.items;


import java.lang.reflect.Constructor;
import java.util.List;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.Options;


public abstract class FactoryImpl<
E extends HasFactory<E,EX,ER,F>,EX,
ER extends EX,
F extends FactoryInternal<E,EX,ER,F>> 
extends IOHelper
implements FactoryInternal<E,EX,ER,F> {
	
	final protected Constructor<ER> constructor;
	@SuppressWarnings("unchecked")
	protected FactoryImpl(Options o) {
		options =o;
//		System.err.println(++cnt+": building: "+getClass().getSimpleName());
		constructor = (Constructor<ER>) o.constructorFor(getClass());
	}
	


	final private Options options;
	@Override
	final public ER parse(String s) {
		ParseContext pc = new ParseContext(s.trim());
		ER rslt = parse(pc);
		if (pc.index != pc.string.length())
			throw new IllegalArgumentException("Syntax error");
		return rslt;
	}

	@Override
	public Options getOptions() {
		return options;
	}

	static int cnt = 0;
	@Override
	@SuppressWarnings("unchecked")
	public E remake(EX t) {
		if (t instanceof HasFactory) {
			if ( ((HasFactory<?,?,?,?>)t).factory() == this)
				return (E)t;
		}
//		System.err.println(++cnt+":"+TestConversions.TEST_NUMBER+" remaking: "+getClass().getSimpleName());
		return fallbackRemake(t);
	}

	@SuppressWarnings("unchecked")
	protected E fallbackRemake(EX t) {
		return (E)parse(toString(t));
	}


	@Override
	public JavaSet<ER> emptyCollectionOf() {
		return options.javaSetFor(null);
	}

	@Override
	public String toString(List<? extends Label> u, EX t) {
		return toString(t);
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

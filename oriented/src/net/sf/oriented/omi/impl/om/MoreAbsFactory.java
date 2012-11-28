/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.om;

import java.util.Collection;
import java.util.List;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.impl.items.IOHelper;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.items.ParseContext;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;


public abstract class MoreAbsFactory<M, S> extends IOHelper {

	final protected FactoryFactory factory;
	public MoreAbsFactory(FactoryFactory f) {
		factory = f;
	}
	protected UnsignedSetFactory unsignedSets() {
		return (UnsignedSetFactory) factory.unsignedSets();
	}
	public M parse(String s) {
		ParseContext pc = new ParseContext(s.trim());
		M r = parse(pc);
		if (pc.index != pc.string.length())
			throw new IllegalArgumentException("Syntax error");
		return r;
	}
	protected M parsePair(ParseContext pc) {
		expect(pc,'(');
		List<LabelImpl> ground = unsignedSets().orderedParse(pc);
		S signedSets = parseMatroid(pc);
		expect(pc,')');
		return construct(ground,signedSets);
	}
	abstract M parse(ParseContext pc) ;
	abstract S parseMatroid(ParseContext pc) ;
	abstract M construct(Collection<? extends Label> ground, S sets);
	

	final public JavaSet<? extends M> emptyCollectionOf() {
		return  factory.options().javaSetFor(null);
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

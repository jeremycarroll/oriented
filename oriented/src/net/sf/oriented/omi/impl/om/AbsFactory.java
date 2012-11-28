/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.om;

import java.util.List;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.impl.items.ParseContext;
import net.sf.oriented.omi.impl.set.SetFactoryInternal;


@SuppressWarnings("rawtypes")
public abstract class AbsFactory<M,S,F extends SetFactoryInternal > extends 
    MoreAbsFactory<M,S> {

	final protected F sets;

	AbsFactory(FactoryFactory f, F sf) {
		super(f);
		sets = sf;
	}

	abstract public List<Label> ground(M s) ;

	@SuppressWarnings("unchecked")
	protected String formatString(M s, M matroidS) {
		List<Label> g = ground(s);
		// TODO: this copyBackingCollection is probably spurious and should be done without copying
		return "( " + unsignedSets().toString(g,unsignedSets().copyBackingCollection(g)) + ", " + sets.toString(g,matroidS) + " )";
	}

	@Override
	M parse(ParseContext pc) {
		if (0 == pc.string.length())
			throw new IllegalArgumentException("Syntax error - empty input");
		switch (pc.string.charAt(0)) {
		case '{':
			return construct(parseMatroid(pc));
		case '(':
			return parsePair(pc);
		default:
				throw new IllegalArgumentException("Syntax error - expected '{' or '('");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	S parseMatroid(ParseContext pc) {
		return (S)sets.parse(pc);
	}

	abstract M construct(S signedSets);

	public M empty() {
		throw new UnsupportedOperationException("No 'empty' oriented matroid");
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

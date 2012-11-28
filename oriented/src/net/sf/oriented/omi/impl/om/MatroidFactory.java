/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.om;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.MFactory;
import net.sf.oriented.omi.MatroidS;
import net.sf.oriented.omi.SetOfUnsignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;



public abstract class MatroidFactory  
extends AbsFactory<MatroidS,SetOfUnsignedSetInternal,SetOfUnsignedSetFactory> 
implements  MFactory
{

	private static final class CFactory extends MatroidFactory {
		private CFactory(FactoryFactory f) {
			super(f);
		}
		@Override
		MatroidS construct(SetOfUnsignedSetInternal signedSets,MatroidAll all) {
			return new MatroidCircuits(signedSets,all);
		}
		@Override
		public String toString(MatroidS s) {
			return formatString(s, s.getCircuits());
		}
	}

	private static final class BFactory extends MatroidFactory {
		private BFactory(FactoryFactory f) {
			super(f);
		}
		@Override
		public String toString(MatroidS s) {
			return formatString(s, s.getBases());
		}
		@Override
		MatroidS construct(SetOfUnsignedSetInternal signedSets,MatroidAll all) {
			return  new Bases(signedSets,all);
		}
	}

	MatroidFactory(FactoryFactory f) {
		super(f, (SetOfUnsignedSetFactory)f.setsOfUnsignedSet());
	}


	static public MatroidFactory circuits(FactoryFactory f) {
		return new CFactory(f);
	}
	static public MatroidFactory bases(FactoryFactory f) {
		return new BFactory(f);
	}

	@Override
	public MatroidS copyBackingCollection(Collection<? extends UnsignedSet> c) {
		return construct(sets.copyBackingCollection(c));
	}
	@Override
	MatroidS construct(SetOfUnsignedSetInternal signedSets) {
		Collection<LabelImpl> ground = new TreeSet<LabelImpl>(signedSets.union().asCollection());
		return construct(ground, signedSets);
	}

	@Override
	MatroidS construct(Collection<? extends Label> ground, SetOfUnsignedSetInternal signedSets) {
		LabelImpl[] g = ground.toArray(new LabelImpl[0] );
		MatroidAll all = new MatroidAll(g,null,factory);
		return construct(signedSets,all);
	}
	abstract MatroidS construct(SetOfUnsignedSetInternal signedSets,MatroidAll all);

	@Override
	public MatroidS fromSets(SetOfUnsignedSet sym) {
		return construct(sets.remake(sym));
	}

	@Override
	public List<Label> ground(MatroidS s) {
		return Arrays.asList(s.ground());
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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.om;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMS;
import net.sf.oriented.omi.OMSFactory;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.set.SetOfSignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfSignedSetInternal;


abstract public class OMFactory 
   extends AbsFactory<OMS,SetOfSignedSetInternal,SetOfSignedSetFactory> 
   implements  OMSFactory {
	private static final class CFactory extends OMFactory {
		private CFactory(FactoryFactory f) {
			super(f);
		}
		@Override
		OMS construct(SetOfSignedSetInternal signedSets,OMAll all) {
			return new Circuits(signedSets,all);
		}
		@Override
		public String toString(OMS s) {
			return formatString(s, s.getCircuits());
		}
	}

	private static final class VFactory extends OMFactory {
		private VFactory(FactoryFactory f) {
			super(f);
		}
		@Override
		public String toString(OMS s) {
			return formatString(s, s.getVectors());
		}
		@Override
		OMS construct(SetOfSignedSetInternal signedSets,OMAll all) {
			return  new Vectors(signedSets,all);
		}
	}

	private static final class MVFactory extends OMFactory {
		private MVFactory(FactoryFactory f) {
			super(f);
		}	
		@Override
		OMS construct(SetOfSignedSetInternal signedSets, OMAll all) {
			return new MaxVectors(signedSets,all);
		}
		@Override
		public String toString(OMS s) {
			return formatString(s, s.getMaxVectors());
		}
	}
	OMFactory(FactoryFactory f) {
		super(f, (SetOfSignedSetFactory)f.symmetricSetsOfSignedSet());
	}


	static public OMFactory circuits(FactoryFactory f) {
		return new CFactory(f);
	}
	static public OMFactory vectors(FactoryFactory f) {
		return new VFactory(f);
	}
	static public OMFactory maxVectors(FactoryFactory f) {
		return new MVFactory(f);
	}

	@Override
	public OMS copyBackingCollection(Collection<? extends SignedSet> c) {
		return construct(sets.copyBackingCollection(c));
	}
	@Override
	OMS construct(SetOfSignedSetInternal signedSets) {
		Set<LabelImpl> ground = new TreeSet<LabelImpl>(signedSets.support().asCollection());
		return construct(ground, signedSets);
	}

	@Override
	OMS construct(Collection<? extends Label> ground, SetOfSignedSetInternal signedSets) {
		LabelImpl[] g = ground.toArray(new LabelImpl[0] );
		OMAll all = new OMAll(g,factory);
		return construct(signedSets,all);
	}
	abstract OMS construct(SetOfSignedSetInternal signedSets,OMAll all);

	@Override
	public OMS fromSignedSets(Label[] ground, SetOfSignedSet sym) {
		return construct(Arrays.asList(ground), sets.remake(sym));
	}

	@Override
	public List<Label> ground(OMS s) {
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

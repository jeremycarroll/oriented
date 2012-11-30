/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.om;


import java.util.Arrays;
import java.util.Iterator;

import net.sf.oriented.omi.Factory;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.MatroidS;
import net.sf.oriented.omi.SetOfUnsignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.set.SetFactoryInternal;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;



abstract class AbsMatroid extends AbsAxioms<UnsignedSet> implements MatroidInternal, MatroidS {

	final private MatroidAll all;
	final SetOfUnsignedSetInternal set;
//	private UnsignedSetImpl support;
	
	AbsMatroid(SetOfUnsignedSetInternal s, MatroidInternal m) {
		all = m.asAll();
		set = s;
	}
	@Override
	public MatroidAll asAll() {
		return all;
	}
	

	@Override
	public FactoryFactory ffactory() {
		return all.ffactory();
	}


	@Override
	public LabelImpl[] ground() {
		return all.ground();
	}
	@Override
	public int rank() {
		return all.rank();
	}

	@Override
	public MatroidInternal dual() {
		return all.dual();
	}

	@Override
	public Bases getBases() {
		return all.getBases();
	}

	@Override
	public MatroidCircuits getCircuits() {
		return all.getCircuits();
	}

	public UnsignedSetInternal union() {
		return set.union();
	}
	
	@Override
	public Iterator<UnsignedSetInternal> iterator() {
		return set.iterator();
	}
	
	@Override
	abstract public String toString() ;
	
	@Override
	public JavaSet<? extends UnsignedSet> asCollection() {
		return set.asCollection();
	}
	@Override
	public boolean contains(UnsignedSet a) {
		return set.contains(a);
	}
	@Override
	public SetOfUnsignedSet intersection(SetOfUnsignedSet b) {
		return set.intersection(b);
	}
	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}
	@Override
	public boolean isSubsetOf(SetOfUnsignedSet b) {
		return set.isSubsetOf(b);
	}
	@Override
	public boolean isSupersetOf(SetOfUnsignedSet b) {
		return set.isSupersetOf(b);
	}
	@Override
	public SetOfUnsignedSet minus(SetOfUnsignedSet b) {
		return set.minus(b);
	}
	@Override
	public SetOfUnsignedSet minus(UnsignedSet b) {
		return set.minus(b);
	}

	@Override
	public SetOfUnsignedSet union(UnsignedSet b) {
		return set.union(b);
	}
	@Override
	public JavaSet<? extends SetOfUnsignedSet> powerSet() {
		return set.powerSet();
	}
	@Override
	public int size() {
		return set.size();
	}
	@Override
	public JavaSet<? extends SetOfUnsignedSet> subsetsOfSize(int i) {
		return set.subsetsOfSize(i);
	}
	@Override
	public SetOfUnsignedSet union(SetOfUnsignedSet b) {
		return set.union(b);
	}
	public SetOfUnsignedSetInternal useCollection(JavaSet<UnsignedSetInternal> bases) {
		return set.useCollection(bases);
	}

	public SetFactoryInternal<UnsignedSetInternal, SetOfUnsignedSetInternal, UnsignedSet, SetOfUnsignedSet, UnsignedSetInternal, SetOfUnsignedSetInternal> factory() {
		return set.factory();
	}


	@Override
	public OMInternal getOM() {
		return all.getOM();
	}


	@Override
	public boolean equalsIsSameSetAs() {
		return false;
	}

	@Override
	public SetOfUnsignedSet respectingEquals() {
		return set;
	}

	@Override
	public boolean sameSetAs(SetOfUnsignedSet other) {
		return set.sameSetAs(other);
	}
	UnsignedSetInternal convert(LabelImpl[] impls) {
		return (UnsignedSetInternal) ffactory().unsignedSets().copyBackingCollection(Arrays.asList(impls));
	}
	
	public String toString(Factory<SetOfUnsignedSet> f) {
		return set.toString(f);
	}
	protected JavaSet<UnsignedSetInternal> emptyCollectionOf() {
		return factory().itemFactory().emptyCollectionOf();
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

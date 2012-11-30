/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.set;


import java.util.Iterator;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetOf;
import net.sf.oriented.omi.impl.items.HasFactory;



public interface SetOfInternal<E extends HasFactory<E,EX,ER>, 
       S extends SetOfInternal<E,S,EX,SX,ER,T>,
       EX,
       SX extends SetOf<EX,SX>,
       ER extends EX,
       T extends SX>
       extends HasFactory<S,SX,T>, SetOf<EX,SX> {
	

	@Override
	SetFactoryInternal<E,S,EX,SX,ER,T> factory();

	ER theMember();
	
	@Override
	boolean sameSetAs(SX other);
	
	@Override
	boolean equalsIsSameSetAs();
	
	@Override
	T respectingEquals();
	
	@Override
	public abstract T union(SX b);
	@Override
	public abstract T union(EX b);

	@Override
	public abstract T intersection(SX b);

	@Override
	public abstract T minus(SX b);
	@Override
	public abstract T minus(EX b);

	@Override
	public abstract boolean contains(EX a);

	@Override
	public abstract Iterator<ER> iterator();

	@Override
	public abstract int size();

	@Override
	public abstract boolean isSubsetOf(SX b);

	@Override
	public abstract boolean isSupersetOf(SX b);

	@Override
	public abstract JavaSet<ER> asCollection();

	@Override
	public boolean isEmpty();

	@Override
	public abstract JavaSet<T> powerSet();

	@Override
	public abstract JavaSet<T> subsetsOfSize(int i);
	
	T useCollection(JavaSet<ER> a);
	
}/************************************************************************
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

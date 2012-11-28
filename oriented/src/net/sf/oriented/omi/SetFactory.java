/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi;

import java.util.Collection;
/**
 * Provides conversion to and from String for some Set class.
 * @author jeremy
 *
 * @param <S> The sets to create.
 * @param <E> The items in such sets.
 */
public interface SetFactory<E,S> extends Factory<S> {
	/*
	 * Create a new set using a Java collection as the backing
	 * collection. The collection must not be modified after
	 * this call.
	 * @param c A collection that will be dedicated to backing this set.
	 * @return
	 */
//	 S fromBackingCollection(Collection<? extends E> c);
		/**
		 * Create a new set by copying a Java collection as the backing
		 * collection. The collection may be modified after
		 * this call, but such modifications will have no effect
		 * on the set.
		 * @param c The members of the set.
		 * @return
		 */
	 S copyBackingCollection(Collection<? extends E> c);
	 /**
	  * The empty set.
	  * @return The empty set.
	  */
	 S empty();
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

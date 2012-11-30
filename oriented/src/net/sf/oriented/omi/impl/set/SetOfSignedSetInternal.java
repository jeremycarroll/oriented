/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.set;

import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;



public interface SetOfSignedSetInternal extends SetOfInternal
< SignedSetInternal,
   SetOfSignedSetInternal,
   SignedSet,
   SetOfSignedSet,
   SignedSetInternal,
   SetOfSignedSetInternal>, SetOfSignedSet {

//	public String toString(UnsignedSet e);
//
//	public String toPlusMinus(UnsignedSet e);

//	public SetOfSignedSet deletion(final Label s);
//
//	public SetOfSignedSetI contraction(Label l);

	@Override
	public UnsignedSetInternal support();
//	public SetOfSignedSetI2 useCollection(Collection<SignedSetI> a) ;
	
	SetOfUnsignedSetInternal unsignedSets();
	/**
	 * 
	 * @param u
	 * @return those members whose support is u
	 */
	SetOfSignedSetInternal withSupport(UnsignedSetInternal u);
/**
 * 
 * @param x
 * @return those members which conform with x
 */
	public SetOfSignedSetInternal conformingWith(SignedSetInternal x);

	/**
	 * 
	 * @param x0
	 * @return the set formed by restricting each member to x0
	 */
	public SetOfSignedSetInternal restriction(UnsignedSet x0);

	// public Map<UnsignedSetInternal, SetOfSignedSetInternal> withSupport(SetOfUnsignedSetInternal vvv);

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

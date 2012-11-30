/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.set;


import java.util.Collection;

import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;



public class SetOfSignedSetFactory extends SetFactoryImpl
< SignedSetInternal,
SetOfSignedSetInternal,
SignedSet,
SetOfSignedSet,
SignedSetInternal,
SetOfSignedSetInternal> 
{
	

	
	final private SetOfUnsignedSetFactory setOfUnsignedSetFactory;
	public SetOfUnsignedSetFactory setOfUnsignedSetFactory() {
		return setOfUnsignedSetFactory;
	}

    final boolean isSymmetric;
	public SetOfSignedSetFactory(UnsignedSetFactory f, boolean symmetric) {
		super(new SignedSetFactory(f.itemFactory, f));
    	isSymmetric = symmetric;
    	setOfUnsignedSetFactory = new SetOfUnsignedSetFactory(f);
	}

	
	public SetOfSignedSetFactory(SetOfSignedSetFactory pair, boolean symmetric) {
		super(pair.itemFactory);
		isSymmetric = symmetric;
		setOfUnsignedSetFactory = pair.setOfUnsignedSetFactory;
	}


//	public SetOfSignedSetInternal fromBackingCollection(JavaSet<SignedSetInternal> c) {
//		return  construct(c,this);
//	}
	
    @Override
	protected void add(Collection<SignedSetInternal> rslt, SignedSet ee) {
		super.add(rslt,ee);
		if (isSymmetric) 
			super.add(rslt,ee.opposite());
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

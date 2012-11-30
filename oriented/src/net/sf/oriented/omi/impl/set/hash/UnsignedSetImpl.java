/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.set.hash;


import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;



public class UnsignedSetImpl extends 
    SetImpl<LabelImpl,UnsignedSetInternal,
    Label,
    UnsignedSet,
     LabelImpl,
     UnsignedSetInternal> implements UnsignedSetInternal {
	

//	public UnsignedSet(String a, UnsignedSetFactory f,boolean copy) {
//		super(f.itemFactory().parse(a),f,copy);
//	}

     public UnsignedSetImpl(JavaSet<LabelImpl> a, UnsignedSetFactory f) {
		super(a,f);
	}


//	public UnsignedSet(Collection<String> a, UnsignedSetFactory f) {
//		super(f.itemFactory().get(a),f);
//	}
	
//	public UnsignedSet(Collection<String> a, UnsignedSetFactory f, boolean copy){
//		super(f.itemFactory().get(a),f,copy);
//	}


//	public String toPlus(UnsignedSet e) {
//		// TO DO toPlus
//		return "";
//	}

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

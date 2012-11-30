/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
/**
 * Copyright 2007, Jeremy J. Carroll
 */
package net.sf.oriented.omi.impl.set.hash;


import java.util.Iterator;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOfUnsignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.Test;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;




/**
 * @author jeremy
 * 
 */
public class SetOfUnsignedSetImpl extends SetImpl
    <UnsignedSetInternal,
     SetOfUnsignedSetInternal, 
     UnsignedSet,
     SetOfUnsignedSet, 
     UnsignedSetInternal,
     SetOfUnsignedSetInternal>  
   implements SetOfUnsignedSetInternal {

	public SetOfUnsignedSetImpl(JavaSet<UnsignedSetInternal> a, SetOfUnsignedSetFactory f) {
		super(a, f);
	}
	
		

	private UnsignedSetInternal computeSupport() {
		Iterator<UnsignedSetInternal> it = iterator();
		UnsignedSetInternal r = it.next();
		while (it.hasNext()) {
			r = r.union(it.next());
		}
		return r;
	}

	
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SetOfUnsignedSetInternal#deletion(omi.Label)
	 */
	public SetOfUnsignedSetInternal deletion(final Label s) {
		return only(new Test<UnsignedSet>(){
			@Override
			public boolean test(UnsignedSet e) {
				return !e.contains(s);
			}});
	}
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SetOfUnsignedSetInternal#contraction(omi.Label)
	 */
	public SetOfUnsignedSetInternal contraction(Label l) {
		JavaSet<UnsignedSetInternal> r = emptyCollectionOf();
		Iterator<UnsignedSetInternal> it = iterator();
		while (it.hasNext()) {
			UnsignedSetInternal s = it.next();
			if (s.contains(l)) {
				r.add(s.minus(l));
				
			}
		}
		return useCollection(r);
		
	}

    private UnsignedSetInternal support;
    /* (non-Javadoc)
	 * @see omi.impl.set.hash.SetOfUnsignedSetInternal#union()
	 */
	@Override
	public UnsignedSetInternal union() {
		if (support == null)
			support = computeSupport();
		return support;
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

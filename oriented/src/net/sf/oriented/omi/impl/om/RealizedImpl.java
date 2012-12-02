/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/

package net.sf.oriented.omi.impl.om;

import java.util.Iterator;

import net.sf.oriented.omi.OMRealized;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.matrix.RationalMatrix;

public class RealizedImpl extends AbsOM  implements OMRealized {

    public RealizedImpl(OMAll all, RationalMatrix mat) {
	super(all);
    }

    @Override
    public boolean verify() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public boolean equals(Object o) {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    public Iterator<? extends SignedSet> iterator() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public RationalMatrix getMatrix() {
	// TODO Auto-generated method stub
	return null;
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
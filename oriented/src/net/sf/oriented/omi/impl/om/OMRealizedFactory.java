/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/

package net.sf.oriented.omi.impl.om;

import java.util.Collection;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMRealized;
import net.sf.oriented.omi.RationalMatrix;
import net.sf.oriented.omi.RealizedFactory;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.items.ParseContext;

public class OMRealizedFactory extends MoreAbsFactory<OMRealized, RationalMatrix> 
    implements RealizedFactory  {

    public OMRealizedFactory(FactoryFactory factory) {
	super(factory);
    }

    @Override
    public String toString(OMRealized t) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public OMRealized construct(RationalMatrix mat) {
	final int n = mat.width();
	return construct(new SimpleLabels(n), mat);
    }

    @Override
    public OMRealized construct(Collection<? extends Label> e,
	    RationalMatrix mat) {
	LabelImpl[] g = e.toArray(new LabelImpl[0]);
	OMAll all = new OMAll(g, factory);
	return new RealizedImpl(all, mat);
    }

   


    @Override
    RationalMatrix parseMatroid(ParseContext pc) {
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
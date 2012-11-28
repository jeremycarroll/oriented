/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.items;

import net.sf.oriented.omi.Label;


public class LabelImpl extends HasFactoryImpl<LabelImpl,Label,LabelImpl,LabelFactory>
    implements Label, Comparable<Label> {
    final String label;
    final private int ordinal;
    
    public LabelImpl(String a,LabelFactory f,int ord) {
    	super(f);
    	label =a;
    	ordinal = ord;
    }
    
    @Override
	public int hashCode() {
    	return label.hashCode();
    }
    @Override
	public boolean equals(Object o) {
    	LabelImpl l = (LabelImpl)o;
    	return l==this || (l != null && factory() != l.factory() && label.equals(l.label));
    	
    }

	/* (non-Javadoc)
	 * @see omi.LabelX#label()
	 */
	@Override
	public String label() {
		return label;
	}

	@Override
	public int compareTo(Label arg0) {
		return label.compareTo(arg0.label());
	}

	public int ordinal() {
		return ordinal;
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

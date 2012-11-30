/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/

package net.sf.oriented.omi.impl.set;

import net.sf.oriented.omi.SetOf;
import net.sf.oriented.omi.impl.items.FactoryInternal;
import net.sf.oriented.omi.impl.items.HasFactory;
import net.sf.oriented.omi.impl.items.HasFactoryImpl;

public class HasSetFactoryImpl<E extends HasFactory<E,EX,ER>, 
S extends SetOfInternal<E,S,EX,SX,EF,SF,ER,SS>,
EX,
SX extends SetOf<EX,SX>,
EF extends FactoryInternal<E,EX,ER>,
SF extends SetFactoryInternal<E,S,EX,SX,EF,SF,ER,SS>,
ER extends EX,
SS extends SX >  extends HasFactoryImpl<S,SX,SS>{

	protected HasSetFactoryImpl(FactoryInternal<S, SX, SS> f) {
		super(f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SetFactoryInternal<E,S,EX,SX,EF,SF,ER,SS> factory() {
			return (SetFactoryInternal<E, S, EX, SX, EF, SF, ER, SS>) super.factory();
		}
	//	private final JavaSet<ER> members;

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
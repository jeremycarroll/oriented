/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.items;

import java.util.List;

import net.sf.oriented.omi.Factory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.Options;

/**
 * 
 * @author Jeremy J. Carroll
 *
 * @param <E>   Is the intrinsic type of thing made by this factory
 * @param <EX>  Is the extrinsic type,
 * @param <ER>  Is the type used in return statements in this factory
 * @param <F>
 */
public interface FactoryInternal<
  E extends HasFactory<E,EX,ER>,EX,
  ER extends EX> 
 extends Factory<EX>
{
    ER parse(ParseContext pc);
    Options getOptions();

	E remake(EX t);
	@Override
	JavaSet<ER> emptyCollectionOf();

	String toString(List<? extends Label> u, EX t);
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

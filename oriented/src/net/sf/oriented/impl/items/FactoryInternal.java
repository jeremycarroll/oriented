/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.items;

import java.util.List;

import net.sf.oriented.omi.Factory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.Options;

/**
 * 
 * @author Jeremy J. Carroll
 * 
 * @param <ITEM_INTERNAL>
 *            Is the intrinsic type of thing made by this factory
 * @param <ITEM>
 *            Is the extrinsic type,
 * @param <ITEM_INTERNAL2>
 *            Is the type used in return statements in this factory
 * @param <F>
 * @formatter:off
 */
public interface FactoryInternal<
      ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
      ITEM, 
      ITEM_INTERNAL2 extends ITEM>
		extends Factory<ITEM> {
//@formatter:on
	ITEM_INTERNAL2 parse(ParseContext pc);

	Options getOptions();

//	ITEM_INTERNAL remake(ITEM t);

	@Override
	JavaSet<ITEM_INTERNAL2> emptyCollectionOf();

	String toString(List<? extends Label> u, ITEM t);

//    ITEM_INTERNAL2 remake(ITEM t);

    <U extends ITEM> U remake(ITEM t);
}
/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * 
 * 
 * 
 * 
 * 
 * The Java Oriented Matroid Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Java Oriented Matroid Library. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

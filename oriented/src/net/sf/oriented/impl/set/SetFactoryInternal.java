/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.set;

import java.util.Collection;
import java.util.List;

import net.sf.oriented.impl.items.FactoryInternal;
import net.sf.oriented.impl.items.HasFactory;
import net.sf.oriented.impl.items.ParseContext;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetFactory;
import net.sf.oriented.omi.SetOf;

//@formatter:off
public interface SetFactoryInternal<
        ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
        SET_INTERNAL extends SetOfInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2>, 
        ITEM, 
        SET extends SetOf<ITEM, SET>, 
        ITEM_INTERNAL2 extends ITEM, 
        SET_INTERNAL2 extends SET>
		extends FactoryInternal<SET_INTERNAL, SET, SET_INTERNAL2>, SetFactory<ITEM, SET> {
//@formatter:on
	/**
	 * bases is dedicated to being the backing collection of the newly
	 * constructed set. It is not copied and must not be modified after this
	 * call.
	 * 
	 * @param bases
	 * @return
	 */
	SET_INTERNAL2 fromBackingCollection(JavaSet<ITEM_INTERNAL2> bases);

	@Override
	SET_INTERNAL2 copyBackingCollection(Iterable<? extends ITEM> c);

	FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> itemFactory();

	@Override
	SET_INTERNAL2 empty();

	List<ITEM_INTERNAL2> orderedParse(ParseContext pc);
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

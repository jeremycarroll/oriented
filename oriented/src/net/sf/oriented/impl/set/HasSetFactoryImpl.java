/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.impl.set;

import net.sf.oriented.impl.items.FactoryInternal;
import net.sf.oriented.impl.items.HasFactory;
import net.sf.oriented.impl.items.HasFactoryImpl;
import net.sf.oriented.omi.SetOf;

/**
 * Things that have factories that build sets.
 * @author jeremycarroll
 *
 * @param <ITEM_INTERNAL> The internal API or implementation for members.
 * @param <SET_INTERNAL> The internal API or implementation for sets.
 * @param <ITEM> The external API for members.
 * @param <SET> The external API for sets.
 * @param <ITEM_INTERNAL2> See {@link net.sf.oriented.impl.util.TypeChecker}
 * @param <SET_INTERNAL2> See {@link net.sf.oriented.impl.util.TypeChecker}
 */
//@formatter:off
public class HasSetFactoryImpl<
        ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
        SET_INTERNAL extends SetOfInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2>, 
        ITEM, 
        SET extends SetOf<ITEM, SET>, 
        ITEM_INTERNAL2 extends ITEM, 
        SET_INTERNAL2 extends SET>
		extends HasFactoryImpl<SET_INTERNAL, SET, SET_INTERNAL2> 
{
//@formatter:on
	protected HasSetFactoryImpl(FactoryInternal<SET_INTERNAL, SET, SET_INTERNAL2> f) {
		super(f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SetFactoryInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2> factory() {
		return (SetFactoryInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2>) super.factory();
	}
	// private final JavaSet<ER> members;

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

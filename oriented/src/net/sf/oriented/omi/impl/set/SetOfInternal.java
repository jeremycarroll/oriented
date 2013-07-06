/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.set;

import java.util.Iterator;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetOf;
import net.sf.oriented.omi.impl.items.HasFactory;
/**
 * Sets of things, with both an internal API for efficient implementation
 * and an external API for allowing switching implementations.
 * To tell whether two sets have the same implementation or not
 * one needs to check that their two factories are equal.
 * 
 * @author jeremycarroll
 *
 *
 * @param <ITEM_INTERNAL> The internal API or implementation for members.
 * @param <SET_INTERNAL> The internal API or implementation for sets.
 * @param <ITEM> The external API for members.
 * @param <SET> The external API for sets.
 * @param <ITEM_INTERNAL2> See {@link net.sf.oriented.util.TypeChecker}
 * @param <SET_INTERNAL2> See {@link net.sf.oriented.util.TypeChecker}
 */
//@formatter:off
public interface SetOfInternal<
            ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
            SET_INTERNAL extends SetOfInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2>, 
            ITEM, 
            SET extends SetOf<ITEM, SET>, 
            ITEM_INTERNAL2 extends ITEM, 
            SET_INTERNAL2 extends SET>
		extends HasFactory<SET_INTERNAL, SET, SET_INTERNAL2>, 
		        SetOf<ITEM, SET>, 
		        Iterable<ITEM_INTERNAL2> {
//@formatter:on

	@Override
	SetFactoryInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2> factory();

	ITEM_INTERNAL2 theMember();

	@Override
	boolean sameSetAs(SET other);

	@Override
	boolean equalsIsSameSetAs();

	@Override
	SET_INTERNAL2 respectingEquals();

	@Override
	public abstract SET_INTERNAL2 union(SET b);

	@Override
	public abstract SET_INTERNAL2 union(ITEM b);

	@Override
	public abstract SET_INTERNAL2 intersection(SET b);

	@Override
	public abstract SET_INTERNAL2 minus(SET b);

	@Override
	public abstract SET_INTERNAL2 minus(ITEM b);

	@Override
	public abstract boolean contains(ITEM a);

	@Override
	public abstract Iterator<ITEM_INTERNAL2> iterator();

	@Override
	public abstract int size();

	@Override
	public abstract boolean isSubsetOf(SET b);

	@Override
	public abstract boolean isSupersetOf(SET b);

	@Override
	public abstract JavaSet<ITEM_INTERNAL2> asCollection();

	@Override
	public boolean isEmpty();

	@Override
	public abstract JavaSet<SET_INTERNAL2> powerSet();

	@Override
	public abstract JavaSet<SET_INTERNAL2> subsetsOfSize(int i);

	SET_INTERNAL2 useCollection(JavaSet<ITEM_INTERNAL2> a);

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

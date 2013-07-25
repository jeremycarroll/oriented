/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Collection;

/**
 * Provides conversion to and from String for some Set class.
 * 
 * @author jeremy
 * 
 * @param <SET>
 *            The sets to create; this does extend {@link SetOf}<code>&lt;SET,LINK&gt;</code>, but this cannot
 *            be stated in Java generics. This means that the results from the methods
 *            in this interface are immutable.
 * @param <ITEM>
 *            The items in such sets.
 */
public interface SetFactory<ITEM, SET> extends Factory<SET> {
	/**
	 * Create an immutable new set by copying a Java collection as the backing collection.
	 * Later modifications made to c after this call will have no effect on the 
	 * returned set.
	 * 
	 * @param c The members of the set.
	 * @return A new set
	 */
	SET copyBackingCollection(Collection<? extends ITEM> c);

	/**
	 * The (immutable) empty set.
	 * 
	 * @return The empty set.
	 */
	SET empty();
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

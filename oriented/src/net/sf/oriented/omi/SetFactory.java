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
 *            The sets to create.
 * @param <ITEM>
 *            The items in such sets.
 */
public interface SetFactory<ITEM, SET> extends Factory<SET> {
	/**
	 * Create a new set by copying a Java collection as the backing collection.
	 * The collection may be modified after this call, but such modifications
	 * will have no effect on the set.
	 * 
	 * @param c
	 *            The members of the set.
	 * @return
	 */
	SET copyBackingCollection(Collection<? extends ITEM> c);

	/**
	 * The empty set.
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

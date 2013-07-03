/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Iterator;

public interface SetOf<ITEM, SET extends SetOf<ITEM, SET>> {
	public abstract SET union(SET b);

	boolean sameSetAs(SET other);

	boolean equalsIsSameSetAs();

	SET respectingEquals();

	public abstract SET union(ITEM b);

	public abstract SET intersection(SET b);

	public abstract SET minus(SET b);

	public abstract SET minus(ITEM b);

	public abstract boolean contains(ITEM a);

	public abstract int size();

	public abstract boolean isSubsetOf(SET b);

	public abstract boolean isSupersetOf(SET b);

	public boolean isEmpty();

	public abstract Iterator<? extends ITEM> iterator();

	public abstract JavaSet<? extends ITEM> asCollection();

	public abstract JavaSet<? extends SET> powerSet();

	public abstract JavaSet<? extends SET> subsetsOfSize(int i);

	// S useCollection(Collection<? extends E> a);

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

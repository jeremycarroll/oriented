/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Iterator;

public interface SetOf<E, S extends SetOf<E, S>> {
	public abstract S union(S b);

	boolean sameSetAs(S other);

	boolean equalsIsSameSetAs();

	S respectingEquals();

	public abstract S union(E b);

	public abstract S intersection(S b);

	public abstract S minus(S b);

	public abstract S minus(E b);

	public abstract boolean contains(E a);

	public abstract int size();

	public abstract boolean isSubsetOf(S b);

	public abstract boolean isSupersetOf(S b);

	public boolean isEmpty();

	public abstract Iterator<? extends E> iterator();

	public abstract JavaSet<? extends E> asCollection();

	public abstract JavaSet<? extends S> powerSet();

	public abstract JavaSet<? extends S> subsetsOfSize(int i);

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

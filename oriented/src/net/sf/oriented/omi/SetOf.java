/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Iterator;
import java.util.Set;

public interface SetOf<ITEM, SET extends SetOf<ITEM, SET>> 
       extends Verify {
	public abstract SET union(SET b);

	/**
	 * This is mathematically set equality, except
	 * for empty sets - which respect type as well ... i.e. two empty sets
	 * that can never be modified by adding the same member to each are different.
	 * @param other
	 * @return
	 */
	boolean sameSetAs(SET other);

	/**
	 * This indicates whether in the implementing class
	 * the notion of equality is set equality or not.
	 * For example, some classes that implement this interface
	 * may also implement {@link #OM} which defines equality as
	 * Oriented Matroid equality which is not the same as set equality.
	 * @return
	 */
	boolean equalsIsSameSetAs();
	
    /**
     * Returns the hash code value for this set.  
     * If {@link #equalsIsSameSetAs()} is true, then the 
     * hash code of a set is defined to be the sum of the hash codes of the elements in the set,
     * see {@link java.util.Set#hashCode()}.
     * If {@link #equalsIsSameSetAs()} is false, then the 
     * hash code of a set is not restricted except to the general contract,
     * see {@link java.lang.Object#hashCode()}.
     *
     * @return the hash code value for this set
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    @Override
    int hashCode();

    /**
     * Return a set <code>s</code> such that:
     * <code>s.sameSetSetAs(this) && s.equalsIsSameSetAs()</code>.
     * If  {@link #equalsIsSameSetAs()} is true, then this method
     * should usually return <code>this</code>.
     * If {@link #equalsIsSameSetAs()} is false, then a different
     * SET containing the same members is returned.
     * @return
     */
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


    /**
     * Check invariants of the object, in particular
     * check that {@link #hashCode()} satisfies its contract.
     * 
     * @return true if the object is not known to have violated its invariants.
     */
    @Override
    void verify() throws AxiomViolation;

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

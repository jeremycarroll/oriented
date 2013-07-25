/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Iterator;
import java.util.Set;

/**
 * Immutable mathematical sets. Supports the standard mathematical
 * operations of: {@link #union(SetOf)}; {@link #intersection(SetOf)}; set difference - {@link #minus(SetOf)};
 * {@link #isSubsetOf(SetOf)}; {@link #isEmpty()}; membership {@link #contains(Object)}.
 * Also supports operation which relate to java sets such as {@link #iterator()}, {@link #asCollection()}.
 * The two operations to produce sets of subsets produce java sets, these are {@link #powerSet()} and {@link #subsetsOfSize(int)}.
 * 
 * {@link #sameSetAs(SetOf)}
 * implements mathematical set equality,
 * and
 *  {@link java.lang.Object#equals(Object)} may be mathematical set equality or not. This is indicated by {@link #equalsIsSameSetAs()}
 * @author jeremycarroll
 *
 * @param <ITEM>
 * @param <SET>
 */
public interface SetOf<ITEM, SET extends SetOf<ITEM, SET>> 
       extends Verify {
	public abstract SET union(SET b);

	/**
	 * This is mathematically set equality, except
	 * for empty sets - which respect type as well. (This is not very well-defined).
	 * @param other
	 * @return true if the two sets have the same members.
	 */
	boolean sameSetAs(SET other);

	/**
	 * This indicates whether in the implementing class
	 * the notion of equality is set equality or not.
	 * For example, some classes that implement this interface
	 * may also implement {@link OM} which defines equality as
	 * Oriented Matroid equality which is not the same as set equality.
	 * @return true if {@link java.lang.Object#equals(Object)} and {@link #sameSetAs(SetOf)} return identical results. 
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
     * @return a set with the same members.
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
     * check that {@link java.lang.Object#hashCode()} satisfies its contract.
     * 
     * @throws AxiomViolation if the set violates its contract.
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

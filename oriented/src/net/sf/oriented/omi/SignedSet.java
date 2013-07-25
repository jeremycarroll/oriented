/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * A signed set is
 * defined by disjoint positive {@link #plus()} and negative {@link #minus()} components, 
 * both of which are {@link UnsignedSet}s.
 * A signed set can be seen as a partial map from the universe of elements
 * to a sign <code>{ &minus;, + }</code>, see {@link #sign(Label)}.
 * These are a key concept in the Oriented Matroid literature.
 * These are immutable.
 * 
 * 
 * @see Bibliography#björnerEtAl1999
 * @author jeremycarroll
 *
 */
public interface SignedSet {

    /**
     * Swaps the positive and negative components
     * @return The opposite signed set
     */
	public abstract SignedSet opposite();

	/**
	 * The positive component.
	 * @return The positive component.
	 */
	public abstract UnsignedSet plus();

	/**
	 * The negative component.
	 * @return The negative component.
	 */
	public abstract UnsignedSet minus();

	/**
	 * Either s is equal to this, or to the opposite of this.
	 * This is short hand for <code>equals(s) || equalsOpposite(s)</code>.
	 * @param s The signed set to compare with.
	 * @return true if this is equal to s or its opposite.
	 */
	public abstract boolean equalsIgnoreSign(SignedSet s);

    /**
     * This is a short hand for <code>s.equals(opposite())</code>.
     * @param s The signed set to compare with
     * @return true if this is equal to the opposite of s
     */
	public abstract boolean equalsOpposite(SignedSet s);

	/**
	 * The set of elements that have different sign in this and b.
	 * @param b The signed set to compare with
	 * @return The unsigned set of elements differing in sign
	 */
	public abstract UnsignedSet separation(SignedSet b);

	/**
	 * Signed set composition.
	 * The composition takes the sign of each element
	 * from this, and if none, then from <code>b</code>
	 * @param b The signed set to compose with
	 * @return The composition of this with b.
	 */
	public abstract SignedSet compose(SignedSet b);

	/**
	 * The number of elements which have a sign in this signed set.
	 * @return The number of elements which have a sign in this signed set.
	 */
	public abstract int size();
	/**
     * Does this conform with <code>x</code>?
     * i.e. is the positive part of this disjoint with the negative part of <code>x</code>,
     * and the negative part of this disjoint with the positive part of <code>x</code>,
     * @param x The signed set to compare with.
     * @return true if this conforms with <code>x</code>.
     * @see #isRestrictionOf(SignedSet)
     */
	public abstract boolean conformsWith(SignedSet x);

	/**
	 * The union of the positive and negative components.
	 * @return  The union of the positive and negative components.
	 */
	public abstract UnsignedSet support();

	/**
	 * Returns 1, -1 or 0 depending on whether e is in the positive,
	 * the negative or neither component respectively.
	 * @param e the label to look up
	 * @return the sign of e in this signed set.
	 */
	public abstract int sign(Label e);

	/**
	 * Is this a restriction of <code>x</code>?
	 * i.e. is the positive part of this a subset of the positive part of <code>x</code>,
	 * and the negative part of this a subset of the negative part of <code>x</code>,
	 * @param x The signed set to compare with.
	 * @return true if this is a restriction of x.
	 * @see #conformsWith(SignedSet)
	 */
	public abstract boolean isRestrictionOf(SignedSet x);

	/**
	 * The restriction of this to <code>x</code>.
	 * We retain only the positive and negative elements
	 * that are in x.
	 * @param x The unsigned set to intersect with.
	 * @return The restriction of this to <code>x</code>
	 */
	public abstract SignedSet restriction(UnsignedSet x);

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

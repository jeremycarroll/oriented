/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import net.sf.oriented.util.combinatorics.Group;
import net.sf.oriented.util.combinatorics.Permutation;

import com.google.common.base.Function;

/**
 * An Oriented Matroid.
 * This interface represents operations on and between oriented matroids
 * independently of the cryptomorphic representations available.
 * These are accessor methods to get a representation specific view of the oriented
 * matroid. For example, to get the circuits of an oriented matroid use
 * {@link #getCircuits()}. This returns an oriented matroid that is equal to this one,
 * and also implements the {@link SetOfSignedSet} interface giving the circuits.
 * Each of five cryptomorphisms is similarly implemented.
 * <p>
 * Many methods return an {@link OM}. In practice it might be found
 * that the returned {@link OM} is always the circuit representation:
 * this is not part of the contract, and should not be relied on - call {@link #getCircuits()}
 * if that is the representation you need.
 * @author jeremycarroll
 *
 */
public interface OM extends Verify {

    /**
     * Get the chirotope representation of the oriented matroid.
     * <code>equals(getChirotope())</code> is true.
     * @return The chirotope representation.
     */
	OMasChirotope getChirotope();

    /**
     * Get the circuit representation of the oriented matroid.
     * <code>equals(getCircuits())</code> is true.
     * @return The circuit representation.
     */
	OMasSignedSet getCircuits();
	 /**
     * Get the vector representation of the oriented matroid.
     * <code>equals(getVectors())</code> is true.
     * @return The vector representation.
     */
	OMasSignedSet getVectors();
	/**
     * Get the maximum vector representation of the oriented matroid.
     * <code>equals(getMaxVectors())</code> is true.
     * @return The maximum vector representation.
     */
	OMasSignedSet getMaxVectors();
    /**
     * Get the realized representation of the oriented matroid.
     * <code>equals(getRealized())</code> is true.
     * This is (currently) only implemented for oriented matroids
     * that were initially created from matrices; i.e. the original realization
     * of this oriented matroid or its dual is remembered.
     * <p>
     * The principal goal of the <a href="http://oriented.sf.net">Java Oriented Matroid</a>
     * project is to improve the implementation of this method.
     * </p>
     * @return The remembered realization
     * @see Bibliography#shor1991
     * @throws UnsupportedOperationException Realization not implemented in most cases.
     */

	OMasRealized getRealized();

	/**
	 * The dual oriented matroid.
	 * @return The dual oriented matroid.
	 */
	public OM dual();
	
	/**
	 * Reorients (i.e. swaps the signs in circuits) of the given set.
	 * @param reorientationSet The elements to reorient
	 * @return a reoriented oriented matroid
	 */
	public OM reorient(Label ... reorientationSet);

	/**
	 * The elements on which this oriented matroid is defined.
	 * The order is significant with respect to some representations (e.g. {@link #getChirotope()}
	 * and {@link #getRealized()}
	 * @return The ordered elements on which this oriented matroid is defined.
	 */
	public Label[] elements(); 
	

    /**
     * This is the same as {@link #elements()}, except it is unordered.
     * @return The unordered elements on which this oriented matroid is defined.
     */
    public UnsignedSet setOfElements();

    /**
     * The underlying matroid.
     * @return The underlying matroid.
     */
	public Matroid getMatroid();

	/**
	 * The rank of this oriented matroid.
	 * @return The rank of this oriented matroid.
	 */
	int rank();

	/**
	 * The hashCode of an Oriented Matroid is the hashCode of the circuit
	 * representation as a {@link SetOfSignedSet}s.
	 * 
	 * @return the hashCode
	 */
	@Override
	int hashCode();

	/**
	 * An Oriented Matroid is equal to any other object implementing this
	 * interface, which represents the same underlying oriented matroid,
	 * including the {@link #elements()} being equal.
	 * 
	 * @param om
	 * @return true if these both represent the same Oriented Matroid
	 */
	@Override
	boolean equals(Object om);

	/**
	 * The integer corresponding to this element (counting from 0).
	 * @param e  An element in {@link #elements()}
	 * @return The index of <code>e</code> in {@link #elements()}
	 */
	int asInt(Label e);
    /**
     * The integers corresponding to these elements (counting from 0).
     * @param e  Elements in {@link #elements()}
     * @return The corresponding indexes of <code>e</code> in {@link #elements()}
     */
	<T extends Label> int[] asInt(T[] e);

	/**
	 * Gets the indexes of each element in <code>u</code>
	 * @param u a subset of {@link #setOfElements()}
	 * @return The increasing corresponding indexes
	 */
	int[] asInt(UnsignedSet u);

	/**
	 *  This returns true if the oriented matroid contains
	 *  no positive circuit, or equivalently, every edge is
	 *  in a positive cocircuit.
	 */
    boolean isAcyclic();
    
    /**
     * Permute the ground set.
     * Returns an oriented matroid  <code>om</code> s.t.
     * <code>om.equals(this)</code>
     * and <code>Arrays.equals(p.permute(this.ground()),om.ground()</code> are the same.
     * @param p The permutation to apply.
     * @return An equal oriented matroid with permuted ground set
     */
    OM permuteGround(Permutation p);
    /**
     * Permute the Oriented Matroid,
     * the ground set is not permuted.
     * So in general, <code>this.equals(this.permute(p))</code> is false.
     * @param p The permutation to apply.
     * @return An oriented matroid after the action of the permutation
     */
    OM permute(Permutation p);

    /**
     * The size of {@link #elements()}
     * @return The size of {@link #elements()}.
     */
    int n();
    
    /**
     * The automorphism group of this oriented matroid.
     * For each permutation <code>π &in; automorphisms()</code>
     * we have <code>this.equals(this.permute(π))</code>
     * 
     * @return The automorphism group of this oriented matroid.
     */
    Group automorphisms();

    /**
     * Convert a permutation of the {@link #elements()} into
     * a function that will map a signed set to the permuted signed set.
     * @param p This is a permutation of {@link #elements()}
     * @return A function that invokes the action of the permutation on signed sets
     */
    Function<SignedSet, SignedSet> signedSetPermuter(Permutation p);

    int asInt(String label);

    FactoryFactory ffactory();


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

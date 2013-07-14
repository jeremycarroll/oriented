/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import net.sf.oriented.combinatorics.Group;
import net.sf.oriented.combinatorics.Permutation;

import com.google.common.base.Function;

public interface OM extends Verify {

	OMChirotope getChirotope();

	OMS getCircuits();

	OMS getVectors();

	OMS getMaxVectors();

	OMRealized getRealized();

	public OM dual();
	
	public OM reorient(Label ... axes);

	public Label[] ground();

	public Matroid getMatroid();

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
	 * including the {@link #ground} being equal.
	 * 
	 * @param om
	 * @return true if these both represent the same Oriented Matroid
	 */
	@Override
	boolean equals(Object om);

	int asInt(Label l);

	<T extends Label> int[] asInt(T[] l);

	/**
	 * 
	 * @param u
	 * @return Sorted array of indexes.
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
     * Returns and om s.t.
     * <code>om.equals(this)</code>
     * and <code>p.permute(ground())</code>
     * and <code>om.ground()</code> are the same.
     * @param p The permutation to apply.
     * @return
     */
    OM permuteGround(Permutation p);
    /**
     * Permute the Oriented Matroid,
     * the ground set is not permuted.
     * So in general, <code>this.equals(this.permute(p))</code> is false.
     * @param p The permutation to apply.
     * @return
     */
    OM permute(Permutation p);

    /**
     * The size of {@link #ground()}
     * @return
     */
    int n();
    
    Group automorphisms();

    Function<SignedSet, SignedSet> signedSetPermuter(Permutation p);

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

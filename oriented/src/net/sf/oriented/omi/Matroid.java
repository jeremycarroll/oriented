/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import net.sf.oriented.util.combinatorics.Group;

/**
 * A minimal matroid interface, not intended as a useful part of this 
 * library.
 * Matroid's are only supported in as much as some processing
 * of oriented matroids requires reference to the underlying matroid, no other usage is 
 * supported.
 * Like {@link OM} multiple cryptomorphisms are supported, but actually only two:
 * {@link #getBases()} and {@link #getCircuits()}.
 * @author jeremycarroll
 *
 */
public interface Matroid extends Verify {

    /**
     * A view of the matroid as a set of circuits.
     * @return  A view of the matroid as a set of circuits.
     */
	public MatroidAsSet getCircuits();

	/**
     * A view of the matroid as a set of bases.
     * @return  A view of the matroid as a set of bases.
     */
	public MatroidAsSet getBases();
	

    public MatroidAsSet  getIndependentSets();

	/**
	 *  The dual of the matroid.
	 * @return The dual of the matroid.
	 */
	public Matroid dual();

	/**
	 * The rank of the matroid.
	 * @return The rank of the matroid.
	 */
	public int rank();

    /**
     * The elements on which this matroid is defined.
     * @return The elements on which this matroid is defined.
     */
	public Label[] elements();

	
    /**
     * This is the same as {@link #elements()}, except it is unordered.
     * @return The set of elements on which this matroid is defined.
     */
    public UnsignedSet setOfElements();

    /**
     * The automorphism group for this matroid.
     * (Yes this would be more useful if you could permute matroids, but no that is not supported).
     * @return The automorphism group for this matroid.
     */
    Group automorphisms();


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

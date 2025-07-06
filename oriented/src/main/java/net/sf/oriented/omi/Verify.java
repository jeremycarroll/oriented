/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * This interface is implemented by classes
 * which represent mathematical concepts that have certain
 * axioms, and/or by java classes that have invariants.
 * In normal operation, it is assumed that these axioms and invariants hold; but
 * programming errors, or poor input, may result in an object that does not satisfy
 * such assumptions.
 * Calling {@link #verify()} on such an object asks it to check all axioms
 * and invariants.
 * 
 * @author jeremycarroll
 *
 */
public interface Verify {
	/**
	 * Check invariants of the object.
	 * Calling code should not assume that this operation is quick.
	 * 
	 * @throws AxiomViolation If an invariant did not hold.
	 */
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

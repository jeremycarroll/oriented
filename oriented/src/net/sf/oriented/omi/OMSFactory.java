/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import net.sf.oriented.impl.om.OMSInternal;

public interface OMSFactory extends SetFactory<SignedSet, OMasSignedSet> {
	/**
	 * This does not, and will not, work. This is inherited from the
	 * {@link SetFactory} interface, and is not appropriate for Oriented
	 * Matroids.
	 * 
	 * @return Never.
	 * @throws UnsupportedOperationException
	 *             Always.
	 */
	@Override
	OMasSignedSet empty();

	/**
	 * 
	 * @param ground
	 *            The ground set from which the symmetric sets are taken.
	 * @param sym
	 *            A symmetric set of signed sets, satisfying all the relevant
	 *            axioms.
	 * @return A new Oriented Matroid, based on the signed sets.
	 */
	OMSInternal fromSignedSets(Label[] ground, SetOfSignedSet sym);

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

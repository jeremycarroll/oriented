/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * Convert a set of sets of elements into a matroid.
 * @see FactoryFactory#bases()
 * @see FactoryFactory#unsignedCircuits()
 * @author jeremycarroll
 *
 */
public interface MatroidFactory extends SetFactory<UnsignedSet, MatroidAsSet> {
	/**
	 * This does not, and will not, work. This is inherited from the
	 * {@link SetFactory} interface, and is not appropriate for Matroids.
	 * 
	 * @return Never.
	 * @throws UnsupportedOperationException
	 *             Always.
	 */
	@Override
	MatroidAsSet empty();

	/**
	 * 
	 * @param sym
	 *            A set of sets, satisfying all the relevant axioms.
	 * @return A new Matroid, based on the sets.
	 */
	MatroidAsSet fromSets(SetOfUnsignedSet sym);

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

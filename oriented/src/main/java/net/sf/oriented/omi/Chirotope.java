/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * The base interface for implementing new chirotope sources.
 * Typically, clients should be looking for {@link FullChirotope}
 * where the contract includes the alternating map.
 * This interface <em>does not</em> provide for an alternating map,
 * and it also counts from 0 rather than from 1.
 * @author jeremycarroll
 *
 */
public interface Chirotope {
	/**
	 * This is only called when it has {@link #rank()} arguments, and they are
	 * strictly monotonic increasing. Implementations may return correct values
	 * for arguments which are not strictly monotonic increasing but are not
	 * required to.
	 * 
	 * @param i an array of {@link #rank()} integers, between 0 and {@link #n()}-1.
	 * @return -1, 0 or 1
	 * @throws IllegalArgumentException
	 *             If arguments are not strictly monotonic increasing, or the
	 *             wrong number, or out of range.
	 * @see FullChirotope#chi(int...) which is not as restricted.
	 */
	int chi(int... i);

	/**
	 * Must be a positive integer, which is constant for each object
	 * implementing this interface.
	 * 
	 * @return The rank associated with this chirotope.
	 */
	int rank();

	/**
	 * The number of elements on which this Chirotope is defined
	 * @return The number of elements on which this Chirotope is defined
	 */
	int n();
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

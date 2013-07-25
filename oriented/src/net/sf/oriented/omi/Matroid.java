/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import net.sf.oriented.util.combinatorics.Group;

public interface Matroid extends Verify {

	public MatroidAsSet getCircuits();

	public MatroidAsSet getBases();

	public Matroid dual();

	public int rank();

	public Label[] elements();

	
    /**
     * This is the same as {@link #elements()}, except it is unordered.
     * @return
     */
    public UnsignedSet setOfElements();

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

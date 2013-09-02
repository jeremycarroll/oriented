/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Collection;
import java.util.Iterator;

/**
 * The face lattice corresponding to an oriented matroid.
 * Each face (except for the {@link #top()} face), is labelled
 * with a covector from the oriented matroid.
 * The lattice partial order is defined by {@link SignedSet#isRestrictionOf(SignedSet)}.
 * @author jeremycarroll
 *
 */
public interface FaceLattice extends Verify {
    /**
     * The (artificial) top face of the lattice.
     * @return the top face.
     */
    Face top();
    /**
     * The bottom face of the lattice, labelled with the empty signed set.
     * @return the bottom face.
     */
    Face bottom();
    /**
     * The grade of the face lattice with faces of a given dimension.
     * The face lattice is graded, meaning that {@link #bottom()} has dimension
     * -1, and {@link #top()} has dimension {@link OM#rank()}, every other face has dimension
     * between these values, and the face lattice forms layers of each dimension.
     * @param d
     * @return the faces with the given dimension.
     */
    Collection<? extends Face> withDimension(int d);
    /**
     * The faces in the lattice.
     * @return An iterator over all the faces.
     */
    Iterator<Face> iterator();
    /**
     * An iterable of the grades of the lattice between lower and upper inclusive.
     * @param lower The inclusive lower bound of the dimension of the returned faces.
     * @param upper The inclusive upper bound of the dimension of the returned faces.
     * @return An iterable over all the faces with given dimensions.
     */
    Iterable<Face> withDimensions(int lower, int upper);
    /**
     * The face labeled with the given covector.
     * @param covector The label of the face.
     * @return The corresponding face or null if the argument is not a covector.
     */
    Face get(SignedSet covector);
}


/************************************************************************
    This file is part of the Java Oriented Matroid Library.  

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Collection;

/**
 * A face in a {@link FaceLattice}
 * @author jeremycarroll
 *
 */
public interface Face extends Verify {
    /**
     * The types of faces in a {@link FaceLattice}
     * @author jeremycarroll
     *
     */
    enum Type {
        /**
         * The (artificial) bottom element of the lattice,
         * corresponding to the empty covector.
         */
        Bottom,
        /**
         * The first layer of the lattice, corresponding to points
         * in the pseudosphere construction, or cocircuits of the oriented matroid.
         */
        Cocircuit,
        /**
         * Normal faces, that are not {@link #Cocircuit}s or {@link #Tope}s.
         */
        Covector,
        /**
         * The top most (real) faces of lattice, corresponding to the topes of the construction.
         */
        Tope,
        /**
         * The artificial top element of the face lattice.
         */
        Top;
    }
    /**
     * 
     * @return The type, i.e. the position of the face in the lattice.ß
     */
    Type type();
    /**
     * The covector labeling the face in the lattice (null for the {@link Type#Top} element)
     * @return The covector labeling this face.
     */
    SignedSet covector();
    /**
     * 
     * @return The dimension of this face, -1 for {@link Type#Bottom}, 0 for points, etc.
     */
    int dimension();
    /**
     * 
     * @return The faces one dimension higher than this face.
     */
    Collection<? extends Face> higher();
    /**
     * 
     * @return The faces one dimension lower than this face.
     */
    Collection<? extends Face> lower();
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

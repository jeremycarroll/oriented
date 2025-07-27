/************************************************************************
 (c) Copyright 2025 Jeremy J. Carroll
 
************************************************************************/
package net.sf.oriented.omi;

/**
 * Interface for providing face lattice functionality for oriented matroids.
 * Unlike the cryptomorphisms (circuits, vectors, etc.), the face lattice is 
 * a derived structure rather than a true cryptomorphism. This interface 
 * separates the face lattice functionality from the core cryptomorphic 
 * representations.
 * 
 * @author jeremycarroll
 */
public interface FaceLatticeProvider {
    
    /**
     * Get the face lattice representation of the oriented matroid.
     * Note that in some sense this is a property of the dual. The labels
     * on the faces of the return face lattice are covectors.
     * 
     * @return The face lattice representation of this oriented matroid.
     */
    OMasFaceLattice getFaceLattice();
    
    /**
     * Get the dual face lattice representation of the oriented matroid.
     * 
     * @return The face lattice representation of the dual oriented matroid.
     */
    OMasFaceLattice getDualFaceLattice();
    
    /**
     * Checks if the face lattice has been computed yet.
     * 
     * @return true if the face lattice is already computed and cached.
     */
    boolean hasFaceLattice();
    
    /**
     * Checks if the dual face lattice has been computed yet.
     * 
     * @return true if the dual face lattice is already computed and cached.
     */
    boolean hasDualFaceLattice();
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
/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi;


/**
 * The face lattice view of an oriented matroid.
 * Note, that from a computational view point this is really a view of the dual.
 * However, this is presented, as in the literature, as a cryptomorphim of the (non-dual)
 * oriented matroid.
 * @author jeremycarroll
 *
 */
public interface OMasFaceLattice extends OM, FaceLattice, Iterable<Face> {


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

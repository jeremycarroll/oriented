/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.BitSet;

import net.sf.oriented.omi.SignedSet;

final class MaxFace extends Face {

    public MaxFace(DualFaceLattice lattice, SignedSet vector, BitSet conform,
            BitSet extend) {
        super(lattice, vector, lattice.maxDimension - 1, conform, extend);
        setDimension(lattice.maxDimension - 1);
        thisIsBelowThat(lattice.top);
    }

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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.BitSet;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.SignedSet;

final class MinFace extends PFace {

    public MinFace(DualFaceLattice lattice, SignedSet circuit) {
        super(lattice, circuit, 0, new BitSet(),new BitSet());
        setDimension(0);
        if (lattice.maxDimension==1) {
            this.thisIsBelowThat(lattice.top);
        }
    }

    @Override
    public Face.Type getFaceType() {
        return Face.Type.Cocircuit;
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

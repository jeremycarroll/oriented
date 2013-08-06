/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import net.sf.oriented.omi.SignedSet;

public class Top extends AbsFace {

    public Top(DualFaceLattice lattice) {
        super(lattice, lattice.n() - lattice.rank());
    }


//    @Override
//    boolean isTop() {
//         return true;
//     }
//
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Top)) {
            return false;
        }
        return lattice == ((Top)o).lattice;
    }
    @Override
    public int hashCode() {
        // no particular rationale for this number.
        return 503;
    }
    

//    @Override
//    public void setIsBelow(Face higher) {
//        throw new IllegalStateException("invariant failure");
//    }
    

    @Override
    public String toString() {
        return "T";
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

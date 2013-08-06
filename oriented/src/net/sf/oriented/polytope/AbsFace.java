/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import static net.sf.oriented.polytope.AbsFace.UNKNOWN;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Verify;

public class AbsFace implements Verify{

    protected static final int UNKNOWN = -2;
    final DualFaceLattice lattice;
    int dimension;
    List<AbsFace> below = new LinkedList<AbsFace>();
    
    AbsFace(DualFaceLattice l, int d) {
        lattice = l;
        dimension = d;
    }
    

//    public void setDimension(int d) {
//        if (dimension != UNKNOWN) {
//            if (d != dimension) {
//                throw new IllegalArgumentException("Dimension mismatch: "+d+" != "+dimension);
//            }
//            return;
//        }
//        dimension = d;
//        for (Face f:below) {
//            f.setDimension(d+1);
//        }
//    }
    
    @Override
    public void verify() throws AxiomViolation {
//        if (dimension == UNKNOWN) {
//            throw new AxiomViolation(lattice, "dimension was not defined in "+this);
//        }
//        Set<Face> seenOnce = new HashSet<Face>();
//        Set<Face> seenTwice = new HashSet<Face>();
//        for (Face oneUp:below) {
//            for (Face twoUp: oneUp.below) {
//                if (seenTwice.contains(twoUp)) {
//                    throw new AxiomViolation(lattice, "Interval from "+this+" to "+twoUp+" has more than four members.");
//                }
//                if (seenOnce.remove(twoUp)) {
//                    seenTwice.add(twoUp);
//                } else {
//                    seenOnce.add(twoUp);
//                }
//            }
//        }
//        if (!seenOnce.isEmpty()) {
//            throw new AxiomViolation(lattice, "Interval from "+this+" to "+seenOnce.iterator().next()+" has only three members.");
//        }
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

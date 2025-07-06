/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

class FLHelper {

    final OM modified;
    final UnsignedSet noCoLoops;
    final SignedSet positive;
    public FLHelper(OM om, UnsignedSet noCoLoops, UnsignedSet positiveTopeMinus ) {
        modified = om;
        this.noCoLoops = noCoLoops;
        
        positive = modified.ffactory().signedSets().construct(noCoLoops.minus(positiveTopeMinus), positiveTopeMinus );
    }

    Face getPositiveFace() {
        return getFace(positive);
    }

    Face getFace(SignedSet covector) {
        Face s = modified.getFaceLattice().get(covector);
        if (s == null) {
            throw new IllegalArgumentException(covector+" is not a face of the modified OM");
        }
        return s;
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

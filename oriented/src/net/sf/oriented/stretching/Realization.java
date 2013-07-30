/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.stretching;

import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class Realization {
    
    private final OM original;
    private final OM reoriented;
    

    public Realization(OM om, String infinity) {
        this(om,om.asInt(infinity));
    }

    public Realization(OM om, Label infinity) {
        this(om,om.asInt(infinity));
    }

    private Realization(OM om, int infinity) {
        if (infinity == -1){
            throw new IllegalArgumentException("Bad choice of infinity");
        }
        if (om.rank() != 3) {
            throw new IllegalArgumentException("Psuedoline stretching algorithm only applies to rank 3 oriented matroids");
        }
        original = om;
        OMasSignedSet topes = om.dual().getMaxVectors();
        Label infLabel = om.elements()[infinity];
        FactoryFactory f = ((OMInternal)om).ffactory();
        UnsignedSet infUS = f.unsignedSets().empty().union(infLabel);
        int bestSize = Integer.MAX_VALUE;
        SignedSet bestTope = null;
        for (SignedSet tope:topes) {
            if (1 == tope.sign(infLabel)) {
                SignedSet adjacent = tope.restriction(infUS).compose(tope);
                if (topes.contains(adjacent)) {
                    UnsignedSet needReorienting = tope.minus();
                    int size = needReorienting.size();
                    if (size < bestSize) {
                        bestSize = size;
                        bestTope = tope;
                    }
                }
            }
        }
        reoriented = om.reorient(bestTope.minus().toArray());
    }

    public String toCrossingsString() {
        // TODO Auto-generated method stub
        return null;
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;


import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class GraphicsPseudoLines extends AbsPseudoLines {

    public GraphicsPseudoLines(OM om, String infinity, String ... alsoReorient) {
        super(om, infinity, alsoReorient);
        setNoCoLoops(modified);
    }

    public GraphicsPseudoLines(OM om, Label infinity) {
        super(om, infinity);
        setNoCoLoops(modified);
    }

    @Override
    Face getPositiveFace() {
        UnsignedSet empty = modified.ffactory().unsignedSets().empty();
        SignedSet positiveTope = modified.ffactory().signedSets().construct(noCoLoops, empty );
        return getFace(positiveTope);
    }

    @Override
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class Bottom extends Face {

    public Bottom(FaceLattice lattice, FactoryFactory ffactory) {
        super(lattice,emptySignedSet(ffactory));
    }

    private static SignedSet emptySignedSet(FactoryFactory ffactory) {
        UnsignedSet emptySet = ffactory.unsignedSets().empty();
        return ffactory.signedSets().construct(emptySet, emptySet);
    }

    @Override
    void considerComposition(Vertex vertex) {
        setIsBelow(vertex);
    
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

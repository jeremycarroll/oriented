/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Arrays;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

import net.sf.oriented.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OMasFaceLattice;
import net.sf.oriented.omi.SignedSet;

class LatticeUtil {

    private static final Function<Face, SignedSet> FACE2COVECTOR = new Function<Face, SignedSet>(){
        @Override
        public SignedSet apply(Face input) {
            return input.covector();
        }
    };
    public static SetOfSignedSetInternal cocircuits(OMasFaceLattice lattice) {
        return collectVectors(lattice,0,0);
    }

    private static SetOfSignedSetInternal collectVectors(OMasFaceLattice lattice, int i, int j) {
        FactoryFactory f = lattice.ffactory();
        return (SetOfSignedSetInternal) f.setsOfSignedSet().copyBackingCollection(
                Iterables.transform(
                lattice.withDimensions(i,j),
                FACE2COVECTOR
                        
                        ));
    }

    public static SetOfSignedSetInternal covectors(OMasFaceLattice lattice) {
        return collectVectors(lattice,-1,lattice.top().dimension()-1);
    }

    public static SetOfSignedSetInternal topes(OMasFaceLattice lattice) {
        return collectVectors(lattice,lattice.top().dimension()-1,lattice.top().dimension()-1);
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

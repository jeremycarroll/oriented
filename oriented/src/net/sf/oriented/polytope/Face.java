/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.Verify;

public class Face extends AbsFace  {

    final SignedSet vector;


    protected Face(DualFaceLattice lattice, int d, SignedSet vector) {
        super(lattice,d);
        this.vector = vector;
        
    }
    
//    private void saveMe() {
////        System.err.println("+ "+this);
////        lattice.faces.put(covector,this);
//    }


    protected Face(DualFaceLattice lattice, SignedSet covector) {
        this(lattice,UNKNOWN,covector);
    }

//   boolean isTop() {
//        return false;
//    }
//
//    protected boolean hasRestriction(Face higher) {
//        return higher.covector().isRestrictionOf(vector);
//    }
//
//
//    protected boolean conformsWith(SignedSet covector2) {
//        return vector.conformsWith(covector2);
//    }

    
    public SignedSet vector() {
        return vector;
    }
    
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Face)) {
            return false;
        }
        Face f = (Face)o;
        return lattice == f.lattice && vector.equals(f.vector());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+":"+vector.toString();
    }
    
    @Override
    public int hashCode() {
        return vector.hashCode();
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

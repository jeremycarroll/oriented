/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedList;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.Verify;

public class Face extends AbsFace  {

    final SignedSet vector;
    private final BitSet conform;
    private final BitSet extend;
    private final int id;
    
    
    


    
    
//    private void saveMe() {
////        System.err.println("+ "+this);
////        lattice.faces.put(covector,this);
//    }


   

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

    
    public Face(DualFaceLattice lattice, SignedSet vector, 
            BitSet conform, BitSet extend) {
        super(lattice,UNKNOWN);
        this.vector = vector;
        this.conform = conform;
        this.extend = extend;
        this.id = lattice.faces.size();
        lattice.faces.add(this);
        if (null != lattice.ss2faces.put(vector, this)) {
            throw new IllegalArgumentException("Duplicate face");
        }
    }

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
        return getClass().getSimpleName()+":"+vector.toString()+"{"+id+"}";
    }
    
    @Override
    public int hashCode() {
        return vector.hashCode();
    }

    public BitSet extendsCircuits() {
        return extend;
    }

    public BitSet conformingCircuits() {
        return conform;
    }
    

    @Override
    public void verify() throws AxiomViolation {
        super.verify();
        if (getHigher().isEmpty()) {
            throw new AxiomViolation(this,"Should be in the middle");
        }
        if (getLower().isEmpty()) {
            throw new AxiomViolation(this,"Should be in the middle");
        }
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.BitSet;
import java.util.List;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.SignedSet;

class Face extends AbsFace  {

    private final SignedSet vector;
    private final BitSet conform;
    private final BitSet extend;
    final int id1;
    final int id2;
    
    public Face(DualFaceLattice lattice, SignedSet vector,int minDimension, 
            BitSet conform, BitSet extend) {
        super(lattice,minDimension,UNKNOWN);
        this.vector = vector;
        this.conform = conform;
        this.extend = extend;
        this.id1 = vector.support().size();
        List<Face> level = lattice.faces[id1];
        this.id2 = level.size();
        level.add(this);
        lattice.size++;
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
        return getClass().getSimpleName()+":"+vector.toString()+"{"+id1+":"+id2+"}["+getMinDimension()+"]";
    }
    
    @Override
    public int hashCode() {
        return vector.hashCode();
    }

    BitSet extendsCircuits() {
        return extend;
    }

    BitSet conformingCircuits() {
        return conform;
    }

    @Override
    public void verify() throws AxiomViolation {
        super.verify();
        if (!getHigher().iterator().hasNext()) {
            throw new AxiomViolation(this,"Should be in the middle");
        }
//        if (!getLower().iterator().hasNext()) {
//            throw new AxiomViolation(this,"Should be in the middle");
//        }
    }

    public void expand() {
        setDimension(this.getMinDimension());
        SignedSet vector = vector();
         BitSet candidates = (BitSet)conformingCircuits().clone();
         BitSet extend = extendsCircuits();
         candidates.andNot(extend);
         int ix = 0;
         while (true) {
             ix = candidates.nextSetBit(ix);
             if (ix == -1) {
                 break;
             }
             Face circuit = lattice.circuits[ix];
             SignedSet circ = circuit.vector;
             if (circ.isRestrictionOf(vector)) {
                 extend.set(ix);
                 circuit.thisIsBelowThat(this);
             } else {
                 SignedSet next = circ.compose(vector);
                 Face n = lattice.ss2faces.get(next);
                 if (n == null) {
                     lattice.initVector(next,this,ix);
                 } else {
                     BitSet exten = n.extendsCircuits();
                     exten.set(ix);
                     exten.or(extend);
                     circuit.thisIsBelowThat(n);
                     thisIsBelowThat(n);
    // need to somehow block some other work
                 }
             }
             ix++;
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.BitSet;
import java.util.List;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.SignedSet;

import net.sf.oriented.omi.Face;

/**
 * Note the P in PFace means nothing, simply to differentiate the
 * word from {@link net.sf.oriented.omi.Face}
 * @author jeremycarroll
 *
 */
class PFace extends AbsFace  {

    private final SignedSet vector;
    private final BitSet conform;
    private final BitSet extend;
    final int id1;
    final int id2;
    
    public PFace(DualFaceLattice lattice, SignedSet vector,int minDimension, 
            BitSet conform, BitSet extend) {
        super(lattice,minDimension,UNKNOWN);
        this.vector = vector;
        this.conform = conform;
        this.extend = extend;
        this.id1 = vector.support().size();
        List<PFace> level = lattice.faces[id1];
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
        if (! (o instanceof PFace)) {
            return false;
        }
        PFace f = (PFace)o;
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
        if (!getLower().iterator().hasNext()) {
            throw new AxiomViolation(this,"Should be in the middle");
        }
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
             extendBy(ix,vector, extend);
             ix++;
         }
         prune();
    }
    private void extendBy(int ix,SignedSet vector, BitSet extend) {
        PFace circuit = lattice.circuits[ix];
        SignedSet circ = circuit.vector;

        SignedSet next = circ.compose(vector);
        PFace n = lattice.ss2faces.get(next);
        if (n == null) {
            lattice.initVector(next,this,ix);
        } else {
            BitSet exten = n.extendsCircuits();
            exten.set(ix);
            exten.or(extend);
            circuit.thisIsBelowThat(n);
            thisIsBelowThat(n);
        }
    }

    public Face.Type getFaceType() {
        return Face.Type.Covector;
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

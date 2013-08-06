/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.BitSet;

import net.sf.oriented.impl.om.AbsOM;
import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class FaceLattice extends AbsOM<Face> {
    
    
 //   final SetOfSignedSet topes;
    final SignedSet circuits[];
    final List<BitSet> vectorConformsWithCircuit = new ArrayList<BitSet>();
    final List<BitSet> vectorExtendsCircuit= new ArrayList<BitSet>();
    final List<SignedSet> vectors = new ArrayList<SignedSet>();
    final Map<SignedSet,Integer> faces = new HashMap<SignedSet,Integer>();

    public FaceLattice(OM om) {
        super((OMInternal)om);
     //  topes = om.dual().getMaxVectors();
         circuits = om.getCircuits().toArray();
         for (SignedSet s:circuits) {
             initCircuit(s);
         }
         
         int pos = 0;
         while (pos < vectors.size()) {
             SignedSet vector = vectors.get(pos);
             BitSet candidates = (BitSet) vectorConformsWithCircuit.get(pos).clone();
             BitSet extend = vectorExtendsCircuit.get(pos);
             candidates.andNot(extend);
             int ix = 0;
             while (true) {
                 ix = candidates.nextSetBit(ix);
                 if (ix == -1) {
                     break;
                 }
                 SignedSet circuit = circuits[ix];
                 if (circuit.isRestrictionOf(vector)) {
                     extend.set(ix);
                 } else {
                     SignedSet next = circuit.compose(vector);
                     Integer key = faces.get(next);
                     if (key == null) {
                         initVector(next,pos,ix);
                     } else {
                         BitSet exten = vectorExtendsCircuit.get(key);
                         exten.set(ix);
                         exten.or(vectorExtendsCircuit.get(pos));
// need to somehow block some other work
                     }
                 }
                 ix++;
             }
             pos++;
         }
         
    }
    private void initVector(SignedSet vector, int pVector, int pCircuit) {
        BitSet conform = (BitSet)vectorConformsWithCircuit.get(pVector).clone();
        int ix = initEntry(vector,conform,vectorExtendsCircuit.get(pVector));
        conform.and(vectorConformsWithCircuit.get(pCircuit));
        vectorExtendsCircuit.get(ix).set(pCircuit);
    }
    private void initCircuit(SignedSet circuit) {
        int ix = initEntry(circuit,new BitSet(),new BitSet());

        vectorExtendsCircuit.get(ix).set(ix);
        BitSet conform = vectorConformsWithCircuit.get(ix);
        conform.set(ix);
        for (int i=0;i<ix;i++) {
            if (vectorConformsWithCircuit.get(i).get(ix)) {
                conform.set(i);
            }
        }
        for (int i=ix+1;i<circuits.length;i++) {
            if (circuit.conformsWith(circuits[i])) {
                conform.set(i);
            }
        }
    }
    public int initEntry(SignedSet circuit,BitSet conform,BitSet extend ) {
        int ix = vectors.size();
        vectors.add(circuit);
        vectorExtendsCircuit.add(extend);
        vectorConformsWithCircuit.add(conform);
        return ix;
    }
    /*
 * A lattice which satisfies the identities

Distributivity of ∨ over ∧
a∨(b∧c) = (a∨b) ∧ (a∨c).
Distributivity of ∧ over ∨
a∧(b∨c) = (a∧b) ∨ (a∧c).

is said to be distributive.
 * 
 * (non-Javadoc)
 * @see net.sf.oriented.omi.Verify#verify()
 */
    @Override
    public void verify() throws AxiomViolation {
    }

    @Override
    public boolean equals(Object o) {
        return this == o || all.equals(o);
    }

    @Override
    public Iterator<? extends Face> iterator2() {
        return null;
    }

   

    int counter = 1;
    public void dump() {
//        System.err.println("["+counter++ + "]===============");
//        for (Map.Entry<SignedSet, Face> entry: faces.entrySet()) {
//            Face f = entry.getValue();
//            System.err.println(entry.getKey()+":" + f+"["+f.dimension+"]");
//            for (Face ff:f.above) {
//                System.err.println("    : " + ff);
//            }
//            
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

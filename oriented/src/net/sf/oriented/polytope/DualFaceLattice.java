/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.ArrayList;
import java.util.Arrays;
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

public class DualFaceLattice extends AbsOM<Face> {
    
    final Bottom bottom = new Bottom(this);
    final Top top = new Top(this);
    
 //   final SetOfSignedSet topes;
    final SignedSet circuits[];
//    final List<BitSet> vectorConformsWithCircuit = new ArrayList<BitSet>();
//    final List<BitSet> vectorExtendsCircuit= new ArrayList<BitSet>();
//    final List<SignedSet> vectors = new ArrayList<SignedSet>();
    final Map<SignedSet,Face> ss2faces = new HashMap<SignedSet,Face>();
    final List<Face> faces = new ArrayList<Face>();

    final List<AbsFace> byDimension[];
    private final int maxDimension;

    public DualFaceLattice(OM om) {
        super((OMInternal)om);
        maxDimension = n() - rank();
        top.setDimension(maxDimension);
        byDimension = new List[maxDimension+2];
     //  topes = om.dual().getMaxVectors();
         circuits = om.getCircuits().toArray();
         for (SignedSet s:circuits) {
             initCircuit(s);
         }
         
         int pos = 0;
         while (pos < faces.size()) {
             Face f = faces.get(pos);
             SignedSet vector = f.vector();
             BitSet candidates = (BitSet)f.conformingCircuits().clone();
             BitSet extend = f.extendsCircuits();
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
                     Face n = ss2faces.get(next);
                     if (n == null) {
                         initVector(next,f,ix);
                     } else {
                         BitSet exten = n.extendsCircuits();
                         exten.set(ix);
                         exten.or(extend);
// need to somehow block some other work
                     }
                 }
                 ix++;
             }
             pos++;
         }
         BitSet circuitConformsWithVector[] = new BitSet[circuits.length];
//         System.err.println("A"+circuits.length);
         for (int i=0;i<circuits.length;i++) {
             circuitConformsWithVector[i] = new BitSet();
         }
         for (int i=0;i<faces.size();i++) {
             Face a = faces.get(i);
             int c = 0;
             while ( true ) {
                 c = a.conformingCircuits().nextSetBit(c);
                 if (c == -1) break;
                 circuitConformsWithVector[c].set(i);
                 c++;
             }
         }
//         System.err.println("B"+faces.size());
         for (int i=0;i<faces.size()-1;i++) {
//             if ((i%1000)==0) System.err.println("C"+i);
             Face a = faces.get(i);
             BitSet extend = a.extendsCircuits();
             int first = extend.nextSetBit(0);
             BitSet comparable = (BitSet)circuitConformsWithVector[first].clone();
             int and = first+1;
             while ( true ) {
                 and = extend.nextSetBit(and);
                 if (and == -1) break;
                 comparable.and( circuitConformsWithVector[and]);
                 and++;
             }
             int j=i+1;
             while ( true ) {
                 j = comparable.nextSetBit(j);
                 if ( j == -1) break;
                 Face b = faces.get(j);
                 computeComparison(a,b);
                 j++;
             }
         }
         readGrades();
//         dump();
         for (Face f:faces) {
             f.prune();
         }
         System.err.println(toString());
    }
    private void readGrades() {
        Iterable<AbsFace> possibilities = Arrays.asList(new AbsFace[]{bottom});
        for (int dimension = -1; dimension < maxDimension; dimension++ ){
            List<AbsFace> grade = byDimension[dimension+1] = new ArrayList<AbsFace>();
            boolean maxVector = dimension == maxDimension-1;
            List<AbsFace> nextLevel = new ArrayList<AbsFace>();
            for (AbsFace me: possibilities) {
                if (me.noLowerLeft()) {
                    me.setDimension(dimension);
                    nextLevel.addAll(me.getALittleHigher());
                    grade.add(me);
                    if (maxVector) {
                        this.lowAndHigh(me, top);
                    }
                } else {
                    if (maxVector) {
                        throw new IllegalStateException("Logic error?");
                    }
                    nextLevel.add(me);
                }
            }
            possibilities = nextLevel;
            for(AbsFace me:grade) {
                for (AbsFace higher:me.getHigher()) {
                    higher.lowerIsDone(me);
                }
            }
        }
    }
    private void computeComparison(Face a, Face b) {
        SignedSet av = a.vector();
        SignedSet bv = b.vector();
        if (av.conformsWith(bv)) {
            if (av.isRestrictionOf(bv)) {
                lowAndHigh(a, b);
            } else if (bv.isRestrictionOf(av)) {
                lowAndHigh(b, a);
            } 
        }
        
    }
    public void lowAndHigh(AbsFace a, AbsFace b) {
        a.addHigher(b);
        b.addLower(a);
    }
    private void initVector(SignedSet vector, Face pVector, int pCircuit) {
        Face circuit = faces.get(pCircuit);
        BitSet conform = (BitSet)pVector.conformingCircuits().clone();
        conform.and(circuit.conformingCircuits());
        Face rslt = initEntry(vector,conform,(BitSet)pVector.extendsCircuits().clone());
        pVector.addOneHigher(rslt);
        rslt.extendsCircuits().set(pCircuit);
        lowAndHigh(pVector,rslt);
        lowAndHigh(circuit,rslt);
    }
    private void initCircuit(SignedSet circuit) {
        int ix = faces.size();
         Face rslt = initEntry(circuit,new BitSet(),new BitSet());
         bottom.addOneHigher(rslt);
         this.lowAndHigh(bottom, rslt);
        rslt.extendsCircuits().set(ix);
        BitSet conform = rslt.conformingCircuits();
        conform.set(ix);
        for (int i=0;i<ix;i++) {
            if (faces.get(i).conformingCircuits().get(ix)) {
                conform.set(i);
            }
        }
        for (int i=ix+1;i<circuits.length;i++) {
            if (circuit.conformsWith(circuits[i])) {
                conform.set(i);
            }
        }
    }
    public Face initEntry(SignedSet circuit, BitSet conform,BitSet extend ) {
         Face rslt = new Face(this,circuit,conform,extend);
         return rslt;
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
        bottom.verify();
        top.verify();
        for (Face f:faces) {
            f.verify();
        }
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
        StringBuilder rslt = new StringBuilder();
        List<AbsFace> byDimension[] = new List[maxDimension+2];
        for (int i=0;i<byDimension.length;i++) {
            byDimension[i] = new ArrayList<AbsFace>();
        }
        for (Face f:faces) {
            byDimension[f.dimension+1].add(f);
        }
        byDimension[bottom.dimension+1].add(bottom);
        byDimension[top.dimension+1].add(top);
        for (int i=0;i<maxDimension+1;i++) {
            System.err.println("==== "+(i-1));
            for (AbsFace f:byDimension[i]) {
                f.dump();
            }
        }
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
    
    @Override
    public String toString() {
        StringBuilder rslt = new StringBuilder();
        int cnts[] = new int[maxDimension+2];
        for (Face f:faces) {
            cnts[f.dimension+1]++;
        }
        cnts[bottom.dimension+1]++;
        cnts[top.dimension+1]++;
        rslt.append("Lattice{");
        for (int i=0;i<maxDimension+1;i++) {
            rslt.append(cnts[i]);
            rslt.append(',');
        }
        rslt.append(cnts[maxDimension+1]);
        rslt.append('}');
        return rslt.toString();
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

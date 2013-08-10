/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.oriented.impl.om.AbsOM;
import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class DualFaceLattice extends AbsOM<Face> {

    private static final int TRACE_FREQ = 100000;
    private static final boolean TRACE = true;
    final int maxDimension = n() - rank();
    private final Bottom bottom;
    final Top top;
    private final SignedSet circuits[];
    final Map<SignedSet,Face> ss2faces = new HashMap<SignedSet,Face>();
    final List<Face> faces = new ArrayList<Face>();

    final long start = System.currentTimeMillis();
    final List<AbsFace> byDimension[];
    private UnsignedSet notCoLoops;

    public DualFaceLattice(OM om) {
        super((OMInternal)om);
        @SuppressWarnings("unchecked")
        List<AbsFace>[] suppressWarning = new List[maxDimension+2];
        byDimension = suppressWarning;
        for (int i=0;i<byDimension.length;i++) {
            byDimension[i] = new ArrayList<AbsFace>();
        }
        bottom = new Bottom(this);
        top = new Top(this);
        notCoLoops = ffactory().unsignedSets().empty();
         circuits = om.getCircuits().toArray();
         for (SignedSet s:circuits) {
             notCoLoops = notCoLoops.union(s.support());
             initCircuit(s);
         }
         
         int pos = 0;
         while (pos < faces.size()) {
             trace(pos,"A");
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
                     faces.get(ix).setIsLower(f);
                 } else {
                     SignedSet next = circuit.compose(vector);
                     Face n = ss2faces.get(next);
                     if (n == null) {
                         initVector(next,f,ix);
                     } else {
                         BitSet exten = n.extendsCircuits();
                         exten.set(ix);
                         exten.or(extend);
                         faces.get(ix).setIsLower(n);
// need to somehow block some other work
                     }
                 }
                 ix++;
             }
             pos++;
         }
         BitSet circuitConformsWithVector[] = new BitSet[circuits.length];
         for (int i=0;i<circuits.length;i++) {
            circuitConformsWithVector[i] = new BitSet();
         }
         for (int i=0;i<faces.size();i++) {
             trace(i,"B");
             Face a = faces.get(i);
             int c = 0;
             while ( true ) {
                 c = a.conformingCircuits().nextSetBit(c);
                 if (c == -1) break;
                 circuitConformsWithVector[c].set(i);
                 c++;
             }
         }
         for (int i=0;i<faces.size()-1;i++) {
             trace(i,"C");
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
             comparable.clear(i);
             clearBits(comparable,a.getLower());
             clearBits(comparable,a.getHigher());
             int j=i+1;
             while ( true ) {
                 j = comparable.nextSetBit(j);
                 if ( j == -1) break;
                 Face b = faces.get(j);
                 computeComparison(a,b);
                 j++;
             }
         }

         int j=0;
         for (Face f:faces) {
             trace(j,"D");
            j++;
             f.prune();
         }
         System.err.println(toString());
    }
    private void trace(int pos, String pref) {
        if (TRACE && pos % TRACE_FREQ == 0) {
             System.err.println(pref+": "+pos + "/"+faces.size()+ (" "+((System.currentTimeMillis()-start)/1000)));
         }
    }
    private void clearBits(BitSet comparable,  Iterable<AbsFace> higher) {
        for (AbsFace f:higher) {
            if (f instanceof Face) {
                comparable.clear(((Face)f).id);
            }
        }
    }
    
    private void computeComparison(Face a, Face b) {
        boolean aMaybeJustUnderB = a.couldSetLower(b);
        boolean bMaybeJustUnderA = b.couldSetLower(a);
        if (!(aMaybeJustUnderB||bMaybeJustUnderA)) {
            return;
        }
        SignedSet av = a.vector();
        SignedSet bv = b.vector();
        if (av.conformsWith(bv)) {
            if (aMaybeJustUnderB && av.isRestrictionOf(bv)) {
                a.setIsLower(b);
            } else if (bMaybeJustUnderA && bv.isRestrictionOf(av)) {
                b.setIsLower(a);
            } 
        }
        
    }
    private void initVector(SignedSet vector, Face pVector, int pCircuit) {
        Face circuit = faces.get(pCircuit);
        BitSet conform = (BitSet)pVector.conformingCircuits().clone();
        conform.and(circuit.conformingCircuits());
        Face rslt = initFace(vector, pVector, conform);
        rslt.extendsCircuits().set(pCircuit);
        pVector.setIsLower(rslt);
        circuit.setIsLower(rslt);
    }
    public Face initFace(SignedSet vector, Face pVector, BitSet conform) {
        BitSet extend = (BitSet)pVector.extendsCircuits().clone();
        if (vector.support().equals(this.notCoLoops)) {
            extend.or(conform);
            System.err.println("Max > "+extend.cardinality()+" circuits");
            return new MaxFace(this,vector,conform,extend);
        } else {
           return new Face(this,vector,pVector.getMinDimension()+1,conform,extend);
        }
    }
    private void initCircuit(SignedSet circuit) {
        int ix = faces.size();
         Face rslt = new MinFace(this,circuit);
         bottom.setIsLower(rslt);
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
    public void dump() {
        for (int i=0;i<byDimension.length;i++) {
            System.err.println("==== "+(i-1));
            for (AbsFace f:byDimension[i]) {
                f.dump();
            }
        }
    }
    
    @Override
    public String toString() {
        StringBuilder rslt = new StringBuilder();
        rslt.append("Lattice{");
        for (int i=0;i<maxDimension+1;i++) {
            rslt.append(byDimension[i].size());
            rslt.append(',');
        }
        rslt.append(byDimension[maxDimension+1].size());
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

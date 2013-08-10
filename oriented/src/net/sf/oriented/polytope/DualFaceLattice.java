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

import com.google.common.collect.Iterables;

import net.sf.oriented.impl.om.AbsOM;
import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class DualFaceLattice extends AbsOM<Face> {

    private static final int TRACE_FREQ = 10000;
    private static final boolean TRACE = true;
    final int maxDimension = n() - rank();
    int size = 2;
    private final Bottom bottom;
    final Top top;
    final Face circuits[];
    final Map<SignedSet,Face> ss2faces = new HashMap<SignedSet,Face>();
    final List<Face> faces[]; 

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
        SignedSet cc[] = om.getCircuits().toArray();
        for (SignedSet s:cc) {
            notCoLoops = notCoLoops.union(s.support());
        }
        circuits = new Face[om.getCircuits().size()];
        @SuppressWarnings("unchecked")
        List<Face>[] suppressWarning2 = new List[notCoLoops.size()+1];
        faces = suppressWarning2;
        for (int j=0;j<faces.length;j++) {
            faces[j] = new ArrayList<Face>();
        }
        int i = 0;
         for (SignedSet s:cc) {
             circuits[i]=initCircuit(s,cc,i);
             i++;
         }
         
         int pos = 0;
         for (Face f:allFaces()) {
             trace(pos++,"A",f);
             f.expand();
         }
//         checkAllComparables();

         pos=0;
         for (Face f:allFaces()) {
             trace(pos++,"D",f);
             f.prune();
         }
//         System.err.println(toString());
    }
   
    private Iterable<Face> allFaces() {
        return Iterables.concat(faces);
    }

    private void trace(int pos, String pref, Face f) {
        if (TRACE && pos % TRACE_FREQ == 0) {
             System.err.println(pref+": "+pos + "/"+size+" [" + f.toString() + "] ( "+((System.currentTimeMillis()-start)/1000)+" )");
         }
    }
    
    void initVector(SignedSet vector, Face pVector, int pCircuit) {
        Face circuit = circuits[pCircuit];
        BitSet conform = (BitSet)pVector.conformingCircuits().clone();
        conform.and(circuit.conformingCircuits());
        Face rslt = initFace(vector, pVector, conform);
        rslt.extendsCircuits().set(pCircuit);
        pVector.thisIsBelowThat(rslt);
        circuit.thisIsBelowThat(rslt);
    }
    public Face initFace(SignedSet vector, Face pVector, BitSet conform) {
        BitSet extend = (BitSet)pVector.extendsCircuits().clone();
        if (vector.support().equals(this.notCoLoops)) {
            extend.or(conform);
//            System.err.println("Max > "+extend.cardinality()+" circuits");
            return new MaxFace(this,vector,conform,extend);
        } else {
           return new Face(this,vector,pVector.getMinDimension()+1,conform,extend);
        }
    }
    private Face initCircuit(SignedSet circuit, SignedSet cc[], int ix) {
         Face rslt = new MinFace(this,circuit);
         bottom.thisIsBelowThat(rslt);
        rslt.extendsCircuits().set(ix);
        BitSet conform = rslt.conformingCircuits();
        conform.set(ix);
        for (int i=0;i<ix;i++) {
            if (circuits[i].conformingCircuits().get(ix)) {
                conform.set(i);
            }
        }
        for (int i=ix+1;i<cc.length;i++) {
            if (circuit.conformsWith(cc[i])) {
                conform.set(i);
            }
        }
        return rslt;
    }
    @Override
    public void verify() throws AxiomViolation {
        bottom.verify();
        top.verify();
        for (Face f:allFaces()) {
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

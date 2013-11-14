/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.oriented.impl.om.Circuits;
import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.Verify;

import com.google.common.collect.Iterables;

public class DualFaceLattice implements Verify{

    private static final int TRACE_FREQ = 10000;
    private static final boolean TRACE = false;
    final int maxDimension;
    int size = 2;
    final Bottom bottom;
    final Top top;
    final PFace circuits[];
    final Map<SignedSet,PFace> ss2faces = new HashMap<>();
    final List<PFace> faces[]; 

    final long start = System.currentTimeMillis();
    final List<AbsFace> byDimension[];
    private UnsignedSet notCoLoops;

    public DualFaceLattice(OM om) {
        maxDimension = om.n() - om.rank();
        @SuppressWarnings("unchecked")
        List<AbsFace>[] suppressWarning = new List[maxDimension+2];
        byDimension = suppressWarning;
        for (int i=0;i<byDimension.length;i++) {
            byDimension[i] = new ArrayList<>();
        }
        bottom = new Bottom(this);
        top = new Top(this);
        notCoLoops = om.ffactory().unsignedSets().empty();
        SignedSet cc[] = om.getCircuits().toArray();
        for (SignedSet s:cc) {
            notCoLoops = notCoLoops.union(s.support());
        }
        circuits = new PFace[om.getCircuits().size()];
        @SuppressWarnings("unchecked")
        List<PFace>[] suppressWarning2 = new List[notCoLoops.size()+1];
        faces = suppressWarning2;
        for (int j=0;j<faces.length;j++) {
            faces[j] = new ArrayList<>();
        }
        int i = 0;
         for (SignedSet s:cc) {
             circuits[i]=initCircuit(s,cc,i);
             i++;
         }
         
         int pos = 0;
         for (PFace f:allFaces()) {
             trace(pos++,"A",f);
             f.expand();
         }
    }
   
    private Iterable<PFace> allFaces() {
        return Iterables.concat(faces);
    }

    @SuppressWarnings("unused")
    private void trace(int pos, String pref, PFace f) {
        if (TRACE && pos % TRACE_FREQ == 0) {
             System.err.println(pref+": "+pos + "/"+size+" [" + f.toString() + "] ( "+((System.currentTimeMillis()-start)/1000)+" )");
         }
    }
    
    void initVector(SignedSet vector, PFace pVector, int pCircuit) {
        PFace circuit = circuits[pCircuit];
        BitSet conform = (BitSet)pVector.conformingCircuits().clone();
        conform.and(circuit.conformingCircuits());
        PFace rslt = initFace(vector, pVector, conform);
        rslt.extendsCircuits().set(pCircuit);
        pVector.thisIsBelowThat(rslt);
        circuit.thisIsBelowThat(rslt);
    }
    public PFace initFace(SignedSet vector, PFace pVector, BitSet conform) {
        BitSet extend = (BitSet)pVector.extendsCircuits().clone();
        if (vector.support().equals(this.notCoLoops)) {
            extend.or(conform);
            return new MaxFace(this,vector,conform,extend);
        } else {
           return new PFace(this,vector,pVector.getMinDimension()+1,conform,extend);
        }
    }
    private PFace initCircuit(SignedSet circuit, SignedSet cc[], int ix) {
         PFace rslt = new MinFace(this,circuit);
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
        for (PFace f:allFaces()) {
            f.verify();
        }
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

    static public OMInternal asFaceLattice(Circuits c,OMInternal omAll) {
        DualFaceLattice lattice = new DualFaceLattice(c);
//        WeakReference<DualFaceLattice> ref = new WeakReference<>(lattice);
        FaceLatticeImpl rslt = new FaceLatticeImpl(omAll,lattice);
        lattice = null;
//        for (int i=0;i<100 && ref.get() != null;i++ )
//            System.gc();
//        if (ref.get() != null) {
//            throw new IllegalStateException("GC did not");
//        }
        rslt.init();
        return rslt;
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

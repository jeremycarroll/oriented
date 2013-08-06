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

public class Face implements Verify {
    
    private static final int UNKNOWN = -2;
    final SignedSet covector;
    final FaceLattice lattice;
    int dimension;
    List<Face> above = new LinkedList<Face>();


    protected Face(FaceLattice lattice, int d, SignedSet covector) {
        dimension = d;
        this.covector = covector;
        this.lattice = lattice;
        saveMe();
        
    }
    
    private void saveMe() {
//        System.err.println("+ "+this);
//        lattice.faces.put(covector,this);
    }


    protected Face(FaceLattice lattice, SignedSet covector) {
        this(lattice,UNKNOWN,covector);
    }

    public void setIsBelow(Face higher) {
//        System.err.println(this+" adding "+higher);
        if (!higher.conformsWith(this.covector)) {
            throw new IllegalArgumentException("precondition failure");
        }
        if (!(higher.isTop() || this.covector.isRestrictionOf(higher.covector))) {
            throw new IllegalArgumentException("precondition failure");
        }
        if (equals(higher)){
            throw new IllegalArgumentException("precondition failure");
        }
        if (higher.isTop()) {
            if (above.contains(higher)) {
                return;
            }
        } else {
            Iterator<Face> it = above.iterator();
            while (it.hasNext()) {
                Face f = it.next();
                if (!f.conformsWith(higher.covector())) {
                    continue;
                }
                if ( higher == f) {
                    return;
                }
                if (higher.equals(f)) {
                    throw new IllegalArgumentException("adding a second copy of "+higher.covector());
                }
                if (f.hasRestriction(higher)) {
                    higher.setIsBelow(f);
                    it.remove();
                    continue;
                }
                if (higher.hasRestriction(f)) {
                    f.setIsBelow(higher);
                    continue;
                }
                for (Face hhigher:higher.above) {
                    if (!f.conformsWith(hhigher.covector())) {
                        continue;
                    }
                    if (hhigher.equals(f)) {
                        throw new IllegalArgumentException("adding a second copy of "+higher.covector());
                    }
                    if (hhigher.hasRestriction(f)) {
                        f.setIsBelow(hhigher);
                    }
                    
                }
            }
        }
        System.err.println("B: "+this+" < "+higher);
        above.add(higher);
    }

   boolean isTop() {
        return false;
    }

    protected boolean hasRestriction(Face higher) {
        return higher.covector().isRestrictionOf(covector);
    }


    protected boolean conformsWith(SignedSet covector2) {
        return covector.conformsWith(covector2);
    }

    public void augment(Vertex vertex) {
//        System.err.println(this+" augmenting "+vertex);
        if (!vertex.covector().conformsWith(this.covector)) {
            throw new IllegalArgumentException("precondition failure");
        }
        if (equals(vertex)) {
            return;
        }
        Iterator<Face> it = above.iterator();
        while (it.hasNext()) {
            Face f = it.next();
            if (f.equals(vertex)) {
                throw new IllegalArgumentException("xxx");
            }
            if (f.hasRestriction(vertex)) {
                System.err.println("A: "+vertex+" < "+f);
                vertex.setIsBelow(f);
                it.remove();
                continue;
            }
            if (f.covector().conformsWith(vertex.covector())) {
                f.augment(vertex);
            }
        }
        considerComposition(vertex);
        
    }

    void considerComposition(Vertex vertex) {
//        SignedSet composed = vertex.covector.compose(covector);
//        Face next = lattice.createIfNew(composed);
//        if (next != null) {
//            vertex.setIsBelow(next);
//            setIsBelow(next);
//        } else {
//            // should both of these should already be true
//            next = lattice.faces.get(composed);
//            if (!vertex.above.contains(next)) {
//            //    throw new IllegalStateException("missing");
//                vertex.setIsBelow(next);
//            }
//            if (!above.contains(next)) {
//          //      throw new IllegalStateException("missing");
//                setIsBelow(next);
//            }
//        }
    }

    private Iterable<Face> above() {
        return above;
    }
    public SignedSet covector() {
        return covector;
    }
    
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Face)) {
            return false;
        }
        Face f = (Face)o;
        return lattice == f.lattice && covector.equals(f.covector());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+":"+covector.toString();
    }
    
    @Override
    public int hashCode() {
        return covector.hashCode();
    }


    public void setDimension(int d) {
        if (dimension != UNKNOWN) {
            if (d != dimension) {
                throw new IllegalArgumentException("Dimension mismatch: "+d+" != "+dimension);
            }
            return;
        }
        dimension = d;
        for (Face f:above) {
            f.setDimension(d+1);
        }
        
    }


    @Override
    public void verify() throws AxiomViolation {
        if (dimension == UNKNOWN) {
            throw new AxiomViolation(lattice, "dimension was not defined in "+this);
        }
        Set<Face> seenOnce = new HashSet<Face>();
        Set<Face> seenTwice = new HashSet<Face>();
        for (Face oneUp:above) {
            for (Face twoUp: oneUp.above) {
                if (seenTwice.contains(twoUp)) {
                    throw new AxiomViolation(lattice, "Interval from "+this+" to "+twoUp+" has more than four members.");
                }
                if (seenOnce.remove(twoUp)) {
                    seenTwice.add(twoUp);
                } else {
                    seenOnce.add(twoUp);
                }
            }
        }
        if (!seenOnce.isEmpty()) {
            throw new AxiomViolation(lattice, "Interval from "+this+" to "+seenOnce.iterator().next()+" has only three members.");
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

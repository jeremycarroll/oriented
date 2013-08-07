/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Verify;

class AbsFace implements Verify{

    protected static final int UNKNOWN = -2;
    protected final DualFaceLattice lattice;
    int dimension;
    List<AbsFace> below = new LinkedList<AbsFace>();
    private final Set<AbsFace> higher = new HashSet<AbsFace>();
    private final Set<AbsFace> lower = new HashSet<AbsFace>();
    private final List<Face> aLittleHigher = new ArrayList<Face>();
    private Set<AbsFace> lowerLeft;
    
    AbsFace(DualFaceLattice l, int d) {
        lattice = l;
        dimension = d;
    }
    

    void setDimension(int d) {
        if (dimension != UNKNOWN) {
            if (d != dimension) {
                throw new IllegalArgumentException("Dimension mismatch: "+d+" != "+dimension);
            }
            return;
        }
        dimension = d;
    }
    
    @Override
    public void verify() throws AxiomViolation {
        if (dimension == UNKNOWN) {
            throw new AxiomViolation(lattice, "dimension was not defined in "+this);
        }
        Set<AbsFace> seenOnce = new HashSet<AbsFace>();
        Set<AbsFace> seenTwice = new HashSet<AbsFace>();
        for (AbsFace oneUp:higher) {
            for (AbsFace twoUp: oneUp.higher) {
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

    void addHigher(AbsFace b) {
        higher.add(b);
    }

    void addLower(AbsFace a) {
        lower.add(a);
    }

    void addOneHigher(Face rslt) {
        aLittleHigher.add(rslt);
    }

    boolean noLowerLeft() {
        initLowerLeft();
        return lowerLeft.isEmpty();
    }

    void initLowerLeft() {
        if (lowerLeft==null) {
           lowerLeft = new HashSet<AbsFace>(this.lower);
        }
    }

    Collection<? extends AbsFace> getALittleHigher() {
        return this.aLittleHigher;
    }

    Set<AbsFace> getHigher() {
        return higher;
    }

    Set<AbsFace> getLower() {
        return lower;
    }

    void lowerIsDone(AbsFace me) {
        initLowerLeft();
        lowerLeft.remove(me);
    }
    
    void prune() {
        if (dimension == UNKNOWN) {
            throw new IllegalStateException("pruning too early");
        }
        prune(higher,dimension+1);
        prune(lower,dimension-1);
    }
    private void prune(Set<AbsFace> s, int d) {
        Iterator<AbsFace> it = s.iterator(); 
        while (it.hasNext()) {
            if (it.next().dimension != d) {
                it.remove();
            }
        }
    }

    public void dump() {
        System.err.println(toString()+":");
        for (AbsFace f:higher) {
            System.err.println("  < ["+f.dimension+"] "+f);
        }
        for (AbsFace f:lower) {
            System.err.println("  > ["+f.dimension+"] " +f);
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

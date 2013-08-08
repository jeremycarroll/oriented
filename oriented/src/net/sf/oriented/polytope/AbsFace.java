/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Verify;

class AbsFace implements Verify{

    protected static final int UNKNOWN = -2;
    protected final DualFaceLattice lattice;
    /**
     * The dimension if known
     */
    private int dimension;
    /**
     * A lower bound on dimension if any
     */
    private int minDimension;
    /**
     * An upper bound on dimension if any
     */
    private int maxDimension;
    /**
     * Invariant:
     * for all x in higher x.minDimension <= this.maxDimension+1
     *   Maintained by throwing x out of higher
     * 
     * for all x in higher x.minDimension > this.minDimension+1
     *   Maintained by throwing error
     */
    private final Set<AbsFace> higher = new HashSet<AbsFace>();
    /**
     * Invariant:
     * for all x in lower x.maxDimension >= this.minDimension-1
     * for all x in lower x.maxDimension < this.maxDimension-1
     *   Maintained by throwing error
     */
    private final Set<AbsFace> lower = new HashSet<AbsFace>();
//    private final List<Face> aLittleHigher = new ArrayList<Face>();
//    private Set<AbsFace> lowerLeft;
    
    AbsFace(DualFaceLattice l, int min, int max) {
        lattice = l;
        dimension = maxDimension = UNKNOWN;
        minDimension = min;
        if (min == UNKNOWN) {
            throw new IllegalArgumentException("We always know the minimum dimension");
        }
        setMaxDimension(max);
    }

    void setMaxDimension(int max) {
        if (max != UNKNOWN && (maxDimension == UNKNOWN || max < maxDimension)) {
            maxDimension = max;
            if (max < minDimension ) {
                throw new IllegalStateException("max < min: logic error");
            }
            if (max == minDimension) {
                setDimension(max);
            }
            for (AbsFace low:lower) {
                low.setMaxDimension(max-1);
            }
        }
    }

    void setMinDimension(int min) {
        if ( min > minDimension) {
            minDimension = min;
            if (maxDimension != UNKNOWN && min > maxDimension ) {
                throw new IllegalStateException("max < min: logic error");
            }
            if (min == maxDimension) {
                setDimension(min);
            }
            for (AbsFace high:higher) {
                high.setMinDimension(min+1);
            }
        }
    }

    public int getDimension() {
        return dimension;
    }
    
    private void setDimension(int d) {
        if (dimension != UNKNOWN) {
            if (d != dimension) {
                throw new IllegalArgumentException("Dimension mismatch: "+d+" != "+dimension);
            }
            return;
        }
        lattice.byDimension[d+1].add(this);
        dimension = d;
    }
    boolean hasDimension() {
        return dimension != UNKNOWN;
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

    private void addHigher(AbsFace b) {
        if (b.maxDimension != UNKNOWN) {
            setMaxDimension(b.maxDimension-1);
        }
        higher.add(b);
    }

    private void addLower(AbsFace a) {
        setMinDimension(a.minDimension+1);
        lower.add(a);
    }

    Set<AbsFace> getHigher() {
        return higher;
    }

    Set<AbsFace> getLower() {
        return lower;
    }
    
    void prune() {
        if (dimension == UNKNOWN) {
            throw new IllegalStateException("pruning too early: "+this);
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

    protected int getMinDimension() {
        return minDimension;
    }

    void setIsLower(AbsFace b) {
        // not worth it for rank3
        if (this.maxDimension != UNKNOWN && maxDimension < b.minDimension - 1 ) {
            return;
        }
        addHigher(b);
        b.addLower(this);
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

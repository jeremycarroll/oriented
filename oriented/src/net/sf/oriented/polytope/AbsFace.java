/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
     * This is the record of the computation and is faces known to be lower
     * in the hierarchy, we only keep ones which are exactly one dimension lower.
     */
    private final Set<AbsFace> lower = new HashSet<AbsFace>();
    
    AbsFace(DualFaceLattice l, int min, int max) {
        lattice = l;
        dimension = UNKNOWN;
        minDimension = min;
        if (min == UNKNOWN) {
            throw new IllegalArgumentException("We always know the minimum dimension");
        }
        if ( min == max) {
            setDimension(min);
        }
    }

    void setMinDimension(int min) {
        if ( min > minDimension) {
            minDimension = min;
        }
    }

    public int getDimension() {
        return dimension;
    }
    
    protected void setDimension(int d) {
        if (dimension != UNKNOWN) {
            if (d != dimension) {
                throw new IllegalArgumentException("Dimension mismatch: "+d+" != "+dimension);
            }
            return;
        }
        lattice.byDimension[d+1].add(this);
        dimension = d;
    }
    
    @Override
    public void verify() throws AxiomViolation {
        if (dimension == UNKNOWN) {
            throw new AxiomViolation(lattice, "dimension was not defined in "+this);
        }
        Map<AbsFace,AbsFace> seenOnce = new HashMap<AbsFace,AbsFace>();
        Map<AbsFace,List<AbsFace>> seenTwice = new HashMap<AbsFace,List<AbsFace>>();
        for (AbsFace oneDown:getLower()) {
            for (AbsFace twoDown: oneDown.getLower()) {
                if (seenTwice.containsKey(twoDown) //&& twoUp instanceof Face && ((Face)twoUp).vector().plus().size()==2
                        ) {
                    throw new AxiomViolation(lattice, "Interval from "+twoDown+" to "+this+" has more than four members: "+seenTwice.get(twoDown)+","+oneDown);
                }
                AbsFace link = seenOnce.remove(twoDown);
                if (link != null) {
                    seenTwice.put(twoDown,Arrays.asList(link,oneDown));
                } else {
                    seenOnce.put(twoDown,oneDown);
                }
            }
        }
        if (!seenOnce.isEmpty()) {
            Entry<AbsFace, AbsFace> entry = seenOnce.entrySet().iterator().next();
            throw new AxiomViolation(lattice, "Interval from "+this+" to "+entry.getKey() +" has only three members ("+entry.getValue() +")");
        }
    }

    private void addLower(AbsFace b) {
        lower.add(b);
    }


    Iterable<AbsFace> getLower() {
        return lower;
    }

    void prune() {
        if (dimension == UNKNOWN) {
            throw new IllegalStateException("pruning too early: "+this);
        }
        prune(getLower(),dimension-1);
    }
    private void prune(Iterable<AbsFace> s, int d) {
        Iterator<AbsFace> it = s.iterator(); 
        while (it.hasNext()) {
            if (it.next().dimension != d) {
                it.remove();
            }
        }
    }

    public void dump() {
        System.err.println(toString()+":");
        for (AbsFace f:getLower()) {
            System.err.println("  > ["+f.dimension+"] "+f);
        }
    }

    protected int getMinDimension() {
        return minDimension;
    }
    
    void thisIsBelowThat(AbsFace b) {
        if (couldAddLower(b)) {
            b.addLower(this);
            b.setMinDimension(minDimension+1);
        } 
    }

    boolean couldAddLower(AbsFace a) {
        return a.minDimension <= dimension + 1;
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

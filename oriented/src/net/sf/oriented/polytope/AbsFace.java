/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Iterables;

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
//    private int maxDimension;
    /**
     * Invariant:
     * for all x in higher x.minDimension <= this.maxDimension+1
     *   Maintained by throwing x out of higher
     * 
     * for all x in higher x.minDimension > this.minDimension+1
     *   Maintained by throwing error
     */
    private final Set<AbsFace> lower = new HashSet<AbsFace>();
//    private Set<AbsFace> oneHigher =  new HashSet<AbsFace>();
    /**
     * Invariant:
     * for all x in lower x.maxDimension >= this.minDimension-1
     * for all x in lower x.maxDimension < this.maxDimension-1
     *   Maintained by throwing error
     */
//    private final Set<AbsFace> lower = new HashSet<AbsFace>();
//    private Set<AbsFace> oneLower =  new HashSet<AbsFace>();
//    private final List<Face> aLittleHigher = new ArrayList<Face>();
//    private Set<AbsFace> lowerLeft;
    
    AbsFace(DualFaceLattice l, int min, int max) {
        lattice = l;
        dimension = UNKNOWN;
//        maxDimension = l.maxDimension+1;
        minDimension = min;
        if (min == UNKNOWN) {
            throw new IllegalArgumentException("We always know the minimum dimension");
        }
        if ( min == max) {
            setDimension(min);
        }
//        setMaxDimension(max==UNKNOWN?l.maxDimension:max);
    }

//    void setMaxDimension(int max) {
//        if (max != UNKNOWN && (maxDimension == UNKNOWN || max < maxDimension)) {
//            maxDimension = max;
//            if (max < minDimension ) {
//                throw new IllegalStateException("max < min: logic error");
//            }
//            if (max == minDimension) {
//                setDimension(max);
//            } else {
////                for (AbsFace low:getLower()) {
////                    low.setMaxDimension(max-1);
////                }
//            }
//        }
//    }

    void setMinDimension(int min) {
        if ( min > minDimension) {
            minDimension = min;
//            if (min > maxDimension ) {
//                throw new IllegalStateException("max < min: logic error");
//            }
//            if (min == maxDimension) {
//                setDimension(min);
//            } else {
////                for (AbsFace high:getHigher()) {
////                    high.setMinDimension(min+1);
////                }
//            }
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
//        splitLower(lower,d-1);
//        splitHigher(higher,d+1);
    }


//    private void splitLower(Set<AbsFace> low, int i) {
//        new MinSplitter(this,i,oneLower).split(low);
//    }

//    private void splitHigher(Set<AbsFace> general, int expected) {
//        new MaxSplitter(this,expected,oneHigher).split(general);
//    }
    /**
     * This is articulated in terms of 
     * 
     * the other way is formed by reversing signs
     * @author jeremycarroll
     *
     */
//    private static abstract class SetSplitter {
//        enum Comparison {
//            RetainAndNeedsSetting,
//            IsExact,
//            IsExactAndNeedsSetting,
//            Discard,
//            Retain;
//        };
//        abstract int getHigher(AbsFace f);
//        abstract int getLower(AbsFace f);
//        abstract void removeMatching(AbsFace f);
//        abstract void setHigher(AbsFace f);
//        abstract void moveMatching(AbsFace f);
//        final AbsFace outer;
//        final int expected;
//        final Set<AbsFace> exact;
//        SetSplitter(AbsFace parent, int expected, Set<AbsFace> exact) {
//            outer = parent;
//            this.expected = expected;
//            this.exact = exact;
//        }
//        Comparison compare(AbsFace f) {
//            int higher = getHigher(f);
//            if ( higher < expected ) {
//                return Comparison.Discard;
//            }
//            boolean isExact = getLower(f) == expected;
//            if (isExact) {
//                return higher==expected ? Comparison.IsExact : Comparison.IsExactAndNeedsSetting;
//            } else {
//                return higher==expected ? Comparison.Retain : Comparison.RetainAndNeedsSetting;
//            }
//        }
//        void split(Set<AbsFace> general) {
//            Iterator<AbsFace> it = general.iterator();
//            List<AbsFace> toSetMax = new ArrayList<AbsFace>();
//            List<AbsFace> toMove = new ArrayList<AbsFace>();
//            while (it.hasNext()) {
//                AbsFace f = it.next();
//                switch ( compare(f)) {
//                case Discard:
//                    removeMatching(f);
//                    break;
//                case IsExactAndNeedsSetting:
//                    toSetMax.add(f);
//                case IsExact:
//                    exact.add(f);
//                    toMove.add(f);
//                    break;
//                case RetainAndNeedsSetting:
//                    toSetMax.add(f);
//                case Retain:
//                    continue;
//                }
//                it.remove();
//            }
//            for (AbsFace f:toSetMax) {
//                setHigher(f);
//            }
//            for (AbsFace f:toMove) {
//                moveMatching(f);
//            }
//        }
//    }
//    private static final class MinSplitter extends SetSplitter {
//
//        MinSplitter(AbsFace parent,int expected, Set<AbsFace> exact) {
//            super(parent, expected, exact);
//        }
//        @Override
//        int getHigher(AbsFace f) {
//            return 0;
////            return f.maxDimension;
//        }
//        @Override
//        int getLower(AbsFace f) {
//            return f.minDimension;
//        }
//        @Override
//        void removeMatching(AbsFace f) {
//            f.removeHigher(outer);
//        }
//        @Override
//        void setHigher(AbsFace f) {
////            f.setMaxDimension(expected);
//        }
//        @Override
//        void moveMatching(AbsFace f) {
////            f.moveHigherToExact(outer);
//        }
//    }
//    private void removeHigher(AbsFace f) {
//        if (!higher.remove(f) ) {
//            // unnecessary ?
////            oneHigher.remove(f);
//        }
//    }
//    public void moveHigherToExact(AbsFace outer) {
//        if (higher.remove(outer)) {
//            oneHigher.add(outer);
//        }
//    }
//    private final class MaxSplitter extends SetSplitter {
//        MaxSplitter(AbsFace parent, int expected, Set<AbsFace> exact) {
//            super(parent, -expected, exact);
//        }
//        @Override
//        int getHigher(AbsFace f) {
//            return -f.minDimension;
//        }
//        @Override
//        int getLower(AbsFace f) {
//            return 0;
////            return -f.maxDimension;
//        }
//        @Override
//        void removeMatching(AbsFace f) {
////            f.removeLower(outer);
//        }
//        @Override
//        void setHigher(AbsFace f) {
//            f.setMinDimension(-expected);
//        }
//        @Override
//        void moveMatching(AbsFace f) {
////            f.moveLowerToExact(outer);
//        }
//        
//    }
//

//    private void removeLower(AbsFace f) {
//        if (!lower.remove(f) ) {
//            // unnecessary?
////            oneLower.remove(f);
//        }
//    }
//    public void moveLowerToExact(AbsFace outer) {
//        if (lower.remove(outer)) {
//            oneLower.add(outer);
//        }
//    }
    
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
//        if (b.maxDimension != UNKNOWN) {
//            setMaxDimension(b.maxDimension-1);
////            if (b.dimension != UNKNOWN && dimension != UNKNOWN ) {
////                if ( dimension == b.dimension - 1) {
////                    higher.remove(b);
////                    oneHigher.add(b);
////                }
////                return;
////            }
//        }
        lower.add(b);
    }

//    private void addLower(AbsFace a) {
//        setMinDimension(a.minDimension+1);
////        if (a.dimension != UNKNOWN && dimension != UNKNOWN ) {
////            if ( dimension == a.dimension + 1) {
////                lower.remove(a);
////                oneLower.add(a);
////            }
////            return;
////        }
////        lower.add(a);
//    }

    Iterable<AbsFace> getLower() {
        return lower;
//                oneHigher==null?higher:Iterables.concat(higher,oneHigher);
    }

//    Iterable<AbsFace> getLower() {
//        return oneLower==null?lower:Iterables.concat(lower,oneLower);
//    }
    
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
//        for (AbsFace f:getLower()) {
//            System.err.println("  > ["+f.dimension+"] " +f);
//        }
        
    }

    protected int getMinDimension() {
        return minDimension;
    }

//    static int cnt = 0;
//    static int cnt2 = 0;
    void thisIsBelowThat(AbsFace b) {
        if (couldAddLower(b)) {
//            addHigher(b);
            b.addLower(this);

            b.setMinDimension(minDimension+1);
//            System.err.println("Y"+cnt2++);
        } else {
//            System.err.println("X"+cnt++);
        }
    }

    boolean couldAddLower(AbsFace a) {
        return a.minDimension <= dimension + 1;
//        return dimension != UNKNOWN && dimension != a.dimension + 1;
//        maxDimension >= a.minDimension - 1;
//                && (!lower.contains(a)) && (!oneLower.contains(a));
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

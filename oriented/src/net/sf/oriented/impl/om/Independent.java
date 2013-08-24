/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Arrays;
import java.util.Iterator;

import net.sf.oriented.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;

public class Independent extends AbsMatroid {

    public Independent(SetOfUnsignedSetInternal signedSets, MatroidAll all) {
        super(signedSets, all);
    }

    @Override
    public void verify() throws AxiomViolation {
        /*
         * 1. The empty set is independent,
         * 2. Every subset of an independent set is independent,
         * 3. If A and B  are two independent sets A has more elements than B, then there exists an element 
         * a in A  that when added to B gives a larger independent set than B. This is sometimes called the augmentation property or the independent set exchange property.
         * */
        verifyEmpty();
        verifySubsets();
        verifyAugmentation();
    }

    private void verifyAugmentation() throws AxiomViolation {
        new ForAllForAllExists<Object,Label>(){

            /**
             * An iterator used in an inner loop for checking the axiom.
             * @return
             */
            @Override
            protected Iterator<? extends Label> innerIterator(UnsignedSet a, UnsignedSet b) {
                return a.minus(b).iterator();
            }

            @Override
            boolean check(Object dummy, UnsignedSet a, UnsignedSet b, Label z) {
                return set.contains(b.union(z));
            }

            @Override
            Iterator<? extends Object> suchThatForAll(UnsignedSet a, UnsignedSet b) {
                return a.size() <= b.size() ? null : Arrays.asList( new Object() ).iterator();
            }
        }.verify();
        
    }

    private void verifySubsets() throws AxiomViolation {
        for (UnsignedSet s:set) {
            for (UnsignedSet subset:s.powerSet()) {
                if (!set.contains(subset)) {
                    throw new AxiomViolation(this,"Contains: "+s+" but not: "+subset);
                }
            }
        }
    }

    private void verifyEmpty() throws AxiomViolation {
        if (!set.contains(ffactory().unsignedSets().empty())) {
            throw new AxiomViolation(this,"The empty set is independent.");
        }
    }

    @Override
    public String toString() {
        return ffactory().independentSets().toString(this);
    }
    
    @Override
    public Independent getIndependentSets() {
        return this;
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

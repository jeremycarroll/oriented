/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

import net.sf.oriented.impl.om.AbsOM;
import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.OMasFaceLattice;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.SignedSet;

import net.sf.oriented.polytope.FaceLatticeImpl.AbsFaceImpl;

class FaceLatticeImpl extends AbsOM<Face> implements OMasFaceLattice, Iterable<AbsFaceImpl> {
    
    static abstract class AbsFaceImpl implements Face {
        
        private final Type type;
        private final SignedSet covector;
        private final int dimension;
        protected final AbsFaceImpl lower[];
        Face higher[];

        AbsFaceImpl(Type t, SignedSet covector, int d, AbsFaceImpl[] lower) {
            type = t;
            this.covector = covector;
            dimension = d;
            this.lower = lower;
        }
        @Override
        public Type type() {
            return type;
        }

        @Override
        public SignedSet covector() {
            return covector;
        }

        @Override
        public int dimension() {
            return dimension;
        }
        @Override
        public Collection<? extends Face> lower() {
            return Arrays.asList(lower);
        }
        @Override
        public Collection<Face> higher() {
            return Arrays.asList(higher);
        }


        private void saveSelfInLowerItems() {
            for (AbsFaceImpl f:lower) {
                f.addHigher(this);
            }
        }

        abstract void addHigher(AbsFaceImpl absFaceImpl);
        abstract void addOne();
        abstract void allocateArrays();

        private void countSelfInLowerItems() {
            for (AbsFaceImpl f:lower) {
                f.addOne();
            }
        }
    }
    static final class TopOrBottomImpl extends AbsFaceImpl {
        TopOrBottomImpl(Type type, int dimension, AbsFaceImpl minOrMax[]) {
            super(type, null, dimension, type==Type.Bottom ? new AbsFaceImpl[0]: minOrMax);
            higher = type==Type.Top ? new Face[0]: minOrMax;
        }

        @Override
        void addHigher(AbsFaceImpl absFaceImpl) {}

        @Override
        void addOne() {}

        @Override
        void allocateArrays() {}

    }

    static final class FaceImpl extends AbsFaceImpl {
        int counter = 0;
        FaceImpl(PFace template,  FaceLatticeImpl parent) {
            super(template.getFaceType(), template.vector(), template.getDimension(),new AbsFaceImpl[template.getLower().size()]);
            int i = 0;
            for (AbsFace l:template.getLower()) {
                lower[i++] = parent.corresponding(l);
            }
            // higher is not yet initialized.
        }
        @Override
        void addHigher(AbsFaceImpl h) {
           higher[counter++] = h;
        }
        @Override
        void addOne() {
           counter++;
        }
        @Override
        void allocateArrays() {
            higher = new AbsFaceImpl[counter];
            counter = 0;
        }
    }
    
    private final AbsFaceImpl top, bottom;
    private final AbsFaceImpl grades[][];


    public FaceLatticeImpl(OMInternal a, DualFaceLattice lattice) {
        super(a);
        grades = new AbsFaceImpl[lattice.maxDimension+2][];
        for (int i=0;i<grades.length;i++) {
            grades[i] = new AbsFaceImpl[lattice.byDimension[i].size()];
        }
        grades[grades.length-1][0]= top = new TopOrBottomImpl(Face.Type.Top, lattice.maxDimension, grades[grades.length-2]);
        grades[0][0]= bottom =  new TopOrBottomImpl(Face.Type.Bottom, -1, grades[1]);
        lattice.top.setFace(top);
        lattice.bottom.setFace(bottom);
        for (int i=1;i<lattice.maxDimension;i++) {
            for (int j=0;j<lattice.byDimension[i].size();j++) {
                PFace template = (PFace)lattice.byDimension[i].get(j);
                FaceImpl ours = new FaceImpl(template,this);
                template.setFace(ours);
                grades[i][j] = ours;
            }
        }
    }
    
    public void init() {
        // count higher items
        for ( AbsFaceImpl f: this ) {
            f.countSelfInLowerItems();
        }
        
        // allocate arrays
        for ( AbsFaceImpl f: this ) {
            f.allocateArrays();
        }
        
        // save higher
        for ( AbsFaceImpl f: this ) {
            f.saveSelfInLowerItems();
        }
    }
    
    

    public AbsFaceImpl corresponding(AbsFace l) {
        return l.asFace();
    }

    @Override
    public void verify() throws AxiomViolation {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Face top() {
        return top;
        
    }

    @Override
    public Face bottom() {
        return bottom;
    }

    @Override
    public Collection<? extends Face> withDimension(int d) {
        return Arrays.asList(grades[d+1]);
    }

    @Override
    public boolean equals(Object o) {
        return all.equals(o);
    }

    @Override
    public Iterator<AbsFaceImpl> iterator2() {
        return iterator();
    }

    @Override
    public Iterator<AbsFaceImpl> iterator() {
        Function<AbsFaceImpl[], Iterator<AbsFaceImpl>> function = new Function<AbsFaceImpl[], Iterator<AbsFaceImpl>>(){
            @Override
            public Iterator<AbsFaceImpl> apply(AbsFaceImpl[] input) {
                return Arrays.asList(input).iterator();
            }
        };
        Iterator<Iterator<AbsFaceImpl>> transformed = Iterators.transform(Arrays.asList(grades).iterator(), function);
        return Iterators.concat(transformed);
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

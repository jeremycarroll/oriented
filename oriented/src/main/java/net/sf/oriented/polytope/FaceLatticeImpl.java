/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.oriented.impl.om.AbsOM;
import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OMasFaceLattice;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;

class FaceLatticeImpl extends AbsOM<Face> implements OMasFaceLattice {
    
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
        

        @Override
        public void verify() throws AxiomViolation {
            for (Face f:lower()) {
                if (f.dimension() != dimension - 1) {
                    throw new AxiomViolation(this," with respect to "+f);
                }
            }
            for (Face f:higher()) {
                if (f.dimension() != dimension + 1) {
                    throw new AxiomViolation(this," with respect to "+f);
                }
            }
            Map<SignedSet,Face> seenOnce = new HashMap<>();
            Map<SignedSet,List<Face>> seenTwice = new HashMap<>();
            for (Face oneDown:lower()) {
                for (Face twoDown: oneDown.lower()) {
                    SignedSet key = twoDown.covector();
                    if (seenTwice.containsKey(key) //&& twoUp instanceof Face && ((Face)twoUp).vector().plus().size()==2
                            ) {
                        throw new AxiomViolation(this, "Interval from "+twoDown+" to "+this+" has more than four members: "+seenTwice.get(twoDown)+","+oneDown);
                    }
                    Face link = seenOnce.remove(key);
                    if (link != null) {
                        seenTwice.put(key,Arrays.asList(link,oneDown));
                    } else {
                        seenOnce.put(key,oneDown);
                    }
                }
            }
            if (!seenOnce.isEmpty()) {
                Entry<SignedSet, Face> entry = seenOnce.entrySet().iterator().next();
                throw new AxiomViolation(this, "Interval from "+this+" to "+entry.getKey() +" has only three members ("+entry.getValue() +")");
            }
            
        }
        
        @Override
        public boolean equals(Object o) {
            return o == this || (o instanceof AbsFaceImpl && this.covector.equals(((AbsFaceImpl)o).covector));
        }
        @Override
        public int hashCode() {
            return covector.hashCode();
        }
        
        @Override
        public String toString() {
            return covector+"["+dimension+":"+higher.length+"-"+lower.length+"]";
        }
    }
    static class TopOrBottomImpl extends AbsFaceImpl {
        TopOrBottomImpl(Type type, SignedSet covector, int dimension, AbsFaceImpl minOrMax[]) {
            super(type, covector, dimension, type==Type.Bottom ? new AbsFaceImpl[0]: minOrMax);
            higher = type==Type.Top ? new Face[0]: minOrMax;
        }

        @Override
        void addHigher(AbsFaceImpl absFaceImpl) {}

        @Override
        void addOne() {}

        @Override
        void allocateArrays() {}
        
//        @Override
//        public String toString() {
//            return type().toString();
//        }

    }
    static final class TopImpl extends TopOrBottomImpl {

        TopImpl(int dimension, AbsFaceImpl[] minOrMax) {
            super(Type.Top, null, dimension, minOrMax);
        }
        @Override
        public void verify() throws AxiomViolation {
            super.verify();
            if (!higher().isEmpty()) {
                throw new AxiomViolation(this," is not top");
            }
            if (lower().isEmpty()) {
                throw new AxiomViolation(this," has nothing below it");
            }
        }
        

        @Override
        public boolean equals(Object o) {
            return o == this || (o instanceof TopImpl);
        }
        @Override
        public int hashCode() {
            return 77;
        }@Override
        public String toString() {
            return "TOP["+dimension()+":0-"+lower.length+"]";
        }
        
    }
    static final class BottomImpl extends TopOrBottomImpl {

        BottomImpl(SignedSet empty, AbsFaceImpl[] minOrMax) {
            super(Type.Bottom, empty, -1, minOrMax);
        }
        

        @Override
        public void verify() throws AxiomViolation {
            super.verify();
            if (!lower().isEmpty()) {
                throw new AxiomViolation(this," is not bottom");
            }
            if (higher().isEmpty()) {
                throw new AxiomViolation(this," has nothing above it");
            }
        }
        
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
//        @Override
//        public String toString() {
//            return type().toString()+": "+covector().toString();
//        }
    }
    
    private final AbsFaceImpl top, bottom;
    private final AbsFaceImpl grades[][];
    private final Map<SignedSet,Face> ss2face = new HashMap<>();
    
    private static final Function<AbsFaceImpl[], Iterator<Face>> ARRAY2ITERATOR = new Function<AbsFaceImpl[], Iterator<Face>>(){
        @Override
        public Iterator<Face> apply(AbsFaceImpl[] input) {
            return cast(Arrays.asList(input).iterator());
        }
    };
    private static final Function<AbsFaceImpl[], Iterable<AbsFaceImpl>> ARRAY2ITERABLE = new Function<AbsFaceImpl[], Iterable<AbsFaceImpl>>(){
        @Override
        public Iterable<AbsFaceImpl> apply(AbsFaceImpl[] input) {
            return Arrays.asList(input);
        }
    };


    public FaceLatticeImpl(OMInternal a, DualFaceLattice lattice) {
        super(a);
        grades = new AbsFaceImpl[lattice.maxDimension+2][];
        for (int i=0;i<grades.length;i++) {
            grades[i] = new AbsFaceImpl[lattice.byDimension[i].size()];
        }
        grades[grades.length-1][0]= top = new TopImpl(lattice.maxDimension, grades[grades.length-2]);
        FactoryFactory ffactory = a.ffactory();
        UnsignedSet empty = ffactory.unsignedSets().empty();
        grades[0][0]= bottom =  new BottomImpl(ffactory.signedSets().construct(empty, empty), grades[1]);
        lattice.top.setFace(top);
        lattice.bottom.setFace(bottom);
        for (int i=1;i<=lattice.maxDimension;i++) {
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
        for ( Face f: this ) {
            ((AbsFaceImpl)f).countSelfInLowerItems();
        }
        
        // allocate arrays
        for ( Face f: this ) {
            ((AbsFaceImpl)f).allocateArrays();
        }
        
        // save higher
        for ( Face f: this ) {
            ((AbsFaceImpl)f).saveSelfInLowerItems();
        }
        
        for ( Face f: this.withDimensions(-1, top.dimension-1)) {
            ss2face.put(f.covector(), f);
        }
    }

    public AbsFaceImpl corresponding(AbsFace l) {
        return l.asFace();
    }

    @Override
    public void verify() throws AxiomViolation {
        for (Face f:this) {
            f.verify();
        }
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
    public Iterator<Face> iterator2() {
        return iterator();
    }

    @Override
    public Iterator<Face> iterator() {
        Iterator<Iterator<Face>> transformed = Iterators.transform(Arrays.asList(grades).iterator(), ARRAY2ITERATOR);
        return Iterators.concat(transformed);
    }
    
    @SuppressWarnings("unchecked")
    static <T, S extends T> Iterable<T> cast(Iterable<S> in) {
        return (Iterable<T>)in;
    }
    @SuppressWarnings("unchecked")
    static <T, S extends T> Iterator<T> cast(Iterator<S> in) {
        return (Iterator<T>)in;
    }
    @Override
    public Iterable<Face> withDimensions(int i, int j) {
        return cast(Iterables.concat(Iterables.transform(Arrays.asList(grades).subList(i+1, j+2), ARRAY2ITERABLE)));
    }

    @Override
    public Face get(SignedSet covector) {
        return ss2face.get(covector);
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

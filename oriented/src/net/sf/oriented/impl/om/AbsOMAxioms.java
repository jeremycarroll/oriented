/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.om;


import net.sf.oriented.impl.items.LabelFactoryImpl;
import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.impl.set.SignedSetFactoryImpl;
import net.sf.oriented.impl.set.UnsignedSetInternal;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.util.combinatorics.Group;
import net.sf.oriented.util.combinatorics.Permutation;

import com.google.common.base.Function;

public abstract class AbsOMAxioms<T> extends AbsAxioms<T> implements OMInternal {


    abstract MatroidAll getMatroidAll();

    abstract void setMatroidAll(MatroidAll m);

    @Override
    public final OM reorient(Label ... axes) {
        @SuppressWarnings("rawtypes")
        AbsOMAxioms<?> rslt = (AbsOMAxioms) this.reorientRaw(axes);
        rslt.setMatroidAll(getMatroidAll());
        return rslt;
    }

    /**
     * This method does the work of reorientation,
     * but doesn't sort out the underlying matroid.
     * The matroid is shared between reorientations of an oriented matroid,
     * this is implemented in the final method above.
     
     * @param axes
     * @return
     */
    protected abstract OMInternal reorientRaw(Label ... axes) ;


    @Override
    public boolean isAcyclic() {
        return getCircuits().isAcyclic();
    }


    @Override
    public int n() {
    	return elements().length;
    }

    @Override
    public Group automorphisms() {
        return getCircuits().automorphisms();
    }
    
    @Override
    public abstract LabelImpl[] elements();

    /**
     * Convert a permutation of the ground set into
     * a function that will map a signed set to the permuted signed set.
     * @param p This is a permutation of the ground set
     */
    @Override
    public Function<SignedSet, SignedSet> signedSetPermuter(Permutation p) {

        final SignedSetFactoryImpl signedSetFactory = (SignedSetFactoryImpl) ffactory().signedSets();
        final Permutation universePermuter = ((LabelFactoryImpl)ffactory().labels()).permuteUniverse(elements(), p);
        return new Function<SignedSet, SignedSet>() {
            @Override
            public SignedSet apply(SignedSet input) {
                return signedSetFactory.construct(
                        ((UnsignedSetInternal)input.plus()).permuteUniverse(universePermuter),
                        ((UnsignedSetInternal)input.minus()).permuteUniverse(universePermuter));
            }
            
        };
    }

    @Override
    public int asInt(String label) {
        return asInt(ffactory().labels().parse(label));
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi.impl.om;


import net.sf.oriented.combinatorics.Group;
import net.sf.oriented.combinatorics.Permutation;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.impl.set.SignedSetInternal;

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
    	return ground().length;
    }

    @Override
    public Group automorphisms() {
        return getCircuits().automorphisms();
    }

    /**
     * Convert a permutation of the ground set into
     * a function that will map a signed set to the permuted signed set.
     * @param p This is a permutation of the ground set
     */
    @Override
    public Function<SignedSet, SignedSet> signedSetPermuter(Permutation p) {

        final Permutation universePermuter = ffactory().labels().permuteUniverse(ground(), p);
        return new Function<SignedSet, SignedSet>() {
            @Override
            public SignedSet apply(SignedSet input) {
                return ((SignedSetInternal)input).permuteUniverse(universePermuter);
            }
            
        };
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

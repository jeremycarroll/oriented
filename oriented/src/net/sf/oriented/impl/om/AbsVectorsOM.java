/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import net.sf.oriented.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMSFactory;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.util.combinatorics.Permutation;

abstract class AbsVectorsOM extends AbsVectors implements OMSInternal {

	AbsVectorsOM(SetOfSignedSetInternal c, Cryptomorphisms cry, OMInternal a) {
		super(c, a);
		all.set(cry, this);
	}
	

    @Override
    public boolean isAcyclic() {
        for (SignedSet ss :vectors){
            if ( ss.minus().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public OM permuteGround(Permutation p) {
        return omsFactory().fromSignedSets(p.permute(ground()), vectors);
    }

    @Override
    public OMSInternal reorientRaw(Label ... axes) {
        return omsFactory().fromSignedSets(ground(), vectors.reorientRaw(axes));
    }


    abstract OMSFactory omsFactory();


    @Override
    public String toString() {
    	return omsFactory().toString(this);
    }

}
/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * 
 * 
 * 
 * 
 * 
 * The Java Oriented Matroid Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Java Oriented Matroid Library. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;

public abstract class AbsOMAxioms<T> extends AbsAxioms<T> implements OMInternal {


    @Override
    public final OM reorient(Label ... axes) {
        OMInternal rslt = this.reorientRaw(axes);
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

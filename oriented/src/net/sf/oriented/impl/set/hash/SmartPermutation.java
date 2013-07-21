/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.set.hash;

import java.util.List;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.LabelFactory;
import net.sf.oriented.util.combinatorics.Permutation;

public class SmartPermutation extends Permutation {
    
    public SmartPermutation(int ... perm) {
        super(perm);
    }

    LabelImpl get(LabelImpl m) {
        int ix = m.ordinal();
        if (ix >= n()) {
            return m;
        }
        List<Label> universe = ((LabelFactory)m.factory()).getUniverse();
        return (LabelImpl) universe.get(get(m.ordinal()));
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

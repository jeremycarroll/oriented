/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import net.sf.oriented.omi.FullChirotope;
import net.sf.oriented.util.combinatorics.Permutation;

class PermutedChirotope implements FullChirotope {

    private final Permutation permutation;
    private final FullChirotope base;
    public PermutedChirotope(Permutation p, FullChirotope base) {
        this.base = base;
        this.permutation = p;
        if (p.n() != base.n()) {
            throw new IllegalArgumentException();
        }
    }
    @Override
    public int rank() {
        return base.rank();
    }

    @Override
    public int n() {
        return base.n();
    }

    @Override
    public int chi(int ... index) {
        return base.chi(permutation.mapAll(index));
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

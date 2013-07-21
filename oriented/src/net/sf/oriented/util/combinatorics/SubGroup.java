/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util.combinatorics;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class SubGroup extends Group {

    private final Set<Permutation> elements;
    public SubGroup(Collection<Permutation> elements) {
        super(elements.iterator().next().n());
        this.elements = new HashSet<Permutation>(elements);
    }

    @Override
    public Iterator<Permutation> iterator() {
        return elements.iterator();
    }

    @Override
    public long order() {
        return elements.size();
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

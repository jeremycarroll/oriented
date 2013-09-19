/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util.graph;

import java.util.Arrays;
import java.util.List;


public class SimplePath<V> implements Path<V> {
    final Object path[];
    
    protected SimplePath(V from, V to) {
        path = new Object[]{from,to};
    }

    protected SimplePath(SimplePath<V> first, SimplePath<V> andThen) {
        path = new Object[first.path.length+andThen.path.length-1];
        System.arraycopy(first.path, 0, path, 0, first.path.length);
        System.arraycopy(andThen.path, 1, path, first.path.length, andThen.path.length - 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getSource() {
        return (V)path[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getDestination() {
        return (V)path[path.length-1];
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<V> getPath() {
        return (List<V>) Arrays.asList(path);
    }
    
    protected boolean canBeFollowedBy(SimplePath<V> p) {
        if (!getDestination().equals(p.getSource())) {
            return false;
        }
        for (int i=0;i<path.length;i++) {
            for (int j=1;j<p.path.length;j++) {
                if (path[i].equals(p.path[j]) && (i!=0 || j!= p.path.length-1) ) {
                    return false;
                }
            }
        }
            
        return true;
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

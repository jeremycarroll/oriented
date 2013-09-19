/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util.graph;


import net.sf.oriented.util.graph.SimplePaths.SimplePath;
import edu.uci.ics.jung.graph.Graph;

public class SimplePaths<V,E> extends AbsPaths<V,E,SimplePath<V>> {

    
    public SimplePaths(Graph<V, E> g) {
        super(g);
    }

    static final class SimplePath<V> implements Path<V> {
        final Object path[];
        
        public SimplePath(V from, V to) {
            path = new Object[]{from,to};
        }

        public SimplePath(SimplePath<V> first, SimplePath<V> andThen) {
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

        @Override
        public Object[] getPath() {
            return path;
        }
        
        boolean canBeFollowedBy(SimplePath<V> p) {
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

    @Override
    protected SimplePath<V> singleStep(Graph<V, E> g, V from, V to) {
        return new SimplePath<V>(from,to);
    }

    @Override
    protected SimplePath<V> combinePaths(SimplePath<V> first, SimplePath<V> andThen) {
        if (first.canBeFollowedBy(andThen)) {
           return new SimplePath<V>(first,andThen);
        } else {
            return null;
        }
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util.graph;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;


public class SimplePath<V> implements Path<V> {
    protected final V path[];
    
    @SuppressWarnings("unchecked")
    protected SimplePath(Class<V> clazzV, V from, V to) {
        path = (V[]) Array.newInstance(clazzV, 2);
        path[0] = from;
        path[1] = to;
    }

    protected SimplePath(SimplePath<V> first, SimplePath<V> andThen) {
        path = Arrays.copyOf(first.path, first.path.length + andThen.path.length - 1);
        System.arraycopy(andThen.path, 1, path, first.path.length, andThen.path.length - 1);
    }

    @Override
    public V getSource() {
        return path[0];
    }

    @Override
    public V getDestination() {
        return path[path.length-1];
    }

    @Override
    public List<V> getPath() {
        return Arrays.asList(path);
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
    
    static <V,E> PathFactory<V,SimplePath<V>> createFactory(Graph<V,E> g) {
        Iterator<V> it = g.getVertices().iterator();
        if (!it.hasNext()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final Class<V> clazzV =(Class<V>) it.next().getClass();
        return new PathFactory<V,SimplePath<V>>(){

            @Override
            public SimplePath<V> create(V from, V to) {
                return new SimplePath<>(clazzV,from,to);
            }


            @Override
            public SimplePath<V> combine(SimplePath<V> first,SimplePath<V> andThen) {
                if (first.canBeFollowedBy(andThen)) {
                    return new SimplePath<>(first,andThen);
                }
                return null;
            }};
        
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

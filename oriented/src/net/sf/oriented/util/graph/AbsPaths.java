/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util.graph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;

import edu.uci.ics.jung.graph.Graph;


public abstract class AbsPaths<V, E, P extends Path<V>> {
    


    private final List<V> vertex;
    private final Map<V,Integer> vertexIndex = new HashMap<V,Integer>();
    /**
     * if the k-th bit is set in path[i][j]
     * then 
     * to get from i to j, one way is to go to k first.
     */
    private final List<P>[][] paths;
    
    protected abstract P singleStep(Graph<V,E>  g, V from, V to) ;
    protected abstract P combinePaths( P first, P andThen) ;
    public AbsPaths( Graph<V,E>  g ) {
        Collection<V> vv = g.getVertices();
        paths = (List<P>[][]) Array.newInstance(List.class, vv.size(), vv.size());
        vertex = new ArrayList<V>(vv);
        initializeVertexIndex();
        initializePathsOfLengthOne(g);
        royWarshall();
    }

    private void initializeVertexIndex() {
        for (int i=0;i<vertex.size();i++) {
            vertexIndex.put(vertex.get(i), i);
        }
    }

    private void initializePathsOfLengthOne(Graph<V,E> graph) {
        for (int i=0;i<vertex.size();i++) {
            V from = vertex.get(i);
            for (V to : graph.getNeighbors(from) ) {
                int j = vertexIndex.get(to);
                P v = singleStep(graph, from, to);
                if (v!=null) {
                    paths[i][j] = new ArrayList<P>();
                    paths[i][j].add(v);
                }
            }
        }
    }

    private void royWarshall() {
        int size = paths.length;
        for (int k=0;k<size;k++) {
            for (int i=0;i<size;i++) {
                if (paths[i][k] == null || i==k) {
                    continue;
                }
                for (int j=0;j<size;j++) {
                    if (paths[k][j] == null || k==j) {
                        continue;
                    }
                    if (paths[i][j] == null) {
                        paths[i][j] = new ArrayList<P>();
                    }
                    for (P p:paths[i][k]) {
                        for (P q:paths[k][j]) {
                            P pq = combinePaths(p,q);
                            if (pq!=null) {
                                paths[i][j].add(pq);
                            }
                        }
                    }
                }
            }
        }
    }

    public Iterable<List<V>> paths(V from, V to) {
        final int source = vertexIndex.get(from);
        final int dest = vertexIndex.get(to);
        return new Iterable<List<V>>(){
            @Override
            public Iterator<List<V>> iterator() {
                return Iterators.transform(paths[source][dest].iterator(),
                        new Function<Path<V>,List<V>>(){
                            @SuppressWarnings("unchecked")
                            @Override
                            public List<V> apply(Path<V> input) {
                                return (List<V>) Arrays.asList(input.getPath());
                            }
                });
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

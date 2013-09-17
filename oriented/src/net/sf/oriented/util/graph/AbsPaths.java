/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util.graph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;


public abstract class AbsPaths<V, E, P extends Path<V>> {
    


    private final List<V> vertex;
    private final Map<V,Integer> vertexIndex = new HashMap<V,Integer>();
    /**
     * if the k-th bit is set in path[i][j]
     * then 
     * to get from i to j, one way is to go to k first.
     */
    private final BitSet[][] paths;
    
    protected abstract P singleStep(Graph<V,E>  g, V from, V to) ;
    
//    {
//        return new SimplePath<V>(from, to);
//    }
    protected abstract P combinePaths( P first, P andThen) ;
//    {
//        return null; // new SimplePath<V>(first, andThen);
//    }
    public AbsPaths( Graph<V,E>  g ) {
        Collection<V> vv = g.getVertices();
        paths = (BitSet[][]) Array.newInstance(BitSet.class, vv.size(), vv.size());
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
                paths[i][j] = new BitSet();
                paths[i][j].set(j);
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
                        paths[i][j] = new BitSet();
                    }
                    paths[i][j].or(paths[i][k]);
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
                return new Iterator<List<V>>(){
                    final private BitSet visited = new BitSet();
                    final private int path[] = new int[vertex.size()];
                    private int currentPos = 0;
                    private boolean isValid = false;
                    private boolean exhausted = false;
                    {
                        path[0] = source;
                        if (source != dest) {
                           visited.set(path[0]);
                        }
                        isValid = advance(0);
                        if (!isValid) {
                            exhausted = true;
                        }
                    }
                    
                    private boolean advance(int fromHere) {
                        while (true) {
                            BitSet next =paths[path[currentPos]][dest];
                            int step;
                            if (next != null) {
                                next =  (BitSet) next.clone();
                                next.andNot(visited);
                                step = next.nextSetBit(fromHere);
                            } else {
                                step = -1;
                            }
                            if (step < 0) {
                                if (currentPos==0) {
                                    return false;
                                }
                                else {
                                    int old = path[currentPos];
                                    visited.clear(old);
                                    currentPos--;
                                    fromHere = old+1;
                                }
                            } else {
                                currentPos++;
                                path[currentPos] = step;
                                visited.set(step);
                                if (step==dest) {
                                    return true;
                                } else {
                                    boolean rslt = advance(0);
                                    if (rslt) {
                                        return true;
                                    }
                                    if (currentPos == 0) {
                                        return false;
                                    }
                                    visited.clear(step);
                                    path[currentPos] = step;
                                    currentPos--;
                                    fromHere = step+1;
//                                    fromHere = 0;
                                }
                            }
                        }
                    }
                    
                    private void advance() {
                           visited.clear(path[currentPos]);
                           currentPos--;
                           boolean rslt = advance(path[currentPos+1]+1);
                           if (rslt) {
                               isValid = true;
                               return;
                           } else {
                               isValid = false;
                               exhausted = true;
                           }
                    }

                    @Override
                    public boolean hasNext() {
                        if (!isValid && !exhausted) {
                            advance();
                        }
                        return isValid;
                    }

                    @Override
                    public List<V> next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        isValid = false;
                        List<V> soln = new ArrayList<V>(currentPos);
                        for (int i=0;i<=currentPos;i++) {
                            soln.add(vertex.get(path[i]));
                        }
                        return soln;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }};
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

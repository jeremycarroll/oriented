/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util.graph;


/**
 * An instance implementing this interface is
 * responsible for creating paths of length 1,
 * and then combining them.
 * If an instance of this class needs to refer to the graph,
 * for example, to check the direction of an edge between two vertices,
 * then it needs to have access to that graph, for example, as an instance field.
 * The convention is that the factory methods may return null to indicate
 * that there is no such path.
 * @author jeremycarroll
 *
 * @param <V>
 * @param <P>
 */
public interface PathFactory<V, P extends Path<V>> {
    /**
     * Create a path of length 1 in the graph.
     * 
     * For each pair of incident vertices this method is invoked twice, with
     * the arguments reversed. For each loop this method is invoked once.
     * @param from
     * @param to    A vertex such that there is an edge of the graph incident with both from and to.
     * @return The new path, or null if there is no such path
     */
    P create(V from, V to) ;
    /**
     * Combine two paths to make a longer path.
     * 
     * This method should return null if the two paths cannot be combined.
     * A cycle is a combination of two paths (in which case {@link Path#getSource()}
     * and {@link Path#getDestination()} are equal.
     * @param first
     * @param andThen
     * @return The combined path or null if the two paths cannot be combined.
     */
    P combine(P first,P andThen);

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

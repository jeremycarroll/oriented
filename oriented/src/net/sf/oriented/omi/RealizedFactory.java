/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.omi;

import java.util.Collection;

import net.sf.oriented.util.matrix.RationalMatrix;

/**
 * An interface for producing oriented matroids from rational matrixes
 * which realize them. Such matrices are retrievable later.
 * Note for some case {@link FactoryFactory#fromMatrix(double, double[][])}
 * may be more appropriate.
 * @author jeremycarroll
 *
 */
public interface RealizedFactory extends Factory<OMasRealized> {
    /**
     * Construct an oriented matroid from a matrix,
     * using default labels for the elements.
     * @param matrix
     * @return An oriented matroid.
     */
	OMasRealized construct(RationalMatrix matrix);
    /**
     * Construct an oriented matroid from a matrix,
     * using given labels for the elements.
     * @param matrix
     * @param elements The labels for the elements
     * @return An oriented matroid.
     */

	OMasRealized construct(Collection<? extends Label> elements, RationalMatrix matrix);
}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * 
 * 
 * 
 * 
 * 
 * The Java Oriented Matroid Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Java Oriented Matroid Library. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

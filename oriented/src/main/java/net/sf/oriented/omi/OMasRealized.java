/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.omi;

import net.sf.oriented.util.matrix.RationalMatrix;

/**
 * For an oriented matroid which was produced, or whose dual was produced,
 * from a rational matrix, retrieve that view.
 * Note this is not implemented for matroids produced from double precision
 * matrixes from {@link FactoryFactory#fromMatrix(double, double[][])}
 * @author jeremycarroll
 *
 */
public interface OMasRealized extends OM {

    /**
     * An underlying matrix realizing this oriented matroid.
     * @return An underlying matrix realizing this oriented matroid.
     */
	RationalMatrix getMatrix();


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

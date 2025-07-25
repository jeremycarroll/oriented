/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

/**
 * The representation of rank 3 oriented matroids as pseudoline drawings
 * in the Euclidean plane can only draw co-loops as the line at infinity.
 * This exception indicates that a co-loop was not the line at infinity.
 * @author jeremycarroll
 *
 */
public class CoLoopCannotBeDrawnException extends Exception {


    CoLoopCannotBeDrawnException(String message) {
        super(message);
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

/**
 * An algorithm that only applies to rank 3 oriented matroids was
 * applied to an oriented matroid of a different rank.
 * The focus of this library is on rank 3 oriented matroids.
 * @author jeremycarroll
 *
 */
public class NotRank3Exception extends IllegalArgumentException {

    NotRank3Exception(String s) {
        super(s);
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

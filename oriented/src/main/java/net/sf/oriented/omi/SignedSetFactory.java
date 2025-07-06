/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * A factory for producing signed sets.
 * As well as the usual methods for to and from a string
 * representation, we can also build a signed set from two disjoint
 * unsigned sets.
 * @author jeremycarroll
 *
 */
public interface SignedSetFactory extends Factory<SignedSet> {

    /**
     * Build a signed set from two disjoint unsigned sets.
     * @param plus The positively signed elements
     * @param minus The negatively signed elements
     * @return The corresponding signed set.
     */
    SignedSet construct(UnsignedSet plus, UnsignedSet minus);

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

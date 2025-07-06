/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Collection;


/**
 * A factory for producing labels, which are used as the points or elements
 * on which our oriented matroids and matroids are defined.
 * Internally labels are ordered and this is implicit in some places in the interface,
 * of exmaple {@link #getUniverse()}<code>.iterator()</code> will return the labels in order.
 * @author jeremycarroll
 *
 */
public interface LabelFactory extends Factory<Label> {

    /**
     * 
     * @return A java collection of all the labels used so far by this factory.
     */
    Collection<Label> getUniverse();

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

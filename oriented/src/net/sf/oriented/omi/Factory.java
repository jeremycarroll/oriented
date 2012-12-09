/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
************************************************************************/
package net.sf.oriented.omi;

/**
 * Provides conversion to and from String for some class.
 * @author jeremy
 *
 * @param <T> The things to create.
 */
public interface Factory<T> {
/**
 * Create a new T.
 * @param s A Sring representing T.
 * @return The new T
 */
    T parse(String s);
    /**
     * Convert a T into a String
     * @param t The item to convert.
     * @return A String representing t.
     */
	String toString(T t);

	 /**
	  * A suggested Java Collection for gathering up some such items.
	  * @return A Java Collection
	  */
	 JavaSet<? extends T> emptyCollectionOf();
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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * The base items in sets and signed sets are these, which essentially
 * correspond to Strings.
 * Reimplementing this interface may, or may not, work. It is not tested, so it probably
 * will not.
 * 
 * @author jeremy
 * 
 */
public interface Label {
	/**
	 * The corresponding String.
	 * 
	 * @return The corresponding String.
	 */
	public abstract String label();

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

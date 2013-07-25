/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * Mathematical sets of signed sets, as defined in Oriented Matroid literature.
 * @see Bibliography#björnerEtAl1999
 * @author jeremycarroll
 *
 */
public interface SetOfSignedSet extends SetOf<SignedSet, SetOfSignedSet> {

    /**
     * This is the union over this set of the support of each member.
     * @return The set of all elements mentioned in an element in this set.
     */
	public UnsignedSet support();

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

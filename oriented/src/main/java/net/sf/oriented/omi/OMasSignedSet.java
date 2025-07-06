/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * An oriented matroid viewed as a set of signed sets:
 * a set of circuits, a set of vectors, or a set of maximum vectors.
 * 
 * Note that the equality contract from {@link OM} trumps
 * the {@link SetOf#sameSetAs(SetOf)} view of equality.
 * @author jeremycarroll
 *
 */
public interface OMasSignedSet extends OM, SetOfSignedSet {

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

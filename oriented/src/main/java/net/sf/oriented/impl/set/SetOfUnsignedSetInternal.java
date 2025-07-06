/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.set;

import net.sf.oriented.omi.SetOfUnsignedSet;
import net.sf.oriented.omi.UnsignedSet;

public interface SetOfUnsignedSetInternal
		extends
		SetOfInternal<UnsignedSetInternal, SetOfUnsignedSetInternal, UnsignedSet, SetOfUnsignedSet, UnsignedSetInternal, SetOfUnsignedSetInternal>,
		SetOfUnsignedSet {

	/**
	 * The union of all the members of this set.
	 * 
	 * @return the union.
	 */
    @Override
	UnsignedSetInternal union();

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

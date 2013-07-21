/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.set;

import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public interface SetOfSignedSetInternal
		extends
		SetOfInternal<SignedSetInternal, SetOfSignedSetInternal, SignedSet, SetOfSignedSet, SignedSetInternal, SetOfSignedSetInternal>,
		SetOfSignedSet {


	@Override
	public UnsignedSetInternal support();


	SetOfUnsignedSetInternal unsignedSets();

	/**
	 * 
	 * @param u
	 * @return those members whose support is u
	 */
	SetOfSignedSetInternal withSupport(UnsignedSetInternal u);

	/**
	 * 
	 * @param x
	 * @return those members which conform with x
	 */
	public SetOfSignedSetInternal conformingWith(SignedSetInternal x);

	/**
	 * 
	 * @param x0
	 * @return the set formed by restricting each member to x0
	 */
	public SetOfSignedSetInternal restriction(UnsignedSet x0);
	

    public SetOfSignedSet reorientRaw(Label ... axes);



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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.set;

import net.sf.oriented.impl.items.HasFactory;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public interface SignedSetInternal extends
		HasFactory<SignedSetInternal, SignedSet, SignedSetInternal>, SignedSet {

	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.SignedSetI#opposite()
	 */
	@Override
	SignedSetInternal opposite();

	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.SignedSetI#plus()
	 */
	@Override
	UnsignedSetInternal plus();

	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.SignedSetI#minus()
	 */
	@Override
	UnsignedSetInternal minus();


	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.SignedSetI#compose(omi.SignedSet)
	 */
	@Override
	SignedSetInternal compose(SignedSet b);


	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.SignedSetI#support()
	 */
	@Override
	UnsignedSetInternal support();

	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.SignedSetI#restriction(omi.UnsignedSetI)
	 */
	@Override
	SignedSetInternal restriction(UnsignedSet x);


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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
************************************************************************/
package net.sf.oriented.omi.impl.set;

import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.HasFactory;

public interface SignedSetInternal 
extends
HasFactory<SignedSetInternal, SignedSet, SignedSetInternal>,SignedSet
{

	/* (non-Javadoc)
	 * @see omi.SignedSetI#opposite()
	 */
	@Override
	SignedSetInternal opposite();

	/* (non-Javadoc)
	 * @see omi.SignedSetI#plus()
	 */
	@Override
	UnsignedSetInternal plus();

	/* (non-Javadoc)
	 * @see omi.SignedSetI#minus()
	 */
	@Override
	UnsignedSetInternal minus();

	/* (non-Javadoc)
	 * @see omi.SignedSetI#equalsIgnoreSign(omi.SignedSetI)
	 */
	@Override
	boolean equalsIgnoreSign(SignedSet s);

	/* (non-Javadoc)
	 * @see omi.SignedSetI#equalsOpposite(omi.SignedSetI)
	 */
	@Override
	boolean equalsOpposite(SignedSet s);

	/* (non-Javadoc)
	 * @see omi.SignedSetI#separation(omi.SignedSet)
	 */
	@Override
	UnsignedSet separation(SignedSet b);

	/* (non-Javadoc)
	 * @see omi.SignedSetI#compose(omi.SignedSet)
	 */
	@Override
	SignedSetInternal compose(SignedSet b);

	/* (non-Javadoc)
	 * @see omi.SignedSetI#size()
	 */
	@Override
	int size();

	/* (non-Javadoc)
	 * @see omi.SignedSetI#conformsWith(omi.SignedSet)
	 */
	@Override
	boolean conformsWith(SignedSet a);

	/* (non-Javadoc)
	 * @see omi.SignedSetI#support()
	 */
	@Override
	UnsignedSetInternal support();

	/* (non-Javadoc)
	 * @see omi.SignedSetI#sign(omi.Label)
	 */
	@Override
	int sign(Label e);

	/* (non-Javadoc)
	 * @see omi.SignedSetI#isRestrictionOf(omi.SignedSetI)
	 */
	@Override
	boolean isRestrictionOf(SignedSet x);

	/* (non-Javadoc)
	 * @see omi.SignedSetI#restriction(omi.UnsignedSetI)
	 */
	@Override
	SignedSetInternal restriction(UnsignedSet x);

}/************************************************************************
    This file is part of the Java Oriented Matroid Library.

     
     
     
    

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/

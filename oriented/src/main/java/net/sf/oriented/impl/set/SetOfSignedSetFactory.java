/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.set;

import java.util.Collection;

import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;

public class SetOfSignedSetFactory
		extends
		SetFactoryImpl<SignedSetInternal, SetOfSignedSetInternal, SignedSet, SetOfSignedSet, SignedSetInternal, SetOfSignedSetInternal> {

	@Override
	public SignedSetFactoryImpl itemFactory() {
		return (SignedSetFactoryImpl) super.itemFactory();
	}

	final private SetOfUnsignedSetFactory setOfUnsignedSetFactory;

	public SetOfUnsignedSetFactory setOfUnsignedSetFactory() {
		return setOfUnsignedSetFactory;
	}

	final boolean isSymmetric;

	public SetOfSignedSetFactory(UnsignedSetFactory f, boolean symmetric) {
		super(new SignedSetFactoryImpl(f.itemFactory(), f));
		isSymmetric = symmetric;
		setOfUnsignedSetFactory = new SetOfUnsignedSetFactory(f);
	}

	public SetOfSignedSetFactory(SetOfSignedSetFactory pair, boolean symmetric) {
		super(pair.itemFactory);
		isSymmetric = symmetric;
		setOfUnsignedSetFactory = pair.setOfUnsignedSetFactory;
	}

	// public SetOfSignedSetInternal
	// fromBackingCollection(JavaSet<SignedSetInternal> c) {
	// return construct(c,this);
	// }

	@Override
	protected void add(Collection<SignedSetInternal> rslt, SignedSet ee) {
		super.add(rslt, ee);
		if (isSymmetric) {
			super.add(rslt, ee.opposite());
		}
	}

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

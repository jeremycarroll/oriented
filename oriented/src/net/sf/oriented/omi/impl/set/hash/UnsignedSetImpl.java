/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.set.hash;

import net.sf.oriented.combinatorics.Permutation;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;

public class UnsignedSetImpl
		extends
		SetImpl<LabelImpl, UnsignedSetInternal, Label, UnsignedSet, LabelImpl, UnsignedSetInternal>
		implements UnsignedSetInternal {

	public UnsignedSetImpl(JavaSet<LabelImpl> a, UnsignedSetFactory f) {
		super(a, f);
	}

    @Override
    public UnsignedSetImpl permuteUniverse(Permutation universePermuter) {
        SmartPermutation s = (SmartPermutation)universePermuter;
        JavaSet<LabelImpl> result = factory().itemFactory().emptyCollectionOf();
        for (LabelImpl m:this) {
            result.add(s.get(m));
        }
        return new UnsignedSetImpl(result,(UnsignedSetFactory) factory());
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

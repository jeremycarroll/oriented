/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.MFactory;
import net.sf.oriented.omi.MatroidS;
import net.sf.oriented.omi.SetOfUnsignedSet;
import net.sf.oriented.omi.UnsignedSet;

public abstract class MatroidFactory extends
		AbsMatroidFromSetFactory<MatroidS,  SetOfUnsignedSet, SetOfUnsignedSetInternal,SetOfUnsignedSetFactory>
		implements MFactory {

	private static final class CFactory extends MatroidFactory {
		private CFactory(FactoryFactory f) {
			super(f);
		}

		@Override
		MatroidS construct(SetOfUnsignedSetInternal signedSets, MatroidAll all) {
			return new MatroidCircuits(signedSets, all);
		}

		@Override
		public String toString(MatroidS s) {
			return formatString(s, s.getCircuits());
		}
	}

	private static final class BFactory extends MatroidFactory {
		private BFactory(FactoryFactory f) {
			super(f);
		}

		@Override
		public String toString(MatroidS s) {
			return formatString(s, s.getBases());
		}

		@Override
		MatroidS construct(SetOfUnsignedSetInternal signedSets, MatroidAll all) {
			return new Bases(signedSets, all);
		}
	}

	MatroidFactory(FactoryFactory f) {
		super(f, (SetOfUnsignedSetFactory) f.setsOfUnsignedSet());
	}

	static public MatroidFactory circuits(FactoryFactory f) {
		return new CFactory(f);
	}

	static public MatroidFactory bases(FactoryFactory f) {
		return new BFactory(f);
	}

	@Override
	public MatroidS copyBackingCollection(Collection<? extends UnsignedSet> c) {
		return construct(sets.copyBackingCollection(c));
	}

	@Override
	MatroidS construct(SetOfUnsignedSet signedSets) {
		Collection<Label> ground = new TreeSet<Label>(signedSets.union().asCollection());
		return construct(ground, signedSets);
	}

	@Override
	MatroidS construct(Collection<? extends Label> ground,
			SetOfUnsignedSet signedSets) {
		LabelImpl[] g = ground.toArray(new LabelImpl[0]);
		MatroidAll all = new MatroidAll(g, null, factory);
		return construct((SetOfUnsignedSetInternal)signedSets, all);
	}

	abstract MatroidS construct(SetOfUnsignedSetInternal signedSets,
			MatroidAll all);

	@Override
	public MatroidS fromSets(SetOfUnsignedSet sym) {
		return construct(sets.remake(sym));
	}

	@Override
	public List<Label> ground(MatroidS s) {
		return Arrays.asList(s.ground());
	}

}
/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
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

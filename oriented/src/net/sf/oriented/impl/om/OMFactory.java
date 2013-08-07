/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.impl.set.SetOfSignedSetFactory;
import net.sf.oriented.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMSFactory;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;

abstract public class OMFactory extends
		AbsMatroidFromSetFactory<OMasSignedSet,  SetOfSignedSet, SetOfSignedSetInternal,SetOfSignedSetFactory>
		implements OMSFactory {
	private static final class CFactory extends OMFactory {
		private CFactory(FactoryFactory f) {
			super(f);
		}

		@Override
		OMSInternal construct(SetOfSignedSetInternal signedSets, OMAll all) {
			return new Circuits(signedSets, all);
		}

		@Override
		public String toString(OMasSignedSet s) {
			return formatString(s, s.getCircuits());
		}
	}

	private static final class VFactory extends OMFactory {
		private VFactory(FactoryFactory f) {
			super(f);
		}

		@Override
		public String toString(OMasSignedSet s) {
			return formatString(s, s.getVectors());
		}

		@Override
		OMSInternal construct(SetOfSignedSetInternal signedSets, OMAll all) {
			return new Vectors(signedSets, all);
		}
	}

	private static final class MVFactory extends OMFactory {
		private MVFactory(FactoryFactory f) {
			super(f);
		}

		@Override
		OMSInternal construct(SetOfSignedSetInternal signedSets, OMAll all) {
			return new MaxVectors(signedSets, all);
		}

		@Override
		public String toString(OMasSignedSet s) {
			return formatString(s, s.getMaxVectors());
		}
	}

	OMFactory(FactoryFactory f) {
		super(f, (SetOfSignedSetFactory) f.symmetricSetsOfSignedSet());
	}

	static public OMFactory circuits(FactoryFactory f) {
		return new CFactory(f);
	}

	static public OMFactory vectors(FactoryFactory f) {
		return new VFactory(f);
	}

	static public OMFactory maxVectors(FactoryFactory f) {
		return new MVFactory(f);
	}

	@Override
	public OMasSignedSet copyBackingCollection(Collection<? extends SignedSet> c) {
		return construct(sets.copyBackingCollection(c));
	}

	@Override
	OMasSignedSet construct(SetOfSignedSet signedSets) {
		Set<Label> ground = new TreeSet<Label>(signedSets.setOfElements()
				.asCollection());
		return construct(ground, signedSets);
	}

	@Override
	OMSInternal construct(Collection<? extends Label> ground,
			SetOfSignedSet signedSets) {
		LabelImpl[] g = ground.toArray(new LabelImpl[0]);
		OMAll all = new OMAll(g, factory);
		return construct((SetOfSignedSetInternal)signedSets, all);
	}

	abstract OMSInternal construct(SetOfSignedSetInternal signedSets, OMAll all);

	@Override
	public OMSInternal fromSignedSets(Label[] ground, SetOfSignedSet sym) {
		return construct(Arrays.asList(ground), sets.remake(sym));
	}

	@Override
	public List<Label> ground(OMasSignedSet s) {
		return Arrays.asList(s.elements());
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

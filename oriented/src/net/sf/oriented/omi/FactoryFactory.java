/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import net.sf.oriented.combinatorics.Permutation;
import net.sf.oriented.omi.impl.items.LabelFactory;
import net.sf.oriented.omi.impl.om.MatroidFactory;
import net.sf.oriented.omi.impl.om.OMChirotopeFactory;
import net.sf.oriented.omi.impl.om.OMFactory;
import net.sf.oriented.omi.impl.om.OMRealizedFactory;
import net.sf.oriented.omi.impl.set.SetOfSignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.omi.impl.set.SignedSetFactory;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;

/**
 * This class generates {@link Factory}'s and {@link SetFactory}'s for the
 * interfaces in the API. Depending on the {@link Options} passed to the
 * constructor, the String representations used by all the factories generated
 * will vary. It is, in general, not possible to pass a String representation
 * output by one group of factories, arising from a particular
 * {@link FactoryFactory} to another group of factories, arising from a
 * different {@link FactoryFactory}. It is however possible to pass an item
 * constructed from String's between groups of factories.
 * 
 * @author jeremy
 * 
 */
final public class FactoryFactory {
	private final LabelFactory label;
	private SetOfSignedSetFactory setsOfSignedSet;
	private SetOfSignedSetFactory symmetricSetsOfSignedSet;
	private SetOfUnsignedSetFactory setsOfUnsignedSet;
	private UnsignedSetFactory unsignedSets;
	private SignedSetFactory signedSets;
	private OMSFactory circuits, vectors, maxVectors;
	private ChirotopeFactory chirotope;
	private Factory<MatroidS> bases;
	private Factory<MatroidS> unsignedCircuits;
	private RealizedFactory realized;

	private final Options options;

	private void init() {
		if (setsOfUnsignedSet != null)
			return;

		unsignedSets = new UnsignedSetFactory(label);
		setsOfSignedSet = new SetOfSignedSetFactory(unsignedSets, false);
		symmetricSetsOfSignedSet = new SetOfSignedSetFactory(setsOfSignedSet,
				true);
		setsOfUnsignedSet = symmetricSetsOfSignedSet.setOfUnsignedSetFactory();

		signedSets = symmetricSetsOfSignedSet.itemFactory();

		circuits = OMFactory.circuits(this);
		vectors = OMFactory.vectors(this);
		maxVectors = OMFactory.maxVectors(this);
		chirotope = new OMChirotopeFactory(this);

		bases = MatroidFactory.bases(this);
		unsignedCircuits = MatroidFactory.circuits(this);
		realized = new OMRealizedFactory(this);
	}

	/**
	 * A new {@link FactoryFactory} with default options.
	 * 
	 */
	public FactoryFactory() {
		this(new Options());
	}

	/**
	 * A new {@link FactoryFactory} with given {@link Options}. These control
	 * the String representations used.
	 * 
	 */
	public FactoryFactory(Options opt) {
		label = opt.getLabelFactory();
		init();
		options = opt;
	}

	/**
	 * A factory for labels.
	 * 
	 * @return A factory for labels.
	 */
	public LabelFactory labels() {
		return label;
	}

	/**
	 * A factory for matroids from bases.
	 * 
	 * @return A factory for matroids.
	 */
	public Factory<MatroidS> bases() {
		return bases;
	}

	/**
	 * A factory for matroids from circuits.
	 * 
	 * @return A factory for matroids.
	 */
	public Factory<MatroidS> unsignedCircuits() {
		return unsignedCircuits;
	}

	/**
	 * A factory for oriented matroids which uses the circuit representation.
	 * 
	 * @return A factory for oriented matroids using circuits.
	 */
	public OMSFactory circuits() {
		return circuits;
	}

	/**
	 * A factory for oriented matroids which uses the chirotope representation.
	 * 
	 * @return A factory for oriented matroids using chirotopes.
	 */
	public ChirotopeFactory chirotope() {
		return chirotope;
	}

	/**
	 * A factory for oriented matroids which uses the vector representation.
	 * 
	 * @return A factory for oriented matroids using vectors.
	 */
	public OMSFactory vectors() {
		return vectors;
	}

	/**
	 * A factory for oriented matroids which uses the maximum vector (co-tope)
	 * representation.
	 * 
	 * @return A factory for oriented matroids using maximum vectors.
	 */
	public OMSFactory maxVectors() {
		return maxVectors;
	}

	/**
	 * A factory for oriented matroids which uses a realization of the OM as the
	 * representation.
	 * 
	 * @return
	 */
	public RealizedFactory realized() {
		return realized;
	}

	/**
	 * A factory for signed sets.
	 * 
	 * @return A factory for signed sets.
	 */
	public Factory<SignedSet> signedSets() {
		return signedSets;
	}

	/**
	 * A factory for unsigned sets.
	 * 
	 * @return A factory for unsigned sets.
	 */
	public SetFactory<Label, UnsignedSet> unsignedSets() {
		return unsignedSets;
	}

	/**
	 * A factory for sets of unsigned sets.
	 * 
	 * @return A factory for sets of unsigned sets.
	 */
	public SetFactory<UnsignedSet, SetOfUnsignedSet> setsOfUnsignedSet() {
		return setsOfUnsignedSet;
	}

	/**
	 * A factory for sets of signed sets.
	 * 
	 * @return A factory for sets of signed sets.
	 */
	public SetFactory<SignedSet, SetOfSignedSet> setsOfSignedSet() {
		return setsOfSignedSet;
	}

	/**
	 * A factory for symmetric sets of signed sets. The String representation
	 * may omit opposites.
	 * 
	 * @return A factory for symmetric sets of signed sets.
	 */
	public SetFactory<SignedSet, SetOfSignedSet> symmetricSetsOfSignedSet() {
		return symmetricSetsOfSignedSet;
	}

	public Options options() {
		return options;
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

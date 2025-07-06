/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Arrays;
import java.util.Iterator;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.impl.set.SetFactoryInternal;
import net.sf.oriented.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.impl.set.UnsignedSetInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Factory;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.MatroidAsSet;
import net.sf.oriented.omi.SetOfUnsignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Group;

abstract class AbsMatroid extends AbsAxioms<UnsignedSet> implements
		MatroidInternal, MatroidAsSet {

	final private MatroidAll all;
	final SetOfUnsignedSetInternal set;

	// private UnsignedSetImpl support;

	AbsMatroid(SetOfUnsignedSetInternal s, MatroidInternal m) {
		all = m.asAll();
		set = s;
	}

	@Override
	public MatroidAll asAll() {
		return all;
	}

	@Override
	public FactoryFactory ffactory() {
		return all.ffactory();
	}

	@Override
	public LabelImpl[] elements() {
		return all.elements();
	}

	@Override
	public int rank() {
		return all.rank();
	}

	@Override
	public MatroidInternal dual() {
		return all.dual();
	}

	@Override
	public Bases getBases() {
		return all.getBases();
	}

	@Override
	public MatroidCircuits getCircuits() {
		return all.getCircuits();
	}

	@Override
    public UnsignedSetInternal union() {
		return set.union();
	}

	@Override
	public Iterator<UnsignedSetInternal> iterator2() {
		return set.iterator2();
	}

	@Override
	abstract public String toString();

	@Override
	public JavaSet<? extends UnsignedSet> asCollection() {
		return set.asCollection();
	}

	@Override
	public boolean contains(UnsignedSet a) {
		return set.contains(a);
	}

	@Override
	public SetOfUnsignedSet intersection(SetOfUnsignedSet b) {
		return set.intersection(b);
	}

	@Override
	public boolean isEmpty() {
		return set.isEmpty();
	}

	@Override
	public boolean isSubsetOf(SetOfUnsignedSet b) {
		return set.isSubsetOf(b);
	}

	@Override
	public boolean isSupersetOf(SetOfUnsignedSet b) {
		return set.isSupersetOf(b);
	}

	@Override
	public SetOfUnsignedSet minus(SetOfUnsignedSet b) {
		return set.minus(b);
	}

	@Override
	public SetOfUnsignedSet minus(UnsignedSet b) {
		return set.minus(b);
	}

	@Override
	public SetOfUnsignedSet union(UnsignedSet b) {
		return set.union(b);
	}

	@Override
	public JavaSet<? extends SetOfUnsignedSet> powerSet() {
		return set.powerSet();
	}

	@Override
	public int size() {
		return set.size();
	}

	@Override
	public JavaSet<? extends SetOfUnsignedSet> subsetsOfSize(int i) {
		return set.subsetsOfSize(i);
	}

	@Override
	public SetOfUnsignedSet union(SetOfUnsignedSet b) {
		return set.union(b);
	}

	public SetOfUnsignedSetInternal useCollection(
			JavaSet<UnsignedSetInternal> bases) {
		return set.useCollection(bases);
	}

	public SetFactoryInternal<UnsignedSetInternal, SetOfUnsignedSetInternal, UnsignedSet, SetOfUnsignedSet, UnsignedSetInternal, SetOfUnsignedSetInternal> factory() {
		return set.factory();
	}

	@Override
	public boolean equalsIsSameSetAs() {
		return false;
	}

	@Override
	public SetOfUnsignedSet respectingEquals() {
		return set;
	}

	@Override
	public boolean sameSetAs(SetOfUnsignedSet other) {
		return set.sameSetAs(other);
	}

	UnsignedSetInternal convert(LabelImpl[] impls) {
		return (UnsignedSetInternal) ffactory().unsignedSets()
				.copyBackingCollection(Arrays.asList(impls));
	}

	public String toString(Factory<SetOfUnsignedSet> f) {
		return set.toString(f);
	}

	protected JavaSet<UnsignedSetInternal> emptyCollectionOf() {
		return factory().itemFactory().emptyCollectionOf();
	}

    @Override
    public Group automorphisms() {
        return all.automorphisms();
    }

    protected void verifyNonEmpty() throws AxiomViolation {
        if (set.isEmpty()) {
            throw new AxiomViolation(this,"non-empty");
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Iterator<UnsignedSet> iterator() {
        return (Iterator)this.iterator2();
    }

    @Override
    public UnsignedSet[] toArray() {
        return this.set.toArray();
    }
    

    @Override
    public Independent getIndependentSets() {
        return all.getIndependentSets();
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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import static net.sf.oriented.impl.om.Cryptomorphisms.CHIROTOPE;
import static net.sf.oriented.impl.om.Cryptomorphisms.CIRCUITS;
import static net.sf.oriented.impl.om.Cryptomorphisms.COCIRCUITS;
import static net.sf.oriented.impl.om.Cryptomorphisms.COVECTORS;
import static net.sf.oriented.impl.om.Cryptomorphisms.DUALCHIROTOPE;
import static net.sf.oriented.impl.om.Cryptomorphisms.DUALREALIZED;
import static net.sf.oriented.impl.om.Cryptomorphisms.MAXVECTORS;
import static net.sf.oriented.impl.om.Cryptomorphisms.REALIZED;
import static net.sf.oriented.impl.om.Cryptomorphisms.TOPES;
import static net.sf.oriented.impl.om.Cryptomorphisms.VECTORS;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.impl.set.UnsignedSetFactory;
import net.sf.oriented.impl.set.UnsignedSetInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasRealized;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Permutation;

public class OMAll extends AbsOMAxioms<Object>  {

	final OMAll dual;

	MatroidAll matroid;

	final LabelImpl[] ground;

	final Map<Label, Integer> indexes;

	OMInternal forms[] = new OMInternal[Cryptomorphisms.values().length / 2];

	/**
	 * allows distinguishing the two parts of a dual pair. Notice these twins,
	 * the older one starts coming out before the younger, but the younger one
	 * comes out first!
	 */
	@SuppressWarnings("unused")
	final private boolean older;

	final private FactoryFactory factory;

	public OMAll(LabelImpl[] g, FactoryFactory f) {
		factory = f;
		ground = g;
		indexes = new HashMap<Label, Integer>();
		for (int i = 0; i < g.length; i++) {
			indexes.put(g[i], Integer.valueOf(i));
		}
		matroid = new MatroidAll(g, this);
		dual = new OMAll(this, (MatroidAll) matroid.dual());
		older = true;
	}

	private OMAll(OMAll d, MatroidAll m) {
		dual = d;
		older = false;
		matroid = m;
		ground = d.ground;
		indexes = d.indexes;
		factory = d.factory;
	}

	public void set(Cryptomorphisms which, OMInternal definedOM) {
		int ix = which.ordinal();
		if (ix >= forms.length) {
			dual.set(which, ix - forms.length, definedOM);
		} else {
			set(which, ix, definedOM);
		}
	}

	private void set(Cryptomorphisms which, int ix, OMInternal definedOM) {
		if (forms[ix] != null)
			throw new IllegalStateException("Cannot set " + which + " twice");
		forms[ix] = definedOM;
	}

	public OMInternal get(Cryptomorphisms which) {
		int ix = which.ordinal();
		if (ix >= forms.length)
			return dual.getNotInDual(ix - forms.length, which.getDual());
		else
			return getNotInDual(ix, which);
	}

	/**
	 * which is the form being sort ... if it is dual() form, then this is the
	 * dual
	 * 
	 * @param ix
	 * @param which
	 * @return
	 */
	private OMInternal getNotInDual(int ix, Cryptomorphisms which) {
		if (forms[ix] != null)
			return forms[ix];

		if (which.isDualForm())
			throw new IllegalArgumentException("Must not be dual form");

		switch (which) {
		case CIRCUITS:
			if (has(CHIROTOPE) || has(DUALCHIROTOPE) || has(COCIRCUITS))
				return new Circuits(getChirotope());
			if (has(VECTORS))
				return new Circuits(getVectors());
			if (has(COVECTORS))
				return new Circuits(getChirotope());
			if (has(MAXVECTORS))
				return new Circuits(getVectors());
			return new Circuits(getChirotope());
		case VECTORS:
			if (has(CIRCUITS) || has(CHIROTOPE) || has(DUALCHIROTOPE)
					|| has(COCIRCUITS) || has(COVECTORS))
				return new Vectors(getCircuits());
			if (has(MAXVECTORS))
				return new Vectors(getMaxVectors());
			return new Vectors(getCircuits());
		case MAXVECTORS:
			return new MaxVectors(getVectors());
		case CHIROTOPE:
			if (has(CIRCUITS))
				return new ChirotopeImpl(getCircuits());
			if (has(DUALCHIROTOPE) || has(COCIRCUITS))
				return new ChirotopeImpl(dual().getChirotope());
			if (has(REALIZED))
				return new ChirotopeImpl(this, getRealized().getMatrix());
			if (has(VECTORS))
				return new ChirotopeImpl(getCircuits());
			if (has(COVECTORS))
				return new ChirotopeImpl(dual().getChirotope());
			if (has(MAXVECTORS))
				return new ChirotopeImpl(getCircuits());
			return new ChirotopeImpl(dual().getChirotope());
		case REALIZED:
			if (has(DUALREALIZED))
				return new RealizedImpl(this, dual().getRealized()
						.getDualBasis());
			throw new UnsupportedOperationException("Realization not implemented");

        case COVECTORS:
        case COCIRCUITS:
        case DUALCHIROTOPE:
        case TOPES:
        case DUALREALIZED:
            throw new IllegalArgumentException("Unreachable");
		}
		return null;
	}

	// public <T extends OMS> T

	@Override
	public Circuits getCircuits() {
		return (Circuits) get(CIRCUITS);
	}

	@Override
	public ChirotopeImpl getChirotope() {
		return (ChirotopeImpl) get(CHIROTOPE);
	}

	@Override
	public Vectors getVectors() {
		return (Vectors) get(VECTORS);
	}

	private boolean has(Cryptomorphisms which) {
		int ix = which.ordinal();
		if (ix >= forms.length)
			return dual.forms[ix - forms.length] != null;
		else
			return forms[ix] != null;
	}

	@Override
	public OMAll asAll() {
		return this;
	}

	@Override
	public OMInternal dual() {
		return dual;
	}

	@Override
	public MaxVectors getMaxVectors() {
		return (MaxVectors) get(MAXVECTORS);
	}

	@Override
	public LabelImpl[] ground() {
		return ground;
	}

	@Override
	public void verify() throws AxiomViolation {
		for (OMInternal f : forms)
			if (f != null) {
			    f.verify();
			}
		matroid.verify();
		if (forms[VECTORS.ordinal()] != null) {
		    verify(MAXVECTORS);
		    verify(CIRCUITS);
		}
	}

	private void verify(Cryptomorphisms v) throws AxiomViolation {
		if ( forms[v.ordinal()] != null
				&& ! ((AbsVectorsOM) forms[v.ordinal()])
						.isSubsetOf((AbsVectorsOM) forms[VECTORS.ordinal()]) ) {
		    throw new AxiomViolation(this,v+" must be a set of vectors");
		}
	}

	@Override
	public MatroidInternal getMatroid() {
		if (!matroid.isSet()) {
			if (has(CHIROTOPE) || has(REALIZED) ) {
				matroid.setBases(new Bases(getChirotope().getBases(), matroid));
			} else if (has(DUALCHIROTOPE) || has(DUALREALIZED) ) {
				dual().getMatroid();
			} else if (has(CIRCUITS)) {
				matroid.setCircuits(new MatroidCircuits(getCircuits(), matroid));
			} else if (has(COCIRCUITS)) {
				dual().getMatroid();
			} else if (has(VECTORS)) {
				matroid.setCircuits(new MatroidCircuits(getVectors(), matroid));
			} else if (has(COVECTORS)) {
				dual().getMatroid();
			} else if (has(MAXVECTORS)) {
				matroid.setCircuits(new MatroidCircuits(getVectors(), matroid));
			} else if (has(TOPES)) {
				dual().getMatroid();
			} else
				throw new IllegalStateException(
						"Oriented matroid without a representation.");
		}
		return matroid;
	}

	@Override
	public int rank() {
		if (has(CHIROTOPE))
			return get(CHIROTOPE).rank();
		if (has(DUALCHIROTOPE))
			return ground().length - get(DUALCHIROTOPE).rank();
		return getMatroid().rank();
	}

	@Override
	public int asInt(Label l) {
		return indexes.get(l).intValue();
	}

	@Override
	public <T extends Label> int[] asInt(T[] l) {
		int r[] = new int[l.length];
		for (int i = 0; i < l.length; i++) {
			r[i] = asInt(l[i]);
		}
		return r;
	}

	@Override
	public int[] asInt(UnsignedSet u) {
		Label b[] = u.asCollection().toArray(new Label[0]);
		int ix[] = asInt(b);
		Arrays.sort(ix);
		return ix;
	}

	UnsignedSetFactory unsignedSets(FactoryFactory factory) {
		return (UnsignedSetFactory) factory.unsignedSets();
	}

	UnsignedSetFactory unsignedSets() {
		return unsignedSets(factory);
	}

	public UnsignedSetInternal asUnsignedSet(int ix[]) {
		JavaSet<LabelImpl> r = unsignedSets().itemFactory().emptyCollectionOf();
		for (int element : ix) {
			r.add(asLabel(element));
		}
		return unsignedSets().fromBackingCollection(r);
	}

	private LabelImpl asLabel(int i) {
		return ground[i];
	}

	@Override
	public FactoryFactory ffactory() {
		return factory;
	}

	public boolean sameGroundAs(OM o) {
		Label g[] = o.ground();
		if (g.length != ground.length)
			return false;
		outer: for (int i = 0; i < g.length; i++) {
			for (int j = 0; j < g.length; j++)
				if (ground[j].equals(g[i])) {
					continue outer;
				}
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		return getCircuits().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || (!(o instanceof OM)))
			return false;
		if (o == this)
			return true;
		if (!(o instanceof OMInternal))
			return equalsByCircuits((OM) o);
		if (((OMInternal)o).ffactory() != ffactory()) {
            return equalsByCircuits((OM) o);
		}
		OMAll a = ((OMInternal) o).asAll();
		int i = hasSameForm(a);
		if (i != -1)
			return forms[i].equals(a.forms[i]);
		i = dual.hasSameForm(a.dual);
		if (i != -1)
			return dual.forms[i].equals(a.dual.forms[i]);
		return equalsByCircuits(a);
	}

	private int hasSameForm(OMAll a) {
		for (int i = 0; i < forms.length; i++)
			if (forms[i] != null && a.forms[i] != null)
				return i;
		return -1;
	}

	boolean equalsByCircuits(OM o) {
		if (!sameGroundAs(o))
			return false;
		return getCircuits().respectingEquals().equals(
				o.getCircuits().respectingEquals());
	}

	@Override
	public OMasRealized getRealized() {
		return (OMasRealized) get(REALIZED);
	}

    private AbsOMAxioms<?> getCircuitsOrChirotope() {
        // we generally prefer to use the circuit
        // implementation, unless we do not have circuits
        // computed, and we have the chirotope.
        if ( (has(Cryptomorphisms.CHIROTOPE)
                || has(Cryptomorphisms.DUALCHIROTOPE) )
             &&
             ! has(Cryptomorphisms.CIRCUITS) ) {
            return getChirotope();
        }
        return getCircuits();
        
    }

    @Override
    public OM permuteGround(Permutation p) {
        return getCircuitsOrChirotope().permuteGround(p);
    }

    @Override
    public OM permute(Permutation p) {
        // we only implement for chirotope, everything else
        // has to piggy back
        return getChirotope().permute(p);
    }

    @Override
    public OMInternal reorientRaw(Label ... axes) {
        return getCircuitsOrChirotope().reorientRaw(axes);
    }


    /**
     * This method is here by an accident of single inheritance.
     * It throws an exception
     * @return
     * @throws UnsupportedOperationException Always.
     */
    @Override
    public Iterator<? extends Object> iterator() {
        throw new UnsupportedOperationException("Not part of the interface");
    }

    @Override
    MatroidAll getMatroidAll() {
        return matroid;
    }

    @Override
    void setMatroidAll(MatroidAll m) {
        matroid = m;
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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasFaceLattice;
import net.sf.oriented.omi.OMasRealized;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Permutation;

public abstract class AbsOM<T> extends AbsOMAxioms<T> {

	protected final OMAll all;

	public AbsOM(OMInternal a) {
		all = a.asAll();
	}

	@Override
	public OMAll asAll() {
		return all;
	}

	@Override
	public OMInternal dual() {
		return all.dual();
	}

	@Override
	public Circuits getCircuits() {
		return all.getCircuits();
	}

	@Override
	public OMasRealized getRealized() {
		return all.getRealized();
	}

	@Override
	public AbsVectorsOM getMaxVectors() {
		return all.getMaxVectors();
	}

	@Override
	public Vectors getVectors() {
		return all.getVectors();
	}

	@Override
	public MatroidInternal getMatroid() {
		return all.getMatroid();
	}

	@Override
	public int rank() {
		return all.rank();
	}

	@Override
	public FactoryFactory ffactory() {
		return all.ffactory();
	}

	@Override
	public int asInt(Label l) {
		return all.asInt(l);
	}

	@Override
	public <TT extends Label> int[] asInt(@SuppressWarnings("unchecked") TT ... l) {
		return all.asInt(l);
	}

    @Override
    public  int[] asInt(String ... l) {
        return all.asInt(l);
    }
    @Override
    public UnsignedSet asSet(int ... l) {
        return all.asSet(l);
    }

	@Override
	public int[] asInt(UnsignedSet u) {
		return all.asInt(u);
	}

	@Override
	public ChirotopeImpl getChirotope() {
		return all.getChirotope();
	}

	@Override
	public LabelImpl[] elements() {
		return all.elements();
	}

	@Override
	abstract public boolean equals(Object o);

	@Override
	public int hashCode() {
		return all.hashCode();
	}

    @Override
    public OM permuteGround(Permutation p) {
        return all.permuteGround(p);
    }

    @Override
    public OM permute(Permutation p) {
        return all.permute(p);
    }
    
    @Override
    public OMInternal reorientRaw(Label ... axes) {
        return all.reorientRaw(axes);
    }
    @Override
    public MatroidAll getMatroidAll() {
        return all.getMatroidAll();
    }

    @Override
    public void setMatroidAll(MatroidAll m) {
        all.setMatroidAll(m);
    }

    @Override
    public boolean isUniform() {
        return all.isUniform();
    }
    

    @Override
    public OMasFaceLattice getFaceLattice() {
        return all.getFaceLattice();
    }

    @Override
    public String toHexString() {
        return all.toHexString();
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

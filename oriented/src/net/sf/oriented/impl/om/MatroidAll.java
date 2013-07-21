/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Arrays;

import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Group;

public class MatroidAll implements MatroidInternal {

	final private MatroidAll dual;

	final private LabelImpl[] ground;

	private Bases bases;

	private MatroidCircuits circuits;

	/**
	 * allows distinguishing the two parts of a dual pair. Notice these twins,
	 * the older one starts coming out before the younger, but the younger one
	 * comes out first!
	 */
	@SuppressWarnings("unused")
	final private boolean older;

	final private FactoryFactory factory;

	MatroidAll(LabelImpl[] g, OMInternal om) {
		this(g, om, om.ffactory());
	}

	MatroidAll(LabelImpl[] g, OMInternal om, FactoryFactory f) {
		ground = g;
		factory = f;
		dual = new MatroidAll(this);
		older = true;
	}

	private MatroidAll(MatroidAll d) {
		dual = d;
		older = false;
		ground = d.ground;
		factory = d.factory;
	}

	@Override
	public MatroidCircuits getCircuits() {
		initCircuits();
		return circuits;
	}

	private void initCircuits() {
		if (circuits == null) {
			circuits = new MatroidCircuits(getBases());
		}
	}

	@Override
	public MatroidAll asAll() {
		return this;
	}

	@Override
	public MatroidInternal dual() {
		return dual;
	}

	@Override
	public LabelImpl[] ground() {
		return ground;
	}

	@Override
	public void verify() throws AxiomViolation {
	    verify1();
	    dual.verify1();
	}
	
	private void verify1() throws AxiomViolation {
	    if (bases != null) {
	        bases.verify();
	    }
	    if (circuits != null ) {
	        circuits.verify();
	    }
	    
	}

	@Override
	public Bases getBases() {
		initBases();
		return bases;
	}

	private void initBases() {
		if (bases == null) {
			if (dual.bases != null) {
				bases = new Bases(dual.bases, this);
			} else if (circuits != null) {
				bases = new Bases(circuits);
			} else {
				if (dual.circuits == null)
					throw new IllegalStateException(
							"Can't initialize ex nihilio");
				bases = new Bases(dual.getBases(), this);
			}
		}
	}

	@Override
	public int rank() {
		return getBases().rank();
	}

	void setCircuits(MatroidCircuits c) {
		circuits = c;
	}

	void setBases(Bases b) {
		bases = b;
	}

	boolean isSet() {
		return !(bases == null && circuits == null && dual.bases == null && dual.circuits == null);
	}


	@Override
	public FactoryFactory ffactory() {
		return factory;
	}
	

    @Override
    public Group automorphisms() {
        return getCircuits().automorphisms();
    }


    @Override
    public UnsignedSet support() {
        return ffactory().unsignedSets().copyBackingCollection(Arrays.asList(ground()));
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

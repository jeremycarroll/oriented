/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMRealized;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelImpl;

abstract class AbsOM extends AbsAxioms<SignedSet> implements OMInternal {

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
    public OMRealized getRealized() {
	return all.getRealized();
    }

    @Override
    public MaxVectors getMaxVectors() {
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
    public <T extends Label> int[] asInt(T[] l) {
	return all.asInt(l);
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
    public LabelImpl[] ground() {
	return all.ground();
    }

    @Override
    abstract public boolean equals(Object o);

    @Override
    public int hashCode() {
	return all.hashCode();
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

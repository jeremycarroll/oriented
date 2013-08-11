/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Function;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FaceLattice;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.Matroid;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.OMasFaceLattice;
import net.sf.oriented.omi.OMasRealized;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Group;
import net.sf.oriented.util.combinatorics.Permutation;

public class DelegatingFaceLattice extends AbsOM<Face> implements
        OMasFaceLattice {
    
    private final FaceLattice delegate;

    public DelegatingFaceLattice(OMInternal a, FaceLattice d) {
        super(a);
        delegate = d;
    }

    @Override
    public void verify() throws AxiomViolation {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Face top() {
        return delegate.top();
    }

    @Override
    public Face bottom() {
        return delegate.bottom();
    }

    @Override
    public Collection<? extends Face> withDimension(int d) {
        return delegate.withDimension(d);
    }

    @Override
    public boolean equals(Object o) {
        return all.equals(o);
    }

    @Override
    public Iterator<? extends Face> iterator2() {
        return delegate.iterator();
    }

    @Override
    public Iterator<? extends Face> iterator() {
        return delegate.iterator();
    }

    @Override
    public Iterable<? extends Face> withDimensions(int i, int j) {
        return delegate.withDimensions(i, j);
    }

}


/************************************************************************
    This file is part of the Java Oriented Matroid Library.  

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/

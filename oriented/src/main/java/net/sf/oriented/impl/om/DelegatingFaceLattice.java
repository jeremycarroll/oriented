/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Collection;
import java.util.Iterator;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FaceLattice;
import net.sf.oriented.omi.FaceLatticeProvider;
import net.sf.oriented.omi.OMasFaceLattice;
import net.sf.oriented.omi.SignedSet;

public class DelegatingFaceLattice extends AbsOM<Face> implements
        OMasFaceLattice, FaceLatticeProvider {
    
    private final FaceLattice delegate;

    public DelegatingFaceLattice(OMInternal a, FaceLattice d) {
        super(a);
        delegate = d;
    }

    @Override
    public void verify() throws AxiomViolation {
         delegate.verify();
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
    public Iterator<Face> iterator() {
        return delegate.iterator();
    }

    @Override
    public Iterable<Face> withDimensions(int i, int j) {
        return delegate.withDimensions(i, j);
    }

    @Override
    public Face get(SignedSet covector) {
        return delegate.get(covector);
    }

    @Override
    public boolean hasFaceLattice() {
        return true;
    }
    
    @Override
    public boolean hasDualFaceLattice() {
        return dual().hasFaceLattice();
    }
    
    @Override
    public OMasFaceLattice getDualFaceLattice() {
        return dual().getFaceLattice();
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

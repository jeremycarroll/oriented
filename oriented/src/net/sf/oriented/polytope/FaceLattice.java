/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.oriented.impl.om.AbsOM;
import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class FaceLattice extends AbsOM<Face> {
    
    
 //   final SetOfSignedSet topes;
    final SetOfSignedSet cocircuits;
    final Face top;
    final Face bottom;
    Map<SignedSet,Face> faces = new HashMap<SignedSet,Face>();

    public FaceLattice(OM om) {
        super((OMInternal)om);
     //  topes = om.dual().getMaxVectors();
        cocircuits = om.dual().getCircuits();
        top = new Top(this,rank());
        bottom = new Bottom(this,ffactory());
        bottom.setIsBelow(top);
        for (SignedSet cc:cocircuits) {
            dump();
            bottom.augment(new Vertex(this,cc));
        }
        bottom.setDimension(-1);
    }
/*
 * A lattice which satisfies the identities

Distributivity of ∨ over ∧
a∨(b∧c) = (a∨b) ∧ (a∨c).
Distributivity of ∧ over ∨
a∧(b∨c) = (a∧b) ∨ (a∧c).

is said to be distributive.
 * 
 * (non-Javadoc)
 * @see net.sf.oriented.omi.Verify#verify()
 */
    @Override
    public void verify() throws AxiomViolation {
        for (Face f:faces.values()) {
            f.verify();
        }
    }

    @Override
    public boolean equals(Object o) {
        return this == o || all.equals(o);
    }

    @Override
    public Iterator<? extends Face> iterator2() {
        return faces.values().iterator();
    }

    Face createIfNew(SignedSet composed) {
        if (faces.containsKey(composed)) {
            return null;
        }
        return new Face(this,composed);
    }

    int counter = 1;
    public void dump() {
        System.err.println("["+counter++ + "]===============");
        for (Map.Entry<SignedSet, Face> entry: faces.entrySet()) {
            Face f = entry.getValue();
            System.err.println(entry.getKey()+":" + f+"["+f.dimension+"]");
            for (Face ff:f.above) {
                System.err.println("    : " + ff);
            }
            
        }
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

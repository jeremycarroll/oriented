/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.Iterator;

import net.sf.oriented.impl.om.AbsOM;
import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;

/**
 * This class represents an arrangment of pseudolines,
 * which corresponds to a rank3 oriented matroid
 * @author jeremycarroll
 *
 */
public class Arrangement  extends AbsOM {
    
    class General {
        SignedSet covector;
        public void hasPoint(Point bp) {
            // TODO Auto-generated method stub
            
        }
    }
    
    class Point extends General {
        
    }
    
    class Edge extends General {
        
    }
    
    class Face extends General {

        
    }

    public Arrangement(OMInternal om) {
        super(om);
        if (om.rank() != 3) {
            throw new IllegalArgumentException("Not a rank 3 oriented matroid");
        }
        SetOfSignedSet topes = om.dual().getMaxVectors();
        for (SignedSet a: om.dual().getCircuits()) {
            Point ap = getPoint(a);
            for (SignedSet b: om.dual().getCircuits()) {
                Point bp = getPoint(b);
                if (a.equals(b)) {
                    continue;
                }
                if (a.conformsWith(b)) {
                    SignedSet covector = a.compose(b);
                    General f;
                    if (topes.contains(covector)) {
                        f = getFace(covector);
                    } else {
                        f = getEdge(covector);
                    }
                    f.hasPoint(ap);
                    f.hasPoint(bp);
                }
            }
        }
    }

    // factory
    private Edge getEdge(SignedSet covector) {
        // TODO Auto-generated method stub
        return null;
    }

    // factory
    private Face getFace(SignedSet covector) {
        // TODO Auto-generated method stub
        return null;
    }

    // factory
    private Point getPoint(SignedSet b) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void verify() throws AxiomViolation {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean equals(Object o) {
        return all.equals(o);
    }

    @Override
    public Iterator<? extends SignedSet> iterator2() {
        throw new UnsupportedOperationException();
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

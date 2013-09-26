/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import net.sf.oriented.omi.Face;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;

public class TGFactory {
    
    private final EuclideanPseudoLines euclideanPseudoLines;
    public TensionGraph result;

    public TGFactory(EuclideanPseudoLines s) {
        this.euclideanPseudoLines = s;
    }
    
    public TensionGraph create() {
        result = new TensionGraph(euclideanPseudoLines);
        initVertices();
        initEdges();
        return result;
        
    }

    private void initEdges() {
        // TODO Auto-generated method stub
        
    }

    private void initVertices() {
        euclideanPseudoLines.switchFaceLattice();
        for (Face f:euclideanPseudoLines.getFaceLattice().withDimension(0)) {
            if (f.covector().sign(euclideanPseudoLines.getInfinity())==1 && f.higher().size()>4)
               TGVertex.fromPoint(f, result,
                       euclideanPseudoLines.notLoops.minus(f.covector().support()),
                       euclideanPseudoLines.ffactory() );
        }
        for (Face f1:euclideanPseudoLines.properFaces()) {
            TGVertex.fromFace(f1, result, euclideanPseudoLines);
        }
    }
    
    

//    public List<TGVertex> getTGVertices() {
//        List<TGVertex> rslt = new ArrayList<TGVertex>();
//        switchFaceLattice();
//        for (Face f:getFaceLattice().withDimension(0)) {
//            if (f.covector().sign(getInfinity())==1 && f.higher().size()>4)
//               rslt.addAll(TGVertex.fromPoint(f, this.notLoops.minus(f.covector().support()),original.ffactory() ));
//        }
//        for (Face f:getFaceLattice().withDimension(2)) {
//            if (f.covector().sign(getInfinity())==1 && !touchesInfinity(f)) {
//                rslt.addAll(TGVertex.fromFace(f,original.ffactory()));
//            }
//        }
//        return rslt;
//    }

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

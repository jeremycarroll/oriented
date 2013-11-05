/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.HashMap;
import java.util.Map;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;

public class TGFactory {
    
    private final EuclideanPseudoLines euclideanPseudoLines;
    private TensionGraph result;
    private Map<SignedSet,TGVertex> id2vertex;

    public TGFactory(EuclideanPseudoLines s) {
        this.euclideanPseudoLines = s;
    }
    
    public TensionGraph create() {
        result = new TensionGraph(euclideanPseudoLines);
        initVertices();
        checkVertices();
        initEdges();
        result.storeEdgesByBit();
        return result;
        
    }

    private void checkVertices() {
       id2vertex = new HashMap<SignedSet,TGVertex>();
       for (TGVertex v:result.getVertices()) {
           TGVertex old = id2vertex.put(v.getId(), v);
           if (old != null) {
               throw new IllegalStateException(v.getId()+" has both "+v + " and "+old);
           }
       }
        
    }

    private void initEdges() {
        TGVertex all[] = new TGVertex[result.getVertexCount()];
        result.getVertices().toArray(all);
        for (int i=0;i<all.length;i++) {
            SignedSet idI = all[i].getId();
            for (int j=i+1;j<all.length;j++) {
//                if (all[i].getSource().dimension()==0
//                   && all[j].getSource().dimension()==0 ) {
//                    System.err.println("1: "+all[i].getId());
//                    System.err.println("2: "+all[j].getId());
//                    
//                }
                SignedSet idJ = all[j].getId();
                // find edge ...
                SignedSet line = idI.intersection(idJ.opposite());
                UnsignedSet unsignedLine = idI.support().intersection(idJ.support());
                if (line.size()==1
                     && unsignedLine.size() == 1
                     && (!all[i].getSource().equals(all[j].getSource()))
                     && all[i].getExtent().intersection(all[j].getExtent()).isEmpty()
                     ) {
                    TGEdge edge;
                    Label lbl = unsignedLine.iterator().next();
                    int ix = euclideanPseudoLines.getEquivalentOM().asInt(lbl);
                    if (all[i].requires(lbl) || all[j].requires(lbl)) {
                        if (line.minus().isEmpty()) {
                            edge = new TGEdge(lbl, ix, all[i], all[j]);
                        } else {
                            edge = new TGEdge(lbl, ix, all[j], all[i]);
                        }
                        result.saveEdgeAsBit(edge);
                        //                    System.err.println(edge);
                        result.addEdge(edge, edge.source, edge.dest);
                    }
                } else if (line.size()>1) {
                    mutuallyExclude(all[i],all[j]);
                } else {
                    if (!all[i].extent.intersection(all[j].extent).isEmpty()) {
                        mutuallyExclude(all[i],all[j]);
                    }
                }
            }
        }
        
    }

    private void mutuallyExclude(TGVertex a, TGVertex b) {
        a.exclude.add(b);
        b.exclude.add(a);
    }

    private void initVertices() {
        euclideanPseudoLines.switchFaceLattice();
        for (Face f:euclideanPseudoLines.getFaceLattice().withDimension(0)) {
            if (f.covector().sign(euclideanPseudoLines.getInfinity())==1 && f.higher().size()>4)
               TGVertex.fromPoint(f, result,
                       euclideanPseudoLines.notLoops.minus(f.covector().support()),
                       euclideanPseudoLines );
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

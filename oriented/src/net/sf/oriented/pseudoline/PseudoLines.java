/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FaceLattice;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Permutation;

public class PseudoLines {
    private final OM original;
    private final OMasChirotope modified;
    private Label[] reorientation;
    private Permutation permutation;
    

    public PseudoLines(OM om, String infinity, String ... alsoReorient) {
        this(om,om.asInt(infinity), alsoReorient);
    }

    public PseudoLines(OM om, Label infinity) {
        this(om,om.asInt(infinity));
    }

    final Map<Label,JavaSet<SignedSet>> line2cocircuit = new HashMap<Label,JavaSet<SignedSet>>();
    final Map<SignedSet,JavaSet<SignedSet>> tope2cocircuit = new HashMap<SignedSet,JavaSet<SignedSet>>();
    final Map<SignedSet,JavaSet<SignedSet>> cocircuit2tope = new HashMap<SignedSet,JavaSet<SignedSet>>();
    
    private PseudoLines(final OM om, final int infinity, String ...also  ) {
        if (infinity == -1){
            throw new IllegalArgumentException("Bad choice of infinity");
        }
        if (om.rank() != 3) {
            throw new IllegalArgumentException("Psuedoline stretching algorithm only applies to rank 3 oriented matroids");
        }
        original = om;
        OMasSignedSet topes = om.dual().getMaxVectors();
        Label infLabel = om.elements()[infinity];
        FactoryFactory f = om.ffactory();
        UnsignedSet empty = f.unsignedSets().empty();
        UnsignedSet infUS = empty.union(infLabel);
        int bestSize = Integer.MAX_VALUE;
        SignedSet bestTope = null;
        for (SignedSet tope:topes) {
            if (1 == tope.sign(infLabel)) {
                SignedSet adjacent = tope.restriction(infUS).opposite().compose(tope);
                if (topes.contains(adjacent)) {
                    UnsignedSet needReorienting = tope.minus();
                    int size = needReorienting.size();
                    if (size < bestSize) {
                        bestSize = size;
                        bestTope = tope;
                    }
                }
            }
        }
        if (bestTope == null) {
            throw new IllegalArgumentException(infLabel.label()+" is a loop and does not appear in any tope");
        }
        //System.err.print(bestTope);
        UnsignedSet r = bestTope.minus();
        for (String mm:also) {
            r = r.union(f.labels().parse(mm));
        }
        reorientation = r.toArray();
        
        final OMasChirotope reoriented = om.reorient(reorientation).getChirotope();

        UnsignedSet notCoLoops = bestTope.support();
        List<Face[]> line = firstLine(infLabel,bestTope,reoriented);
        int newOrder[] = new int[om.n()];
        newOrder[0] = infinity;
        readLine(line,reoriented,notCoLoops,newOrder,1);
        permutation = new Permutation(newOrder);
        this.modified = reoriented.permuteGround(permutation).getChirotope();
        
    }

    private void readLine(List<Face[]> line, OM om, UnsignedSet notCoLoops, int[] newOrder, int i) {
        for (Face[] pointEdge:line) {
            newOrder[i++] = lineNumber(om,getLineLabel(notCoLoops,pointEdge[1].covector()));
        }
    }

    private int lineNumber(OM om, Label lineLabel) {
        return om.asInt(lineLabel);
    }

    private List<Face[]> firstLine(Label line, SignedSet startHere, OM om) {
        Face s = get(startHere);
        Face xEdge = edgeOnLine(line,s);
        s = otherFace(xEdge,s);
        Face[] edges = edgesTouchingLine(line, s);
        UnsignedSet support = startHere.support();
        Label xline0 = getLineLabel(support,edges[0].covector());
        Label xline1 = getLineLabel(support,edges[2].covector());
        int χ = om.getChirotope().chi(om.asInt(line,xline0,xline1));
        Label xline;
        switch (χ) {
        case -1:
            xline = xline1;
            break;
        case 1:
            xline = xline0;
            break;
        case 0:
            throw new IllegalArgumentException("chi returned 0, so "+startHere+" was not a good starting point");
        default:
             throw new IllegalStateException("chi returned "+χ);
        }
        return followLine(s.covector(), line, xline) ;
    }

    private Face get(FaceLattice fl, SignedSet startHere) {
        Face s = fl.get(startHere);
        if (s == null) {
            throw new IllegalArgumentException(startHere+" is not a face of the original OM");
        }
        return s;
    }

    /** return the label of one of the elements in the tope, that is not in the edge.
     * There is typically exactly one, but there can be more than one when there are parallel
     * edges.
     * @param tope
     * @param edge
     * @return
     */
    public static Label getLineLabel(UnsignedSet tope, SignedSet edge) {
        return tope.minus(edge.support()).asCollection().iterator().next();
    }
    public static Face edgeOnLine(Label line, Face face) {
        Face edgePoints[] = new Face[2];
        if (1 != edgesWithNPointsOnLine(line, face, edgePoints,2)) {
            throw new IllegalArgumentException(line+" is not an edge of "+face);
        }
        return edgePoints[0];
    }
    public static Face[] edgesTouchingLine(Label line, Face face) {
        Face edgePoints[] = new Face[4];
        if (2 != edgesWithNPointsOnLine(line, face, edgePoints,1) ) {
            throw new IllegalArgumentException(line+" is not touching "+face);
        }
            
        return edgePoints;
    }

    public static int edgesWithNPointsOnLine(Label line, Face face,
            Face[] edgePoints, int n) {
        int eCnt = 0;
        for (Face edge : face.lower()) {
            int cnt = 0;
            Face endPoint = null;
            for (Face point: edge.lower()) {
                if (point.covector().sign(line)==0) {
                    cnt++;
                    endPoint = point;
                }
            }
            if (cnt==n) {
                edgePoints[eCnt++] = edge;
                edgePoints[eCnt++] = endPoint;
            }
            switch (cnt) {
            case 0:
            case 1:
            case 2:
                continue;
            default:
                throw new IllegalStateException("unexpected topology");
            }
        }
        return eCnt/2;
    }

    public String[] toCrossingsString() {
        final Label ground[] = modified.elements();
        int n = ground.length;
        @SuppressWarnings({ "unchecked" })
        List<Face[]> result[] = new List[n];
        boolean oneChar = allOneChar(ground);
        UnsignedSet noCoLoops = original.dual().getMaxVectors().iterator().next().support();
        UnsignedSet minus = original.ffactory().unsignedSets().copyBackingCollection(Arrays.asList(reorientation));
        SignedSet positiveTope = original.ffactory().signedSets().construct(noCoLoops.minus(minus), minus );
        Face positiveFace = get(positiveTope);
        Face startTope = positiveFace;
        for (int i=0;i<n;i++) {
            startTope = otherFace(edgeOnLine(ground[i], startTope),startTope);
            result[i] = followLine(startTope.covector(), ground[i], ground[i==0?1:0]) ;
        }
        
        String crossings[] = new String[result.length];
        for (int cnt=0;cnt<result.length;cnt++) {
            crossings[cnt] = toString(cnt,result[cnt],ground,!oneChar, noCoLoops);
        }
        return crossings;
    }

    private Face get(SignedSet positiveTope) {
        return get(original.getFaceLattice(),positiveTope);
    }

    private boolean allOneChar(Label[] ground) {
        for (Label l:ground) {
            if (l.label().length()>1) {
                return false;
            }
        }
        return true;
    }

    private String toString(int line, List<Face[]> result, Label[] ground, boolean useCommas, UnsignedSet tope) {
        StringBuffer rslt = new StringBuffer();
        rslt.append(ground[line].label());
        rslt.append(':');
        boolean insideBracket = false;
        for (int i=0;i<result.size()-1;i++) {
            boolean nextInsideBracket = result.get(i)[0] == result.get(i+1)[0];
            if (nextInsideBracket && !insideBracket) {
                rslt.append('(');
            }
            rslt.append(getLineLabel(tope, result.get(i)[1].covector()));
            if (insideBracket && !nextInsideBracket) {
                rslt.append(')');
            }
            insideBracket = nextInsideBracket;
            if (useCommas) {
                rslt.append(',');
            }
            
        }
        rslt.append(getLineLabel(tope, result.get(result.size()-1)[1].covector()));
        if (insideBracket) {
            rslt.append(')');
        }
        return rslt.toString();
    }

    public OM getEquivalentOM() {
        return modified;
    }

    public Label[] getReorientation() {
        return this.reorientation;
    }
    
    public Permutation getPermutation() {
        return permutation;
    }

    public List<Face[]> followLine(SignedSet start, Label along, Label last) {
        Face face = get(start);
        Face eps[] = edgesTouchingLine(along,face);
        List<Face[]> rslt = new ArrayList<Face[]>();
        Face edge;
        Face point;
        if (eps[1].covector().sign(last)==0) {
            edge = eps[0];
            point = eps[1];
        } else {
            if (eps[3].covector().sign(last)!=0) {
                throw new IllegalArgumentException("xxx");
            }
            edge = eps[2];
            point = eps[3];
        }
        Face firstPoint = point;
        do {
            addToResult(rslt,point,edge);
            Face next[] = crossEdge(point,edge,face, along);
            point = next[0];
            edge = next[1];
            face = next[2];
        } while (point.covector().sign(last)!=0 || point==firstPoint);
        return rslt;
    }

    private static Face[] crossEdge(Face point, Face edge, Face face, Label along) {
        Face nextFace = otherFace(edge, face);
        Face eps[] = edgesTouchingLine(along,nextFace);
        if (eps[0] == edge) {
            return new Face[]{eps[3],eps[2],nextFace};
        } else {
            return new Face[]{eps[1],eps[0],nextFace};
        }
    }

    public static Face otherFace(Face edge, Face face) {
        for (Face f:edge.higher()) {
            if (f != face) {
                return  f;
            }
        }
        throw new IllegalArgumentException(edge + " is not a subtope of "+ face);
    }

    private static void addToResult(List<Face[]> rslt, Face point, Face edge) {
        rslt.add(new Face[]{point,edge});
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FaceLattice;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Permutation;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * This class takes a rank 3 oriented matroid and prepares it for 
 * processing from a Euclidean perspective. The origin
 * is forced to be near the line at infinity (i.e. in an unbounded face).
 * One of the lines is chosen  as the line at infinity, and a face lying on that line is chosen
 * as the positive face, and the oriented matroid may need reorienting to make that face
 * actually be the positive face.
 * @author jeremycarroll
 *
 */
public class EuclideanPseudoLines {
    public final OM original;
    private final OMasChirotope modified;
    private Label[] reorientation;
    private Permutation permutation;
    private FLHelper flHelper;
    


    /**
     * Prepare the given oriented matroid for Euclidean processing, with
     * the given line at infinity, and a hint, which may or may not be followed, concerning
     * which lines to re-orient.
     * @param om The oriented matroid to process
     * @param infinity Project this element to be the line at infinity
     * @throws NotRank3Exception If om is not of rank 3.
     * @param alsoReorient reorient these elements too, before processing. To find which elements are actually reoriented you must call {@link #getReorientation()}
     */
    public EuclideanPseudoLines(OM om, String infinity, String ... alsoReorient) {
        this(om,om.asInt(infinity), alsoReorient);
    }

    /**
     * Prepare the given oriented matroid for Euclidean processing, with
     * the given line at infinity.
     * @param om The oriented matroid to process
     * @param infinity Project this element to be the line at infinity
     * @throws NotRank3Exception If om is not of rank 3.
     */
    public EuclideanPseudoLines(OM om, Label infinity) {
        this(om,om.asInt(infinity));
    }

    /**
     * TODO: this field is incorrectly named
     */
    public UnsignedSet notLoops;
    private List<Face[]>[] fullPseudoLines;
    private Face[][] pseudoLines;
    
    
    
    private EuclideanPseudoLines(final OM om, final int infinity, String ...also  ) {
        if (infinity == -1){
            throw new IllegalArgumentException("Bad choice of infinity");
        }
        if (om.rank() != 3) {
            throw new NotRank3Exception("Psuedoline stretching algorithm only applies to rank 3 oriented matroids");
        }
        original = om;
        OMasSignedSet topes = setNoCoLoops(om);
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
        UnsignedSet minus = original.ffactory().unsignedSets().copyBackingCollection(Arrays.asList(reorientation));
        
        flHelper = new FLHelper(original, notLoops, minus);
        List<Face[]> line = firstLine(infLabel,bestTope,reoriented);
        int newOrder[] = new int[om.n()];
        newOrder[0] = infinity;
        readLine(line,reoriented,notLoops,newOrder,1);
        permutation = new Permutation(newOrder);
        this.modified = reoriented.permuteGround(permutation).getChirotope();
        
    }
    
    /**
     * Prepare to draw this arrangement of pseudolines.
     * @return A drawing from which an image can be constructed.
     * @throws CoLoopCannotBeDrawnException If one of the elements other than infinity is a co-loop.
     */
    public PseudoLineDrawing asDrawing() throws CoLoopCannotBeDrawnException {
        this.switchFaceLattice();
        return new PseudoLineDrawing(this);
    }

    /*
     * TODO: this method is incorrectly named.
     */
    UnsignedSet incorrectlyNamed() {
        return this.notLoops;
    }
    private OMasSignedSet setNoCoLoops(final OM om) {
        OMasSignedSet topes = om.dual().getMaxVectors();
        notLoops = topes.iterator().next().support();
        return topes;
    }
    
    /**
     * This method impacts performance. Calling it may make your program faster, or it may make it slower.
     * It is unlikely to leave the performance unchanged.
     * Computing the face lattice is expensive. The algorithms need to compute the face lattice of the original
     * oriented matroid; it may be more efficient to recompute the face lattice for the modified oriented matroid too, or it may not.
     * Invoking this method does that recomputation.
     */
    public void switchFaceLattice() {
        setNoCoLoops(modified);
        flHelper = new FLHelper(modified, notLoops, modified.ffactory().unsignedSets().empty());
    }

    private void readLine(List<Face[]> line, OM om, UnsignedSet notCoLoops, int[] newOrder, int i) {
        for (Face[] pointEdge:line) {
            newOrder[i++] = lineNumber(om,getLineLabel(pointEdge[1].covector()));
        }
    }

    private int lineNumber(OM om, Label lineLabel) {
        return om.asInt(lineLabel);
    }

    private List<Face[]> firstLine(Label line, SignedSet startHere, OM om) {
        Face s = getFace(startHere);
        Face xEdge = edgeOnLine(line,s);
        s = otherFace(xEdge,s);
        Face[] edges = edgesTouchingLine(line, s);
        Label xline0 = getLineLabel(edges[0].covector());
        Label xline1 = getLineLabel(edges[2].covector());
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


    /** return the label of one of the elements in the om, that is not in the edge.
     * There is typically exactly one, but there can be more than one when there are parallel
     * edges.
     * @param edge
     * @param tope
     * @return label
     */
    private Label getLineLabel(SignedSet edge) {
        return notLoops.minus(edge.support()).asCollection().iterator().next();
    }
    private static Face edgeOnLine(Label line, Face face) {
        Face edgePoints[] = new Face[2];
        if (1 != edgesWithNPointsOnLine(line, face, edgePoints,2)) {
            throw new IllegalArgumentException(line+" is not an edge of "+face);
        }
        return edgePoints[0];
    }
    private static Face[] edgesTouchingLine(Label line, Face face) {
        Face edgePoints[] = new Face[4];
        if (2 != edgesWithNPointsOnLine(line, face, edgePoints,1) ) {
            throw new IllegalArgumentException(line+" is not touching "+face);
        }
            
        return edgePoints;
    }

    private static int edgesWithNPointsOnLine(Label line, Face face,
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

    /**
     * Gives a representation of the modified oriented matroid
     * as a list of crossings strings that can be used in
     * {@link FactoryFactory#fromCrossings(String...)}
     * @see #getEquivalentOM()
     * @return A list of crossing strings.
     */
    public String[] toCrossingsString() {
        final Label ground[] = modified.elements();
        boolean oneChar = allOneChar(ground);
        String crossings[] = new String[getPseudoLines().length];
        for (int cnt=0;cnt<pseudoLines.length;cnt++) {
            crossings[cnt] = toString(cnt,fullPseudoLines[cnt],ground,!oneChar, notLoops);
        }
        return crossings;
    }

    private List<Face[]>[] computePseudoLines() {
        final Label ground[] = modified.elements();
        int n = ground.length;
        @SuppressWarnings({ "unchecked" })
        List<Face[]> result[] = new List[n];
        Face startTope = getPositiveFace();
        for (int i=0;i<n;i++) {
            startTope = otherFace(edgeOnLine(ground[i], startTope),startTope);
            result[i] = followLine(startTope.covector(), ground[i], ground[i==0?1:0]) ;
        }
        return result;
    }

    private Face getPositiveFace() {
        return flHelper.getPositiveFace();
    }

    private Face getFace(SignedSet covector) {
        return flHelper.getFace(covector);
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
            boolean nextInsideBracket = result.get(i)[0].equals(result.get(i+1)[0]);
            if (nextInsideBracket && !insideBracket) {
                rslt.append('(');
            }
            rslt.append(getLineLabel(result.get(i)[1].covector()));
            if (insideBracket && !nextInsideBracket) {
                rslt.append(')');
            }
            insideBracket = nextInsideBracket;
            if (useCommas) {
                rslt.append(',');
            }
            
        }
        rslt.append(getLineLabel(result.get(result.size()-1)[1].covector()));
        if (insideBracket) {
            rslt.append(')');
        }
        return rslt.toString();
    }

    /**
     * This is the oriented matroid which is actually being processed.
     * This is a reorientaiton of the oriented matroid passed to the constructor.
     * @return An equivalent oriented matroid
     * @see #EuclideanPseudoLines(OM, String, String...)
     * @see #EuclideanPseudoLines(OM, Label)
     * @see #getReorientation()
     */
    public OM getEquivalentOM() {
        return modified;
    }

    /**
     * This is the set of elements which were reoriented in the processing of this class.
     * @return A list of elements reoriented for processing as a Euclidean arrangement of Psuedolines
     * @see #EuclideanPseudoLines(OM, String, String...)
     * @see #EuclideanPseudoLines(OM, Label)
     * @see #getEquivalentOM()
     */
    public Label[] getReorientation() {
        return this.reorientation;
    }
    
    /**
     * The elements of the original oriented matroid are permuted in the equivalent oriented
     * matroid. This method gives access to the permutation.
     * @return The permutation of the elements between the original oriented matroid, and the one used for processing as a Euclidean arrangement of Psuedolines
     */
    public Permutation getPermutation() {
        return permutation;
    }

    private List<Face[]> followLine(SignedSet start, Label along, Label last) {
        Face face = getFace(start);
        Face eps[] = edgesTouchingLine(along,face);
        List<Face[]> rslt = new ArrayList<>();
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
        } while (point.covector().sign(last)!=0 || point.equals(firstPoint));
        return rslt;
    }

    private static Face[] crossEdge(Face point, Face edge, Face face, Label along) {
        Face nextFace = otherFace(edge, face);
        Face eps[] = edgesTouchingLine(along,nextFace);
        if (eps[0].equals(edge)) {
            return new Face[]{eps[3],eps[2],nextFace};
        } else {
            return new Face[]{eps[1],eps[0],nextFace};
        }
    }

    private static Face otherFace(Face edge, Face face) {
        for (Face f:edge.higher()) {
            if (!f.equals(face)) {
                return  f;
            }
        }
        throw new IllegalArgumentException(edge + " is not a subtope of "+ face);
    }

    private static void addToResult(List<Face[]> rslt, Face point, Face edge) {
        rslt.add(new Face[]{point,edge});
    }

    public FaceLattice getFaceLattice() {
        return flHelper.modified.getFaceLattice();
    }

    /**
     * This is the element at infinity as from the constructor.
     * @return The element at infinity.
     * @see #EuclideanPseudoLines(OM, String, String...)
     * @see #EuclideanPseudoLines(OM, Label)
     */
    public Label getInfinity() {
        return modified.elements()[0];
    }

//    public Collection<Difficulty> getDifficulties() {
//        DirectedGraph<Face,Tension> tensions = getTensions();
//        
//        return null;
//    }
//    
//    public TensionGraph getTensions() {
//        TensionGraph rslt = new TensionGraph(this);
//        switchFaceLattice();
//        for (Face f:getFaceLattice().withDimension(0)) {
//            if (f.covector().sign(getInfinity())==1 && f.higher().size()>4)
//               rslt.addVertex(f);
//        }
//        for (Face f:getFaceLattice().withDimension(2)) {
//            if (f.covector().sign(getInfinity())==1 && !touchesInfinity(f)) {
//                rslt.addVertex(f);
//            }
//        }
//        for (Face from:rslt.getVertices()) {
//            for (Face to:rslt.getVertices()) {
//                if (from.equals(to)) {
//                    continue;
//                }
//                UnsignedSet tension = notLoops.minus(from.covector().minus()).minus(to.covector().plus());
//                for (Label l:tension) {
//                    rslt.addEdge(new Tension(l, this.modified.asInt(l), from, to), from, to);
//                }
//            }
//        }
//        return rslt;
//    }

    public boolean touchesInfinity(Face f) {
        for (Face e:f.lower()) {
            for (Face v:e.lower()) {
                if (v.covector().sign(getInfinity())==0) {
                    return true;
                }
            }
        }
        return false;
    }

    public FactoryFactory ffactory() {
        return original.ffactory();
    }

    public boolean areParallel(Label line1, Label line2) {
        return lineIntersection(line1,line2).sign(getInfinity()) == 0;
    }

    public SignedSet lineIntersection(Label line1, Label line2) {
        Set<Face> points = new HashSet<>(Arrays.asList(getPseudoLine(line1)));
        points.retainAll(Arrays.asList(getPseudoLine(line2)));
        if (points.size() != 1) {
            throw new IllegalStateException("Pseudolines intersect in two points???!!!");
        }
        return points.iterator().next().covector();
    }

    public void collectConformingTopes(SignedSet triangleId, Set<Face> extent) {
        // TODO something in the non-Uniform case
        for (Face f:properFaces()) {
            if (f.covector().conformsWith(triangleId)) {
                extent.add(f);
            }
        }
    }
    
    private Face[] getPseudoLine(Label line) {
        int ix = modified.asInt(line);
        return getPseudoLines()[ix];
    }

    private Face[][] getPseudoLines() {
        if (pseudoLines == null) {
            fullPseudoLines = computePseudoLines();
            pseudoLines = new Face[fullPseudoLines.length][];
            for (int i=0;i<pseudoLines.length;i++) {
                int duplicates = 0;
                List<Face[]> full = fullPseudoLines[i];
                for (int j=1;j<full.size();j++) {
                    if (full.get(j-1)[0].equals(full.get(j)[0])) {
                        duplicates++;
                    }
                }
                pseudoLines[i] = new Face[full.size()-duplicates];
                Face pl[] = pseudoLines[i];
                pl[0] = full.get(0)[0];
                int ix = 1;
                for (int j=1;j<full.size();j++) {
                    if (!full.get(j-1)[0].equals(full.get(j)[0])) {
                        pl[ix++] = full.get(j)[0];
                    }
                }
            }
        }
        return pseudoLines;
    }

    public Iterable<? extends Face> properFaces() {
        return Iterables.filter(getFaceLattice().withDimension(2), new Predicate<Face>(){
            @Override
            public  boolean apply(Face f) {
                return f.covector().sign(getInfinity())==1 && !touchesInfinity(f);
            }
        });
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

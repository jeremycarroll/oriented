/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import net.sf.oriented.impl.util.Misc;
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
    @SuppressWarnings("unused")
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
        
//        UnsignedSet ground = bestTope.support();
        
//        for (SignedSet cc:reoriented.dual().getCircuits()) {
//            for (Label l: ground.minus(cc.support())) {
//                line2cocircuit.get(l).add(cc);
//            }
//            
//            
//        }
        
        int newOrder[] = Permutation.from0toN(om.n());
        newOrder[infinity] = 0;
        newOrder[0] = infinity;
        Integer newOrderBoxed[] = Misc.box(newOrder);
        Arrays.sort(newOrderBoxed,1,newOrder.length,new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                int chi = reoriented.chi(infinity,o1,o2);
//                if (chi == 0) {
//                    int ix0 = "875".indexOf(om.elements()[o1].label());
//                    int ix1 = "875".indexOf(om.elements()[o1].label());
//                    if ( ix0!=-1
//                        && ix1!=-1 ) {
//                        System.err.println("xxx");
//                        return ix1 - ix0;
//                    }
//                }
                return chi;
            }});
        permutation = new Permutation(Misc.unbox(newOrderBoxed));
        SignedSet bestTopeR = reoriented.ffactory().signedSets().construct(bestTope.support(), empty);
        System.err.print(om.asInt(followLine(infLabel,bestTopeR,reoriented))+ " = ");
        System.err.println(newOrder[1]);
        this.modified = reoriented.permuteGround(permutation).getChirotope();
        
    }

    private Label followLine(Label line, SignedSet startHere, OM om) {
        FaceLattice fl = om.getFaceLattice();
        Face s = fl.get(startHere);
        Face[] edges = edgesTouchingLine(line, s);
        Label xline0 = getLine(startHere,edges[0].covector());
        Label xline1 = getLine(startHere,edges[1].covector());
        int χ = om.getChirotope().chi(om.asInt(line,xline0,xline1));
        Face firstLine;
        Label xline;
        switch (χ) {
        case -1:
            firstLine = edges[0];
            xline = xline1;
            break;
        case 1:
            firstLine = edges[1];
            xline = xline0;
            break;
        case 0:
            throw new IllegalArgumentException("chi returned 0, so "+startHere+" was not a good starting point");
        default:
             throw new IllegalStateException("chi returned "+χ);
        }
        return xline;
    }

    /** return the label of one of the elements in the tope, that is not in the edge.
     * There is typically exactly one, but there can be more than one when there are parallel
     * edges.
     * @param tope
     * @param edge
     * @return
     */
    private Label getLine(SignedSet tope, SignedSet edge) {
        return tope.support().minus(edge.support()).asCollection().iterator().next();
    }

    public Face[] edgesTouchingLine(Label line, Face face) {
        Face edges[] = new Face[2];
        int eCnt = 0;
        for (Face edge : face.lower()) {
            int cnt = 0;
            for (Face point: edge.lower()) {
                if (point.covector().sign(line)==0) {
                    cnt++;
                }
            }
            switch (cnt) {
            case 0:
                continue;
            case 1:
                edges[eCnt++] = edge;
                continue;
            case 2:
                // edge lies along line
                continue;
            default:
                throw new IllegalStateException("unexpected topology");
            }
        }
        return edges;
    }

    public String[] toCrossingsString() {
        final Label ground[] = modified.elements();
        int n = ground.length;
        Integer result[][] = new Integer[n][];
        boolean oneChar = allOneChar(ground);
        
/* for i < j < k we have
        int sA = sign(i,j,k);
        int sB = sign(j,i,k);
        int sC = sign(k,i,j);
   are all the same, where the sign(i,j,k) is the j.compare(k) in the i-th crossings
   moreover this is chi(i,j,k)
   
   3, 4, 5    +
   3, 5, 4    +
   5, 3, 4    +
   5, 4, 3    +
   4, 3, 5    -
   4, 5, 3    -
   
   
 */
        result[0] = Misc.box(Permutation.from0toN(n));
        for (int cnt=1;cnt<n;cnt++) {
            final int i = cnt;
//            Label lbl = ground[i];
//            System.err.println(lbl.label());
            result[i] = Misc.box(Permutation.from0toN(n));
            result[i][0] = i;
            result[i][i] = 1;
            result[i][1] = 0;
            Arrays.sort(result[i],2,n,new Comparator<Integer>(){
                @Override
                public int compare(Integer o1, Integer o2) {
//                    String l1 = ground[o1].label();
//                    String l2 = ground[o2].label();
//                    System.err.println(l1 + " <> " + l2);
                    int sign = (o1 - i)*(o2 - i) < 0 ? -1: 1;
                    return -sign*modified.chi(i,o1,o2);
                }});
        }
        String crossings[] = new String[result.length];
        for (int cnt=0;cnt<result.length;cnt++) {
            crossings[cnt] = toString(cnt,result[cnt],ground,!oneChar);
        }
        return crossings;
    }

    private boolean allOneChar(Label[] ground) {
        for (Label l:ground) {
            if (l.label().length()>1) {
                return false;
            }
        }
        return true;
    }

    private String toString(int line, Integer[] lines, Label[] ground, boolean useCommas) {
        StringBuffer rslt = new StringBuffer();
        rslt.append(ground[line].label());
        rslt.append(':');
        boolean insideBracket = false;
        for (int i=1;i<lines.length-1;i++) {
            boolean nextInsideBracket = modified.chi(line,lines[i],lines[i+1])==0;
            if (nextInsideBracket && !insideBracket) {
                rslt.append('(');
            }
            rslt.append(ground[lines[i]].label());
            if (insideBracket && !nextInsideBracket) {
                rslt.append(')');
            }
            insideBracket = nextInsideBracket;
            if (useCommas) {
                rslt.append(',');
            }
            
        }
        rslt.append(ground[lines[lines.length-1]].label());
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.stretching;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

import net.sf.oriented.impl.util.Misc;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Permutation;

public class Realization {
    
    private final OM original;
    private final OMasChirotope modified;
    private Label[] reorientation;
    private Permutation permutation;
    

    public Realization(OM om, String infinity) {
        this(om,om.asInt(infinity));
    }

    public Realization(OM om, Label infinity) {
        this(om,om.asInt(infinity));
    }

    private Realization(OM om, final int infinity) {
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
        UnsignedSet infUS = f.unsignedSets().empty().union(infLabel);
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
        reorientation = bestTope.minus().toArray();
        
        final OMasChirotope reoriented = om.reorient(reorientation).getChirotope();
        
        int newOrder[] = Permutation.from0toN(om.n());
        newOrder[infinity] = 0;
        newOrder[0] = infinity;
        Integer newOrderBoxed[] = Misc.box(newOrder);
        Arrays.sort(newOrderBoxed,1,newOrder.length,new Comparator<Integer>(){
            @Override
            public int compare(Integer o1, Integer o2) {
                return reoriented.chi(infinity,o1,o2);
            }});
        permutation = new Permutation(Misc.unbox(newOrderBoxed));
        this.modified = reoriented.permuteGround(permutation).getChirotope();
        
    }

    public String[] toCrossingsString() {
        Label ground[] = modified.elements();
        int n = ground.length;
        Integer result[][] = new Integer[n][];
        
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
            result[i] = Misc.box(Permutation.from0toN(n));
            result[i][0] = i;
            result[i][i] = 1;
            result[i][1] = 0;
            Arrays.sort(result[i],2,n,new Comparator<Integer>(){
                @Override
                public int compare(Integer o1, Integer o2) {
                    int sign = (o1 - i)*(o2 - i) < 0 ? -1: 1;
                    return sign*modified.chi(i,o1,o2);
                }});
        }
        String crossings[] = new String[result.length];
        for (int cnt=0;cnt<result.length;cnt++) {
            crossings[cnt] = toCrossing(cnt,result[cnt],ground);
        }
        return crossings;
    }

    private String toCrossing(int line, Integer[] lines, Label[] ground) {
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

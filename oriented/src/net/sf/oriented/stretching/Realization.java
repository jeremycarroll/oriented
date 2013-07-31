/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.stretching;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.Permutation;

public class Realization {
    
    private final OM original;
    private final OM modified;
    private Label[] reorientation;
    

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
        
        final OM reoriented = om.reorient(reorientation);
        
        int newOrder[] = Permutation.from0toN(om.n());
        newOrder[infinity] = 0;
        newOrder[0] = infinity;
        Integer newOrderBoxed[] = new Integer[newOrder.length];
        for (int i=0;i<newOrder.length;i++) {
            newOrderBoxed[i] = newOrder[i];
        }
        Arrays.sort(newOrderBoxed,1,newOrder.length,new Comparator<Integer>(){

            @Override
            public int compare(Integer o1, Integer o2) {
                return reoriented.getChirotope().chi(infinity,o1,o2);
            }});
        for (int i=0;i<newOrder.length;i++) {
            newOrder[i] = newOrderBoxed[i];
        }
        Permutation permutation = new Permutation(newOrder);
        this.modified = reoriented.permuteGround(permutation);
        
    }

    public String toCrossingsString() {
        // TODO Auto-generated method stub
        return null;
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

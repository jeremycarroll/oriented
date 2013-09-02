/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package oriented;

import java.util.Arrays;
import java.util.Map;

import net.sf.oriented.impl.util.Misc;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.util.combinatorics.CoLexicographic;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Run the main method to get some of the workings of my answer to the
 * <a href="http://mathoverflow.net/questions/129698/realizability-of-extensions-of-a-free-oriented-matroid-by-an-independent-set">
 * Realizability of extensions of a free oriented matroid by an independent set</a>
 * @author jeremycarroll
 */
public class MathOverflow129698 {
    private int notBasis;
    private int basisAndDependent;
    private int basisAndIndependent;
    private final String name;
    private final OM om;

    private MathOverflow129698(String name, OM om) {
        notBasis = 0;
        basisAndDependent = 0;
        basisAndIndependent = 0;
        this.name = name;
        this.om = om;
    }

    public static void main(String args[]) {
        for (Map.Entry<String,OM> entry:Examples.all().entrySet() ) {
            new MathOverflow129698(entry.getKey(),entry.getValue()).check();
        }
    }

    void check() {
        if (om.rank() * 2 < om.n()) {
            final OM dual = om.dual();
            for (int args[] : new CoLexicographic(dual.n(), dual.rank() ) ) {
                checkBasis(args, dual);
            }
            System.err.println(name+": good = "+ basisAndIndependent + "; not basis = "+notBasis +"; bad = " + basisAndDependent);
        }
    }

    private void checkBasis(int[] possibleBasis, final OM dual) {
        if (dual.getChirotope().chi(possibleBasis) != 0 ) { 
            // linearly independent
            UnsignedSet basis = dual.ffactory().unsignedSets()
                    .copyBackingCollection(
                    Lists.transform(Arrays.asList(Misc.box(possibleBasis)),
                            new Function<Integer,Label>(){
                        @Override
                        public Label apply(Integer i) {
                            return dual.elements()[i];
                        }
                    }));
            UnsignedSet other = dual.setOfElements().minus(basis);
            boolean independent = dual.getMatroid().getIndependentSets().contains(other);
            if (independent) {
                basisAndIndependent++;
            } else {
                basisAndDependent++;
            }
        } else {
            notBasis++;
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

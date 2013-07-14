/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;


/**
 * This is the mathematically concept of finite group,
 * however we consider every group as a subgroup of a permutation group,
 * and every group element is a {@link Permutation}. 
 * @author jeremycarroll
 *
 */
public abstract class Group implements Iterable<Permutation> {
    
    private Permutation identity;
    private final int n;
    public Group(int n) {
        this.n = n;
    }
    public Permutation identity(){
        if (identity == null) {
            identity = identityN(n());
        }
        return identity;
    }
    private static Permutation identityN(int n) {
        return new Permutation(Permutation.from0toN(n));
    }
    public abstract long order();
    
    public int n() {
        return n;
    }
    public static Group symmetric(int n) {
        return new SymmetricGroup(n);
    }
    public static Group identityGroup(int n) {
        return new SubGroup(Arrays.asList(identityN(n)));
    }
    
    /**
     * Return a subgroup created by accepting some of the elements
     * in the group.
     * @param pred This predicate must define a subgroup - no checking is provided.
     * @return
     */
    public Group filter(Predicate<Permutation> pred) {
        List<Permutation> contents = new ArrayList<Permutation>();
        for (Permutation p:this) {
            if (pred.apply(p)) {
                contents.add(p);
            }
        }
        return new SubGroup(contents);
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util.combinatorics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    protected Group(int n) {
        this.n = n;
    }
    /**
     * The identity element of this group.
     */
    public Permutation identity(){
        if (identity == null) {
            identity = identityN(n());
        }
        return identity;
    }
    private static Permutation identityN(int n) {
        return new Permutation(Permutation.from0toN(n));
    }
    /**
     * The number of elements in this group.
     */
    public abstract long order();
    

    /**
     * The permutations in this group permute the numbers <code>0, 1, .... n - 1</code>
     */
    public int n() {
        return n;
    }
    /**
     * The symmetric group of permutations over <code>0, 1, .... n - 1</code>
     */
    public static Group symmetric(int n) {
        return new SymmetricGroup(n);
    }
    /**
     * The group consisting of the identity permutation over  <code>0, 1, .... n - 1</code>
     */
    public static Group identityGroup(int n) {
        return new SubGroup(Arrays.asList(identityN(n)));
    }
    
    /**
     * Create a subgroup by accepting some of the elements
     * in the group.
     * @param pred This predicate must define a subgroup - no checking is provided.
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

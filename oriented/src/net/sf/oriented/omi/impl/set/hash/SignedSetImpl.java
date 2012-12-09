/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.set.hash;

import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.HasFactoryImpl;
import net.sf.oriented.omi.impl.set.SignedSetFactory;
import net.sf.oriented.omi.impl.set.SignedSetInternal;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;

public class SignedSetImpl extends
	HasFactoryImpl<SignedSetInternal, SignedSet, SignedSetInternal>
	implements SignedSetInternal {
    @Override
    public SignedSetFactory factory() {
	return (SignedSetFactory) super.factory();
    }

    final private UnsignedSetInternal plus, minus;

    // final static private SignedSet EMPTY = new SignedSet(UnsignedSet.EMPTY);
    //
    // static public SignedSet empty(UnsignedSet support) {
    // return EMPTY;
    // }

    public SignedSetImpl(UnsignedSetInternal p, UnsignedSetInternal m,
	    SignedSetFactory f) {
	super(f);
	plus = p;
	minus = m;
	if (plus.intersection(minus).size() != 0)
	    throw new IllegalArgumentException(
		    "plus and minus must be disjoint.");
    }

    // public SignedSet(UnsignedSet p) {
    // this(p, UnsignedSet.EMPTY);
    // }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#opposite()
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#opposite()
     */
    @Override
    public SignedSetImpl opposite() {
	return new SignedSetImpl(minus, plus, factory());
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#plus()
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#plus()
     */
    @Override
    public UnsignedSetInternal plus() {
	return plus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#minus()
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#minus()
     */
    @Override
    public UnsignedSetInternal minus() {
	return minus;
    }

    // public SignedSet empty() {
    // return EMPTY;
    // }

    @Override
    public int hashCode() {
	return plus.hashCode() + 35 * minus.hashCode();
    }

    @Override
    public boolean equals(Object o) {
	if (o == null) return false;
	if (!(o instanceof SignedSet)) return false;
	SignedSet s = (SignedSet) o;
	return plus.equals(s.plus()) && minus.equals(s.minus());
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#equalsIgnoreSign(omi.SignedSetI)
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#equalsIgnoreSign(omi.SignedSet)
     */
    @Override
    public boolean equalsIgnoreSign(SignedSet s) {
	return equals(s) || equalsOpposite(s);
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#equalsOpposite(omi.SignedSetI)
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#equalsOpposite(omi.SignedSet)
     */
    @Override
    public boolean equalsOpposite(SignedSet s) {
	return equals(s.opposite());
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#separation(omi.SignedSet)
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#separation(omi.SignedSet)
     */
    @Override
    public UnsignedSet separation(SignedSet b) {
	return plus.intersection(b.minus()).union(minus.intersection(b.plus()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#compose(omi.SignedSet)
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#compose(omi.SignedSet)
     */
    @Override
    public SignedSetImpl compose(SignedSet b) {
	return new SignedSetImpl(plus.union(b.plus().minus(minus)),
		minus.union(b.minus().minus(plus)), factory());
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#size()
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#size()
     */
    @Override
    public int size() {
	return plus.size() + minus.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#conformsWith(omi.SignedSet)
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#conformsWith(omi.SignedSet)
     */
    @Override
    public boolean conformsWith(SignedSet a) {
	return plus.intersection(a.minus()).isEmpty()
		&& minus.intersection(a.plus()).isEmpty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#support()
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#support()
     */
    @Override
    public UnsignedSetInternal support() {
	return plus.union(minus);
    }

    // public String toString() {
    // return "(" + plus.toString() + "," + minus.toString() + ")";
    // }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#sign(omi.Label)
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#sign(omi.Label)
     */
    @Override
    public int sign(Label e) {
	if (plus().contains(e)) return 1;
	if (minus().contains(e)) return -1;
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#isRestrictionOf(omi.SignedSetI)
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#isRestrictionOf(omi.SignedSet)
     */
    @Override
    public boolean isRestrictionOf(SignedSet x) {
	return plus().isSubsetOf(x.plus()) && minus().isSubsetOf(x.minus());
    }

    /*
     * (non-Javadoc)
     * 
     * @see omi.SignedSetI#restriction(omi.UnsignedSetI)
     */
    /*
     * (non-Javadoc)
     * 
     * @see omi.impl.set.hash.SignedSetInternalx#restriction(omi.UnsignedSet)
     */
    @Override
    public SignedSetImpl restriction(UnsignedSet x) {
	return new SignedSetImpl(plus().intersection(x), minus()
		.intersection(x), factory());
    }

}
/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * 
 * 
 * 
 * 
 * 
 * The Java Oriented Matroid Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Java Oriented Matroid Library. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

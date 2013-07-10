/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi.impl.set;

import java.util.Iterator;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetOf;
import net.sf.oriented.omi.impl.items.FactoryInternal;
import net.sf.oriented.omi.impl.items.HasFactory;

/**
 * Sets built around java hash sets
 * @author jeremycarroll
 * 
 * @param <ITEM_INTERNAL>
 *            The internal API or implementation for members.
 * @param <SET_INTERNAL>
 *            The internal API or implementation for sets.
 * @param <ITEM>
 *            The external API for members.
 * @param <SET>
 *            The external API for sets.
 * @param <ITEM_INTERNAL2>
 *            See {@link net.sf.oriented.util.TypeChecker}
 * @param <SET_INTERNAL2>
 *            See {@link net.sf.oriented.util.TypeChecker}
 */
//@formatter:off
abstract public class AbsSetImpl<
                  ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
                  SET_INTERNAL extends SetOfInternal<ITEM_INTERNAL, SET_INTERNAL, 
                                                     ITEM, SET, 
                                                     ITEM_INTERNAL2, SET_INTERNAL2>, 
                  ITEM, 
                  SET extends SetOf<ITEM, SET>, 
                  ITEM_INTERNAL2 extends ITEM, 
                  SET_INTERNAL2 extends SET
                  >
        extends HasSetFactoryImpl<
              ITEM_INTERNAL, 
              SET_INTERNAL, 
              ITEM, 
              SET, 
              ITEM_INTERNAL2, 
              SET_INTERNAL2> implements
        SetOfInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2> {

//@formatter:on

    protected AbsSetImpl(FactoryInternal<SET_INTERNAL, SET, SET_INTERNAL2> f) {
        super(f);
    }

    @Override
    public boolean verify() {
        if (!equalsIsSameSetAs()) {
            return true;
        }
        int hashCode = 0;
        for (ITEM_INTERNAL2 member:this) {
            hashCode += member.hashCode();
        }
        return hashCode == hashCode();
    }

    public SET_INTERNAL2 only(Test<ITEM> t) {
    	JavaSet<ITEM_INTERNAL2> r = emptyCollectionOf();
    	Iterator<ITEM_INTERNAL2> i = iterator();
    	while (i.hasNext()) {
    		ITEM_INTERNAL2 n = i.next();
    		if (t.test(n)) {
    			r.add(n);
    		}
    	}
    	return useCollection(r);
    }

    protected JavaSet<ITEM_INTERNAL2> emptyCollectionOf() {
    	return factory().itemFactory().emptyCollectionOf();
    }

    protected ITEM_INTERNAL2 ith(int x) {
    	Iterator<ITEM_INTERNAL2> it = iterator();
    	while (x > 0) {
    		x--;
    		it.next();
    	}
    	return it.next();
    }

    @Override
    public ITEM_INTERNAL2 theMember() {
    	Iterator<ITEM_INTERNAL2> it = iterator();
    	ITEM_INTERNAL2 r = it.next();
    	if (it.hasNext())
    		throw new IllegalStateException("More than one member.");
    	return r;
    }

    @Override
    public boolean equalsIsSameSetAs() {
    	return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SET_INTERNAL2 respectingEquals() {
    	return (SET_INTERNAL2) this;
    }
}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
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

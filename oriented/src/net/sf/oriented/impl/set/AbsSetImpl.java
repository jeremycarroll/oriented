/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.set;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Iterator;

import net.sf.oriented.impl.items.FactoryInternal;
import net.sf.oriented.impl.items.HasFactory;
import net.sf.oriented.impl.util.RuntimeClass;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetOf;

import com.google.common.reflect.TypeToken;

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
 *            See {@link net.sf.oriented.impl.util.TypeChecker}
 * @param <SET_INTERNAL2>
 *            See {@link net.sf.oriented.impl.util.TypeChecker}
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

            private final class PowerJavaSet extends AbstractCollection<SET_INTERNAL2> implements
                    JavaSet<SET_INTERNAL2> {

                final int size;

                private PowerJavaSet(int sz) {
                    size = 1 << sz;
                }

                @Override
                public Iterator<SET_INTERNAL2> iterator() {
                    return new Iterator<SET_INTERNAL2>() {
                        int i = 0;

                        @Override
                        public boolean hasNext() {
                            return i < size;
                        }

                        @Override
                        public SET_INTERNAL2 next() {
                            JavaSet<ITEM_INTERNAL2> m = emptyCollectionOf();
                            Iterator<ITEM_INTERNAL2> it = AbsSetImpl.this.iterator2();
                            int j = 0;
                            while (it.hasNext()) {
                                ITEM_INTERNAL2 n = it.next();
                                if (((1 << j) & i) != 0) {
                                    m.add(n);
                                }
                                j++;
                            }
                            i++;
                            return useCollection(m);
                        }

                        @Override
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }

                @Override
                public int size() {
                    return size;
                }
            }

    protected AbsSetImpl(FactoryInternal<SET_INTERNAL, SET, SET_INTERNAL2> f) {
        super(f);
    }

    @Override
    public void verify() throws AxiomViolation {
        if (!equalsIsSameSetAs()) {
            return;
        }
        int hashCode = 0;
        for (ITEM member:this) {
            hashCode += member.hashCode();
        }
        if ( hashCode != hashCode() ) {
            throw new AxiomViolation(this,"hashCode and equals (Java Contract)");
        }
    }

    public SET_INTERNAL2 only(Test<ITEM> t) {
    	JavaSet<ITEM_INTERNAL2> r = emptyCollectionOf();
    	Iterator<ITEM_INTERNAL2> i = iterator2();
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
    	Iterator<ITEM_INTERNAL2> it = iterator2();
    	while (x > 0) {
    		x--;
    		it.next();
    	}
    	return it.next();
    }

    @Override
    public ITEM_INTERNAL2 theMember() {
    	Iterator<ITEM_INTERNAL2> it = iterator2();
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
    public SET_INTERNAL2 respectingEquals() {
    	return (SET_INTERNAL2) this;
    }

    @Override
    public int hashCode() {
    	int rslt = 0;
    	Iterator<ITEM_INTERNAL2> it = iterator2();
    	while (it.hasNext()) {
    		rslt += it.next().hashCode();
    	}
    	return rslt;
    }

    @Override
    public boolean equals(Object o) {
    	if (o == null || (!(o instanceof SetOf)))
    		return false;
    	SET sx = (SET) o;
    	return sx.equalsIsSameSetAs() && sameSetAs(sx);
    }

    @Override
    public boolean sameSetAs(SET a) {
    	if (size() != a.size())
    		return false;
    	return isSubsetOf(a) && isSupersetOf(a);
    }

    /**
     * r is dedicated to being the backing collection for this set. It is *not*
     * copied, and must not be modified after this call.
     * 
     * @param bases
     * @return
     */
    @Override
    public SET_INTERNAL2 useCollection(JavaSet<ITEM_INTERNAL2> bases) {
    	return factory().fromBackingCollection(bases);
    }

    public SET_INTERNAL2 excluding(Test<ITEM> t) {
    	JavaSet<ITEM_INTERNAL2> r = emptyCollectionOf();
    	Iterator<ITEM_INTERNAL2> i = iterator2();
    	while (i.hasNext()) {
    		ITEM_INTERNAL2 n = i.next();
    		if (!t.test(n)) {
    			r.add(n);
    		}
    	}
    	return useCollection(r);
    }

    @Override
    public JavaSet<SET_INTERNAL2> powerSet() {
    	final int sz = size();
    	if (sz > 30)
    		throw new IllegalArgumentException("unimplemented powerset sz > 30");
    	return new PowerJavaSet(sz);
    }
    
    @Override
    public Iterator<ITEM> iterator() {
        return (Iterator<ITEM>) this.iterator2();
    }
        
    @Override
    public ITEM[] toArray(){
        ITEM rslt[] = (ITEM[]) Array.newInstance(new RuntimeClass<ITEM>(){ 
            @Override
            protected TypeToken<ITEM> getTypeToken(Class<?> x) {
               return new TypeToken<ITEM>(x){};
           }
        }.getRuntimeClass(getClass()), this.size() );
        int i = 0;
        for (ITEM x:this) {
            rslt[i++] = x;
        }
        return rslt;
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

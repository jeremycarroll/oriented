/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.items;

import com.google.common.reflect.TypeToken;

import net.sf.oriented.impl.util.TypeChecker;
import net.sf.oriented.omi.Factory;
import net.sf.oriented.omi.FactoryFactory;

/**
 * 
 * Things that have factories.
 * @author jeremycarroll
 *
 * @param <ITEM_INTERNAL> The internal API or implementation for things.
 * @param <ITEM> The external API for things.
 * @param <ITEM_INTERNAL2> See {@link net.sf.oriented.impl.util.TypeChecker}
 */
// @formatter:off
public abstract class HasFactoryImpl<
     ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
     ITEM, 
     ITEM_INTERNAL2 extends ITEM>
	implements HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> {
 // @formatter:on

	protected final FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> factory;

	protected HasFactoryImpl(FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> f) {
		factory = f;
		if (FactoryFactory.additionalRuntimeChecking) {
		    new TypeChecker<ITEM_INTERNAL,ITEM_INTERNAL2>(){ 
		        @Override
		        protected TypeToken<ITEM_INTERNAL> getTypeToken(Class<?> x) {
		            return new TypeToken<ITEM_INTERNAL>(x){};
		        }
		        @Override
		        protected TypeToken<ITEM_INTERNAL2> getTypeToken2(Class<?> x) {
		            return new TypeToken<ITEM_INTERNAL2>(x){};
		        }
		    }.check(getClass());
		}
	}

	@Override
	public FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> factory() {
		return factory;
	}

	@Override
	final public String toString() {
		return toString(factory);
	}

	@Override
	@SuppressWarnings("unchecked")
	final public String toString(Factory<ITEM> f) {
		return f.toString((ITEM) this);
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

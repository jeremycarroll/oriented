/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.items;

import net.sf.oriented.omi.Factory;
import net.sf.oriented.util.TypeChecker;


// @formatter:off
public class HasFactoryImpl<
     ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
     ITEM, 
     ITEM_INTERNAL2 extends ITEM>
	implements HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> {
 // @formatter:on

	protected final FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> factory;

	protected HasFactoryImpl(FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> f) {
		factory = f;
        TypeChecker.check(this);
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

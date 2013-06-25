/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.items;

import net.sf.oriented.omi.Factory;

public class HasFactoryImpl<E extends HasFactory<E, EX, ER>, EX, ER extends EX>
		implements HasFactory<E, EX, ER> {

	protected final FactoryInternal<E, EX, ER> factory;

	protected HasFactoryImpl(FactoryInternal<E, EX, ER> f) {
		factory = f;
	}

	@Override
	public FactoryInternal<E, EX, ER> factory() {
		return factory;
	}

	@Override
	final public String toString() {
		return toString(factory);
	}

	@Override
	@SuppressWarnings("unchecked")
	final public String toString(Factory<EX> f) {
		return f.toString((EX) this);
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

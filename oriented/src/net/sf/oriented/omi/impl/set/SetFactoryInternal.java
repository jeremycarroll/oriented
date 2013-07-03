/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.set;

import java.util.Collection;
import java.util.List;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetFactory;
import net.sf.oriented.omi.SetOf;
import net.sf.oriented.omi.impl.items.FactoryInternal;
import net.sf.oriented.omi.impl.items.HasFactory;
import net.sf.oriented.omi.impl.items.ParseContext;

public interface SetFactoryInternal<
        E extends HasFactory<E, EX, ER>, 
        S extends SetOfInternal<E, S, EX, SX, ER, T>, 
        EX, 
        SX extends SetOf<EX, SX>, 
        ER extends EX, 
        T extends SX>
		extends FactoryInternal<S, SX, T>, SetFactory<EX, SX> {
	/**
	 * bases is dedicated to being the backing collection of the newly
	 * constructed set. It is not copied and must not be modified after this
	 * call.
	 * 
	 * @param bases
	 * @return
	 */
	T fromBackingCollection(JavaSet<ER> bases);

	@Override
	T copyBackingCollection(Collection<? extends EX> c);

	FactoryInternal<E, EX, ER> itemFactory();

	@Override
	T empty();

	List<ER> orderedParse(ParseContext pc);
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

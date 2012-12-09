/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.omi.impl.set;

import net.sf.oriented.omi.SetOf;
import net.sf.oriented.omi.impl.items.FactoryInternal;
import net.sf.oriented.omi.impl.items.HasFactory;
import net.sf.oriented.omi.impl.items.HasFactoryImpl;

public class HasSetFactoryImpl<E extends HasFactory<E, EX, ER>, S extends SetOfInternal<E, S, EX, SX, ER, SS>, EX, SX extends SetOf<EX, SX>, ER extends EX, SS extends SX>
	extends HasFactoryImpl<S, SX, SS> {

    protected HasSetFactoryImpl(FactoryInternal<S, SX, SS> f) {
	super(f);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SetFactoryInternal<E, S, EX, SX, ER, SS> factory() {
	return (SetFactoryInternal<E, S, EX, SX, ER, SS>) super.factory();
    }
    // private final JavaSet<ER> members;

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

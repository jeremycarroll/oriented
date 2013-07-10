/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi.impl.set;

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
        // TODO Auto-generated method stub
        return false;
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

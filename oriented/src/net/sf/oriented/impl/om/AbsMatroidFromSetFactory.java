/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.List;

import net.sf.oriented.impl.items.ParseContext;
import net.sf.oriented.impl.set.SetFactoryInternal;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOf;

/**
 * Create matroids, possible oriented, from sets of things.
 * @author jeremycarroll
 *
 * @param <MATROID>
 * @param <SET_STRUCTURE>
 * @param <SETFACTORY>
 */
//@formatter:off
public abstract class AbsMatroidFromSetFactory<
       MATROID, 
       SET_STRUCTURE2 extends SetOf<?,SET_STRUCTURE2>,
       SET_STRUCTURE extends SET_STRUCTURE2, 
       SETFACTORY extends SetFactoryInternal<?,?,?,SET_STRUCTURE2,?,?>
       > 
       extends AbsMatroidFactory<MATROID, SET_STRUCTURE2> {
//@formatter:on
    final protected SETFACTORY sets;

    AbsMatroidFromSetFactory(FactoryFactory f, SETFACTORY sf) {
        super(f);
        sets = sf;
    }

    abstract public List<Label> ground(MATROID s);

    protected String formatString(MATROID s, SET_STRUCTURE2 matroidS) {
        List<Label> g = ground(s);
        // TODO: this copyBackingCollection is probably spurious and should be
        // done without copying
        return "( "
                + unsignedSets().toString(g,
                        unsignedSets().copyBackingCollection(g)) + ", "
                + sets.toString(g, matroidS) + " )";
    }

    @SuppressWarnings("unchecked")
    @Override
    SET_STRUCTURE parseMatroid(ParseContext pc) {
        return  (SET_STRUCTURE)sets.parse(pc);
    }

    public MATROID empty() {
        throw new UnsupportedOperationException("No 'empty' oriented matroid");
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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import java.util.List;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.impl.items.ParseContext;
import net.sf.oriented.omi.impl.set.SetFactoryInternal;

//@formatter:off
@SuppressWarnings("rawtypes")
public abstract class AbsFactory<MATROID, STRUCTURE, SETFACTORY extends SetFactoryInternal> 
       extends MoreAbsFactory<MATROID, STRUCTURE> {
//@formatter:on
    final protected SETFACTORY sets;

    AbsFactory(FactoryFactory f, SETFACTORY sf) {
        super(f);
        sets = sf;
    }

    abstract public List<Label> ground(MATROID s);

    @SuppressWarnings("unchecked")
    protected String formatString(MATROID s, MATROID matroidS) {
        List<Label> g = ground(s);
        // TODO: this copyBackingCollection is probably spurious and should be
        // done without copying
        return "( "
                + unsignedSets().toString(g,
                        unsignedSets().copyBackingCollection(g)) + ", "
                + sets.toString(g, matroidS) + " )";
    }

    @Override
    @SuppressWarnings("unchecked")
    STRUCTURE parseMatroid(ParseContext pc) {
        return (STRUCTURE) sets.parse(pc);
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

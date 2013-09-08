/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.items;

import java.lang.reflect.Constructor;
import java.util.List;

import com.google.common.reflect.TypeToken;


import net.sf.oriented.impl.util.TypeChecker;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.Options;

//@formatter:off
public abstract class FactoryImpl<
        ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
        ITEM, 
        ITEM_INTERNAL2 extends ITEM>
        extends AbsFactoryImpl<ITEM> implements FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> {
//@formatter:on

    final protected Constructor<ITEM_INTERNAL2> constructor;

    @SuppressWarnings("unchecked")
    protected FactoryImpl(Options o) {
        options = o;
        constructor = (Constructor<ITEM_INTERNAL2>) ((OptionsInternal)o).constructorFor(getClass());
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

    final private Options options;

    @Override
    final public ITEM_INTERNAL2 parse(String s) {
        ParseContext pc = new ParseContext(s.trim());
        ITEM_INTERNAL2 rslt = parse(pc);
        if (pc.index != pc.string.length())
            throw new IllegalArgumentException("Syntax error");
        return rslt;
    }

    @Override
    public Options getOptions() {
        return options;
    }

    static int cnt = 0;

    @Override
    public JavaSet<ITEM_INTERNAL2> emptyCollectionOf() {
        return options.javaSetFor(null);
    }

    @Override
    public String toString(List<? extends Label> u, ITEM t) {
        return toString(t);
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

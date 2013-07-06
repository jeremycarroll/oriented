/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.items;

import java.lang.reflect.Constructor;
import java.util.List;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.Options;
import net.sf.oriented.util.TypeChecker;

//@formatter:off
public abstract class FactoryImpl<
        ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
        ITEM, 
        ITEM_INTERNAL2 extends ITEM>
		extends IOHelper implements FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> {
//@formatter:on

	final protected Constructor<ITEM_INTERNAL2> constructor;

	@SuppressWarnings("unchecked")
	protected FactoryImpl(Options o) {
        TypeChecker.check(this);
		options = o;
		// System.err.println(++cnt+": building: "+getClass().getSimpleName());
		constructor = (Constructor<ITEM_INTERNAL2>) o.constructorFor(getClass());
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
	@SuppressWarnings("unchecked")
	public ITEM_INTERNAL remake(ITEM t) {
		if (t instanceof HasFactory) {
			if (((HasFactory<?, ?, ?>) t).factory() == this)
				return (ITEM_INTERNAL) t;
		}
		// System.err.println(++cnt+":"+TestConversions.TEST_NUMBER+" remaking: "+getClass().getSimpleName());
		return fallbackRemake(t);
	}

	@SuppressWarnings("unchecked")
	protected ITEM_INTERNAL fallbackRemake(ITEM t) {
		return (ITEM_INTERNAL) parse(toString(t));
	}

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

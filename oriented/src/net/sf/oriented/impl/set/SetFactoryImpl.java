/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.set;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.impl.items.FactoryImpl;
import net.sf.oriented.impl.items.FactoryInternal;
import net.sf.oriented.impl.items.HasFactory;
import net.sf.oriented.impl.items.ParseContext;
import net.sf.oriented.impl.util.Misc;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOf;

//@formatter:off
abstract public class SetFactoryImpl<
       ITEM_INTERNAL extends HasFactory<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2>, 
       SET_INTERNAL extends SetOfInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2>, 
       ITEM, 
       SET extends SetOf<ITEM, SET>, 
       ITEM_INTERNAL2 extends ITEM, 
       SET_INTERNAL2 extends SET>
		extends FactoryImpl<SET_INTERNAL, SET, SET_INTERNAL2> implements
		SetFactoryInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2> {
//@formatter:on
	protected SetFactoryImpl(FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> f) {
		super(f.getOptions());
		itemFactory = f;
		e = fromBackingCollection(itemFactory.emptyCollectionOf());
	}

	final protected FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> itemFactory;

	@Override
	public FactoryInternal<ITEM_INTERNAL, ITEM, ITEM_INTERNAL2> itemFactory() {
		return itemFactory;
	}

	@Override
	final public SET_INTERNAL2 parse(ParseContext pc) {
		JavaSet<ITEM_INTERNAL2> rslt = itemFactory.emptyCollectionOf();
		parseItems(pc, rslt);
		return fromBackingCollection(rslt);
	}

	protected void parseItems(ParseContext pc, Collection<ITEM_INTERNAL2> rslt) {
		expect(pc, '{');
		while (pc.index < pc.string.length()
				&& pc.string.charAt(pc.index) != '}') {
			ITEM_INTERNAL2 ee = itemFactory.parse(pc);
			add(rslt, ee);
		}
		expect(pc, '}');
	}

	@Override
	final public List<ITEM_INTERNAL2> orderedParse(ParseContext pc) {
		List<ITEM_INTERNAL2> ll = new ArrayList<ITEM_INTERNAL2>();
		parseItems(pc, ll);
		return ll;
	}

	final private SET_INTERNAL2 e;

	@Override
	public SET_INTERNAL2 empty() {
		return e;
	}

	@Override
	final public String toString(SET s) {
		return toString(getOptions().getUniverse(), s);
	}

	@Override
	public String toString(List<? extends Label> u, SET s) {
		StringBuffer rslt = new StringBuffer();
		String sep = "";
		rslt.append("{");
		Iterator<? extends ITEM> it = s.iterator();
		while (it.hasNext()) {
			rslt.append(sep);
			rslt.append(itemFactory.toString(u, it.next()));
			sep = ",";
		}
		rslt.append("}");
		return rslt.toString();
	}

	@Override
	public SET_INTERNAL2 copyBackingCollection(Collection<? extends ITEM> c) {
		JavaSet<ITEM_INTERNAL2> r = itemFactory.emptyCollectionOf();
		Iterator<? extends ITEM> it = c.iterator();
		while (it.hasNext()) {
			add(r, it.next());
		}
		return fromBackingCollection(r);
	}

	@SuppressWarnings("unchecked")
	protected void add(Collection<ITEM_INTERNAL2> r, ITEM next) {
		r.add((ITEM_INTERNAL2) itemFactory.remake(next));
	}

	@Override
	final public SET_INTERNAL2 fromBackingCollection(JavaSet<ITEM_INTERNAL2> c) {
		return construct(c, this);
	}

	@SuppressWarnings("unchecked")
    @Override
	protected <T extends SET> T fallbackRemake(SET t) {
		return (T)copyBackingCollection(t.asCollection());
	}

	protected SET_INTERNAL2 construct(JavaSet<ITEM_INTERNAL2> c,
			SetFactoryInternal<ITEM_INTERNAL, SET_INTERNAL, ITEM, SET, ITEM_INTERNAL2, SET_INTERNAL2> f) {
	    return Misc.invoke(constructor,c,f);
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

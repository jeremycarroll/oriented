/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.set;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.Options;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.LabelFactory;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.items.ParseContext;

public class UnsignedSetFactory
		extends
		SetFactoryImpl<LabelImpl, UnsignedSetInternal, Label, UnsignedSet, LabelImpl, UnsignedSetInternal> {

	@Override
	public LabelFactory itemFactory() {
		return (LabelFactory) super.itemFactory();
	}

	public UnsignedSetFactory(LabelFactory f) {
		super(f);
	}

	@Override
	protected void parseItems(ParseContext pc, Collection<LabelImpl> rslt) {
		Options opt = getOptions();
		if (opt.getPlusMinus()) {
			parsePlusMinus(pc, rslt);
			return;
		}
		if (opt.getSingleChar()) {
			parseSingleChar(pc, rslt);
			return;
		}
		super.parseItems(pc, rslt);
	}

	private void parseSingleChar(ParseContext pc, Collection<LabelImpl> rslt) {

		String s = uptoSeparator(pc);
		if ("*".equals(s))
			return;
		LabelFactory itemF = itemFactory();
		for (int i = 0; i < s.length(); i++) {
			rslt.add(itemF.parse(s.substring(i, i + 1)));
		}
	}

	private void parsePlusMinus(ParseContext pc, Collection<LabelImpl> rslt) {

		String a = uptoSeparator(pc);
		List<Label> u = getOptions().getUniverse();
		for (int i = 0; i < a.length(); i++) {
			switch (a.charAt(i)) {
			case '0':
				break;
			case '1':
				rslt.add((LabelImpl) u.get(i));
				break;
			default:
				throw new IllegalArgumentException("not a sequence of 1, 0:"
						+ a);
			}
		}
	}

	/**
	 * @param u
	 * @param s
	 * @return
	 */
	private String toPlusMinus(final List<? extends Label> u, UnsignedSet plus) {
		return toString(u, plus, new Printing() {
			char rslt[];

			@Override
			public void in(int i, Label m) {
				rslt[i] = '1';
			}

			@Override
			public void init() {
				rslt = new char[u.size()];
			}

			@Override
			public void out(int i, Label m) {
				rslt[i] = '0';
			}

			@Override
			public String result() {
				return new String(rslt);
			}
		});
	}

	interface Printing {
		void init();

		void in(int i, Label m);

		void out(int i, Label m);

		String result();
	}

	private String toString(List<? extends Label> u, UnsignedSet plus,
			Printing p) {
		// Options opt = getOptions();
		// opt.extendUniverse(plus.asCollection());
		plus = remake(plus);

		p.init();

		Iterator<? extends Label> it = u.iterator();
		for (int i = 0; it.hasNext(); i++) {
			Label m = it.next();
			if (plus.contains(m)) {
				p.in(i, m);
			} else {
				p.out(i, m);
			}
		}
		return p.result();
	}

	private String toShortString(List<? extends Label> u, UnsignedSet plus) {
		final char rslt[] = new char[plus.size()];
		if (rslt.length == 0)
			return "*";
		return toString(u, plus, new Printing() {
			int j = 0;

			@Override
			public void init() {
			}

			@Override
			public void in(int i, Label m) {
				rslt[j++] = m.label().charAt(0);
			}

			@Override
			public void out(int i, Label m) {
			}

			@Override
			public String result() {
				return new String(rslt);
			}
		});
	}

	@Override
	public String toString(List<? extends Label> u, UnsignedSet s) {
		Options opt = getOptions();
		if (opt.getPlusMinus())
			return toPlusMinus(u, s);
		if (opt.getSingleChar())
			return toShortString(u, s);
		else
			return toLongString(u, s);
	}

	private String toLongString(List<? extends Label> u, UnsignedSet s) {
		final StringBuffer rslt = new StringBuffer();
		rslt.append("{");

		return toString(u, s, new Printing() {
			String sep = "";

			@Override
			public void init() {
			}

			@Override
			public void in(int i, Label m) {
				rslt.append(sep);
				rslt.append(m.label());
				sep = ",";
			}

			@Override
			public void out(int i, Label m) {
			}

			@Override
			public String result() {
				rslt.append("}");
				return rslt.toString();
			}
		});
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

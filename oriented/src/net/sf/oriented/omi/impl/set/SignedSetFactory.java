/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.set;

import java.util.Iterator;
import java.util.List;

import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.Options;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.FactoryImpl;
import net.sf.oriented.omi.impl.items.LabelFactory;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.items.ParseContext;
import net.sf.oriented.util.Misc;

public class SignedSetFactory extends
		FactoryImpl<SignedSetInternal, SignedSet, SignedSetInternal> {

	public static final long MASK = (1l << 32) - 1;

	public final UnsignedSetFactory unsignedF;

	private final SignedSetInternal empty;

	SignedSetFactory(LabelFactory f, UnsignedSetFactory uf) {
		super(f.getOptions());
		unsignedF = uf;
		empty = createSignedSet(unsignedF.empty(), unsignedF.empty());
	}

	public SignedSetInternal empty() {
		return empty;
	}

	@Override
	public SignedSetInternal parse(ParseContext pc) {
		Options opt = getOptions();
		if (opt.getPlusMinus())
			return parsePlusMinus(pc);
		if (opt.getSingleChar())
			return parseSingleChar(pc);
		return parseNormal(pc);
	}

	private SignedSetInternal parseNormal(ParseContext pc) {
		expect(pc, '(');
		UnsignedSetInternal p = unsignedF.parse(pc);
		UnsignedSetInternal m = unsignedF.parse(pc);
		expect(pc, ')');
		return createSignedSet(p, m);
	}

	private SignedSetInternal createSignedSet(UnsignedSetInternal p,
			UnsignedSetInternal m) {
        return Misc.invoke(constructor,p,m,this);
	}

	private SignedSetInternal parsePlusMinus(ParseContext pc) {

		String a = uptoSeparator(pc);
		JavaSet<LabelImpl> p = unsignedF.itemFactory.emptyCollectionOf();
		JavaSet<LabelImpl> m = unsignedF.itemFactory.emptyCollectionOf();
		List<Label> u = getOptions().getUniverse();
		for (int i = 0; i < a.length(); i++) {
			switch (a.charAt(i)) {
			case '0':
				break;
			case '+':
				p.add((LabelImpl) u.get(i));
				break;
			case '-':
				m.add((LabelImpl) u.get(i));
				break;
			default:
				throw new IllegalArgumentException("not a seuqence of +, 0, -:"
						+ a);
			}
		}
		return createSignedSet(unsignedF.fromBackingCollection(p),
				unsignedF.fromBackingCollection(m));
	}

	private SignedSetInternal parseSingleChar(ParseContext pc) {
		boolean plus = true;
		LabelFactory lf = unsignedF.itemFactory();
		JavaSet<LabelImpl> p = unsignedF.itemFactory.emptyCollectionOf();
		JavaSet<LabelImpl> m = unsignedF.itemFactory.emptyCollectionOf();
		String a = uptoSeparator(pc);
		if ("*".equals(a))
			return empty;
		for (int i = a.length() - 1; i >= 0; i--) {
			char ch = a.charAt(i);
			if (ch == '\'') {
				if (!plus)
					throw new IllegalArgumentException(
							"Syntax error: two primes");
				if (i == 0)
					throw new IllegalArgumentException(
							"Syntax error: initial prime");
				plus = false;
			} else {
				if (plus) {
					p.add(lf.parse(new String(new char[] { ch })));
				} else {
					m.add(lf.parse(new String(new char[] { ch })));
				}

				plus = true;
			}
		}
		return createSignedSet(unsignedF.fromBackingCollection(p),
				unsignedF.fromBackingCollection(m));
	}

	@Override
	public String toString(SignedSet s) {
		return toString(getOptions().getUniverse(), s);
	}

	@Override
	public String toString(List<? extends Label> u, SignedSet s) {
		Options opt = getOptions();

		if (opt.getPlusMinus())
			return toPlusMinus(u, s);
		if (opt.getSingleChar())
			return toShortString(u, s);
		else
			return "(" + unsignedF.toString(u, s.plus()) + ","
					+ unsignedF.toString(u, s.minus()) + ")";
	}

	/**
	 * @param s
	 * @return
	 */
	private String toPlusMinus(List<? extends Label> u, SignedSet s) {

		UnsignedSet plus = unsignedF.remake(s.plus());
		UnsignedSet minus = unsignedF.remake(s.minus());

		Iterator<? extends Label> it = u.iterator();
		char rslt[] = new char[u.size()];
		for (int i = 0; it.hasNext(); i++) {
			Label m = it.next();
			if (plus.contains(m)) {
				rslt[i] = '+';
			} else if (minus.contains(m)) {
				rslt[i] = '-';
			} else {
				rslt[i] = '0';
			}
		}
		return new String(rslt);
	}

	private String toShortString(List<? extends Label> u, SignedSet s) {
		// Options opt = getOptions();
		UnsignedSet plus = s.plus();
		UnsignedSet minus = s.minus();

		plus = unsignedF.remake(plus);
		minus = unsignedF.remake(minus);

		// opt.extendUniverse(plus.asCollection());
		// opt.extendUniverse(minus.asCollection());

		char rslt[] = new char[plus.size() + 2 * minus.size()];
		if (rslt.length == 0)
			return "*";
		Iterator<? extends Label> it = u.iterator();
		for (int i = 0; it.hasNext();) {
			Label m = it.next();
			String l = m.toString();
			if (plus.contains(m)) {
				rslt[i++] = l.charAt(0);
			} else if (minus.contains(m)) {
				rslt[i++] = l.charAt(0);
				rslt[i++] = '\'';
			}
		}
		return new String(rslt);
	}

	public SignedSetInternal create(UnsignedSetInternal plus,
			UnsignedSetInternal minus) {
		return createSignedSet(plus, minus);
	}

	private SignedSetInternal cache[];

	public void addToCache(int plus, int minus, SignedSetInternal s) {
		initCache();
		long value = toLong(plus, minus);
		int key = key(value);
		cache[key] = s;
	}

	private int key(long value) {
		return (int) (value % 18371);
	}

	private void initCache() {
		if (cache == null) {
			cache = new SignedSetInternal[18371];
		}
	}

	public SignedSetInternal cached(long l) {
		return cache[key(l)];
	}

	static public long toLong(int p, int m) {
		return (((long) p) << 32) | (m & MASK);
	}

	static public int minus(long l) {
		return (int) (l & MASK);
	}

	static public int plus(long l) {
		return (int) (l >> 32);
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

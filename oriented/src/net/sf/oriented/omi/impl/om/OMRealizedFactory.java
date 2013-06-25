/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package net.sf.oriented.omi.impl.om;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.oriented.matrix.PerisicField;
import net.sf.oriented.matrix.PerisicFieldElement;
import net.sf.oriented.matrix.RationalMatrix;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OMRealized;
import net.sf.oriented.omi.RealizedFactory;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.items.ParseContext;

public class OMRealizedFactory extends
		MoreAbsFactory<OMRealized, RationalMatrix> implements RealizedFactory {

	private static final class ParseResult {
		final ParseContext pc;
		List<PerisicFieldElement> nextColumn = new ArrayList<PerisicFieldElement>();
		final List<List<PerisicFieldElement>> data = new ArrayList<List<PerisicFieldElement>>();

		ParseResult(ParseContext pc) {
			this.pc = pc;
		}

		public void endColumn() {
			data.add(nextColumn);
			if (nextColumn.size() != data.get(0).size())
				throw new IllegalArgumentException("Array sizing problem in: "
						+ pc.string + " at position " + pc.index);
			nextColumn = new ArrayList<PerisicFieldElement>();
		}

		public RationalMatrix matrix() {
			return new RationalMatrix(data);
		}

		public void addDatum(String rational) {
			nextColumn.add(PerisicField.rational(rational));
		}

	}

	public OMRealizedFactory(FactoryFactory factory) {
		super(factory);
	}

	@Override
	public String toString(OMRealized t) {
		List<Label> g = Arrays.asList(t.ground());
		// TODO: this copyBackingCollection is probably spurious and should be
		// done without copying
		return "( "
				+ unsignedSets().toString(g,
						unsignedSets().copyBackingCollection(g)) + ", "
				+ t.getMatrix().toString() + " )";
	}

	@Override
	public OMRealized construct(RationalMatrix mat) {
		final int n = mat.width();
		return construct(new SimpleLabels(n), mat);
	}

	@Override
	public OMRealized construct(Collection<? extends Label> e,
			RationalMatrix mat) {
		LabelImpl[] g = e.toArray(new LabelImpl[0]);
		OMAll all = new OMAll(g, factory);
		return new RealizedImpl(all, mat);
	}

	static Pattern entry = Pattern
			.compile("(\\[)|(\\])|([ \t\n\r,]+)|(-?[0-9]+(/[0-9]+)?)");

	@Override
	RationalMatrix parseMatroid(ParseContext pc) {
		Matcher m = entry.matcher(pc.string);
		m.region(pc.index, pc.string.length());
		ParseResult result = new ParseResult(pc);
		int depth = 0;
		while (m.find()) {
			if (pc.index != m.start())
				throw new IllegalArgumentException(
						"Syntax error in: "
								+ pc.string
								+ "Expected  one of: \"0-9,[]\" or whitespace at position "
								+ pc.index + " found \""
								+ pc.string.substring(pc.index, pc.index + 4));
			pc.index = m.end();
			switch (pc.string.charAt(m.start())) {
			case '[':
				depth++;
				if (depth > 2)
					throw new IllegalArgumentException("Syntax error in: "
							+ pc.string + " too many '['s, at position "
							+ pc.index);
				break;
			case ']':
				depth--;
				switch (depth) {
				case 1:
					result.endColumn();
					break;
				case 0:
					skip(pc);
					return result.matrix();
				default:
					throw new IllegalArgumentException("Syntax error in: "
							+ pc.string + " not expecting ']' at position "
							+ pc.index);
				}
				break;
			case '-':
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				result.addDatum(m.group());
				break;
			case ' ':
			case '\t':
			case '\n':
			case '\r':
				break;
			default:
				throw new IllegalArgumentException(
						"Unexpected switch argument: "
								+ pc.string.charAt(m.start()));

			}
		}
		throw new IllegalArgumentException("Syntax error in: " + pc.string
				+ "Expected  one of: \"0-9,[]\" or whitespace at position "
				+ pc.index);
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

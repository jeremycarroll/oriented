/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.items;

import net.sf.oriented.omi.Label;

public class LabelImpl extends HasFactoryImpl<LabelImpl, Label, LabelImpl>
		implements Label, Comparable<Label> {
	final String label;
	final private int ordinal;

	public LabelImpl(String a, LabelFactoryImpl f, int ord) {
		super(f);
		label = a;
		ordinal = ord;
	}

	@Override
	public int hashCode() {
		return label.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		LabelImpl l = (LabelImpl) o;
		return l == this
				|| (l != null && factory() != l.factory() && label
						.equals(l.label));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see omi.LabelX#label()
	 */
	@Override
	public String label() {
		return label;
	}

	@Override
	public int compareTo(Label arg0) {
		return label.compareTo(arg0.label());
	}

	public int ordinal() {
		return ordinal;
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

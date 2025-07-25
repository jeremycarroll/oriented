/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.set;

import net.sf.oriented.omi.SetOfUnsignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class SetOfUnsignedSetFactory
		extends
		SetFactoryImpl<UnsignedSetInternal, SetOfUnsignedSetInternal, UnsignedSet, SetOfUnsignedSet, UnsignedSetInternal, SetOfUnsignedSetInternal>

{

	@Override
	public UnsignedSetFactory itemFactory() {
		return (UnsignedSetFactory) super.itemFactory();
	}

	public SetOfUnsignedSetFactory(UnsignedSetFactory f) {
		super(f);
	}

	// public SetOfUnsignedSetInternal
	// fromBackingCollection(JavaSet<UnsignedSetInternal> c) {
	// return construct(c,this);
	// }

	// static public SetOfUnsignedSet parse(String s) {
	// return new SetOfUnsignedSet(parsex(s, false));
	// }
	//
	// static Collection<UnsignedSet> parsex(String a, boolean symmetric) {
	// int lp = a.indexOf('{');
	// if (lp != 0)
	// throw new IllegalArgumentException(
	// "set to parse() must start with '{'");
	// String members[] = a.split("[{} ,]+");
	// SignedSet ss[] = new SignedSet[(symmetric ? 2 : 1)
	// * (members.length - 1)];
	// int j = 0;
	// for (int i = 1; i < members.length; i++) {
	// ss[j++] = SignedSet.parse(members[i]);
	// if (symmetric) {
	// ss[j] = ss[j - 1].opposite();
	// j++;
	// }
	// }
	// // TO DO: implement parsex
	// return null; //Arrays.asList(ss);
	// }

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

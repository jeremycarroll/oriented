/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Arrays;

import net.sf.oriented.combinatorics.CombinatoricUtils;

/**
 * 
 * @author jeremy
 * 
 */
public class Alternating implements FullChirotope {

    final private Chirotope base;

    public Alternating(Chirotope base) {
	this.base = base;
    }

    @Override
    public int chi(int... x) {
	if (base instanceof FullChirotope) return base.chi(x);

	int y[] = x.clone();
	Arrays.sort(y);
	int sign = CombinatoricUtils.sign(x, y);
	return sign == 0 ? 0 : sign * base.chi(y);
    }

    @Override
    public int rank() {
	return base.rank();
    }

    @Override
    public int n() {
	return base.n();
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

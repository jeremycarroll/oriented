/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
/**
 * Copyright 2007, Jeremy J. Carroll
 */
package net.sf.oriented.omi.impl.om;

import java.util.regex.Pattern;

import com.google.common.math.IntMath;


class RankAndChirotope {
    private static Pattern chi = Pattern.compile("^[+0-]*$");
	String chirotope;
	int rank;
    public void checkSize(int n) {
        if ( IntMath.binomial(n, rank) != chirotope.length() ) {
            throw new IllegalArgumentException("Chirotope of wrong length: "
                       + chirotope.length() + " != " + n + "C" + rank );
        }
        if (! chi.matcher(chirotope).matches() ) {
            throw new IllegalArgumentException("Chirotope must be only +, -, 0");
        }
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

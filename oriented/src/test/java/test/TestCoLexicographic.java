/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import net.sf.oriented.util.combinatorics.CoLexicographic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized.TestName;

import com.google.common.math.IntMath;

@RunWith(value = BetterParameterized.class)
public class TestCoLexicographic {
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { { "10-4", 10, 4 },
				{ "3-2", 3, 2 }, { "9-6", 9, 6 } });

	}

	final CoLexicographic lex;
	final int n, r;

	public TestCoLexicographic(String nm, int n, int r) {
		lex = new CoLexicographic(n, r);
		this.n = n;
		this.r = r;
	}

	@TestName
	static public String name(String nm, int n, int r) {
		return nm;
	}

	@Test
	public void check() {
		int sz = IntMath.binomial(n, r);
		Iterator<int[]> it = lex.iterator();
		for (int i = 0; i < sz; i++) {
			assertTrue(it.hasNext());
			int[] seq = it.next();
			assertEquals(i, CoLexicographic.index(seq));
		}
		assertFalse(it.hasNext());
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

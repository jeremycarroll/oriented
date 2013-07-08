/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package test;

import junit.framework.Assert;
import net.sf.oriented.matrix.RationalMatrix;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.Options;

import org.junit.Test;

public class TestRealization {
	static FactoryFactory f;
	static {
		Options options = new Options();
		options.setShortLabels();
		f = new FactoryFactory(options);
	}

	static int chap1[][] = { { 1, 1, 1, 0, 0, 0 }, { 0, 1, 1, 1, 1, 0 },
			{ 0, 0, 1, 0, 1, 1 } };

	static OM testDatum() {
		RationalMatrix matrix = new RationalMatrix(chap1);
		return f.realized().construct(matrix);
	}

	@Test
	public void testToString() {
	    Assert.assertTrue(
	            testDatum()
	               .toString()
	               .contains("[ [ 1  0  0 ] [ 1  1  0 ] [ 1  1  1 ] [ 0  1  0 ] [ 0  1  1 ] [ 0  0  1 ] ]"));
	}

	@Test
	public void testToChirotope() {
        Assert.assertTrue(
                testDatum().getChirotope().toString().contains("+0++-0++++--0++++++0"));
	}

	@Test
	public void testEquals() {
	    Assert.assertTrue(testDatum().equals(Examples.chapter1));
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

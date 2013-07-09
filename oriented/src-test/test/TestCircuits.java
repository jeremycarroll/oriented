/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static net.sf.oriented.omi.Examples.chapter1;
import junit.framework.Assert;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMS;
import net.sf.oriented.omi.Options;
import net.sf.oriented.omi.UnsignedSet;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestCircuits {

	static FactoryFactory f;
	static {
		Options options = new Options();
		options.setShortLabels();
		f = new FactoryFactory(options);
	}
	static OMS tooLittle, tooMuch;
	static UnsignedSet e;

	@BeforeClass
	public static void setUpBeforeClass() {
		tooLittle = f.circuits().parse("{12'4,13'5,45'6,12'56',13'46,23'4'5}");
		tooMuch = f.circuits().parse(
				"{12'4,13'5,23'6,45'6,12'56',13'46,23'4'5,12'345'}");
		// e = new UnsignedSet(Arrays.asList(new
		// String[]{"1","2","3","4","5","6"}),true);
	}

	@Test
	public void testVerify() {
		assertTrue("chapter1", chapter1.verify());
		assertFalse("tooLittle", tooLittle.verify());
		assertFalse("tooMuch", tooMuch.verify());
	}
	
	@Test
	public void testToString() {
        testToString(chapter1);
        testToString(chapter1.dual());
	    
	}

    private void testToString(OM om) {
        testStringFormat( om.getCircuits().toString() );
        testStringFormat( om.getVectors().toString() );
        testStringFormat( om.getMaxVectors().toString() );
        testStringFormat( om.getChirotope().toString() );
    }
	
	/**
	 * Oriented Matroids and Matroids are systematically
	 * represented in this library with a string format starting with "( "
	 * and ending with " )".
	 * @param m
	 */
	public static void testStringFormat(String m) {
	    //System.err.println(m);
	    Assert.assertTrue(m.startsWith("( "));
	    Assert.assertTrue(m.endsWith(" )"));
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

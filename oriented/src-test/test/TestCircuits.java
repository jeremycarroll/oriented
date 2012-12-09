/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.sf.oriented.omi.FactoryFactory;
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
    static OMS chapter1, tooLittle, tooMuch;
    static UnsignedSet e;

    @BeforeClass
    public static void setUpBeforeClass() {
	chapter1 = f.circuits().parse(
		"{12'4,13'5,23'6,45'6,12'56',13'46,23'4'5}");
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
	System.out.println("Circuits: " + chapter1.toString());
	// System.err.println(chapter1.toString());
    }
    //
    // @Test
    // public void testVerifyWeakElimination() {
    // assertFalse("tooLittle",tooLittle.verifyWeakElimination());
    // assertTrue("tooMuch",tooMuch.verifyWeakElimination());
    // assertTrue("chapter1",chapter1.verifyWeakElimination());
    // }
    //
    // @Test
    // public void testVerifyIncomparability() {
    // assertTrue(chapter1.verifyIncomparability());
    // assertTrue(tooLittle.verifyIncomparability());
    // assertFalse(tooMuch.verifyIncomparability());
    // }

    // @Test
    // public void testToStringSetOfOfString() {
    // System.out.println("Chapter1: " + chapter1.toString(e));
    // System.out.println("Toomuch: " + tooMuch.toString(e));
    // System.out.println("Too Little: " + tooLittle.toString(e));
    // assertEquals("chapter1",chapter1,Circuits.parse(chapter1.toString(e)));
    // assertEquals("too much",tooMuch,Circuits.parse(tooMuch.toString(e)));
    // assertEquals("too little",tooLittle,Circuits.parse(tooLittle.toString(e)));
    // }
    //
    // @Test
    // public void testToPlusMinus() {
    // System.out.println("Chapter1: " + chapter1.toPlusMinus(e));
    // System.out.println("Toomuch: " + tooMuch.toPlusMinus(e));
    // System.out.println("Too Little: " + tooLittle.toPlusMinus(e));
    //
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

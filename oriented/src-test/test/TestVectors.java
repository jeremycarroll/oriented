/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OMS;
import net.sf.oriented.omi.Options;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestVectors {

	static FactoryFactory f;
	static {
		Options options = new Options();
		options.setShortLabels();
		f = new FactoryFactory(options);
	}
	static OMS chapter1;
	static OMS vectors;

	// static UnsignedSet e;
	@BeforeClass
	public static void setUpBeforeClass() {
		chapter1 = f.circuits().parse(
				"{12'4,13'5,23'6,45'6,12'56',13'46,23'4'5}");
		// e = new UnsignedSet(Arrays.asList(new
		// String[]{"1","2","3","4","5","6"}),true);
		vectors = chapter1.getVectors();
	}

	@Test
	public void testVerify() {
		assertTrue("chapter1", vectors.verify());
	}

	@Test
	public void testToStringSetOfOfString() {
		System.out.println("Vectors: " + vectors.toString());
		assertEquals("chapter1", vectors, f.vectors().parse(vectors.toString()));
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

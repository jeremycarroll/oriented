/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static net.sf.oriented.omi.Examples.chapter1;
import static org.junit.Assert.assertEquals;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OMS;
import net.sf.oriented.omi.Options;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestMaxVectors {

	static FactoryFactory f;
	static {
		Options options = new Options();
		options.setShortLabels();
		f = new FactoryFactory(options);
	}
	static OMS vectors;

	// static UnsignedSet e;
	@BeforeClass
	public static void setUpBeforeClass() {
		vectors = chapter1.getMaxVectors();
	}

	@Test
	public void testVerify() throws AxiomViolation {
		vectors.verify();
	}

	@Test
	public void testToStringSetOfOfString() {
		assertEquals("chapter1", vectors,
				f.maxVectors().parse(vectors.toString()));
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

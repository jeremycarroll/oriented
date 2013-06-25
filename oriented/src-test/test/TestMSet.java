/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;

import org.junit.Test;

public class TestMSet {

	// @BeforeClass
	// public static void setUpBeforeClass() throws Exception {
	// f = new FactoryFactory();
	// }
	static FactoryFactory f = new FactoryFactory();

	@Test
	public void testUnion() {
		assertEquals(unsigned("{a,b,c}").union(unsigned("{d,b,c}")),
				unsigned("{a,d,b,c}"));
	}

	@Test
	public void testIntersection() {
		assertEquals(unsigned("{a,b,c}").intersection(unsigned("{d,f,g}")),
				empty());

		assertEquals(unsigned("{a,b,c}").intersection(unsigned("{d,f,a,g}")),
				unsigned("{a}"));
	}

	@Test
	public void testMinus() {
		assertEquals(unsigned("{a,b,c,d}").minus(unsigned("{b,c}")),
				unsigned("{a,d}"));
	}

	@Test
	public void testParse() {
		int result = 0;
		int count = 0;
		Iterator<? extends Label> it = unsigned("{4,1,2,4}").iterator();
		while (it.hasNext()) {
			result |= Integer.parseInt(it.next().toString());
			count++;
		}
		assertEquals(result, 7);
		assertEquals(count, 3);
	}

	@Test
	public void testContains() {
		assertTrue(unsigned("{a,b,c,d}").contains(f.labels().parse("b")));
	}

	@Test
	public void testHashCode() {
		assertEquals(unsigned("{a}").hashCode(), unsigned("{a}").hashCode());
	}

	@Test
	public void testEquals() {
		assertEquals("singleton", unsigned("{a}"), unsigned("{a}"));
		assertEquals("empty", unsigned("{}"), empty());
	}

	private UnsignedSet empty() {
		return f.unsignedSets().empty();
	}

	@Test
	public void testSubsetsOfSize() {
		assertEquals("1 empty", 1, unsigned("{a,b,c,d}").subsetsOfSize(0)
				.size());
		assertEquals("1 elements", 1, unsigned("{a}").subsetsOfSize(1).size());
		assertEquals("4 elements", 4, unsigned("{a,b,c,d}").subsetsOfSize(1)
				.size());
		assertEquals("6 pairs", 6, unsigned("{a,b,c,d}").subsetsOfSize(2)
				.size());
		assertEquals("4 triples", 4, unsigned("{a,b,c,d}").subsetsOfSize(3)
				.size());

	}

	private UnsignedSet unsigned(String string) {
		return f.unsignedSets().parse(string);
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

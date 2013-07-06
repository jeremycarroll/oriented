/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static net.sf.oriented.omi.Examples.chapter1;
import org.junit.Test;

public class TestChirotope {

	@Test
	public void testX() {
		net.sf.oriented.omi.OMChirotope m = chapter1.getChirotope();
		System.out.println("Chirotope: " + m.toString());
		System.out
				.println("CoChirotope: " + m.dual().getChirotope().toString());
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

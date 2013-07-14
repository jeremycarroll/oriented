/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static net.sf.oriented.omi.Examples.chapter1;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Matroid;
import net.sf.oriented.omi.Options;

import org.junit.Test;

public class TestMatroid {

	static FactoryFactory f;
	static {
		Options options = new Options();
		options.setShortLabels();
		f = new FactoryFactory(options);
	}

	private void testMatroid(Matroid m) {
	    TestCircuits.testStringFormat(m.getBases().toString());
        TestCircuits.testStringFormat(m.getCircuits().toString());
	}

	@Test
	public void testM() {
		Matroid m = chapter1.getMatroid();
		testMatroid(m);
		testMatroid(m.dual());
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

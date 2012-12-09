/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
************************************************************************/
package test;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Matroid;
import net.sf.oriented.omi.OMS;
import net.sf.oriented.omi.Options;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestMatroid {

	static FactoryFactory f;
	static { Options options = new Options();
	options.setShortLabels();
	f = new FactoryFactory(options);
	}
	static OMS chapter1;
	@BeforeClass
	public static void setUpBeforeClass()  {
		chapter1 = f.circuits().parse("{12'4,13'5,23'6,45'6,12'56',13'46,23'4'5}");
	}


	@Test
	public void testM() {
		System.out.println("Circuits: " + chapter1.getCircuits().toString());
		Matroid m = chapter1.getMatroid();
		System.out.println("CoCircuits: " + m.dual().getCircuits().toString());

		System.out.println("Bases: " + m.getBases().toString());
		System.out.println("CoBases: " + m.dual().getBases().toString());
	}

}
/************************************************************************
    This file is part of the Java Oriented Matroid Library.

     
     
     
    

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/

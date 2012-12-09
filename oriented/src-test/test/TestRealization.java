/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package test;

import org.junit.Test;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.Options;
import net.sf.oriented.omi.matrix.RationalMatrix;

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
	System.err.println(testDatum().toString());
    }

    @Test
    public void testToChirotope() {
	System.err.println(testDatum().getChirotope().toString());
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

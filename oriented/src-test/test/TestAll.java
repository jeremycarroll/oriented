/************************************************************************
  (c) Copyright 2010 Jeremy J. Carroll
  
 ************************************************************************/

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({ TestAlternating.class, TestChirotope.class, TestCircuits.class,
		TestConversions.class, TestLexicographic.class, TestMatroid.class,
		TestMaxVectors.class, TestMSet.class, TestSignedSet.class,
		TestVectors.class, TestLU.class, TestGramSchmidt.class,
		TestDualRealized.class

})
@RunWith(Suite.class)
public class TestAll {

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

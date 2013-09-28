/************************************************************************
  (c) Copyright 2010 Jeremy J. Carroll
  
 ************************************************************************/

package test;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Verify;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@SuiteClasses({ 
    TestAlternating.class, 
    TestCircuits.class,    
    TestConversions.class, 
    TestCoLexicographic.class, 
    TestGetRuntimeClass.class,
    TestLexicographic.class, 
    TestLexicographic2.class, 
    TestMatroid.class,
    TestMaxVectors.class, 
    TestMSet.class, 
    TestSignedSet.class,
    TestVectors.class, 
    TestGramSchmidt.class,
    TestDualRealizedBits32.class, 
    TestDualRealizedHash.class, 
    TestExamples.class,
    TestExamples2.class,
    TestPermutations.class,
    TestRealization.class,
    TestGrassmannPlucker.class,
    TestDrawing.class,
    TestPlusMinusPlus.class,
})
@RunWith(Suite.class)
public class TestAll {
    public static boolean verify(Verify v) {
        try {
            v.verify();
            return true;
        }
        catch (AxiomViolation e) {
            return false;
        }
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

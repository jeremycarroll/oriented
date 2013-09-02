/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized.TestName;

import com.google.common.math.IntMath;

@RunWith(BetterParameterized.class)
public class TestExamples2 {
    
    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> rslt = new ArrayList<Object[]>();
        for (Map.Entry<String,OM> entry:Examples.all().entrySet() ) {
            rslt.add(new Object[]{entry.getKey(),entry.getValue()});
        }
        return rslt;
    }

    @TestName
    public static String name(String nm, OM om) {
        return nm;
    }

    private final OM om;
    private final String name;

    public TestExamples2(String nm, OM om) {
        this.om = om;
        name = nm;
    }

    @Test
    public void testAcyclic() {
        Assume.assumeTrue(!name.contains("suvorov14"));  // this one is cyclic!
        Assert.assertTrue(name,om.isAcyclic());
    }
    

    @Test
    public void testVerify() throws AxiomViolation {
          om.verify();
    }
    
    @Test
    public void testIndependentSets() throws AxiomViolation {
        om.getMatroid().getIndependentSets().verify();
    }

    @Test
    public void testDualIndependentSets() throws AxiomViolation {
        Assume.assumeTrue(om.dual().rank()<8);  // otherwise it takes a long time! (well about a minute for tsukamoto
        om.getMatroid().dual().getIndependentSets().verify();
    }
    @Test
    public void testFaceLattice() throws AxiomViolation {
        testFaceLattice(om);
    }

    public void testFaceLattice(OM om2) throws AxiomViolation {
        om2.getFaceLattice().verify();
    }

    @Test
    public void testDualFaceLattice() throws AxiomViolation {
        if (om.dual().rank()<10) {
            testFaceLattice(om.dual());
        }
    }
    
    @Test
    public void testPseudolines() {
        int n = om.n();
        int p = IntMath.binomial(n-1,2);
        for (Label l:om.elements()) {
            EuclideanPseudoLines r = new EuclideanPseudoLines(om,l);
            if ( n < 10 ) {
                // this is entirely optional, but with it things go slower!
               r.switchFaceLattice();
            }
            Assert.assertEquals(om, r.getEquivalentOM().reorient(r.getReorientation()));
            String lex = r.getEquivalentOM().getChirotope().toLexicographicString().substring(0,p);
            Assert.assertTrue(r.getEquivalentOM().isAcyclic());
            Assert.assertEquals(-1, lex.indexOf('-') );
            Assert.assertTrue(r.getReorientation().length<n/2);
            Assert.assertEquals(FactoryFactory.fromCrossings(r.toCrossingsString()),r.getEquivalentOM());
        }
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

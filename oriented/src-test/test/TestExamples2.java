/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import net.sf.oriented.impl.util.Misc;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.PseudoLines;

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
        Class<?> ex = Examples.class;
        for (Method m:ex.getMethods()) {
//            if (m.getName().contains("suv"))
             if (Modifier.isStatic(m.getModifiers())
                      && OM.class.isAssignableFrom( m.getReturnType() )  
                        ) {
                 switch (m.getParameterTypes().length) {
                 case 0:
                     rslt.add(new Object[]{m.getName(),Misc.invoke(m, null)});
                     break;
                 case 1:
                     rslt.add(new Object[]{m.getName()+"[+1]",Misc.invoke(m, null,1)});
                     rslt.add(new Object[]{m.getName()+"[0]",Misc.invoke(m, null,0)});
                     rslt.add(new Object[]{m.getName()+"[-1]",Misc.invoke(m, null,-1)});
                     break;
                  default:
                         throw new IllegalStateException("Problem with method: "+m.getName());
                 }
             }
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
    public void testRealizationA() {
        int n = om.n();
        int p = IntMath.binomial(n-1,2);
//        Assume.assumeTrue(om.n()<11);
//        System.err.print(name+": ");
//        long start = System.currentTimeMillis();
        for (Label l:om.elements()) {
//            System.err.println(l.label() + " " +(System.currentTimeMillis() - start));
            PseudoLines r = new PseudoLines(om,l);
            Assert.assertEquals(om, r.getEquivalentOM().reorient(r.getReorientation()));
            String lex = r.getEquivalentOM().getChirotope().toLexicographicString().substring(0,p);
//            System.err.print(l.label()+"="+r.getReorientation().length
//                       //+" "+lex
//                           +", ");
            Assert.assertTrue(r.getEquivalentOM().isAcyclic());
            Assert.assertEquals(-1, lex.indexOf('-') );
            Assert.assertTrue(r.getReorientation().length<n/2);
            Assert.assertEquals(FactoryFactory.fromCrossings(r.toCrossingsString()),r.getEquivalentOM());
            
        }

//        System.err.println( +(System.currentTimeMillis() - start));
//        System.err.println();
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import net.sf.oriented.impl.util.Misc;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.Matroid;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.PseudoLines;
import net.sf.oriented.util.combinatorics.CoLexicographic;

import org.junit.Assume;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized.TestName;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
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
    public void testIndependentSets() throws AxiomViolation {
        om.getMatroid().getIndependentSets().verify();
    }

    @Test
    public void testDualIndependentSets() throws AxiomViolation {
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
    
    @Ignore
    @Test
    public void testMathsOverflow129698() {
        if (om.rank() * 2 < om.n()) {
            final OM dual = om.dual();
            final Matroid dualMatroid = dual.getMatroid();
            for (int args[] : new CoLexicographic(dual.n(), dual.rank() ) ) {
                if (dual.getChirotope().chi(args) != 0 ) { 
                    // linearly independent
                    UnsignedSet basis = dual.ffactory().unsignedSets()
                            .copyBackingCollection(
                            Lists.transform(Arrays.asList(Misc.box(args)),
                                    new Function<Integer,Label>(){
                                @Override
                                public Label apply(Integer i) {
                                    return dual.elements()[i];
                                }
                            }));
                    UnsignedSet other = dual.setOfElements().minus(basis);
                    boolean independent = dualMatroid.getIndependentSets().contains(other);
                }
            }
        }
    }
    
    @Test
    public void testPseudolines() {
        int n = om.n();
        int p = IntMath.binomial(n-1,2);
        for (Label l:om.elements()) {
            PseudoLines r = new PseudoLines(om,l);
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

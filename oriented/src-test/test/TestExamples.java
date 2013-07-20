/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.OM;

import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class TestExamples {
    
    @Test
    public void testVerify() throws AxiomViolation {
        for (Pair<OM,String> om: testOrientedMatroids()) {
            om.getKey().verify();
        }
    }
    private Iterable<Pair<OM,String>> testOrientedMatroids() {
        Class<?> ex = Examples.class;
        List<Pair<OM,String>> rslt = new ArrayList<Pair<OM,String>>();
        for (Field f:ex.getFields()) {
            if (Modifier.isStatic(f.getModifiers())
                  && OM.class.isAssignableFrom( f.getType() )  
                    ) {
                try {
                    rslt.add(new Pair<OM,String>((OM)f.get(null),f.getName()));
                }
                catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
                catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return rslt;
    }
    @Test
    public void testAcyclic() {
        for (Pair<OM,String> om: testOrientedMatroids()) {
            Assert.assertTrue(om.getValue(),om.getKey().isAcyclic());
            
        }
    }

    @Test
    public void testUniform4() {
        OM fromEuclideanLines = Examples.fromEuclideanLines(
                new int[][][]{
                        {{2,1},{1,1},},
                        {{ 2, 1 },{1,2}},
                        {{1,1},{1,2}},
                });
        /*
         * [0]=(1,1,0)
         * [1]=(0,10000,10000)        θ = 0
         * [2]=(7071,7071,21213)      θ = 45
         * [3]=(10000,0,10000)        θ = 90
         */
        Assert.assertEquals(Examples.uniform4, 
                fromEuclideanLines);
    }
    
    @Test
    public void testCeva() {
        // There are four poitns of intersection in Ceva's construction.
       Assert.assertTrue( Examples.ceva.getChirotope().toShortString().matches("^.*0.*0.*0.*0.*$") );
    }
    @Test
    public void testFromCrossing() {
        OM fromEuclideanCrossings = Examples.fromCrossings(
                  "A:BCD",
                  "B:ACD",
                  "C:ABD",
                  "D:ABC" );
        /*
         * [0]=(1,1,0)
         * [1]=(0,10000,10000)        θ = 0
         * [2]=(7071,7071,21213)      θ = 45
         * [3]=(10000,0,10000)        θ = 90
         */
        Assert.assertEquals(Examples.uniform4.dual().getMaxVectors(), fromEuclideanCrossings.dual().getMaxVectors());
    }
//    @Test
//    public void testFromEuclideanLines() {
//        Assert.assertEquals(Examples.circularSaw3, Examples.circularSaw3A);
//    }
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

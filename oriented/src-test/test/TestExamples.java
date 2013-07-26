/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;


import junit.framework.Assert;
import net.sf.oriented.impl.om.ExamplesHelper;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;

import org.junit.Test;

public class TestExamples {
  

    @Test
    public void testUniform4() {
        OM fromEuclideanLines = FactoryFactory.fromEuclideanLines(
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
        Assert.assertEquals(Examples.uniform4(), 
                fromEuclideanLines);
    }
    
    @Test
    public void testΠάππος() {
        // There are nine points of intersection in Pappus's construction.
        Assert.assertTrue( Examples.πάππος().getChirotope().toShortString().matches("^.*0.*0.*0.*0.*0.*0.*0.*0.*0.*$") );
        
    }
    @Test
    public void testCeva() {
        // There are four points of intersection in Ceva's construction.
       Assert.assertTrue( Examples.ceva().getChirotope().toShortString().matches("^.*0.*0.*0.*0.*$") );
    }
    
   
    @Test
    public void testFromCrossing() {
        OM fromEuclideanCrossings = FactoryFactory.fromCrossings(
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
        Assert.assertEquals(Examples.uniform4().dual().getMaxVectors(), fromEuclideanCrossings.dual().getMaxVectors());
    }
    
    @Test
    public void testSuv14() {
        Assert.assertEquals(Examples.suvorov14(), ExamplesHelper.suv14(1.780776,5.186363));
        Assert.assertEquals(0, Examples.suvorov14().getChirotope().chi(0,12,13));
        Assert.assertEquals(0, Examples.suvorov14().getChirotope().chi(5,9,10));
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

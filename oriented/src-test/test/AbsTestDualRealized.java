/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import junit.framework.Assert;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.impl.om.OMAll;

import org.junit.Test;

public abstract class AbsTestDualRealized {

    @Test
    public void testBasic() {
    	Assert.assertNotNull(getAll().getRealized());
    	Assert.assertNotNull(getAll().getCircuits());
    	Assert.assertNotNull(getAll().getMaxVectors());
    	Assert.assertNotNull(getAll().getChirotope());
    }

    
    abstract OMAll getAll();

    @Test
    public void testDual() {
    	Assert.assertNotNull(getAll().dual().getRealized());
    	Assert.assertNotNull(getAll().getRealized());
    	Assert.assertNotNull(getAll().dual().getRealized());
    	Assert.assertNotNull(getAll().dual().getCircuits());
    	Assert.assertNotNull(getAll().dual().getMaxVectors());
    }

    @Test
    public void testCoCircuits() {
    	Assert.assertNotNull(getAll().dual().getCircuits());
    }

    @Test
    public void testDualCoCircuits() {
    	Assert.assertNotNull(getAll().dual().getRealized());
    	Assert.assertNotNull(getAll().dual().getCircuits());
    }

    @Test
    public void testDualDualCoCircuits() {
    		OM dual = getAll().ffactory().realized()
    		              .parse(getAll().dual().getRealized().toString());
    //		System.err.println(dual.dual().getRealized().toString());
    //		System.err.println(dual.dual().getCircuits().toString());
    		Assert.assertEquals(getAll().getCircuits(), 
    		        dual.dual().getCircuits());
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

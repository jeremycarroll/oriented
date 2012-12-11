/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
************************************************************************/

package test;

import junit.framework.Assert;

import org.junit.Test;

import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.impl.om.OMAll;
import net.sf.oriented.omi.impl.om.OMInternal;

public class TestDualRealized {
    private final OMAll all = TestConversions.asAll(TestRealization.testDatum());
    
    @Test
    public void testBasic() {
	Assert.assertNotNull(all.getRealized());
	Assert.assertNotNull(all.getCircuits());
	Assert.assertNotNull(all.getMaxVectors());
	Assert.assertNotNull(all.getChirotope());
    }
    @Test
    public void testDual() {
	Assert.assertNotNull(all.dual().getRealized());
	Assert.assertNotNull(all.getRealized());
	Assert.assertNotNull(all.dual().getRealized());
	Assert.assertNotNull(all.dual().getCircuits());
	Assert.assertNotNull(all.dual().getMaxVectors());
    }
    @Test
    public void testCoCircuits() {
	Assert.assertNotNull(all.dual().getCircuits());
    }
    @Test
    public void testDualCoCircuits() {
	Assert.assertNotNull(all.dual().getRealized());
	Assert.assertNotNull(all.dual().getCircuits());
    }
    @Test
    public void testDualDualCoCircuits() {
	OM dual = all.ffactory().realized().parse( all.dual().getRealized().toString() );
	System.err.println(dual.dual().getRealized().toString());
	System.err.println(dual.dual().getCircuits().toString());
	Assert.assertEquals(all.getCircuits().toString(), dual.dual().getCircuits().toString() );
    }
}


/************************************************************************
    This file is part of the Java Oriented Matroid Library.

    The Java Oriented Matroid Library is free software: you can 
     
     
    

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/
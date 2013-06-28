/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.OM;

import org.apache.commons.math3.util.Pair;
import org.junit.Test;

public class TestExamples {
    
    @Test
    public void testVerify() {
        for (Pair<OM,String> om: testOrientedMatroids()) {
            Assert.assertTrue(om.getValue(),om.getKey().verify());
            System.err.println(om.getValue()+" "+om.getKey().getChirotope().toString());
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

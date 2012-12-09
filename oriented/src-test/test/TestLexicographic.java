/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
************************************************************************/
package test;

import static net.sf.oriented.combinatorics.CombinatoricUtils.choose;
import static net.sf.oriented.omi.impl.om.ChirotopeImpl.pos;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import net.sf.oriented.combinatorics.Lexicographic;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized.TestName;


@RunWith(value=BetterParameterized.class)
public class TestLexicographic {
    @Parameters
    public static Collection<Object[]> data() {
    	return Arrays.asList(
    			new Object[][]{
    					{"10-4", 10, 4},
    					{"3-2", 3, 2},
    					{"9-6", 9, 6}
    			}
    			);
    	
    }
    final Lexicographic lex;
    final int n, r;
    public TestLexicographic(String nm, int n, int r) {
    	lex = new Lexicographic(n,r);
    	this.n = n;
    	this.r = r;
    }
    
    @TestName
    static public String name(String nm, int n, int r) {
    	return nm;
    }
    @Test
    public void check() {
    	int sz = choose(n,r);
    	Iterator<int[]> it = lex.iterator();
    	for (int i=0;i<sz;i++) {
        	assertTrue(it.hasNext());
    		int[] seq = it.next();
//    		for (int j=0;j<r;j++)
//    			System.err.print(seq[j]+",");
//    		System.err.println();
			assertEquals(i,pos(n,r,0,seq));
    	}
    	assertFalse(it.hasNext());
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import net.sf.oriented.omi.Examples;
import net.sf.oriented.util.combinatorics.CoLexicographic;
import net.sf.oriented.util.combinatorics.Lexicographic;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.math.IntMath;

public class TestLexicographic2 {
    
    @Test
    public void testLex2CoLex2Lex() {
        String data = "@The Java Oriented Matroid Library is distributed in the hope that it!";
        Assert.assertEquals(IntMath.binomial(8, 4),data.length());
        Assert.assertEquals(data,Lexicographic.fromCoLexicographic(8, 4, CoLexicographic.fromLexicographic(8, 4, data)));
    }
    

    @Test
    public void testCoLex2Lex2CoLex() {
        String data = "@The Java Oriented Matroid Library is distributed in the hope that it!";
        Assert.assertEquals(IntMath.binomial(8, 4),data.length());
        Assert.assertEquals(data,CoLexicographic.fromLexicographic(8, 4, Lexicographic.fromCoLexicographic(8, 4, data)));
    }
    
    @Test
    public void testAngleOrder() {
        Assert.assertTrue(Examples.ringel().getChirotope().toLexicographicString().substring(0,28).matches("^[+]*$"));
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

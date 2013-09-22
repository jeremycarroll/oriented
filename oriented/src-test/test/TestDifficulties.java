/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.TensionGraph;
import net.sf.oriented.pseudoline.WAM;

import org.junit.Ignore;
import org.junit.Test;


public class TestDifficulties {
// circularSaw3.getChirotope().mutate(1,1,2,3); - a circ saw that isn't

    @Test
    public void testCeva() {
        count("ceva","0",10,4,2,10000);
    }

    @Test
    public void testSaw() {
        count("circularsaw3","0",10,4,1,1000);
    }


    @Test
    public void testCeva2() {
        count("ceva","0",10,4,2,10000);
    }

    @Test
    public void testSaw2() {
        count("circularsaw3","0",10,4,1,1000);
    }

    @Test
    public void testCeva3() {
        count("ceva","0",10,4,2,10000);
    }

    @Test
    public void testSaw3() {
        count("circularsaw3","0",10,4,1,1000);
    }
    @Test
    public void testCeva2a() {
        count("ceva","0",10,4,2,10000);
    }

    @Test
    public void testSaw2a() {
        count("circularsaw3","0",10,4,1,1000);
    }

    @Test
    public void testCeva3a() {
        count("ceva","0",10,4,2,10000);
    }

    @Test
    public void testSaw3a() {
        count("circularsaw3","0",10,4,1,1000);
    }

    @Ignore
    @Test
    public void testRingel() {
        count("ringel","0",21,4,-6);
    }
    @Ignore
    @Test
    public void testCevaA() {
        count("ceva","A",8,0,0);
    }

    @Ignore
    @Test
    public void testSawA() {
        count("circularsaw3","A",10,0,0);
    }
    @Ignore
    @Test
    public void testChap1() {
        count("chapter1","1",4,0,0);
    }

    @Test
    public void testSawSimplified() {
        System.err.println("=== simplified ===");
        TensionGraph ten = circSawResult();
        ten.dumpEdges();
        ten.dumpVertices();
        WAM wam = new WAM(ten);
//        wam.setDebugExpected(circSawResult());
        assertEquals(1, wam.search().size() );
//        Assert.assertEquals(prunedTensions,ten.getVertices().size());
        
        //Assert.assertEquals(expectedCount,pseudoLines.getDifficulties().size());
    }

    private TensionGraph circSawResult() {
        OM om = Examples.circularsaw3();
        EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om,"0");
        TensionGraph ten = pseudoLines.getTensions();
        Assert.assertEquals(10,ten.getVertices().size());
        List<Face> togo = new ArrayList<Face>();
        for (Face f: ten.getVertices()) {
            if (f.lower().size() == 4) {
                togo.add(f);
            }
        }
        for (Face f:togo) {
            ten.removeVertex(f);
        }
        Assert.assertEquals(4,ten.getVertices().size());
        ten.prune();
        Assert.assertEquals(4,ten.getVertices().size());
        return ten;
    }
    private void count(String omName, String inf, int tensions, int prunedTensions, int expectedDifficultCount) {
      this.count(omName, inf, tensions, prunedTensions, expectedDifficultCount, Integer.MAX_VALUE);
    }
    private void count(String omName, String inf, int tensions, int prunedTensions, int expectedDifficultCount, int maxTransitions) {
        System.err.println(omName+" ===== "+inf+" ==");
        OM om = Examples.all().get(omName);
        EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om,inf);
        TensionGraph ten = pseudoLines.getTensions();
        Assert.assertEquals(tensions,ten.getVertices().size());
        ten.prune();
//        if (prunedTensions != ten.getVertices().size()) {
//            ten.dumpEdges();
//            ten.dumpVertices();
//        }
        WAM wam = new WAM(ten);
//        wam.setDebugExpected(circSawResult());
        int actualDificultyCount = wam.search().size();
        System.err.println(wam.transitions+" wam transitions");
        assertEquals(expectedDifficultCount, actualDificultyCount );
        Assert.assertTrue("Too many transitions",wam.transitions<maxTransitions);
//        Assert.assertEquals(prunedTensions,ten.getVertices().size());
        //Assert.assertEquals(expectedCount,pseudoLines.getDifficulties().size());
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

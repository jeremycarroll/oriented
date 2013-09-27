/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import junit.framework.Assert;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline2.TGFactory;
import net.sf.oriented.pseudoline2.TGVertex;
import net.sf.oriented.pseudoline2.TensionGraph;

import org.junit.Ignore;
import org.junit.Test;


public class TestTwistedGraphs {
// circularSaw3.getChirotope().mutate(1,1,2,3); - a circ saw that isn't

    @Test
    public void testCeva() {
        count("ceva","0",14,24,8,12);
    }

    @Test
    public void testSaw() {
        count("circularsaw3","0",22,24,4,6);
    }
    
    @Ignore
    @Test
    public void testSawParts() {
        OM om = Examples.circularsaw3();
        EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om,"0");
        TensionGraph ten = new TGFactory(pseudoLines).create();
        
        int bad = 0; // 0
        int tri1_0 = 0; // 4
        int tri1_1 = 0; // 6
        int tri1_2 = 0; // 3
        int tri1_3 = 0; // 3
        int quad2_2 = 0; // 3
        int quad2_3 = 0; // 3
        for (TGVertex v:ten.getVertices()) {
            int tri = 0;
            int quad = 0;
//            for (Face f:v.getExtent()) {
//                switch (f.lower().size()) {
//                case 3:
//                    tri++;
//                    break;
//                case 4:
//                    quad++;
//                    break;
//                default:
//                     bad++;
//                }   
//            }
            switch (tri) {
            case 1:
                switch (quad) {
                case 0:
                    tri1_0++;
                    break;
                case 1:
                    tri1_1++;
                    break;
                case 2:
                    tri1_2++;
                    break;
                case 3:
                    tri1_3++;
                    break;
                default:
                        bad++;
                }
                break;
            case 2:
                switch (quad) {
                case 2:
                    quad2_2++;
                    break;
                case 3:
                    quad2_3++;
                    break;
                default:
                        bad++;
                }
                break;
            default:
                bad++;
            }
        }
        Assert.assertEquals(4,tri1_0);
        Assert.assertEquals(6,tri1_1);
        Assert.assertEquals(3,tri1_2);
        Assert.assertEquals(3,tri1_3);
        Assert.assertEquals(3,quad2_2);
        Assert.assertEquals(3,quad2_3);
        Assert.assertEquals(0,bad);
    }

    @Test
    public void testDeformedCeva() {
        count("deformedCeva","0",16,14,0,0);
    }

    @Test
    public void testDeformedSaw() {
        count("deformedCircularSaw","0",26,19,0,0);
    }

    @Test
    public void testRingel() {
        count("ringel","0",63,172,17,54);
    }
    @Test
    public void testChap1() {
        count("chapter1","1",6,2,0,0);
    }

    private void count(String omName, String inf, int vCount, int eCount, int vCount2, int eCount2) {
        System.err.println(omName+" ===== "+inf+" ==");
        OM om = Examples.all().get(omName);
        EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om,inf);
        TensionGraph ten = new TGFactory(pseudoLines).create();
        for (TGVertex v:ten.getVertices()) {
            System.err.println(v);
        }
        Assert.assertEquals(vCount,ten.getVertexCount());
        Assert.assertEquals(eCount,ten.getEdgeCount());
        ten.prune();
        Assert.assertEquals(vCount2,ten.getVertexCount());
        Assert.assertEquals(eCount2,ten.getEdgeCount());
//        if (prunedTensions != ten.getVertices().size()) {
//            ten.dumpEdges();
//            ten.dumpVertices();
//        }
//        WAM wam = new WAM(ten);
//        wam.setDebugExpected(circSawResult());
//        int actualDificultyCount = wam.search().size();
//        System.err.println(wam.transitions+" wam transitions");
//        assertEquals(expectedDifficultCount, actualDificultyCount );
//        Assert.assertTrue("Too many transitions",wam.transitions<maxTransitions);
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

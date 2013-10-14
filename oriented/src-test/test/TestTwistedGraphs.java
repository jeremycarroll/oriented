/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import junit.framework.Assert;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.CoLoopCannotBeDrawnException;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.ImageOptions;
import net.sf.oriented.pseudoline2.DEdge;
import net.sf.oriented.pseudoline2.DPath;
import net.sf.oriented.pseudoline2.DPaths;
import net.sf.oriented.pseudoline2.Difficulty;
import net.sf.oriented.pseudoline2.DifficultyDrawing;
import net.sf.oriented.pseudoline2.TGFactory;
import net.sf.oriented.pseudoline2.TensionGraph;
import net.sf.oriented.pseudoline2.WAM;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.uci.ics.jung.graph.Graph;


public class TestTwistedGraphs extends TestWithTempDir {
    

// circularSaw3.getChirotope().mutate(1,1,2,3); - a circ saw that isn't

    private String bad;
    @Test
    public void testCeva() {
        count("ceva","0",50,48,8,12,2);
    }

    @Ignore
    @Test
    public void testCircSaw5() {
        count("circularsaw5","0",96,880,76,640,287);
    }

    @Ignore
    @Test
    public void testCircSaw5A() {
        count("_saw5A","0",116,1500,106,1340,262);
    }

    @Ignore
    @Test
    public void testDeformCircSaw5() {
        count("_deformSaw5","0",-1,-1,-1,-1,263);
    }

    @Test
    public void testDisconnected() {
        count("_disconnected","0",-1,-1,-1,-1,-1);
    }

    @Ignore
    @Test
    public void testPappus() {
        count("pappus","0",-1,-1,-1,-1,-1);
    }

    @Ignore
    @Test
    public void testSaw() {
        count("circularsaw3","0",22,36,4,6,1);
    }
    
    

    @Ignore
    @Test
    public void testDeformedCeva() {
        count("_deformedCeva","0",42,28,0,0);
    }

    @Ignore
    @Test
    public void testDeformedSaw() {
        count("_deformedCircularSaw","0",26,29,0,0);
    }


    @Test
    public void testTsukamotoPlusA() {
        count("tsukamoto13.+1","A",312,6193,304,5957,10975);
    }

    @Test
    public void testTsukamotoMinusA() {
        count("tsukamoto13.-1","A", 308  , 6039  , 300  , 5791,17357);
    }

    @Test
    public void testTsukamotoPlusB() {
        count("tsukamoto13.+1","B",312,6193,304,5957,10975);
    }

    @Test
    public void testTsukamotoMinusB() {
        count("tsukamoto13.-1","B", 308  , 6039  , 300  , 5791,17357);
    }


    @Ignore
    @Test
    public void testSuvorov14() {
        count("suvorov14","A", 716  , 35463  , 675  , 31549 ,10975);
    }

    @Ignore
    @Test
    public void testOmage14Plus() {
        count("omega14.+1","A",312,6193,304,5957,10975);
    }

    @Ignore
    @Test
    public void testOmage14Minus() {
        count("omega14.-1","A",312,6193,304,5957,10975);
    }
    

    @Ignore
    @Test
    public void testRingel() {
        count("ringel","0",63,348,25,99,16);
    }

    @Ignore
    @Test
    public void testMany() {
        for (int i=0;i<2;i++) {
            testRingel();
//            testCircSaw5();
//            testCircSaw5A();
        }
    }

    @Ignore
    @Test
    public void testChap1() {
        count("chapter1","1",6,2,0,0);
    }

    static int soln = 0;
    private void count(String omName, String inf, int vCount, int eCount, int vCount2, int eCount2) {
        count(omName, inf, vCount, eCount, vCount2, eCount2, 0);
    }
    private void count(String omName, String inf, int vCount, int eCount, int vCount2, int eCount2, int dCount) {
        try {
            System.err.println("\n"+omName+" ===== "+inf+" ==");
            OM om = Examples.all().get(omName);
            EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om,inf);
            TensionGraph ten = new TGFactory(pseudoLines).create();
//        for (TGVertex v:ten.getVertices()) {
//            System.err.println(v);
//        }
            usuallyAssertEquals(vCount,ten.getVertexCount());
            usuallyAssertEquals(eCount,ten.getEdgeCount());
            ten.prune();
            usuallyAssertEquals(vCount2,ten.getVertexCount());
            usuallyAssertEquals(eCount2,ten.getEdgeCount());
            if (vCount2 != 0) {
                soln ++;
                ImageOptions options = ImageOptions.defaultBlackAndWhite();
                WAM wam = new WAM(ten);
                Difficulty[][] diff = wam.search();
                
                System.err.println(wam.transitions+" wam transitions");
                System.err.println(wam.foundDifficultyCount+" original difficulty count");
                System.err.println(diff[0].length+" difficulties");
                usuallyAssertEquals(dCount,diff[0].length);
                if (false)
                for (int i=0;i<diff[0].length;i++) {
                    Graph<Face, DEdge> rslt = diff[0][i].getSimplifiedRslt(ten);
                    Collection<DPath> cycles = new DPaths(rslt,pseudoLines).cycles();
                    
                    if (!searchForCyclePair(rslt, cycles)) {
                        continue;
                    }
                    
                    
                    System.err.println("Candidate found: "+i+": "+cycles.size()+" "+rslt.getEdgeCount());
                    for (Face f:rslt.getVertices()) {
                        if ( rslt.getNeighborCount(f) == 3 && f.dimension() == 0) {
                            System.err.println("!!!!");
                        }
                    }
                    
                    DifficultyDrawing euclid = new DifficultyDrawing(pseudoLines, ten, diff[0][i]);
                    ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
                    ImageOutputStream imageOutput = ImageIO.createImageOutputStream(new File(tmp+"/" + omName + "-" + inf+"-"+ 
                            soln + "-" + (i<10?"0":"")+i +".jpeg"));
                    iw.setOutput(imageOutput);
                    iw.write(euclid.image(options));
                    euclid.verify();
                    imageOutput.close();
                    iw.dispose();
                    
                }
            }
            if (bad != null) {
                Assert.fail(bad);
            }
        }
        catch (CoLoopCannotBeDrawnException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (AxiomViolation e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean searchForCyclePair(Graph<Face, DEdge> rslt,
            Collection<DPath> cycles) {
        int sz = rslt.getVertexCount();
        List<DPath> smallerCycles = new ArrayList<DPath>();
        for (DPath dp:cycles) {
            if (dp.getPath().size()<sz-1) {
                smallerCycles.add(dp);
            }
        }
        for (int i=0;i<smallerCycles.size();i++) {
            List<Face> iPath = smallerCycles.get(i).getPath();
            int isz = iPath.size() - 1;
            for (int j=i+1; j<smallerCycles.size(); j++) {
                List<Face> jPath = smallerCycles.get(j).getPath();
                int jsz = jPath.size() - 1;
                if (isz + jsz <= sz) {
                    if (!overlap(iPath,jPath)) {
                        return true;
                    }
                }
                
            }
        }
        return false;
    }

    private boolean overlap(List<Face> iPath, List<Face> jPath) {
        for (Face fi:iPath) {
            for (Face fj:jPath) {
                if (fi.equals(fj)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void usuallyAssertEquals(int expected, int actual) {
        if ( expected != -1 && expected != actual) {
            if (bad == null) {
                bad = "";
            }
            bad = bad + expected +" != " + actual + "; ";
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

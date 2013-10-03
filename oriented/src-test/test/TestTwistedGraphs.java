/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import junit.framework.Assert;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.CoLoopCannotBeDrawnException;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.ImageOptions;
import net.sf.oriented.pseudoline2.Difficulty;
import net.sf.oriented.pseudoline2.DifficultyDrawing;
import net.sf.oriented.pseudoline2.TGFactory;
import net.sf.oriented.pseudoline2.TensionGraph;
import net.sf.oriented.pseudoline2.WAM;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


public class TestTwistedGraphs {
    

    private static String tmp;
    private static boolean fixedDir = true;
    
    @BeforeClass
    public static void createTmpDir() throws IOException {
        File tFile = File.createTempFile("oriented", ".d");
        if (fixedDir) {
            // change this directory as appropriate
            tmp = "/Users/jeremycarroll/tmp";
        } else {
            tmp = tFile.getAbsolutePath();
            tFile.delete();
            tFile.mkdir();
        }
    }
    
    @AfterClass
    public static void deleteTmpDir() {
        if (!fixedDir) {
            File dir = new File(tmp);
            for (File f : dir.listFiles() ) {
                f.delete();
            }
            dir.delete();
        }
    }

// circularSaw3.getChirotope().mutate(1,1,2,3); - a circ saw that isn't

    @Test
    public void testCeva() {
        count("ceva","0",50,48,8,12,2);
    }

    @Test
    public void testCircSaw5() {
        count("circularsaw5","0",96,880,76,640,314);
    }

    @Test
    public void testCircSaw5A() {
        count("_saw5A","0",116,1500,106,1340,271);
    }

    @Test
    public void testDeformCircSaw5() {
        count("_deformSaw5","0",-1,-1,-1,-1,307);
    }


    @Test
    public void testPappus() {
        count("pappus","0",-1,-1,-1,-1,-1);
    }
    
    @Test
    public void testSaw() {
        count("circularsaw3","0",22,36,4,6,1);
    }
    
    

    @Test
    public void testDeformedCeva() {
        count("_deformedCeva","0",42,28,0,0);
    }

    @Test
    public void testDeformedSaw() {
        count("_deformedCircularSaw","0",26,29,0,0);
    }


    @Test
    public void testTsukamotoPlus() {
        count("tsukamoto13.+1","A",312,6193,304,5957,13527);
    }
    

    @Test
    public void testRingel() {
        count("ringel","0",63,348,25,99,-1);
    }

    @Ignore
    @Test
    public void testMany() {
        for (int i=0;i<2;i++) {
            testRingel();
            testCircSaw5();
            testCircSaw5A();
        }
    }

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
            System.err.println(omName+" ===== "+inf+" ==");
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
                List<Difficulty> diff = wam.search();
                
                System.err.println(wam.transitions+" wam transitions");
                System.err.println(wam.foundDifficultyCount+" original difficulty count");
                System.err.println(diff.size()+" difficulties");
                usuallyAssertEquals(dCount,diff.size());
                if (true)
                for (int i=0;i<diff.size();i++) {
                    if (i>40) {
                        break;
                    }
                    DifficultyDrawing euclid = new DifficultyDrawing(pseudoLines, ten, diff.get(i));
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

    private void usuallyAssertEquals(int expected, int actual) {
        if (expected != -1) {
            Assert.assertEquals(expected, actual);
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.CoLoopUnrepresentableException;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.ImageOptions;
import net.sf.oriented.pseudoline.PseudoLines;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author jeremycarroll
 *
 */
public class TestDrawing {
    
    private static String tmp;
    private static boolean fixedDir = false;
    
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

    @Test
    public void testWheel0() throws IOException, CoLoopUnrepresentableException {
        testDrawing(Examples.wheel12(),"0","wheel");
    }
    

    @Test(expected=CoLoopUnrepresentableException.class)
    public void testWheelA() throws IOException, CoLoopUnrepresentableException {
        testDrawing(Examples.wheel12(), "A", "wheel");
    }

    @Test
    public void testPappus() throws IOException, CoLoopUnrepresentableException {
        testDrawing(Examples.πάππος(), "pappus");
    }
    @Test
    public void testCeva() throws IOException, CoLoopUnrepresentableException {
        testDrawing(Examples.ceva(), "ceva");
    }
    
    @Test
    public void testRingel() throws IOException, CoLoopUnrepresentableException {
        testDrawing(Examples.ringel(),"3", "ringel");
    }

    @Test
    public void testTsukamoto13_1() throws IOException, CoLoopUnrepresentableException {
        testDrawing(Examples.tsukamoto13(1),  "K", "tsukamoto(1)");
    }

    @Test
    public void testSuv() throws IOException, CoLoopUnrepresentableException {
        testDrawing(Examples.suvorov14(),"N",  "suvorov");
    }
    
    @Test
    public void testSaw() throws IOException, CoLoopUnrepresentableException {
        testDrawing(Examples.circularsaw3(),"A","saw");
    }
    
    @Test
    public void testTTT() throws IOException, CoLoopUnrepresentableException {
        testCeva(true,true,true);
    }
    
    @Test
    public void testTTF() throws IOException, CoLoopUnrepresentableException {
        testCeva(true,true,false);
    }
    
    @Test
    public void testTFT() throws IOException, CoLoopUnrepresentableException {
        testCeva(true,false,true);
    }
    
    @Test
    public void testTFF() throws IOException, CoLoopUnrepresentableException {
        testCeva(true,false,false);
    }

    @Test
    public void testFTT() throws IOException, CoLoopUnrepresentableException {
        testCeva(false,true,true);
    }
    
    @Test
    public void testFTF() throws IOException, CoLoopUnrepresentableException {
        testCeva(false,true,false);
    }
    
    @Test
    public void testFFT() throws IOException, CoLoopUnrepresentableException {
        testCeva(false,false,true);
    }
    
    @Test
    public void testFFF() throws IOException, CoLoopUnrepresentableException {
        testCeva(false,false,false);
    }
    private void testCeva(boolean showOrigin, boolean showLabels, boolean showVertices) throws IOException, CoLoopUnrepresentableException {
        ImageOptions opts = ImageOptions.defaultBlackAndWhite();
        opts.showOrigin = showOrigin;
        opts.setShowLabels(showLabels);
        opts.showVertices = showVertices;
        testDrawing(Examples.ceva(), "0", "CEVA"+(showOrigin?"-origin":"")+(showLabels?"-labels":"")+(showVertices?"-vertices":""), opts);
    }
    private void testDrawing(OM om,String name) throws IOException, CoLoopUnrepresentableException {
        for (Label lbl: om.elements()){
            testDrawing(om, lbl.label(), name);
        }
    }

    private void testDrawing(OM om, String label, String name)
            throws IOException, CoLoopUnrepresentableException {
        ImageOptions options = ImageOptions.defaultColor();
        testDrawing(om, label, name, options);
    }
    private void testDrawing(OM om, String label, String name,
            ImageOptions options) throws IOException, CoLoopUnrepresentableException {
        PseudoLines pseudoLines = new PseudoLines(om,label);
        EuclideanPseudoLines euclid = pseudoLines.asEuclideanPseudoLines();
        ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(new File(tmp+"/" + name + "-" + label+".jpeg"));
        iw.setOutput(imageOutput);
        iw.write(euclid.image(options));
        euclid.checkForOverlappingEdge();
        imageOutput.close();
        iw.dispose();
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

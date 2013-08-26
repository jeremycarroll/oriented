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
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.PseudoLines;

import org.junit.Test;

/**
 * @author jeremycarroll
 *
 */
public class TestDrawing {
    

    @Test
    public void testPappus() throws IOException {
        testDrawing(Examples.πάππος(),"pappus");
    }
    @Test
    public void testCeva() throws IOException {
        testDrawing(Examples.ceva(),"ceva");
    }
    
    @Test
    public void testRingel() throws IOException {
        testDrawing(Examples.ringel(),"3", "ringel");
    }

    @Test
    public void testTsukamoto13_1() throws IOException {
        testDrawing(Examples.tsukamoto13(1),  "tsukamoto(1)");
    }

    @Test
    public void testSuv() throws IOException {
        testDrawing(Examples.suvorov14(),"N",  "suvorov");
    }
    
    @Test
    public void testSaw() throws IOException {
        testDrawing(Examples.circularsaw3(),"A","saw");
    }
    private void testDrawing(OM om,String name) throws IOException {
        for (Label lbl: om.elements()){
            testDrawing(om, lbl.label(), name);
        }
    }

    private void testDrawing(OM om, String label, String name)
            throws IOException {
//        long usedMemory = Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
//        if (usedMemory> 400000000 ) {
//            System.err.println("Attach");
//            System.in.read();
//        }
//        System.err.println(usedMemory);
        System.err.println("=== "+name+" === "+label);
        PseudoLines pseudoLines = new PseudoLines(om,label);
        EuclideanPseudoLines euclid = pseudoLines.asEuclideanPseudoLines();
        ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(new File("/Users/jeremycarroll/tmp/" + name + "-" + label+".jpeg"));
        iw.setOutput(imageOutput);
        iw.write(euclid.image());
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

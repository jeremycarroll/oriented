/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test.ringel;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.PseudoLines;

import org.junit.Test;

public class TestDrawing {

    @Test
    public void testRingel() throws IOException {
        testDrawing(Examples.ringel(),"ringel");
    }

    @Test
    public void testT1() throws IOException {
        testDrawing(Examples.tsukamoto13(1),"tsukamoto(1)");
    }

    @Test
    public void testSuv() throws IOException {
        testDrawing(Examples.suvorov14(),"suvorov");
    }
    @Test
    public void testSaw() throws IOException {
        testDrawing(Examples.circularsaw3(),"saw");
    }
    private void testDrawing(OM om,String name) throws IOException {
        System.err.println(om.dual().getMaxVectors());
        for (Label lbl: om.elements()){
        		System.err.print(lbl.label()+" ");
        PseudoLines pseudoLines = new PseudoLines(om,lbl);
//        System.err.println(Arrays.asList(pseudoLines.toCrossingsString()));
          EuclideanPseudoLines euclid = pseudoLines.asEuclideanPseudoLines();
        System.err.println(euclid.toString());
        euclid.arrangePoints();
          System.err.println(Arrays.asList(pseudoLines.toCrossingsString()));
          ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
          iw.setOutput(ImageIO.createImageOutputStream(new File("/Users/jeremycarroll/tmp/" + name + "-" + lbl+".jpeg")));
          iw.write(euclid.image());
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

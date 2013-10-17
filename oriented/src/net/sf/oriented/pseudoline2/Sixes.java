/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.LabelFactory;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.pseudoline.CoLoopCannotBeDrawnException;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.PseudoLineDrawing;
import net.sf.oriented.util.combinatorics.Permutation;

/**
 * Find all angle constraining pseudoline diagrams fo six lines
 * @author jeremycarroll
 *
 */
public class Sixes {
    
    private static Sixes theInstance = new Sixes();
    public static Sixes get() {
        return theInstance;
    }
    private class Six {
        final OMasChirotope om;
        final Label triangles[][];
        Six(OMasChirotope om, String ... tri) {
            this.om = om;
            LabelFactory f = om.ffactory().labels();
            triangles = new Label[4][];
            for (int i=0;i<4;i++) {
                triangles[i] = new Label[]{ f.parse(tri[i].substring(0, 1)), 
                                            f.parse(tri[i].substring(1, 2)), 
                                            f.parse(tri[i].substring(2, 3)) };
            }
        }
        public Six(Six seed, int i) {
            om = seed.om.reorient(seed.om.elements()[i]).getChirotope();
            triangles = seed.triangles;
        }
        public Six(OMasChirotope om, Six six) {
            this.om = om;
            this.triangles = six.triangles;
        }
        public Six zero(int m) {
            OMasChirotope om = this.om;
           for (int i=0;i<4;i++) {
               if (((1<<i)&m)!=0) {
                   om = om.mutate(0, triangles[i]);
               }
           }
           return new Six(om, this);
        }
    }
    private final OMasChirotope seed2 = FactoryFactory.fromCrossings("0:ABCDEF",
            "A:0ECDBF",
            "B:0CDEAF",
            "C:0BEAFD",
            "D:0BEAFC",
            "E:0BDCAF",
            "F:0ABDCE").getChirotope();
    private final Six seeds[] = new Six[]{
      new Six(seed2.ffactory().chirotope().remake(Examples.circularsaw3().getChirotope()),
            "AEF", "BDF", "CDE", "ABC" ),
      new Six(seed2, "ACE", "BDE", "ABF", "CDF" ),
      new Six(seed2.flip(0,2,3), "ACE", "BDE", "ABF", "CDF" ),
      new Six(seed2.mutate(0,0,2,3), "ACE", "BDE", "ABF", "CDF" ),
      new Six(seed2.flip(0,5,6), "ACE", "BDE", "ABF", "CDF" ),
      new Six(seed2.mutate(0,0,5,6), "ACE", "BDE", "ABF", "CDF" ),
      new Six(seed2.flip(0,2,3).flip(0,5,6), "ACE", "BDE", "ABF", "CDF" ),
      new Six(seed2.flip(0,2,3).mutate(0,0,5,6), "ACE", "BDE", "ABF", "CDF" ),
      new Six(seed2.mutate(0,0,2,3).flip(0,5,6), "ACE", "BDE", "ABF", "CDF" ),
    };
    private List<Six> all = new ArrayList<Six>();
    private Sixes() {
        for (Six seed:seeds) {
            for (int i=0; i<12; i++ ) {
                seed = new Six(seed,i%6);
                all.add(seed);
            }
        }
        // add each of the 16 possibilities of zeros 
        for (int i=all.size()-1;i>=0;i--) {
            Six seed = all.get(i);
            for (int m=1;m<16;m++) {
                all.add(seed.zero(m));
            }
        }
    }
    
    public static void main(String a[]) throws IOException, AxiomViolation, CoLoopCannotBeDrawnException {
        int i=0;
        for (Six s:Sixes.get().all) {
        EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(s.om,"0");
        PseudoLineDrawing euclid = pseudoLines.asDrawing();
        ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(new File("/users/jeremycarroll/tmp/six-" + i++ + ".jpeg"));
        iw.setOutput(imageOutput);
        iw.write(euclid.image());
        euclid.verify();
        imageOutput.close();
        iw.dispose();
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

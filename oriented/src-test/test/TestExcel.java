/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.io.IOException;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.ExcelExport;
import net.sf.oriented.pseudoline2.Difficulty;
import net.sf.oriented.pseudoline2.TGFactory;
import net.sf.oriented.pseudoline2.TensionGraph;
import net.sf.oriented.pseudoline2.WAM;

import org.junit.Test;

public class TestExcel extends TestWithTempDir {


    @Test
    public void testDisconnected() {
        count("_disconnected","0");
    }


    private void count(String omName, String inf) {
        try {
            System.err.println(omName+" ===== "+inf+" ==");
            OM om = Examples.all().get(omName);
            EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om,inf);
            TensionGraph ten = new TGFactory(pseudoLines).create();
            ten.prune();
                WAM wam = new WAM(ten);
                Difficulty[] diff = wam.search(null);
                
                System.err.println(wam.transitions+" wam transitions");
                System.err.println(wam.foundDifficultyCount+" original difficulty count");
                System.err.println(diff.length+" difficulties");
                ExcelExport excel = new ExcelExport(pseudoLines);
                excel.make();
                for (int i=0;i<diff.length;i++) {
//                    Graph<Face, DEdge> rslt = diff[0][i].getSimplifiedRslt(ten);
//                    Collection<DPath> cycles = new DPaths(rslt,pseudoLines).cycles();
////                    
////                    if (!searchForCyclePair(rslt, cycles)) {
////                        continue;
////                    }
//                    
//                    
//                    System.err.println("Candidate found: "+i+": "+cycles.size()+" "+rslt.getEdgeCount());
//                    for (Face f:rslt.getVertices()) {
//                        if ( rslt.getNeighborCount(f) == 3 && f.dimension() == 0) {
//                            System.err.println("!!!!");
//                        }
//                    }
//                    
//                    DifficultyDrawing euclid = new DifficultyDrawing(pseudoLines, ten, diff[0][i]);
//                    ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
//                    ImageOutputStream imageOutput = ImageIO.createImageOutputStream(new File(tmp+"/" + omName + "-" + inf+"-"+ 
//                            soln + "-" + (i<10?"0":"")+i +".jpeg"));
//                    iw.setOutput(imageOutput);
//                    iw.write(euclid.image(options));
//                    euclid.verify();
//                    imageOutput.close();
//                    iw.dispose();
//                    
            }
                String fname = tmp+"/"+omName+"-"+inf+".xlsx";
                System.err.println("Saving to "+fname);
                excel.save(fname);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            // nothing
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

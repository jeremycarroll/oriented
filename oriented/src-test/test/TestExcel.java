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

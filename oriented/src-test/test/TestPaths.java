/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import net.sf.oriented.util.graph.Paths;

import org.junit.Test;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.TestGraphs;

public class TestPaths {
    @Test
    public void testSmallPaths() {
        Graph<String, Number> g = TestGraphs.getSmallGraph();
        Paths<String, Number> p = new Paths<String, Number>(g);
        printPaths(p,"0", "1",1);
        printPaths(p,"0","2",1);
        printPaths(p,"0","0",1);
        printPaths(p,"1","2",1);
        printPaths(p,"2","2",1);
        printPaths(p,"1","1",2);
    }
    
    
    

    private void printPaths(Paths<String, Number> p, String f,
            String t, int expected) {
        Assert.assertEquals(expected, printPaths(p,f,t));
    }




    private int printPaths(Paths<String, Number> p, String f, String t) {
        int count =0;
        for (Iterator<List<String>> iterator = p.paths(f, t).iterator(); iterator
                .hasNext();) {
            iterator.next();
            //            for (String v:path) {
            //                System.err.print(v+",");
            //            }
            //            System.err.println();
            count++;
        }
//                System.err.println("# = " + count);
        return count;
    }
    

    @Test
    public void testDemoPaths() {
        Graph<String, Number> g = TestGraphs.getDemoGraph();
        Paths<String, Number> p = new Paths<String, Number>(g);
        printPaths(p,"a","e", 2);
        printPaths(p,"a","c", 2);
        printPaths(p,"b","c", 2);
        printPaths(p,"a","d", 2);
        printPaths(p,"c3", "c5", 109601);
        printPaths(p,"c1", "c5", 109601);
        printPaths(p,"c5", "c3", 109601);
        Assert.assertTrue(printPaths(p,"p12","p17")<109601);
        printPaths(p,"c1","p14", 0);
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

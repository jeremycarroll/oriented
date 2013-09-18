/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import net.sf.oriented.util.graph.SimplePaths;

import org.junit.Ignore;
import org.junit.Test;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.TestGraphs;

public class TestPaths {
    @Test
    public void testSmallPaths() {
        Graph<String, Number> g = TestGraphs.getSmallGraph();
        SimplePaths<String, Number> p = new SimplePaths<String, Number>(g);
        printPaths(p,"0", "1",1);
        printPaths(p,"0","2",1);
        printPaths(p,"0","0",1);
        printPaths(p,"1","2",1);
        printPaths(p,"2","2",1);
        printPaths(p,"1","1",2);
    }
    
    
    

    private void printPaths(SimplePaths<String, Number> p, String f,
            String t, int expected) {
        Assert.assertEquals(expected, printPaths(p,f,t));
    }




    private int printPaths(SimplePaths<String, Number> p, String f, String t) {
        int count =0;
        for (Iterator<List<String>> iterator = p.paths(f, t).iterator(); iterator
                .hasNext();) {
            List<String> path = iterator.next();
                        for (String v:path) {
                            System.err.print(v+",");
                        }
                        System.err.println();
            count++;
        }
//                System.err.println("# = " + count);
        return count;
    }
    

    @Test
    public void testDemoPaths() {
        Graph<String, Number> g = TestGraphs.getDemoGraph();
        g.removeVertex("c2");
        g.removeVertex("c4");
        g.removeVertex("c6");
        g.removeVertex("c7");
        g.removeVertex("p11");
        g.removeVertex("p13");
        g.removeVertex("p15");
        g.removeVertex("p16");
        SimplePaths<String, Number> p = new SimplePaths<String, Number>(g);
        printPaths(p,"a","e", 2);
        printPaths(p,"a","c", 2);
        printPaths(p,"b","c", 2);
        printPaths(p,"a","d", 2);
        printPaths(p,"c3", "c5", 65);
        printPaths(p,"c1", "c5", 65);
        printPaths(p,"c5", "c3", 65);
        Assert.assertTrue(printPaths(p,"p12","p17")<65);
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

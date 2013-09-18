/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import net.sf.oriented.util.graph.SimplePaths;

import org.junit.Test;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.TestGraphs;

public class TestPaths {


    @Test public void testSmall0_1() { testSmall("0","1",1); }
    @Test public void testSmall0_2() { testSmall("0","2",1); }
    @Test public void testSmall0_0() { testSmall("0","0",1); }
    @Test public void testSmall1_2() { testSmall("1","2",1); }
    @Test public void testSmall2_2() { testSmall("2","2",1); }
    @Test public void testSmall1_1() { testSmall("1","1",2); }
    @Test public void testDemo_a_e() { testDemo("a","e",2); }
    @Test public void testDemo_a_c() { testDemo("a","c",2); }
    @Test public void testDemo_b_c() { testDemo("b","c",2); }
    @Test public void testDemo_a_d() { testDemo("a","d",2); }
    @Test public void testDemo_c3_c5() { testDemo("c3","c5",65); }
    @Test public void testDemo_c1_c5() { testDemo("c1","c5",65); }
    @Test public void testDemo_c5_c3() { testDemo("c5","c3",65); }
    @Test public void testDemo_c1_p14() { testDemo("c1","p14",0); }
    @Test
    public void testDemo_p12_p17() {
        SimplePaths<String, Number> p = new SimplePaths<String, Number>(simplifiedDemoGraph());
        Assert.assertTrue(printPaths(p,"p12","p17")<65);
    }



    private void testSmall(String f, String t, int cnt) {
        Graph<String, Number> g = TestGraphs.getSmallGraph();
        SimplePaths<String, Number> p = new SimplePaths<String, Number>(g);
        this.printPaths(p, f, t,cnt);
    }
    private void testDemo(String f, String t, int cnt) {
        Graph<String, Number> g = simplifiedDemoGraph();
        SimplePaths<String, Number> p = new SimplePaths<String, Number>(g);
        this.printPaths(p, f, t,cnt);
    }
    private Graph<String, Number> simplifiedDemoGraph() {
        Graph<String, Number> g = TestGraphs.getDemoGraph();
        g.removeVertex("c2");
        g.removeVertex("c4");
        g.removeVertex("c6");
        g.removeVertex("c7");
        g.removeVertex("p11");
        g.removeVertex("p13");
        g.removeVertex("p15");
        g.removeVertex("p16");
        return g;
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
//            for (String v:path) {
//                System.err.print(v+",");
//            }
//            System.err.println();
            count++;
        }
        //                System.err.println("# = " + count);
        return count;
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

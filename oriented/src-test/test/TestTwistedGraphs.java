/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import junit.framework.Assert;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.CoLoopCannotBeDrawnException;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.ImageOptions;
import net.sf.oriented.pseudoline2.DEdge;
import net.sf.oriented.pseudoline2.DPath;
import net.sf.oriented.pseudoline2.Difficulty;
import net.sf.oriented.pseudoline2.DifficultyDrawing;
import net.sf.oriented.pseudoline2.Sixes;
import net.sf.oriented.pseudoline2.TGFactory;
import net.sf.oriented.pseudoline2.TensionGraph;
import net.sf.oriented.pseudoline2.WAM;
import net.sf.oriented.sines.Sines;

import org.junit.Ignore;
import org.junit.Test;

import edu.uci.ics.jung.graph.Graph;


public class TestTwistedGraphs extends TestWithTempDir {
    

// circularSaw3.getChirotope().mutate(1,1,2,3); - a circ saw that isn't

    private String bad;

    @Ignore
    @Test
    public void testCeva() {
        count("ceva","0",50,48,8,12,2);
    }

    @Test
    public void testCircSaw5() {
        count("circularsaw5","0",96,855,76,620,287);
    }

    @Test
    public void testCircSaw5A() {
        count("circularsaw5","A",140,1658,58,382,287);
    }
    @Test
    public void testCirc_Saw5A() {
        count("_saw5A","0",116,1445,106,1290,262);
    }

    @Test
    public void testDeformCircSaw5() {
        count("_deformSaw5","0",-1,-1,-1,-1,263);
    }

    @Test
    public void testDeformCircSaw5A() {
        count("_deformSaw5","A",-1,-1,-1,-1,263);
    }
    @Test
    public void testDeformCircSaw5B() {
        count("_deformSaw5","B",-1,-1,-1,-1,263);
    }
    @Ignore
    @Test
    public void testDisconnected() {
        count("_disconnected","0",-1,-1,-1,-1,-1);
    }

    @Test
    public void testPappus() {
        count("pappus","0",-1,-1,-1,-1,-1);
    }

    @Ignore
    @Test
    public void testSaw() {
        count("circularsaw3","0",22,36,4,6,1);
    }

    @Ignore
    @Test
    public void testDeformedCeva() {
        count("_deformedCeva","0",42,28,0,0);
    }

    @Test
    public void testDeformedSaw() {
        count("_deformedCircularSaw","0",26,29,0,0);
    }

    @Ignore
    @Test
    public void testTsukamotoPlusA() {
        count("tsukamoto13.+1","A",312,5605,304,5431,10292);  // 84791
    }
//    junit.framework.AssertionFailedError: 6193 != 5605; 5957 != 5431; 10975 != 10807; 


    @Ignore
    @Test
    public void testTsukamotoMinusA() {
        count("tsukamoto13.-1","A", 308  , 5465  , 300  , 5277, 15917); // 266666
    }

//    junit.framework.AssertionFailedError: 6039 != 5465; 5791 != 5277; 17357 != 17260; 

    @Ignore
    @Test
    public void testTsukamotoPlusB() {
        count("tsukamoto13.+1","B",312,6193,304,5957,10975); // > 15471548
    }

    @Ignore
    @Test
    public void testTsukamotoMinusB() {
        count("tsukamoto13.-1","B", 308  , 6039  , 300  , 5791,17357);
    }


    @Ignore
    @Test
    public void testTsukamotoPlusC() {
        count("tsukamoto13.+1","C",312,6193,304,5957,10975); // > 15471548
    }

    @Ignore
    @Test
    public void testTsukamotoMinusC() {
        count("tsukamoto13.-1","C", 308  , 6039  , 300  , 5791,17357);
    }
    @Ignore
    @Test
    public void testSuvorov14() {
        count("suvorov14","A", 716  , 35463  , 675  , 31549 ,10975);
    }

    @Ignore
    @Test
    public void testOmage14Plus() {
        count("omega14.+1","A",312,6193,304,5957,10975);
    }

    @Ignore
    @Test
    public void testOmage14Minus() {
        count("omega14.-1","A",312,6193,304,5957,10975);
    }
    

    @Test
    public void testRingel() {
        count("ringel","0",63,343,25,99,16);
    }

    @Ignore
    @Test
    public void testMany() {
        for (int i=0;i<10;i++) {
            testRingel();
//            testCircSaw5();
//            testCircSaw5A();
        }
    }
    @Ignore
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
            System.err.println("\n"+omName+" ===== "+inf+" ==");
            OM om = Examples.all().get(omName);
            EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om,inf);
            int m = Sixes.get().matches(pseudoLines);
            System.err.println("Matches: "+m);
            if (true) {
            TensionGraph ten = new TGFactory(pseudoLines).create();
//        for (TGVertex v:ten.getVertices()) {
//            System.err.println(v);
//        }
            usuallyAssertEquals(vCount,ten.getVertexCount());
            usuallyAssertEquals(eCount,ten.getEdgeCount());
            ten.prune();
            usuallyAssertEquals(vCount2,ten.getVertexCount());
            usuallyAssertEquals(eCount2,ten.getEdgeCount());
            if (false && vCount2 != 0) {
                soln ++;
                WAM wam = new WAM(ten);
                Difficulty[][] diff = wam.search(omName+"*"+inf);
                
                System.err.println(wam.transitions+" wam transitions");
                System.err.println(wam.foundDifficultyCount+" original difficulty count");
                System.err.println(diff[0].length+" difficulties");
                usuallyAssertEquals(dCount,diff[0].length);
                String namename = omName + "-" + inf+"-"+ (soln<10?"0":"")+soln;
                if (false)
                    dumpDrawings(diff[0], pseudoLines, ten, namename);
            }
            if (bad != null) {
//                System.err.println(bad);
                Assert.fail(bad);
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

    protected void dumpDrawings(Difficulty[] difficulties,
            EuclideanPseudoLines pseudoLines, TensionGraph ten, String namename)
                    throws CoLoopCannotBeDrawnException, IOException, AxiomViolation {
//        if (true) {
//            return;
//        }
        ImageOptions options = ImageOptions.defaultBlackAndWhite();
        int i=0;
        for (Difficulty diff:difficulties) {
//            if (rslt.getEdgeCount() != 6) continue;
//            if (i > 40) {
//                break;
//            }
            Sines sines = diff.getSines(ten);
            if (sines.isOK()) {
                continue;
            }
            i++;
//            if (i > 20) {
//                break;
//            }
//            if (true)
//            continue;
            Graph<Face, DEdge> rslt = diff.getSimplifiedRslt(ten);
            UnsignedSet labels = pseudoLines.ffactory().unsignedSets().empty();
            for (DEdge d: rslt.getEdges()) {
                Label l = d.getLabel();
                labels = labels.union(l);
            }
            //                    Graph<Face, DEdge> rslt = difficulties[i].getSimplifiedRslt(ten);
            String fileName = namename + "-" + labels.toString() + "-" + (i<10?"0":"") + i;
            System.err.println(fileName);
            sines.dump();
            if (i > 40) {
                continue;
            }
            //                    Collection<DPath> cycles = new DPaths(rslt,pseudoLines).cycles();

            //                    if (!searchForCyclePair(rslt, cycles)) {
            //                        continue;
            //                    }
            //                    
            //                    
            //                    System.err.println("Candidate found: "+i+": "+cycles.size()+" "+rslt.getEdgeCount());
            //                    for (Face f:rslt.getVertices()) {
            //                        if ( rslt.getNeighborCount(f) == 3 && f.dimension() == 0) {
            //                            System.err.println("!!!!");
            //                        }
            //                    }

            DifficultyDrawing euclid = new DifficultyDrawing(pseudoLines, ten, diff);
            ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
            ImageOutputStream imageOutput = ImageIO.createImageOutputStream(new File(tmp + "/" + fileName + ".jpeg"));
            // System.err.println(fileNmae+" "+diff[0][i].unnecessary+ " "+ rslt.getEdgeCount());
            iw.setOutput(imageOutput);
            iw.write(euclid.image(options));
            euclid.verify();
            imageOutput.close();
            iw.dispose();

        }
    }

    protected boolean searchForCyclePair(Graph<Face, DEdge> rslt,
            Collection<DPath> cycles) {
        int sz = rslt.getVertexCount();
        List<DPath> smallerCycles = new ArrayList<>();
        for (DPath dp:cycles) {
            if (dp.getPath().size()<sz-1) {
                smallerCycles.add(dp);
            }
        }
        for (int i=0;i<smallerCycles.size();i++) {
            List<Face> iPath = smallerCycles.get(i).getPath();
            int isz = iPath.size() - 1;
            for (int j=i+1; j<smallerCycles.size(); j++) {
                List<Face> jPath = smallerCycles.get(j).getPath();
                int jsz = jPath.size() - 1;
                if (isz + jsz <= sz) {
                    if (!overlap(iPath,jPath)) {
                        return true;
                    }
                }
                
            }
        }
        return false;
    }

    private boolean overlap(List<Face> iPath, List<Face> jPath) {
        for (Face fi:iPath) {
            for (Face fj:jPath) {
                if (fi.equals(fj)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void usuallyAssertEquals(int expected, int actual) {
        if ( expected != -1 && expected != actual) {
            if (bad == null) {
                bad = "";
            }
            bad = bad + expected +" != " + actual + "; ";
        }
        
    }

}
/*
 
ceva ===== 0 ==
1   80
2   107
3   47
4   32
5   31
7   32
9   2
=   331
Matches: 1
17 wam transitions
2 original difficulty count
2 difficulties

circularsaw5 ===== 0 ==
Matches: 0
7225 wam transitions
406 original difficulty count
287 difficulties

_saw5A ===== 0 ==
Matches: 8
7345 wam transitions
334 original difficulty count
262 difficulties

_deformSaw5 ===== 0 ==
Matches: 0
5998 wam transitions
358 original difficulty count
263 difficulties

_disconnected ===== 0 ==
Matches: 0
111 wam transitions
1 original difficulty count
1 difficulties

pappus ===== 0 ==
Matches: 3
117 wam transitions
9 original difficulty count
7 difficulties

circularsaw3 ===== 0 ==
Matches: 1
9 wam transitions
1 original difficulty count
1 difficulties

_deformedCeva ===== 0 ==
Matches: 0

_deformedCircularSaw ===== 0 ==
Matches: 0

tsukamoto13.+1 ===== A ==
Matches: 19
656898 wam transitions
19077 original difficulty count
10292 difficulties

tsukamoto13.-1 ===== A ==
Matches: 19
Redo[17/5312:3$27484]+   2/3 J:D'GIJ⇒C'J'M
1205275 wam transitions
33699 original difficulty count
15917 difficulties

ringel ===== 0 ==
Matches: 3
243 wam transitions
20 original difficulty count
16 difficulties

 */

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

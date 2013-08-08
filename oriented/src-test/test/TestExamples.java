/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;


import java.util.Arrays;

import junit.framework.Assert;
import net.sf.oriented.impl.om.ExamplesHelper;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.polytope.DualFaceLattice;
import net.sf.oriented.pseudoline.Realization;

import org.junit.Ignore;
import org.junit.Test;

public class TestExamples {
  

    @Ignore
    @Test
    public void testUniform4() {
        OM fromEuclideanLines = FactoryFactory.fromEuclideanLines(
                new int[][][]{
                        {{2,1},{1,1},},
                        {{ 2, 1 },{1,2}},
                        {{1,1},{1,2}},
                });
        /*
         * [0]=(1,1,0)
         * [1]=(0,10000,10000)        θ = 0
         * [2]=(7071,7071,21213)      θ = 45
         * [3]=(10000,0,10000)        θ = 90
         */
        Assert.assertEquals(Examples.uniform4(), 
                fromEuclideanLines);
    }
    
    @Test
    public void testΠάππος() {
        // There are nine points of intersection in Pappus's construction.
        Assert.assertTrue( Examples.πάππος().getChirotope().toCoLexicographicString().matches("^.*0.*0.*0.*0.*0.*0.*0.*0.*0.*$") );
        
    }
    @Test
    public void testCeva() {
        // There are four points of intersection in Ceva's construction.
       Assert.assertTrue( Examples.ceva().getChirotope().toCoLexicographicString().matches("^.*0.*0.*0.*0.*$") );
    }
    
   
    @Test
    public void testFromCrossing() {
        OM fromEuclideanCrossings = FactoryFactory.fromCrossings(
                  "A:BCD",
                  "B:ACD",
                  "C:ABD",
                  "D:ABC" );
        /*
         * [0]=(1,1,0)
         * [1]=(0,10000,10000)        θ = 0
         * [2]=(7071,7071,21213)      θ = 45
         * [3]=(10000,0,10000)        θ = 90
         */
        Assert.assertEquals(Examples.uniform4().dual().getMaxVectors(), fromEuclideanCrossings.dual().getMaxVectors());
    }
    
    @Test
    public void testSuv14() {
        Assert.assertEquals(Examples.suvorov14(), ExamplesHelper.suv14(1.780776,5.186363));
        Assert.assertEquals(0, Examples.suvorov14().getChirotope().chi(0,12,13));
        Assert.assertEquals(0, Examples.suvorov14().getChirotope().chi(5,9,10));
    }
    
    @Ignore
    @Test
    public void testRingelAt7() {
        Realization realization = new Realization(Examples.ringel(),"7");
        String[] crossings = realization.toCrossingsString();
        OM expected = Examples.ringel()
                .reorient(realization.getReorientation()).permuteGround(realization.getPermutation());
        OM actual = FactoryFactory.fromCrossings(crossings);
        Assert.assertEquals(expected.getChirotope().chi(1,2,3),actual.getChirotope().chi(1,2,3));
        Assert.assertEquals(expected, actual);
    }

    @Ignore
    @Test
    public void testOmega14_0() {
        OM om = Examples.omega14(0);
//        Label g[] = om.elements();
//        for (Label lbl : g) {
//            System.err.println("Label: "+lbl.label());
        Realization realization = new Realization(om,"1","4","6","5","8","7");
        Label one = om.ffactory().labels().parse("1");
        String[] crossings = realization.toCrossingsString();
        for (String str:crossings) {
            System.out.println(str);
        }
        System.out.print("Circuits of size 3, with 1+: ");
        for (SignedSet circuit:realization.getEquivalentOM().getCircuits()) {
            if (circuit.size()==3 && circuit.sign(one)==1) {
                System.out.print(circuit+" ");
            }
        }
        
        System.out.println("\nCoCircuits: " + realization.getEquivalentOM().dual().getCircuits());
        System.out.println("Topes: " + realization.getEquivalentOM().dual().getMaxVectors());
        System.out.println(om.ffactory().unsignedSets().copyBackingCollection(Arrays.asList(realization.getReorientation())));
        OM expected = om.reorient(realization.getReorientation()).permuteGround(realization.getPermutation());
        OM actual = FactoryFactory.fromCrossings(crossings);
      //  Assert.assertEquals(expected.getChirotope().chi(1,2,3),actual.getChirotope().chi(1,2,3));
        Assert.assertEquals(expected, actual);
//        }
    }
    
    @Test
    public void testCevaFL() throws AxiomViolation {
        DualFaceLattice fl = new DualFaceLattice(Examples.ceva().dual());
//        fl.dump();
        fl.verify();
    }
    @Ignore
    @Test
    public void testCevaDualFL() throws AxiomViolation {
        DualFaceLattice fl = new DualFaceLattice(Examples.ceva());
//        fl.dump();
        fl.verify();
    }
    @Ignore
    @Test
    public void testSuvorov14Dual() throws AxiomViolation {
        OM suvorov14 = Examples.suvorov14();
        System.err.println("Circuits ");
        suvorov14.getCircuits();
//        System.err.println("Vectors ");
//        suvorov14.getVectors();
//        System.err.println("MaxVectors ");
//        suvorov14.getMaxVectors();
        System.err.println("Done ");
        OM dual = suvorov14.dual();
        new DualFaceLattice(dual).verify();
//        new DualFaceLattice(suvorov14);
    }

    @Ignore // This one is very hard - out of memory on 2G, and 13 minutes at the moment
    @Test
    public void testTsukamoto13_1() throws AxiomViolation {
        OM tsukamoto = Examples.tsukamoto13(1);
        DualFaceLattice lattice = new DualFaceLattice(tsukamoto);
        System.err.println(lattice);
        lattice.verify();
    }
    @Ignore
    @Test
    public void testChap1x() {
        OM om = Examples.chapter1();
//        Label g[] = om.elements();
//        for (Label lbl : g) {
//            System.err.println("Label: "+lbl.label());
        Realization realization = new Realization(om,"2");
        String[] crossings = realization.toCrossingsString();
        for (String str:crossings) {
            System.out.println(str);
        }
        System.out.println("Circuits: " + realization.getEquivalentOM().getCircuits());
        System.out.println("CoCircuits: " + realization.getEquivalentOM().dual().getCircuits());
        System.out.println("Topes: " + realization.getEquivalentOM().dual().getMaxVectors());
        System.out.println(om.ffactory().unsignedSets().copyBackingCollection(Arrays.asList(realization.getReorientation())));
        OM expected = om.reorient(realization.getReorientation()).permuteGround(realization.getPermutation());
        OM actual = FactoryFactory.fromCrossings(crossings);
      //  Assert.assertEquals(expected.getChirotope().chi(1,2,3),actual.getChirotope().chi(1,2,3));
        Assert.assertEquals(expected, actual);
        }
//    }
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

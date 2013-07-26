/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/

package net.sf.oriented.omi;



/**
 * Here are some 'interesting' oriented matroids. Of course, all we mean by
 * interesting is interesting to me, and my projects of pseudoline stretching.
 * 
 * @author jeremycarroll
 * 
 */
public class Examples {
    
    private static OM chapter1;
    
    private static OM uniform3;
    private static OM uniform4;
    private static OM ceva;

    private static OM πάππος;
    
    private static OM ringel;  
    /* Draw a circular saw on squared paper and take the co-ordinates of two
     * points on each line
     *
     */
    /**
     * See the "Sharpness of Circular Saws (2000)" {@link Bibliography#carroll2000d}
     */
    private static OM circularSaw3;
   
    private static boolean FreshEachTime = true;

    /**
     * This is from the Oriented Matroid book {@link Bibliography#björnerEtAl1999}
     */
    public static OM chapter1() {
        if (chapter1 == null || FreshEachTime) {
            chapter1 = FactoryFactory.fromCircuits("{12'4,13'5,23'6,45'6,12'56',13'46,23'4'5}");
        }
        return chapter1;
    }

    public static OM uniform3() {
        if (uniform3 == null || FreshEachTime) {
            uniform3 = FactoryFactory.fromChirotope(3, 3, "+" );
        }
        return uniform3;
    }

    public static OM uniform4() {
        if (uniform4 == null || FreshEachTime) {
            uniform4  = FactoryFactory.fromChirotope(4, 3, "++++" );
        }
        return uniform4;
    }

    /**
     * See {@link Bibliography#ceva1678}
     */
    public static OM ceva() {
        if (ceva == null || FreshEachTime) {
            ceva  = FactoryFactory.fromCrossings("0:ABCDEF",
                    "A:0(BC)D(EF)",
                    "B:0(AC)(DF)E",
                    "C:0(AB)F(DE)",
                    "D:0A(BF)(CE)",
                    "E:0(AF)B(CD)",
                    "F:0(AE)(BD)C");
        }
        return ceva;
    }

    public static OM pappus() {
        return πάππος();
    }

    public static OM πάππος() {
        if (πάππος == null || FreshEachTime) {
            πάππος  = FactoryFactory.fromCrossings(
                    "0:(12)(34)5(67)8",
                    "1:(02)4(35)7(68)",
                    "2:(01)(48)(57)63",
                    "3:(04)(15)(78)62",
                    "4:(03)1(28)7(56)",
                    "5:0(13)8(27)(46)",
                    "6:(07)(18)32(45)",
                    "7:(06)1(38)(25)4",
                    "8:0(16)(37)5(24)"
                    );
        }
        return πάππος;
    }

    public static OM ringel() {
        if (ringel == null || FreshEachTime) {
        ringel = FactoryFactory.fromCrossings(
                "0:98765321",
                "9:07631582",
                "8:07653192",
                "7:08963152",
                "6:08971235",
                "5:08319726",
                "3:08597126",
                "2:01987536",
                "1:02859736"
                );
        }
        return ringel;
    }

    public static OM circularsaw3() {
        if (circularSaw3 == null || FreshEachTime) {
            circularSaw3= FactoryFactory.fromEuclideanLines(new int[][][] {
                    // here is the first line, two points
                    { { 10, 75 }, { 65, 90 } },
                    // here is the second line, two points
                    { { 10, 75 }, { 65, 75 } }, 
                    // notice that in the circular saw construction we have the pointy bits
                    // as the obvious point to specify both lines going through it.
                    { { 40, 75 }, { 85, 10 } }, 
                    { { 85, 10 }, { 55, 75 } },
                    { { 70, 40 }, { 65, 120 } },
                    { { 65, 60 }, { 65, 120 } },
                    }

            );
            
        }
        return circularSaw3;
    }
}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * The Java Oriented Matroid Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Java Oriented Matroid Library. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

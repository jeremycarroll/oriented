/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/

package net.sf.oriented.omi;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import net.sf.oriented.impl.om.ExamplesHelper;
import net.sf.oriented.impl.util.Misc;



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
    
    private static OM _disconnected;

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
    
    private static OM omega14[] = new OM[3];
    
    private static OM suv14;
    

    private static OM tsukamoto13[] = new OM[3];
   
    private static boolean FreshEachTime = false;

    private static OM wheel12;

    private static OM circularSaw5;

    private static OM circularSaw5A;

    /**
     * This is from the Oriented Matroid book {@link Bibliography#björnerEtAl1999}
     */
    public static OM chapter1() {
        if (chapter1 == null || FreshEachTime) {
            chapter1 = FactoryFactory.fromCircuits("{12'4,13'5,23'6,45'6,12'56',13'46,23'4'5}");
        }
        return chapter1;
    }

    /**
     * The uniform oriented matroid of 3 points of rank 3
     */
    public static OM uniform3() {
        if (uniform3 == null || FreshEachTime) {
            uniform3 = FactoryFactory.fromCoLexicographic(3, 3, "+" );
        }
        return uniform3;
    }

    /**
     * The uniform oriented matroid of 4 points of rank 3
     */
    public static OM uniform4() {
        if (uniform4 == null || FreshEachTime) {
            uniform4  = FactoryFactory.fromCoLexicographic(4, 3, "++++" );
        }
        return uniform4;
    }

    

    /**
     * This is from the Oriented Matroid book {@link Bibliography#björnerEtAl1999}
     */
    public static OM wheel12() {
        if (wheel12 == null || FreshEachTime) {
            wheel12 = FactoryFactory.fromCrossings("0:ABCDEFGHIJK",
                    "A:0(BCDEFGHIJK)",
                    "B:0(ACDEFGHIJK)",
                    "C:0(ABDEFGHIJK)",
                    "D:0(ABCEFGHIJK)",
                    "E:0(ABCDFGHIJK)",
                    "F:0(ABCDEGHIJK)",
                    "G:0(ABCDEFHIJK)",
                    "H:0(ABCDEFGIJK)",
                    "I:0(ABCDEFGHJK)",
                    "J:0(ABCDEFGHIK)",
                    "K:0(ABCDEFGHIJ)");
        }
        return wheel12;
    }
    /**
     * An oriented matroid representing Ceva's theorem.
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

    /**
     * The oriented matroid corresponding to the arrangement in Pappus's theorm
     * @see Bibliography#πάπποςC340
     * @see Bibliography#πάπποςAndJones1986b
     */
    public static OM pappus() {
        return πάππος();
    }

    /**
     * The oriented matroid corresponding to the arrangement in Pappus's theorm
     * @see Bibliography#πάπποςC340
     * @see Bibliography#πάπποςAndJones1986b
     */
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

    /**
     * The oriented matroid corresponding to Ringel's uniform non-realizable, non-Pappus configuration.
     * @see Bibliography#ringel1956
     */
    public static OM ringel() {
        if (ringel == null || FreshEachTime) {
        ringel = FactoryFactory.fromCrossings(
                "0:12345678",
                "1:03468527",
                "2:03456817",
                "3:02146857",
                "4:02138765",
                "5:02681374",
                "6:02513874",
                "7:08123564",
                "8:07251364"
                // old
//                "0:98765321",
//                "9:07631582",
//                "8:07653192",
//                "7:08963152",
//                "6:08971235",
//                "5:08319726",
//                "3:08597126",
//                "2:01987536",
//                "1:02859736"
                );
        }
        return ringel;
    }

    /**
    The oriented matroid corresponding to the circular saw diagram of size three.
    @see Bibliography#carroll2000d
    */
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
    /**
    The oriented matroid corresponding to a circular saw diagram of size five.
    @see Bibliography#carroll2000d
    */
    public static OM circularsaw5() {
        if (circularSaw5 == null || FreshEachTime) {
            circularSaw5= FactoryFactory.fromCrossings(
                    "0:ABCDEFGHIJ",
                    "A:0CDHGEFBIJ",
                    "B:0CDHGEFAIJ",
                    "C:0BADHGIJFE",
                    "D:0BACHGIJFE",
                    "E:0GHBAIJFCD",
                    "F:0GHBAIJECD",
                    "G:0FEHBACDJI",
                    "H:0FEGBACDJI",
                    "I:0ABFECDJGH",
                    "J:0ABFECDIGH");
            
        }
        return circularSaw5;
    }
    public static OM _saw5A() {
        if (circularSaw5A == null || FreshEachTime) {
            circularSaw5A= FactoryFactory.fromCrossings(
                    "0:AJCBEDGFIH",
                    "A:0JCDHGEFBI",
                    "J:0AHGIDCEFB",
                    
                    "C:0ADHGIJFEB",
                    "B:0DHGEFAIJC",

                    "E:0DGHBAIJFC",
                    "D:0EBACHGIJF",
                    
                    "G:0EHBACDJIF",
                    "F:0HBAIJECDG",
                    
                    "I:0HABFECDJG",
                    "H:0IFEGBACDJ"

                    
                    );
            
        }
        return circularSaw5A;
    }
    public static OM _deformSaw5() {
        return circularsaw5().getChirotope().mutate(0,"C","D","H");
    }

    public static OM _disconnected() {
        if (_disconnected != null && !FreshEachTime) {
            return _disconnected;
        }
        OM om = FactoryFactory.fromCrossings(
                    "0:ABCDFGHIJ",
                    "A:0CDHGFBIJ",
                    "B:0CDHGFAIJ",
                    "C:0BADHGIJF",
                    "D:0BACHGIJF",
                    "F:0GHBAIJCD",
                    "G:0FHBACDJI",
                    "H:0FGBACDJI",
                    "I:0ABFCDJGH",
                    "J:0ABFCDIGH");
        _disconnected = om.getChirotope().mutate(1,"C","D","H").flip("D","I","J").flip("C","I","J").flip("F","I","J"). flip("C","I","F");
        return  _disconnected;
    }
    
    public static OM _deformedCircularSaw() {
        return circularsaw3().getChirotope().mutate(1,1,2,3);
    }
    public static OM _deformedCeva() {
        return ceva().getChirotope().mutate(-1,1,2,3).mutate(-1,3,4,5);
    }
    /**
     * Suvorov's non-isotopic Oriented Matroid,
     * (as presented in {@link Bibliography#björnerEtAl1999}
     * @see Bibliography#suvorov1988
     */
    public static OM suvorov14() {
        if (suv14 == null ||FreshEachTime) {
            suv14 = ExamplesHelper.suv14(1.780776,3.936742);
        }
        return suv14;
    }

    /**
     * Tsukamoto's non-isotopic Oriented Matroid
     * @param i  1, 0 or -1 giving the chirotope χ(8,11,12) (counting from 0)
     * @see Bibliography#tsukamoto2013
     */
    public static OM tsukamoto13(int i) {

        if (tsukamoto13[i+1]==null || FreshEachTime) {
            tsukamoto13[i+1] = 
                    ExamplesHelper.tsukamoto13(0.5,0.5,1.0/3.0)
                       .getChirotope().mutate(i,8,11,12);
        }
        return tsukamoto13[i+1];
    }
    /**
     * Richter-Gebert's interesting oriented matroids.
     * @param i  1, 0 or -1 giving the chirotope χ(11,12,13) (counting from 0)
     * @see Bibliography#richterGebert1996
     */
    public static OM omega14(int i) {
        if (omega14[1]==null) {
            omega14[1] = FactoryFactory.fromMatrix( new int[][] {
                    {1, 0, 1, 0, 1, 1, 2, 3, 2, 3, 1, 1, -1, 0},
                    {0, 1, 0, 1, 1, 2, 1, 2, 3, 1, 3, -1, 1, 0},
                    {0, 0, 1, 1, 2, 2, 2, 4, 4, 4, 4,  4, 4, 1}
            });
        }
        if (omega14[i+1]==null || FreshEachTime) {
            Label elements[] = omega14[1].elements();
            omega14[i+1] = omega14[1].getChirotope().mutate(i,elements[11],elements[12],elements[13]);
        }
        return omega14[i+1];
    }

    /**
     * Richter-Gebert's interesting oriented matroids.
     * @param i  1, 0 or -1 giving the chirotope χ(11,12,13) (counting from 0)
     * @see Bibliography#richterGebert1996
     */
    public static OM Ω14(int i) {
        return omega14(i);
    }

    public static Map<String,OM> all() {
        Map<String,OM> rslt = new HashMap<>();

        Class<?> ex = Examples.class;
        for (Method m:ex.getMethods()) {
             if (Modifier.isStatic(m.getModifiers())
                      && OM.class.isAssignableFrom( m.getReturnType() )  
                        ) {
                 switch (m.getParameterTypes().length) {
                 case 0:
                     rslt.put(m.getName(),(OM) Misc.invoke(m, null));
                     break;
                 case 1:
                     rslt.put(m.getName()+".+1",(OM) Misc.invoke(m, null,1));
                     rslt.put(m.getName()+".0",(OM) Misc.invoke(m, null,0));
                     rslt.put(m.getName()+".-1",(OM) Misc.invoke(m, null,-1));
                     break;
                  default:
                     throw new IllegalStateException("Problem with method: "+m.getName());
                 }
             }
        }
        return rslt;
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/

package net.sf.oriented.omi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.oriented.matrix.RationalMatrix;

/**
 * Here are some 'interesting' oriented matroids. Of course, all we mean by
 * interesting is interesting to me, and my projects of pseudoline stretching.
 * 
 * @author jeremycarroll
 * 
 */
public class Examples {
    
    /**
     * This is from the Oriented Matroid book {@link Bibliography#bj�rnerEtAl1999}
     */
    public final static OM chapter1 = fromCircuits("{12'4,13'5,23'6,45'6,12'56',13'46,23'4'5}");
    
    public final static OM uniform3 = fromChirotope(3, 3, "+" );
    public final static OM uniform4 = fromChirotope(4, 3, "++++" );
    /**
     * See {@link Bibliography#ceva1678}
     */
    public final static OM ceva = fromCrossings("0:ABCDEF",
             "A:0(BC)D(EF)",
             "B:0(AC)(DF)E",
             "C:0(AB)F(DE)",
             "D:0A(BF)(CE)",
             "E:0(AF)B(CD)",
             "F:0(AE)(BD)C");
            
    public final static OM pappus = fromCrossings(
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

    public final static OM πάππος = pappus;
    /* Draw a circular saw on squared paper and take the co-ordinates of two
     * points on each line
     *
     */
    /**
     * See the "Sharpness of Circular Saws (2000)" {@link Bibliography#carroll2000d}
     */
    public final static OM circularSaw3 = fromEuclideanLines(new int[][][] {
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

    public static OM fromChirotope(int n, int r, String chi) {
        Options opt = new Options();
        String u = createUniverse(n, opt);
        return new FactoryFactory(opt).chirotope().parse(
                "(" + u + ", " + r + ", " + chi.replaceAll(" ", "")
                        + ")");
    }
    
    public static OM fromCircuits(String circuits) {
            Options options = new Options();
            options.setShortLabels();
            return new FactoryFactory(options).circuits().parse(circuits);
    }

    static FactoryFactory f;

    static {
        Options options = new Options();
        options.setShortLabels();
        f = new FactoryFactory(options);
    }

    private static String createUniverse(int n, Options opt) {
        String universe[] = new String[n];
        char u[] = new char[n];
        for (int i = 0; i < n; i++) {
            char c = (char) ('A' + i);
            universe[i] = new String(new char[] { c });
            u[i] = c;
        }
        opt.setUniverse(universe);
        opt.setShortLabels();
        return new String(u);
    }

    private final static double epsilon = 0.00000000001d;
    /**
     * Generate an OM from lines being specified as a pair of points (integer coordinates)
     * 
     * This is very easy to do from a reasonably precise accurate sketch, see {@link #circularSaw3} for a documented example.
     * @param lines
     * @return
     */
    public static OM fromEuclideanLines(int[][] ... lines) {
        int n = lines.length+1;
        Options opt = new Options();
        createUniverse(n, opt);
        int projective[][] = new int[3][n];
        set(projective,0,1,1,0);
        int ix=1;
        for (int line[][]:lines) {
            // Find perpendicular
            int yyy = line[0][1];
            int yy2 = line[1][1];
            double xx = (yy2 - yyy);
            int xxx = line[0][0];
            int xx2 = line[1][0];
            double yy = (xxx - xx2);
            // Normalize
            double length = Math.sqrt(xx*xx+yy*yy);
            xx = xx/length;
            yy = yy/length;
            double dotProductA = xx*xxx+yy*yyy;
            if (dotProductA < epsilon && dotProductA > -epsilon) {
                throw new IllegalArgumentException("line went through origin");
            }
           // double dotProductB = xx*xx2+yy*yy2;
            if (yy<0.0 || (yy==0.0 && xx < 0.0) ) {
                 xx = -xx;
                yy = -yy;
                dotProductA = -dotProductA;
            }
             int x = (int) Math.floor(xx*10000);
            int y = (int) Math.floor(yy*10000);
            int z = (int) Math.floor(dotProductA*10000);
            set(projective,ix++,x,y,z);
        }
        return new FactoryFactory(opt).realized().construct(new RationalMatrix(projective));
    }

    private static void set(int[][] projective, int ix, int x, int y, int z) {
//        System.err.println("["+ix+"]=("+x+","+y+","+z+")");
        projective[0][ix] = x;
        projective[1][ix] = y;
        projective[2][ix] = z;
    }

    private static final class Crossings {
        private final FactoryFactory factory;
        private final Map<String,Integer> s2i = new HashMap<String,Integer>();
        private final int positions[][];
        private final String ground[];

        public Crossings( String[] crossings) {
            Options opt = new Options();
            positions =  new int[crossings.length][];
            opt.setShortLabels();
            ground = new String[crossings.length];
            for (int i=0;i<crossings.length;i++) {
                ground[i]=crossings[i].substring(0, 1);
                s2i.put(ground[i], i);
            }
            opt.setUniverse(ground);
            if (!Arrays.equals(ground, toStrippedArray(crossings[0]))) {
                throw new IllegalArgumentException("The line at infinity must be in same order as remaining crossings (1)");
            }
            String sortedG[] = new String[ground.length];
            System.arraycopy(ground, 0, sortedG, 0, ground.length);
            Arrays.sort(sortedG);
            for (int i=1;i<crossings.length;i++) {
                String stripped[] = toStrippedArray(crossings[i]);
                if (!stripped[0].equals(ground[i])) {
                    throw new IllegalArgumentException("The line at infinity must be in same order as remaining crossings (2)");
                }
                if (!stripped[1].equals(ground[0])) {
                    throw new IllegalArgumentException("First of each crossing must be the line at infinity");
                }
                Arrays.sort(stripped);
                if (!Arrays.equals(sortedG, stripped)) {
                    throw new IllegalArgumentException("Each line should cross every other line exactly once");
                }
            }
            for (int i=0;i<crossings.length;i++) {
                positions[i] = analyze(crossings[i]);
            }
            factory = new FactoryFactory(opt);
            verify();
        }

        private void verify() {
            for (int i=0;i<ground.length;i++)
                for (int j=i+1;j<ground.length;j++)
                    for (int k=j+1;k<ground.length;k++) {
                        verify(i,j,k);
                    }
            
        }

        private void verify(int i, int j, int k) {
            int sA = sign(i,j,k);
            int sB = sign(j,i,k);
            int sC = sign(k,i,j);
            if (sA != sB || sA != sC ) {
                throw new IllegalArgumentException("Inconsistent crossing information: "+ground[i]+ground[j]+ground[k]);
            }
        }
        

        private int sign(int i, int j, int k) {
            int rslt = positions[i][k] - positions[i][j];
            return rslt<0?-1:rslt>0?1:0;
        }

        private int[] analyze(String crossings) {
            if (crossings.charAt(1)!=':') {
                throw new IllegalArgumentException("Syntax error: expecting ':'");
            }
            int rslt[] = new int[positions.length];
            boolean insideBrackets = false;
            int ix = 1;
            for (int i=2;i<crossings.length();i++) {
                switch (crossings.charAt(i)) {
                case '(':
                    insideBrackets = changeFromTo(insideBrackets,true);
                    break;
                case ')':
                    insideBrackets = changeFromTo(insideBrackets,false);
                    ix++;
                    break;
               default:
                   String k = crossings.substring(i, i+1);
                   Integer code = s2i.get(k);
                   if (code == null) {
                       throw new  IllegalArgumentException("Syntax error: unexpected input: '"+k+"'");
                   }
                   rslt[code] = ix;
                   if (!insideBrackets) {
                       ix++;
                   }
                }
            }
            if (insideBrackets) {
                throw new IllegalArgumentException("Syntax error: unmatched bracket");
                
            }
            return rslt;
        }

        private boolean changeFromTo(boolean oldValue, boolean newValue) {
            if (oldValue == newValue) {
                throw new IllegalArgumentException("Syntax error: bracket nesting");
            }
            return newValue;
        }

        private String[] toStrippedArray(String oneLine) {
            String strippedLine = oneLine.replaceAll("[\\:\\(\\)]", "");
            String rslt[] = new String[strippedLine.length()];
            for (int i=0;i<rslt.length;i++) {
                rslt[i] = strippedLine.substring(i,i+1);
            }
            return rslt;
        }

        public OM om() {
            Label labels[] = new Label[ground.length];
            for (int i=0;i<ground.length;i++) {
                labels[i] = factory.labels().parse(ground[i]);
            }
            return factory.chirotope().construct(Arrays.asList(labels), new Chirotope(){

                @Override
                public int chi(int ... args) {
                    return sign(args[0],args[1],args[2]);
                }

                @Override
                public int rank() {
                    return 3;
                }

                @Override
                public int n() {
                    return ground.length;
                }});
        }
        
    }
    /**
     * This method is for turning a pseudoline configuration into an oriented matroid.
     * Draw a pseudo line configuration and add a line at infinity going 
     * every intersection (parallel lines cross on the line at infinity).
     * Add an origin in a region next to the line at infinity (but inside it).
     * Call the line at infinity A, and then label the other lines
     * going counter-clockwise: B, C, ....
     * the first argument is this information about the line at infinity and is
     * written "A:BC....N"
     * if two or three of these are parallel and cross at infinity e.g. CDE, enclose them
     * in () e.g. "A:B(CDE)F".
     * Then for each of those lines in turn write the line letter and then the lines that it crosses
     * in order, proceeding counterclockwise from the line at infinity. Again if three or more lines
     * meet at a point uses () to show it.
     * @param crossings
     * @return
     */
    public static OM fromCrossings( String ... crossings) { 
        return new Crossings(crossings).om();
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/

package net.sf.oriented.omi;

import net.sf.oriented.matrix.RationalMatrix;

/**
 * Here are some 'interesting' oriented matroids. Of course, all we mean by
 * interesting is interesting to me, and my projects of pseudoline stretching.
 * 
 * @author jeremycarroll
 * 
 */
public class Examples {
    

    public final static OM chapter1 = fromCircuits("{12'4,13'5,23'6,45'6,12'56',13'46,23'4'5}");
    /**
     * See the "Sharpness of Circular Saws (2000)"
     */
    public final static OM circularSaw3 = fromChirotope(7, 3,
    // 0
            "+++++ ++++ +++ ++ +" +
            // 1
                    "---- --- -- +" +
                    // 2
                    "--- -- +" +
                    // 3
                    "++ +" +
                    // 4
                    "+");

    /* Draw a circular saw on squared paper and take the co-ordinates of two
     * points on each line
     *
     */
    public final static OM circularSaw3A = fromEuclideanLines(new int[][][] {
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

    private static OM fromChirotope(int n, int r, String chi) {
        Options opt = new Options();
        String u = createUniverse(n, opt);
        return new FactoryFactory(opt).chirotope().parse(
                "(" + u + ", " + r + ", " + chi.replaceAll(" ", "")
                        + ")");
    }
    
    private static OM fromCircuits(String circuits) {
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

    /**
     * Generate an OM from lines being specified as a pair of points (integer coordinates)
     * 
     * This is very easy to do from a reasonably precise accurate sketch, see {@link #circularSaw3A} for a documented example.
     * @param lines
     * @return
     */
    private static OM fromEuclideanLines(int[][] ... lines) {
        int n = lines.length+1;
        Options opt = new Options();
        createUniverse(n, opt);
        int projective[][] = new int[3][n];
        set(projective,0,1,1,0);
        int ix=1;
        for (int line[][]:lines) {
            // Find perpendicular
            double xx = (line[1][1] - line[0][1]);
            double yy = (line[0][0] - line[1][0]);
            // Normalize
            double length = Math.sqrt(xx*xx+yy*yy);
            xx = xx/length;
            yy = yy/length;
            double dotProductA = xx*line[0][0]+yy*line[0][1];
//            double dotProductB = xx*line[1][0]+yy*line[1][1];
//            System.err.println(".A = "+dotProductA+ " .B = "+dotProductB);
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
        System.err.println("["+ix+"]=("+x+","+y+","+z+")");
        projective[0][ix] = x;
        projective[1][ix] = y;
        projective[2][ix] = z;
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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.google.common.math.IntMath;

import net.sf.oriented.impl.items.LabelFactoryImpl;
import net.sf.oriented.impl.om.MatroidFactoryImpl;
import net.sf.oriented.impl.om.OMChirotopeFactory;
import net.sf.oriented.impl.om.OMFactory;
import net.sf.oriented.impl.om.OMRealizedFactory;
import net.sf.oriented.impl.set.SetOfSignedSetFactory;
import net.sf.oriented.impl.set.SetOfUnsignedSetFactory;
import net.sf.oriented.impl.set.SignedSetFactoryImpl;
import net.sf.oriented.impl.set.UnsignedSetFactory;
import net.sf.oriented.util.combinatorics.CoLexicographic;
import net.sf.oriented.util.matrix.RationalMatrix;

/**
 * This class generates {@link Factory}'s and {@link SetFactory}'s for the
 * interfaces in the API. Depending on the {@link Options} passed to the
 * constructor, the String representations used by all the factories generated
 * will vary. It is, in general, not possible to pass a String representation
 * output by one group of factories, arising from a particular
 * {@link FactoryFactory} to another group of factories, arising from a
 * different {@link FactoryFactory}. It is however possible to pass an item
 * constructed from String's between groups of factories.
 * <p>
 * Various convenience methods are also provided, for some easy ways 
 * to produce an oriented matroid.
 * 
 * @author jeremy
 * 
 */
final public class FactoryFactory {
	private static final class Crossings {
        private final FactoryFactory factory;
        private final Map<String,Integer> s2i = new HashMap<String,Integer>();
        private final int positions[][];
        private final String ground[];
    
        public Crossings( String[] crossings) {
            positions =  new int[crossings.length][];
            ground = new String[crossings.length];
            Options opt = extractUniverse(crossings,ground);
            if (!Arrays.equals(ground, toStrippedArray(opt.getSingleChar(),crossings[0]))) {
                throw new IllegalArgumentException("The line at infinity must be in same order as remaining crossings (1): "+crossings[0]);
            }
            String sortedG[] = new String[ground.length];
            System.arraycopy(ground, 0, sortedG, 0, ground.length);
            Arrays.sort(sortedG);
            for (int i=1;i<crossings.length;i++) {
                String stripped[] = toStrippedArray(opt.getSingleChar(),crossings[i]);
                if (!stripped[0].equals(ground[i])) {
                    throw new IllegalArgumentException("The line at infinity must be in same order as remaining crossings (2): "+crossings[i]);
                }
                if (!stripped[1].equals(ground[0])) {
                    throw new IllegalArgumentException("First of each crossing must be the line at infinity: "+crossings[i]);
                }
                Arrays.sort(stripped);
                if (!Arrays.equals(sortedG, stripped)) {
                    throw new IllegalArgumentException("Each line should cross every other line exactly once: "+crossings[i]);
                }
            }
            for (int i=0;i<crossings.length;i++) {
                positions[i] = analyze(opt.getSingleChar(),crossings[i]);
            }
            factory = new FactoryFactory(opt);
            verify();
        }


        private Options extractUniverse(String[] crossings,String[] g) {
            boolean oneChar = true;
            for (int i=0;i<crossings.length;i++) {
                int colon = crossings[i].indexOf(':');
                oneChar = oneChar && (colon == 1);
                g[i]=crossings[i].substring(0, colon);
                s2i.put(g[i], i);
            }
            Options opt = new Options();
            if (oneChar) {
                opt.setShortLabels();
            } else {
                opt.setLongLabels();
            }
            opt.setUniverse(g);
            return opt;
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
    
        private static enum CToken {
            Colon,
            Comma,
            Line,
            LParen,
            RParen,
            End;
        }
        private abstract class CLexer {
            int pos = 0;
            int lastLine = -1;
            protected final String input;
            CLexer(String in) {
                input = in;
            }
            CToken next() {
                if (pos == input.length()) {
                    return CToken.End;
                }
                switch (input.charAt(pos++)) {
                case '(':
                    return CToken.LParen;
                case ')':
                    return CToken.RParen;
                case ',':
                    return CToken.Comma;
                case ':':
                    return CToken.Colon;
                default:
                    pos --;
                    lastLine = checkNextWord();
                    return CToken.Line;
                }
            }
            public int checkNextWord() {
                String token = findNextWord();
                Integer code = s2i.get(token);
                if (code == null) {
                    throw new  IllegalArgumentException("Syntax error: unexpected input: '"+token+"'");
                }
                pos += token.length();
                return code;
            }
            abstract public String findNextWord();
        }
        private class MultiCharLexer extends CLexer {

            MultiCharLexer(String in) {
                super(in);
            }
            @Override
            public String findNextWord() {
                return input.substring(pos).replaceFirst("($|[(),:].*$)","");
            }
            
        }
        private class SingleCharLexer extends CLexer {

            SingleCharLexer(String in) {
                super(in);
            }

            @Override
            public String findNextWord() {
                return input.substring(pos,pos+1);
            }
            
        }
        private int[] analyze(boolean singleChar, String crossings) {
            CLexer lexer = singleChar ? new SingleCharLexer(crossings):new MultiCharLexer(crossings);
//                crossings = crossings.replaceAll("([^(:])(?!([:)]|$))","$1,");
            if (lexer.next() != CToken.Line || lexer.next() != CToken.Colon ) {
                throw new IllegalArgumentException("Syntax error: expecting ':'");
            }
            int rslt[] = new int[positions.length];
            boolean insideBrackets = false;
            int ix = 1;
outer:
            while (true ) {
                switch (lexer.next()) {
                case LParen:
                    insideBrackets = changeFromTo(insideBrackets,true);
                    break;
                case RParen:
                    insideBrackets = changeFromTo(insideBrackets,false);
                    ix++;
                    break;
                case Colon:
                    throw new  IllegalArgumentException("Syntax error: unexpected input: ':'");
                case Comma:
                    break;
                case Line:
                   rslt[lexer.lastLine] = ix;
                   if (!insideBrackets) {
                       ix++;
                   }
                   break;
                case End:
                    break outer;
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

        private String[] toStrippedArray(boolean singleChar, String oneLine) {
            return singleChar?toStrippedArrayOneChar(oneLine):toStrippedArrayMultiChar(oneLine);
        }
        private String[] toStrippedArrayMultiChar(String oneLine) {
            String strippedLine = oneLine.replaceAll("[\\(\\)]", "").replace(':', ',');
            String justCommas = strippedLine.replaceAll("[^,]","");
            String rslt[] = new String[justCommas.length()+1];
            int pos = 0;
            for (int i=0;i<rslt.length-1;i++) {
                int nextComma = strippedLine.indexOf(',',pos);
                rslt[i] = strippedLine.substring(pos,nextComma);
                pos = nextComma + 1;
            }
            rslt[rslt.length-1] = strippedLine.substring(pos);
            return rslt;
        }


        private String[] toStrippedArrayOneChar(String oneLine) {
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

    private final LabelFactoryImpl label;
	private SetOfSignedSetFactory setsOfSignedSet;
	private SetOfSignedSetFactory symmetricSetsOfSignedSet;
	private SetOfUnsignedSetFactory setsOfUnsignedSet;
	private UnsignedSetFactory unsignedSets;
	private SignedSetFactoryImpl signedSets;
	private OMSFactory circuits, vectors, maxVectors;
	private ChirotopeFactory chirotope;
	private Factory<MatroidAsSet> bases;
	private Factory<MatroidAsSet> unsignedCircuits;
	private RealizedFactory realized;

	private final Options options;
    private final static double epsilon = 0.00000000001d;

	private void init() {
		if (setsOfUnsignedSet != null)
			return;

		unsignedSets = new UnsignedSetFactory(label);
		setsOfSignedSet = new SetOfSignedSetFactory(unsignedSets, false);
		symmetricSetsOfSignedSet = new SetOfSignedSetFactory(setsOfSignedSet,
				true);
		setsOfUnsignedSet = symmetricSetsOfSignedSet.setOfUnsignedSetFactory();

		signedSets = symmetricSetsOfSignedSet.itemFactory();

		circuits = OMFactory.circuits(this);
		vectors = OMFactory.vectors(this);
		maxVectors = OMFactory.maxVectors(this);
		chirotope = new OMChirotopeFactory(this);

		bases = MatroidFactoryImpl.bases(this);
		unsignedCircuits = MatroidFactoryImpl.circuits(this);
		realized = new OMRealizedFactory(this);
	}

	/**
	 * A new {@link FactoryFactory} with default options.
	 * 
	 */
	public FactoryFactory() {
		this(new Options());
	}

	/**
	 * A new {@link FactoryFactory} with given {@link Options}. These control
	 * the String representations used.
	 * 
	 */
	public FactoryFactory(Options opt) {
		label = opt.getLabelFactory();
		init();
		options = opt;
	}

	/**
	 * A factory for labels.
	 * 
	 * @return A factory for labels.
	 */
	public LabelFactory labels() {
		return label;
	}

	/**
	 * A factory for matroids from bases.
	 * 
	 * @return A factory for matroids.
	 */
	public Factory<MatroidAsSet> bases() {
		return bases;
	}

	/**
	 * A factory for matroids from circuits.
	 * 
	 * @return A factory for matroids.
	 */
	public Factory<MatroidAsSet> unsignedCircuits() {
		return unsignedCircuits;
	}

	/**
	 * A factory for oriented matroids which uses the circuit representation.
	 * 
	 * @return A factory for oriented matroids using circuits.
	 */
	public OMSFactory circuits() {
		return circuits;
	}

	/**
	 * A factory for oriented matroids which uses the chirotope representation.
	 * 
	 * @return A factory for oriented matroids using chirotopes.
	 */
	public ChirotopeFactory chirotope() {
		return chirotope;
	}

	/**
	 * A factory for oriented matroids which uses the vector representation.
	 * 
	 * @return A factory for oriented matroids using vectors.
	 */
	public OMSFactory vectors() {
		return vectors;
	}

	/**
	 * A factory for oriented matroids which uses the maximum vector (co-tope)
	 * representation.
	 * 
	 * @return A factory for oriented matroids using maximum vectors.
	 */
	public OMSFactory maxVectors() {
		return maxVectors;
	}

	/**
	 * A factory for oriented matroids which uses a realization of the OM as the
	 * representation.
	 */
	public RealizedFactory realized() {
		return realized;
	}

	/**
	 * A factory for signed sets.
	 * 
	 * @return A factory for signed sets.
	 */
	public SignedSetFactory signedSets() {
		return signedSets;
	}

	/**
	 * A factory for unsigned sets.
	 * 
	 * @return A factory for unsigned sets.
	 */
	public SetFactory<Label, UnsignedSet> unsignedSets() {
		return unsignedSets;
	}

	/**
	 * A factory for sets of unsigned sets.
	 * 
	 * @return A factory for sets of unsigned sets.
	 */
	public SetFactory<UnsignedSet, SetOfUnsignedSet> setsOfUnsignedSet() {
		return setsOfUnsignedSet;
	}

	/**
	 * A factory for sets of signed sets.
	 * 
	 * @return A factory for sets of signed sets.
	 */
	public SetFactory<SignedSet, SetOfSignedSet> setsOfSignedSet() {
		return setsOfSignedSet;
	}

	/**
	 * A factory for symmetric sets of signed sets. The String representation
	 * may omit opposites.
	 * 
	 * @return A factory for symmetric sets of signed sets.
	 */
	public SetFactory<SignedSet, SetOfSignedSet> symmetricSetsOfSignedSet() {
		return symmetricSetsOfSignedSet;
	}

	/**
	 * The options used by this factory.
	 * @return The options used by this factory.
	 */
	public Options options() {
		return options;
	}

	/**
	 * Gives an oriented matroid corresponding to a chirotope in
	 * colexicographic form.
	 * @param n  The number of elements on which the oriented matroid is defined.
	 * @param r  The rank
	 * @param chi The colexicographic chirotope
	 * @return An oriented matroid
     * @see ChirotopeFactory#fromCoLexicographic(int, String)
     */
    public static OM fromCoLexicographic(int n, int r, String chi) {
        Options opt = new Options();
        String u = createUniverse(n, opt);
        return new FactoryFactory(opt).chirotope().parse(
                "(" + u + ", " + r + ", " + chi.replaceAll(" ", "")
                        + ")");
    }

    /**
     * Gives an oriented matroid corresponding to a chirotope in
     * lexicographic form.
     * <p>
     * NB: this library uses colexicographic ordering throughout
     * and lexicographic ordering is only available here, and at the two other listed methods.
     * @param n  The number of elements on which the oriented matroid is defined.
     * @param r  The rank
     * @param chi The lexicographic chirotope
     * @return An oriented matroid
     * @see ChirotopeFactory#fromLexicographic(int, String)
     * @see FactoryFactory#fromLexicographic(int, int, String)
     * @see OMasChirotope#toLexicographicString()
     */
    public static OM fromLexicographic(int n, int r, String chi) {
        return fromCoLexicographic(n,r,CoLexicographic.fromLexicographic(n, r, chi));
    }
    /**
     * Given a string representation of a set of circuits
     * create an oriented matroid.
     * @param circuits A string representation of a set of circuits
     * @return A oriented matroid.
     */
    public static OM fromCircuits(String circuits) {
            Options options = new Options();
            options.setShortLabels();
            return new FactoryFactory(options).circuits().parse(circuits);
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
     * Generate an OM from lines being specified as a pair of points (integer coordinates).
     * <p>
     * This is very easy to do from a reasonably precise accurate sketch, see {@link Examples#circularsaw3()} for a documented example.
     * @param lines a list of lines each specified by two points by x, y coordinates.
     * @return An oriented matroid.
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
     * @param crossings Information about the order in which the pseudolines cross each other
     * @return An oriented matroid
     */
    public static OM fromCrossings( String ... crossings) { 
        return new Crossings(crossings).om();
    }

    /**
     * This method is for turning a simple matrix of integers into 
     * an oriented matroid over the columns.
     * Each array in the array of arrays is a row.
     * The number of arrays is the rank.
     * @param matrix
     * @return  An oriented matroid
     */
    public static OM fromMatrix(int[][] matrix) {
        Options opt = new Options();
        return new FactoryFactory(opt).realized().construct(new RationalMatrix(matrix));
    }
    /**
     * This method is for turning a simple matrix of doubles into 
     * an oriented matroid over the columns.
     * Each array in the array of arrays is a row.
     * The number of arrays is the rank.
     * The <code>threshold</code> parameter is to deal
     * with rounding errors. Any determinant which is close to zero
     * is treated as zero, with the largest close-to-zero determinant
     * being at least a factor of threshold smaller than the smallest non-zero determinant.
     * @param matrix
     * @param threshold Should be 1000 ?
     * @return An oriented matroid
     */
    public static OM fromMatrix(double threshold, double[][] matrix) {
        int rank = matrix.length;
        int n = matrix[0].length;
        
        RealMatrix data = MatrixUtils.createRealMatrix(matrix);
        double determinants[] = new double[IntMath.binomial(n, rank)];
        int rowIndexes[] = new int[rank];
        for (int i=0;i<rank;i++) {
            rowIndexes[i] = i;
        }
        int j = 0;
        for (int ix[]:new CoLexicographic(n, rank)) {
            determinants[j++] = new LUDecomposition(data
                    .getSubMatrix(rowIndexes, ix)
                    ).getDeterminant();
        }
        double epsilon = guessEpsilon(threshold, determinants);
        
        char signs[] = new char[determinants.length];
        char plusMinus[] = new char[]{ '-', '0', '+' };
        for (int i=0;i<determinants.length;i++) {
            signs[i] = plusMinus[1+sign(epsilon, determinants[i])];
        }
        return fromCoLexicographic(n, rank, new String(signs));
    }

    private static int sign(double epsilon, double d) {
        if (d<epsilon) {
            if (d>-epsilon) {
                return 0;
            } else {
                return -1;
            }
        } else {
            return 1;
        }
    }

    private static double guessEpsilon(double threshold, double[] determinants) {
        // now compute epsilon
        double absDeterminants[] = new double[determinants.length];
        for (int i=0;i<determinants.length;i++) {
            absDeterminants[i] = Math.abs(determinants[i]);
        }
        Arrays.sort(absDeterminants);
        
        for (int i=1;i<absDeterminants.length;i++) {
            if (absDeterminants[i-1]==0) {
                continue;
            }
            if (absDeterminants[i-1]*threshold > absDeterminants[i]) {
                continue;
            }
            return (absDeterminants[i-1]+absDeterminants[i])/2.0;
        }
        return Double.MIN_NORMAL;
    }

}
/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * 
 * 
 * 
 * 
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

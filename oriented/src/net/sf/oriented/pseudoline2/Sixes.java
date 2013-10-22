/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.LabelFactory;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.pseudoline.CoLoopCannotBeDrawnException;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;

/**
 * Find all angle constraining pseudoline diagrams fo six lines
 * @author jeremycarroll
 *
 */
public class Sixes {
    
    private static final class SixC {

        private final int triangles[][];
        
        private int signs[];
        
        SixC(SixB sx) {
            triangles = sx.triangles;
            signs = sx.signBits();
        }

        public Six toSix(int[] index6) {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public int hashCode() {
            return (Arrays.hashCode(signs) << 1 ) ^ (Arrays.deepHashCode(triangles));
        }
        
        @Override
        public boolean equals(Object o) {
            if (! (o instanceof SixC)) {
                return false;
            }
            SixC other = (SixC)o;
            return Arrays.deepEquals(triangles, other.triangles)
                    && Arrays.equals(signs, other.signs);
        }

    }
    private static class SixB {

        private final OMasChirotope om;
        private final long bits;
        private final String name;
        private final int triangles[][];

        public SixB(SixA six) {
            EuclideanPseudoLines epl = new EuclideanPseudoLines(six.om,"0");
            om = epl.getEquivalentOM().getChirotope();
            bits = toLong(om,1,2,3,4,5,6);
            name = six.name;
            triangles = new int[six.triangles.length][];
            for (int i=0;i<triangles.length;i++) {
                triangles[i] = om.asInt(six.triangles[i]);
                Arrays.sort(triangles[i]);
            }
            Arrays.sort(triangles,Ints.lexicographicalComparator());
        }

        int[] signBits() {
            int firstSign = 1 | (firstSign(1)<<1) | (firstSign(2)<<2) | (firstSign(3)<<3);
            int secondSign = 15 & ~firstSign;
            boolean firstOK = checkSign(firstSign);
            boolean secondOK = checkSign(secondSign);
            if (firstOK) {
                if (secondOK) {
                    return new int[]{firstSign, secondSign};
                }
                return new int[]{firstSign};
            } else {
                if (!secondOK) {
                    throw new IllegalStateException("Logic Error");
                }
                return new int[]{secondSign};
            }
        }

        private boolean checkSign(int firstSign) {
            for (int i=0;i<4;i++) {
                int chi = om.chi(triangles[i]);
                if (chi == 0) {
                    continue;
                }
                if ((chi == 1) == ((firstSign & (1<<i))!=0) ) {
                    continue;
                }
                return false;
            }
            return true;
        }

        private int firstSign(int i) {
            for (int k=0;k<triangles[i].length; k++) {
                int x = triangles[i][k];
                for (int j = 0; j< triangles[0].length; j++) {
                    int y = triangles[0][j];
                    if (x == y) {
                        boolean yIsPlus = j%2 == 0;
                        boolean xWouldBePlus = k%2 == 0;
                        return yIsPlus == xWouldBePlus ? 0 : 1;
                    }
                }
            }
            throw new IllegalArgumentException();
        }
    }
    public static Sixes get() {
        return theInstance;
    }
    
    private interface FoundMatch {
        void found(int matchId, int ...index6);
    };
    public int matches(EuclideanPseudoLines epl) {
        OMasChirotope om = epl.getEquivalentOM().getChirotope();
        final int count[] = new int[1];
        matches(om, new FoundMatch() {
            @Override
            public void found(int matchId, int ... index6) {
                count[0] ++;
            }
        });
        return count[0];
    }

    private void matches(OMasChirotope om, FoundMatch act) {
        int n = om.elements().length;
        int ix[] = new int[6];
        matches(om,0,n,ix, act);
    }
    
    private void matches(OMasChirotope om, int i, int n, int[] ix, FoundMatch act) {
        if (i==6) {
            long key = toLong(om,ix);
            int ixx = Arrays.binarySearch(all, key);
            if (ixx>=0) {
                act.found(ixx, ix);
            }
        } else {
            int start = i==0  ? 1:( ix[i-1] + 1);
            for (ix[i]=start;ix[i]<n;ix[i]++) {
                matches(om,i+1,n,ix, act);
            }
        }
    }

    private static class SixA {
        final OMasChirotope om;
        final Label triangles[][];
        final String name;
        SixA(String name, OMasChirotope om, String ... tri) {
            this.om = om;
            LabelFactory f = om.ffactory().labels();
            triangles = new Label[4][];
            for (int i=0;i<4;i++) {
                triangles[i] = new Label[]{ f.parse(tri[i].substring(0, 1)), 
                                            f.parse(tri[i].substring(1, 2)), 
                                            f.parse(tri[i].substring(2, 3)) };
            }
            this.name = name;
        }
        public SixA(String name, SixA seed, int i) {
            om = seed.om.reorient(seed.om.elements()[i]).getChirotope();
            triangles = seed.triangles;
            this.name = name;
        }
        public SixA(String name, OMasChirotope om, SixA six) {
            this.om = om;
            this.triangles = six.triangles;
            this.name = name;
        }
        public SixA zero(int m) {
            OMasChirotope om = this.om;
           for (int i=0;i<4;i++) {
               if (((1<<i)&m)!=0) {
                   om = om.mutate(0, triangles[i]);
               }
           }
           return new SixA(name+".z"+m, om, this);
        }
    }
    private final OMasChirotope seed2 = FactoryFactory.fromCrossings("0:ABCDEF",
            "A:0ECDBF",
            "B:0CDEAF",
            "C:0BEAFD",
            "D:0BEAFC",
            "E:0BDCAF",
            "F:0ABDCE").getChirotope();
    private final SixA seeds[] = new SixA[]{
      new SixA("j",seed2.ffactory().chirotope().remake(Examples.circularsaw3().getChirotope()),
            "AEF", "CDE", "BDF",  "ABC" ),
      new SixA("a", seed2, "ACE", "CDF", "BDE", "ABF" ),
      new SixA("b", seed2.flip(0,2,3), "ACE", "CDF", "BDE", "ABF" ),
      new SixA("d", seed2.flip(0,5,6), "ACE", "CDF", "BDE", "ABF" ),
      new SixA("f", seed2.flip(0,2,3).flip(0,5,6), "ACE", "CDF", "BDE", "ABF" ),
    };
    private List<SixB> allB = new ArrayList<SixB>();
    private Map<Long,Integer> lg = new HashMap<Long,Integer>();
    private long all[];
    private SixC[] allSixC;
    private Sixes() {
        List<SixA> allA = new ArrayList<SixA>();
        for (SixA seed:seeds) {
            for (int i=0; i<12; i++ ) {
                seed = new SixA(seed.name+i,seed,i%6);
                allA.add(seed);
            }
        }
        // add each of the 16 possibilities of zeros 
        for (int i=allA.size()-1;i>=0;i--) {
            SixA seed = allA.get(i);
            for (int m=1;m<16;m++) {
                allA.add(seed.zero(m));
            }
        }
        long chi[] = new long[allA.size()];
        int i=0;
        for (SixA six:allA) {
            SixB sb = new SixB(six);
            chi[i++] = sb.bits;
            allB.add(sb);
        }
        Arrays.sort(chi);
        int counts[] = new int[64];
        int run = 0;
        long last = 0;
        for (long x:chi) {
            if (x != last) {
                lg.put(last,run);
                counts[run]++;
                run=0;
            }
            last = x;
            run++;
        }
        lg.put(last,run);
        counts[run]++;
        int total = 0;
        for (int j=1;j<counts.length;j++) {
           if (counts[j]!=0) {
               System.err.println(j+"\t"+counts[j]);
               total += counts[j];
           }
        }
        System.err.println("=\t"+total);
        all = new long[total];
        i = 0;
        last = 0;
        for (long x:chi) {
            if (x != last) {
                all[i++] = x;
            }
            last = x;
        }
        allSixC = new SixC[total];
        for (SixB sb:allB) {
            int ix = Arrays.binarySearch(all, sb.bits);
            if (allSixC[ix]==null) {
                allSixC[ix] = new SixC(sb);
            } else {
                if (!allSixC[ix].equals(new SixC(sb))) {
                    throw new IllegalArgumentException("SixC issue");
                }
            }
            
            
        }
        
    }
    
    public static long toLong(OMasChirotope om, Label ...labels ) {
        Preconditions.checkArgument(labels.length==6);
        return toLong(om,om.asInt(labels));
    }
    
    private static int sixCthree[] = new int[20];
    static {
        int j = 0;
        for (int i=0;i<(1<<6);i++) {
            if (Integer.bitCount(i)==3) {
                sixCthree[j++] = i;
            }
        }
    }
    private static int[] to3bits(int m) {
        for (int i=0;i<32;i++) {
            if ((m & (1<<i))!=0) {
                for (int j=i+1;j<32;j++) {
                    if ((m & (1<<j))!=0) {
                        for (int k=j+1;k<32;k++) {
                            if ((m & (1<<k))!=0) {
                                return new int[]{i,j,k};
                            }
                        }
                    }
                }
            }
        }
        throw new IllegalArgumentException(m+" does not have 3 bits");
    }
    public static long toLong(OMasChirotope om, int ... ix) {
        Preconditions.checkArgument(ix.length==6);
        int i=0;
        long rslt = 0;
        int sign = om.chi(0,1,6);
        switch (sign) {
        case 1:
            break;
        case 0:
            throw new Error();
        case -1:
            System.err.println(-1);
            break;
        }
        for (int m:sixCthree) {
            /**
             * Semantics - two bits per item in chirotope:
             * 
             * 00 - 0 01 - 1 10 - undefined 11 - -1
             */
            int[] b0t5 = to3bits(m);
            long bits = (sign*om.chi(ix[b0t5[0]],ix[b0t5[1]],ix[b0t5[2]]))&3;
            rslt |= bits << i;
            i += 2;
        }
        return rslt;
    }

    public static void main(String a[]) throws IOException, AxiomViolation, CoLoopCannotBeDrawnException {
        int i=0;
        Sixes sixes = Sixes.get();
//        for (SixB s:sixes.all) {
//           // if (sixes.lg.get(s.bits) == 1) {
//                EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(s.om,"0");
//                PseudoLineDrawing euclid = pseudoLines.asDrawing();
//                ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
//
//                String fName = "/users/jeremycarroll/tmp/six-" +Long.toHexString(s.bits)+"=" + sixes.lg.get(s.bits) + "=" + i++ + "="+ s.name +".jpeg";
//                ImageOutputStream imageOutput = ImageIO.createImageOutputStream(new File(fName));
//                iw.setOutput(imageOutput);
//                iw.write(euclid.image());
//                euclid.verify();
//                imageOutput.close();
//                iw.dispose();
//          //  }
//        }
    }

    private static Sixes theInstance = new Sixes();
    public Iterable<Six> analyze(OM om) {
        final List<Six> sixes = new ArrayList<Six>();
        matches(om.getChirotope(), new FoundMatch(){
            @Override
            public void found(int matchId, int ... index6) {
                sixes.add(allSixC[matchId].toSix(index6));
            }});
        return sixes;
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

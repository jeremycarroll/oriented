/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import net.sf.oriented.pseudoline2.Difficulty;

public class MinimalSubsetFactory {

    /**
     * The basic algorithm here is from
     * <a href="http://stackoverflow.com/a/8996897/2276263">Aaron McDaid's comment</a>,
     * modified by first sorting the BitSets in size of cardinality, and then
     * relying on only half of McDaid's algorithm (which covers the more general
     * case of augmenting a minimal set of sets).
     * @author jeremycarroll
     *
     */
    private static final class TransposeMinimalSubsets implements MinimalSubsets {
        private static final class TSEntry implements Comparable<TSEntry> {
            private static int ID_COUNTER = 0;
            private final int id = ID_COUNTER++;
            final int bitsets[];
            int ix;
            TSEntry(int bs[]) {
                bitsets = bs;
                ix = 0;
            }
            @Override
            public int compareTo(TSEntry o) {
                int r = peek() - o.peek();
                return r!=0?r:(id - o.id);
            }
            private int peek() {
                return bitsets[ix];
            }
            public boolean advance(int i) {
                int pos = Arrays.binarySearch(bitsets, ix, bitsets.length, i);
                if (pos < 0) {
                    ix = -1 - pos;
                } else {
                    ix = pos;
                }
                return ix < bitsets.length;
            }
            
        }

        int cross[][];
        @Override
        public List<BitSet> minimal(Collection<BitSet> full) {
            BitSet sorted[] = new BitSet[full.size()];
            int bad = 0;
            full.toArray(sorted);
            Arrays.sort(sorted, new Comparator<BitSet>(){
                @Override
                public int compare(BitSet o1, BitSet o2) {
                    return o1.cardinality() - o2.cardinality();
                }});
            final int maxCardinality = sorted[sorted.length-1].cardinality();
            int firstMax = -(1 + Arrays.binarySearch(sorted, null, new Comparator<BitSet>(){

                @Override
                public int compare(BitSet o1, BitSet o2) {
                    if (o1 == null) {
                        if (o2.cardinality() == maxCardinality) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else if (o2 == null) {
                        if (o1.cardinality() == maxCardinality) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else {
                        throw new IllegalArgumentException();
                    }
                }}));
            int max = max(full);
            int counts[] = new int[max];
            for (BitSet bs:full) {
                for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
                    counts[i]++;
                }
            }
            cross = new int[max][];
            for (int i=0;i<cross.length;i++) {
                cross[i] = new int[counts[i]];
            }
            Arrays.fill(counts,0);
            for (int j=0;j<sorted.length;j++) {
                BitSet bs = sorted[j];
                for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
                    cross[i][counts[i]++] = j;
                }
            }
            outer:
            for (int ix=0;ix<firstMax;ix++) {
                BitSet bs = sorted[ix];
                if (bs == null) {
                    continue;
                }
                TreeSet<TSEntry> ts = new TreeSet<TSEntry>();
                for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i+1)) {
                    final TSEntry entry = new TSEntry(cross[i]);
                    if (!entry.advance(ix+1)) {
                        continue outer;
                    }
                    ts.add(entry);
                }
                int lastBS = ts.last().peek();
                while (true) {
                    final TSEntry first = ts.first();
                    int firstBS = first.peek();
                    int next;
                    if (firstBS == lastBS) {
                        // subset
                        bad++;
                        sorted[firstBS] = null;
                        next = firstBS + 1;
                    } else {
                        next = lastBS;
                    }
                    ts.remove(first);
                    int nextBS;
                    do {
                        if (!first.advance(next)) {
                            continue outer;
                        }
                        nextBS = first.peek();
                        next = nextBS + 1;
                    } while (sorted[nextBS]==null);
                    ts.add(first);
                    if (nextBS > lastBS) {
                        lastBS = nextBS;
                    }
                }
            }
            BitSet rslt[] = new BitSet[sorted.length - bad];
            int i = 0;
            for (BitSet b:sorted) {
                if (b!=null) {
                    rslt[i++] = b;
                }
            }
            return Arrays.asList(rslt);
        }
        
    }
    private static final class NaiveMinimalSubsets implements MinimalSubsets {

        int max = 0;

        private class SetAndComplement {

            SetAndComplement(BitSet b) {
                bits = b;
                missingBits = (BitSet) bits.clone();
                missingBits.flip(0,max);
            }
            boolean nonMinimal;
            private final BitSet bits;
            private final BitSet missingBits;
            
        }
        @Override
        public List<BitSet> minimal(Collection<BitSet> full) {
            max = max(full);
            SetAndComplement r[] = new SetAndComplement[full.size()];
            int i = 0;
            for (BitSet b:full) {
                r[i++] = new SetAndComplement(b);
            }
          
          int sz = r.length;
          int bad = 0;
          Arrays.sort(r, new Comparator<SetAndComplement>(){

              @Override
              public int compare(SetAndComplement o1, SetAndComplement o2) {
                  return o1.bits.cardinality() - o2.bits.cardinality();
              }});
          for (i = 0;i<sz-1; i++ ) {
              SetAndComplement di = r[i];
              if (di.nonMinimal) {
                  continue;
              }
              for (int j=i+1;j<sz;j++) {
                  SetAndComplement dj = r[j];
                  if (dj.nonMinimal) {
                      continue;
                  }
                  if (!di.bits.intersects(dj.missingBits)) {
                      dj.nonMinimal = true;
                      bad++;
                  }
              }
              
          }
                     
          BitSet rr[] = new BitSet[sz-bad];
          i = 0;
          for (SetAndComplement sc:r) {
              if (!sc.nonMinimal) {
                  rr[i++] = sc.bits;
              }
          }
          return Arrays.asList(rr);
        }
    }
    public static MinimalSubsets naive() {
        
        return new NaiveMinimalSubsets();
    }
    
    /**
     * Use an implementation based on:
     * <a href="http://stackoverflow.com/a/8996897/2276263">Aaron McDaid's comment</a>:
     * @return
     */
    public static MinimalSubsets mcdaid() {
        
        return new TransposeMinimalSubsets();
    }

    private static int max(Collection<BitSet> full) {
        int m = 0;
        for (BitSet b:full) {
            int l =  b.length();
            if (l>m) {
                m = l;
            }
        }
        return m;
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

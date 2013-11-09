/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;


final class NaiveMinimalSubsets implements MinimalSubsets {

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
        max = MinimalSubsetFactory.max(full);
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

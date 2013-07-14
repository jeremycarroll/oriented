/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi.impl.set.bits32;

import net.sf.oriented.combinatorics.Permutation;


public class SmartPermutation extends Permutation {
    
    private final int map[][] = new int[4][256];
    
    public SmartPermutation(int ... perm) {
        super(perm);
        for (int i=0;i<4;i++) {
            for (int j=0;j<256;j++) {
                map[i][j] = naiveMap(j<<(i*8),i*8,(i+1)*8);
            }
        }
    }
    
    @Override
    public int get(int k) {
        if (k>=n()) {
            return k;
        }
        return super.get(k);
    }
    private int naiveMap(int bits, int leastBit, int biggestBit) {
        int rslt = 0;
        for (int b=leastBit;b<biggestBit;b++) {
            if ((bits&(1<<b))!=0) {
                rslt |= (1<<get(b));
            }
        }
        return rslt;
    }

    int mapAll(int m) {
        return map[0][m&255]|map[1][(m>>8)&255]|map[2][(m>>16)&255]|map[3][(m>>24)&255];
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

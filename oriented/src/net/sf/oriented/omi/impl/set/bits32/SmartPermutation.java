/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi.impl.set.bits32;

import net.sf.oriented.combinatorics.Permutation;


public class SmartPermutation extends Permutation {
    
    private final int map[][] = new int[8][16];
    
    public SmartPermutation(int ... perm) {
        super(perm);
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
        if (leastBit >= n()) {
            return bits;
        }
        for (int b=leastBit;b<biggestBit;b++) {
            if ((bits&(1<<b))!=0) {
                rslt |= (1<<get(b));
            }
        }
        return rslt;
    }

    int mapAll(int m) {
        int rslt = mapAll16(m&((1<<16)-1),0)|mapAll16(m>>16,1);
        return rslt;
    }

    private int mapAll16(int bits, int offset) {
        if (bits==0) return 0;
        return mapAll8(bits&((1<<8)-1),offset*2)|mapAll8(bits>>8,offset*2+1);
    }
    private int mapAll8(int bits, int offset) {
        if (bits==0) return 0;
        return mapAll4(bits&((1<<4)-1),offset*2)|mapAll4(bits>>4,offset*2+1);
    }
    private int mapAll4(int bits, int offset) {
        if (bits==0) return 0;
        if (map[offset][bits]==0) {
            map[offset][bits] = naiveMap(bits<<(offset*4),(offset*4),((1+offset)*4));
        }
        return map[offset][bits];
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

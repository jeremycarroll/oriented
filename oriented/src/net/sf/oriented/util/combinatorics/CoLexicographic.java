/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.util.combinatorics;


import java.util.AbstractCollection;
import java.util.Iterator;

import com.google.common.math.IntMath;

public class CoLexicographic extends AbstractCollection<int[]> {
	final int n, r, sz;

	public CoLexicographic(int n, int r) {
		this.n = n;
		this.r = r;
		sz = IntMath.binomial(n, r);
	}

	@Override
	public Iterator<int[]> iterator() {
		return new Iterator<int[]>() {
			int cnt = 0;


			int rslt[];

			@Override
			public boolean hasNext() {
				return cnt < sz;
			}

			@Override
			public int[] next() {
				cnt++;
				if (rslt == null) {
					rslt = new int[r];
					for (int i = 0; i < r; i++) {
						rslt[i] = i;
					}
				} else {
				    int pos = 0;
				    while (pos != r-1 && rslt[pos]+1 == rslt[pos+1] ) {
				        pos ++;
				    }
				    rslt[pos]++;
				    for (int p=0;p<pos;p++) {
				        rslt[p] = p;
				    }
				}
				return rslt.clone();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public int size() {
		return sz;
	}
	
    public static int index(int ... ix) {
        int r = ix.length;
        int result = 0;
        for (int i=0;i<r;i++) {
            if (i+1<=ix[i]) {
               result += IntMath.binomial(ix[i], i+1);
            }
        }
        return result;
        
//        0: 0: 0 1 2 
//        1: 0: 0 1 3 
//        2: 0: 0 2 3 
//        3: 0: 1 2 3 
//        4: 0: 0 1 4 
//        5: 0: 0 2 4 
//        6: 0: 1 2 4 
//        7: 0: 0 3 4 
//        8: 0: 1 3 4 
//        9: 0: 2 3 4 
//        10: 0: 0 1 5 

 // 6 => 20 6.5.4/3.2.1
 // 5 => 10 5.4.3/3.2.1
 // 7 => 35 7.6.5/3.2.1
 
        
//    	if (i[p] < 0 || i[p] >= n)
//    		throw new IllegalArgumentException("value out of range.");
//    	for (int q = 1; q < r; q++)
//    		if (i[p + q] > i[p]) {
//    			i[p + q] -= (i[p] + 1);
//    		} else
//    			throw new IllegalArgumentException("sequence not monotonic");
//    	int rslt = 0;
//    	int nextN = n - 1 - i[p];
//    	while (i[p] > 0) {
//    		rslt += IntMath.binomial(n - i[p], r - 1);
//    		i[p]--;
//    	}
//    	return rslt + pos5(nextN, r - 1, p + 1, i);
    }

    public static void main(String args[]) {
	    CoLexicographic lex = new CoLexicographic(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
	    int i = 0;
	    for ( int[] vals : lex ) {
	        System.out.print(i++ +": "+index(vals)+": ");
	        for (int v : vals) {
	            System.out.print(Integer.toHexString(v)+" ");
	        }
	        System.out.println();
	    }
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

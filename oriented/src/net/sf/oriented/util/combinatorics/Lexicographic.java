/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.util.combinatorics;


import java.util.AbstractCollection;
import java.util.Iterator;

import com.google.common.math.IntMath;

public class Lexicographic extends AbstractCollection<int[]> {
	final int n, r, sz;

	public Lexicographic(int n, int r) {
		this.n = n;
		this.r = r;
		sz = IntMath.binomial(n, r);
	}

	@Override
	public Iterator<int[]> iterator() {
		return new Iterator<int[]>() {
			int cnt = 0;

			int pos = r - 1;

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
					while (rslt[pos] == n - (r - pos)) {
						pos--;
					}
					rslt[pos]++;
					for (pos++; pos < r; pos++) {
						rslt[pos] = rslt[pos - 1] + 1;
					}
					pos--;
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
	
	public static void main(String args[]) {
	    Lexicographic lex = new Lexicographic(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
	    int i = 0;
	    for ( int[] vals : lex ) {
	        System.out.print(i++ +": ");
	        for (int v : vals) {
	            System.out.print(v+" ");
	        }
	        System.out.println();
	    }
	}

    public static int index(int n2, int[] index) {
        // TODO Auto-generated method stub
        return 0;
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

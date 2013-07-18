/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import java.util.Iterator;

import net.sf.oriented.omi.Verify;

abstract class AbsAxioms<U> {

    /**
     * Check an axiom of the form Forall a, forall b  c
     * where the forall's are over the iterator of this set,
     * and the c is of type T
     * @author jeremycarroll
     *
     * @param <T>
     */
	abstract class ForAllForAllExists<T,W> implements Verify {
	    /**
	     * return true if e, x and y satisfy the axiom
	     * with z provided by innerIterator()
	     * @param e
	     * @param x
	     * @param y
	     * @param z
	     * @return
	     */
		abstract boolean check(T e, U x, U y, W z);

		/**
		 * Checks the axiom for x and y
		 * return null if this is trivially true
		 * otherwise return an iterator over Ts that are then checked
		 * @param x
		 * @param y
		 * @return
		 */
		abstract Iterator<? extends T> check(U x, U y);

		boolean postCheck(U x, U y) {
			return true;
		}

		@Override
		public boolean verify() {
			Iterator<? extends U> i, j;
			Iterator<? extends W> k;
			i = iterator();
			while (i.hasNext()) {
				U a = i.next();
				j = iterator();
				while (j.hasNext()) {
					U b = j.next();
					Iterator<? extends T> u = check(a, b);
					if (u == null) {
						continue;
					}
					loopu: while (u.hasNext()) {
						T e = u.next();
						k = innerIterator(a,b);
						while (k.hasNext())
							if (check(e, a, b, k.next())) {
								continue loopu;
							}
						return false;
					}
					if (!postCheck(a, b))
						return false;
				}
			}
			return true;
		}

		/**
		 * An iterator used in an inner loop for checking the axiom.
		 * @return
		 */
        @SuppressWarnings("unchecked")
        protected Iterator<? extends W> innerIterator(U a, U b) {
            return (Iterator<? extends W>) iterator();
        }
		
		
	}

	abstract class ForAllForAll implements Verify {
		abstract boolean check(U a, U b);

		@Override
		public boolean verify() {
			Iterator<? extends U> i, j;
			i = iterator();
			while (i.hasNext()) {
				U a = i.next();
				j = iterator();
				while (j.hasNext()) {
					if (!check(a, j.next()))
						return false;
				}
			}
			return true;
		}
	}

	abstract public Iterator<? extends U> iterator();
	


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

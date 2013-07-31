/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.Arrays;
import java.util.Iterator;

import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.Verify;

/**
 * 
 * @author jeremycarroll
 *
 * @param <ForAll> Type of things we have axioms about
 */
abstract class AbsAxioms<ForAll> {

    /**
     * Check an axiom of the form Forall a, forall b suchthat(a,b) forall c in c(a,b) exists z condition(a,b,c,z)
     * 
     * where the forall's are over the iterator of this set,
     * and the c is of type T
     * @author jeremycarroll
     *
     * @param <ForAll3> type returned by first check
     * @param <Exists> type returned by inner iterator, suggest <U>
     */
	abstract class ForAllForAllExists<ForAll3,Exists> implements Verify {
	    
	    private String name = "Some axiom";
	    /**
	     * return true if e, x and y satisfy the axiom
	     * with z provided by innerIterator()
	     * @param e
	     * @param x
	     * @param y
	     * @param z
	     * @return
	     */
		abstract boolean check(ForAll3 e, ForAll x, ForAll y, Exists z);

		/**
		 * Checks the axiom for x and y
		 * return null if this is trivially true
		 * otherwise return an iterator over ForAll3s that are then checked
		 * @param x
		 * @param y
		 * @return
		 */
		abstract Iterator<? extends ForAll3> suchThatForAll(ForAll x, ForAll y);

		boolean postCheck(ForAll x, ForAll y) {
			return true;
		}

		@Override
		public void verify()  throws AxiomViolation  {
			Iterator<? extends ForAll> i, j;
			Iterator<? extends Exists> k;
			i = ForAllForAllExists.this.iterator();
			while (i.hasNext()) {
				ForAll a = i.next();
				j = ForAllForAllExists.this.iterator();
				while (j.hasNext()) {
					ForAll b = j.next();
					Iterator<? extends ForAll3> u = suchThatForAll(a, b);
					if (u == null) {
						continue;
					}
					loopu: while (u.hasNext()) {
						ForAll3 e = u.next();
						k = innerIterator(a,b);
						while (k.hasNext())
							if (check(e, a, b, k.next())) {
								continue loopu;
							}
						throw new AxiomViolation(AbsAxioms.this,name);
					}
					if (!postCheck(a, b)) {
                        throw new AxiomViolation(AbsAxioms.this,name);
					}
				}
			}
		}

		/**
		 * This method can be overridden to give the forall A forall B at the beginning
		 * of the expression
		 * @return
		 */
		protected Iterator<? extends ForAll> iterator() {
            return AbsAxioms.this.iterator2();
        }

        /**
		 * An iterator used in an inner loop for checking the axiom.
		 * @return
		 */
        @SuppressWarnings("unchecked")
        protected Iterator<? extends Exists> innerIterator(ForAll a, ForAll b) {
            return (Iterator<? extends Exists>) iterator();
        }
		
		
	}

	abstract class ForAllForAll implements Verify {
	    private String name = "some axiom";
		abstract boolean check(ForAll a, ForAll b);

		@Override
		public void verify() throws AxiomViolation {
			Iterator<? extends ForAll> i, j;
			i = iterator2();
			while (i.hasNext()) {
				ForAll a = i.next();
				j = iterator2();
				while (j.hasNext()) {
					if (!check(a, j.next())) {
                        throw new AxiomViolation(AbsAxioms.this,name);
					}
				}
			}
		}
	}

	abstract public Iterator<? extends ForAll> iterator2();
	
	public UnsignedSet setOfElements() {
	    return ffactory().unsignedSets().copyBackingCollection(Arrays.asList(elements()));
	}

	abstract Label[] elements();
	
	abstract FactoryFactory ffactory();

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

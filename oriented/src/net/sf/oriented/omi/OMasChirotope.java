/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * The chirotope view of an oriented matroid.
 * NB in the literature some authors use lexicographic ordering,
 * and some authors use colexicographic ordering. This interface
 * defaults to colexicographic.
 * @author jeremycarroll
 *
 */
public interface OMasChirotope extends OM, FullChirotope {

    /**
     * The simplest form of the chirotope: in colexicographic ordering.
     * @return A string consisting of +, -, 0, of length {@link #n()} choose {@link #rank()}.
     */
    String toCoLexicographicString();

    /**
     * The simplest form of the chirotope: in lexicographic ordering.
     * <p>
     * NB: this library uses colexicographic ordering throughout
     * and lexicographic ordering is only available here, and at the two other listed methods.
     * @return A string consisting of +, -, 0, of length {@link #n()} choose {@link #rank()}.
     * @see ChirotopeFactory#fromLexicographic(int, String)
     * @see FactoryFactory#fromLexicographic(int, int, String)
     * @see OMasChirotope#toLexicographicString()
     */
    String toLexicographicString();

    /**
     * Produce a new oriented matroid by setting the value of the chirotope on
     * the given basis to i
     * This method does not check that the mutation is legal. Call {@link OM#verify()}
     * on the result to check the axioms.
     * @param i In { +1, 0, -1}. The value of the resulting oriented matroid's chirotope for this basis
     * @param basis An array of length {@link OM#rank()}, saying which basis to mutate
     * @return A possibly invalid oriented matroid with the given mutation.
     */
    OMasChirotope mutate(int i, Label ... basis);
    /**
     * Produce a new oriented matroid by setting the value of the chirotope on
     * the given basis to i
     * This method does not check that the mutation is legal. Call {@link OM#verify()}
     * on the result to check the axioms.
     * @param i In { +1, 0, -1}. The value of the resulting oriented matroid's chirotope for this basis
     * @param basis An array of length {@link OM#rank()}, indicating by index which basis to mutate
     * @return A possibly invalid oriented matroid with the given mutation.
     */
    OMasChirotope mutate(int i, int ... basis);

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

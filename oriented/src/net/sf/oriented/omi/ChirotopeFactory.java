/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Collection;

/**
 * Create Oriented Matroids from their Chirotope.
 * <p>
 * NB: This library uses co-lexicographic ordering, not lexicograpghic ordering.
 * When using chirotopes from the literature you need to ensure that you know where they are using
 * lexicographic like {@link Bibliography#björnerEtAl1999} or co-lexicographic like {@link Bibliography#f
 * 
 * @author jeremycarroll
 *
 */
public interface ChirotopeFactory extends Factory<OMasChirotope> {
    /**
     * Create an oriented matroid from a chirotope provided programmatically.
     * The element labels are generated automatically.
     * @param chi This is accessed only in monotone increasing fashion, and is not assumed to be alternating
     * @return A new oriented matroid
     */
	OMasChirotope construct(Chirotope chi);

    /**
     * Create an oriented matroid from a chirotope provided programmatically, with given
     * element labels.
     * @param e The elment labels taken in the order of their iterator.
     * @param chi This is accessed only in monotone increasing fashion, and is not assumed to be alternating
     * @return A new oriented matroid
     */
	OMasChirotope construct(Collection<? extends Label> e, Chirotope chi);

	/**
	 * Create an oriented matroid from its colexicographic representation as a series of +, -, 0s.
	 * We also need to know its rank.
	 * @param rank  The rank of the oriented matroid encoded in the chirotope
	 * @param chi The chirotope string, a sequence of + - or 0
	 */
	OMasChirotope fromCoLexicographic(int rank, String chi);
    /**
     * Create an oriented matroid from its lexicographic representation as a series of +, -, 0s.
     * We also need to know its rank.
     * <p>
     * NB: this library uses colexicographic ordering throughout
     * and lexicographic ordering is only available here, and at the two other listed methods.
     * @param rank The rank of the oriented matroid encoded in the chirotope
     * @param chi  The chirotope string, a sequence of + - or 0
     * @see ChirotopeFactory#fromLexicographic(int, String)
     * @see FactoryFactory#fromLexicographic(int, int, String)
     * @see OMasChirotope#toLexicographicString()
     */
    OMasChirotope fromLexicographic(int rank, String chi);
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

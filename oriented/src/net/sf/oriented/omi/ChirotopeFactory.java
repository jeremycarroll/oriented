/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.util.Collection;

public interface ChirotopeFactory extends Factory<OMChirotope> {
	OMChirotope construct(Chirotope chi);

	/**
	 * Note that the contract for Chirotope only requires it to be defined
	 * on {@link Chirotope#rank()} monotonic increasing values. The factory
	 * is responsible for implementing the alternating nature of the chirotope.
	 * @param e
	 * @param chi
	 * @return
	 */
	OMChirotope construct(Collection<? extends Label> e, Chirotope chi);

	/**
	 * Create an oriented matroid from its representation as a series of +, -, 0s.
	 * We also need to know its rank.
	 * @param rank
	 * @param plusMinusZeros
	 */
	OMChirotope fromShortString(int rank, String plusMinusZeros);
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

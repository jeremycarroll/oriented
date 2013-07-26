/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

public interface OMasChirotope extends OM, FullChirotope {

    /**
     * The simplest form of the chirotope.
     * @return A string consisting of +, -, 0, of length {@link #n()} choose {@link #rank()}.
     */
    String toShortString();

    /**
     * Produce a new oriented matroid by setting the value of the chirotope on
     * the given basis to i
     * @param i In { +1, 0, -1}. The value of the resulting oriented matroid's chirotope for this basis
     * @param basis An array of length {@link OM#rank()}, saying which basis to mutate
     * @return
     */
    OMasChirotope mutate(int i, Label ... basis);
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

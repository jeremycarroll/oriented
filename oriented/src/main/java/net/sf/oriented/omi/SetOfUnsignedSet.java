/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * Mathematical sets {@link SetOf} of mathematical sets {@link SetOf} of {@link Label}s.
 * @author jeremycarroll
 *
 */

public interface SetOfUnsignedSet extends SetOf<UnsignedSet, SetOfUnsignedSet> {
    /**
     * The union over this set of the members of this set. I.e. 
     * <code>a &in; b.union()</code> if and only if <code>&exist; c</code> s.t. <code>c &in; b && a &in; c</code>
     * @return The set theoretic union of the set.
     */
    UnsignedSet union();

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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

public interface SignedSet {

    public abstract SignedSet opposite();

    public abstract UnsignedSet plus();

    public abstract UnsignedSet minus();

    public abstract boolean equalsIgnoreSign(SignedSet s);

    public abstract boolean equalsOpposite(SignedSet s);

    public abstract UnsignedSet separation(SignedSet b);

    public abstract SignedSet compose(SignedSet b);

    public abstract int size();

    public abstract boolean conformsWith(SignedSet a);

    public abstract UnsignedSet support();

    public abstract int sign(Label e);

    public abstract boolean isRestrictionOf(SignedSet x);

    public abstract SignedSet restriction(UnsignedSet x);

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

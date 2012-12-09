/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Matroid;
import net.sf.oriented.omi.impl.items.LabelImpl;

public interface MatroidInternal extends Matroid {

    /**
     * Not part of API
     * 
     * @return
     */
    MatroidAll asAll();

    @Override
    public MatroidCircuits getCircuits();

    @Override
    public Bases getBases();

    @Override
    public MatroidInternal dual();

    @Override
    public int rank();

    @Override
    public LabelImpl[] ground();

    @Override
    OMInternal getOM();

    FactoryFactory ffactory();

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

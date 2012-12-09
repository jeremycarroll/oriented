/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.impl.items.LabelImpl;

public interface OMInternal extends OM {

    OMAll asAll();

    @Override
    Circuits getCircuits();

    @Override
    Vectors getVectors();

    @Override
    MaxVectors getMaxVectors();

    @Override
    public OMInternal dual();

    @Override
    public LabelImpl[] ground();

    @Override
    public MatroidInternal getMatroid();

    @Override
    ChirotopeImpl getChirotope();

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

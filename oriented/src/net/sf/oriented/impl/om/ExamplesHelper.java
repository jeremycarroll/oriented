/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.om;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;

public class ExamplesHelper {
    public static OM suv14(double s, double t) {
        double M = 1.0;
        return FactoryFactory.fromMatrix(1000.0, new double[][]{
                { 0, M, 0, M, M, 0, M,     0, s,   2*s,   2*s+M,      -2*s, 2*s,      t - s - M },
                { M, 0, 0, M, 0, M, M,     M, s+M, 3*s+M, 2*(s+M), t-3*s-M, 3*s-t+M,     0 },
                { 0, 0, M, M, M, M, 2*M, 2*M, s+M, 4*s,   3*M+2*s,   t - 4*s, 3*s-t+M,   s -M }
        }).getChirotope();
    }
}


/************************************************************************
    This file is part of the Java Oriented Matroid Library.  

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/

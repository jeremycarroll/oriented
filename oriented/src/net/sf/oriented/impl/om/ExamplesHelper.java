/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.om;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;

public class ExamplesHelper {
    public static OM suv14(double s, double t) {
        return FactoryFactory.fromMatrix(1000.0, new double[][]{
                {  0, 1, 0, 1, 1, 0, 1, 0, s,    2*s,    2*s+ 1,  -2*s,    2*s,      t - s -  1 },
                {  1, 0, 0, 1, 0, 1, 1, 1, s+ 1, 3*s+ 1, 2*(s+1), t-3*s-1, 3*s-t+ 1, 0          },
                {  0, 0, 1, 1, 1, 1, 2, 2, s+ 1, 4*s,    3+2*s,   t-4*s,   3*s-t+ 1, s - 1      }
        });
    }

    public static OM tsukamoto13(double s, double t, double u) {
        return FactoryFactory.fromMatrix(1000.0, new double[][]{
         { 1, 0, 0, 1, s, s, 0, 1, 1,  s*t,   s+t-u-s*t+s*u, s+t-s*t-s*s*u, s*(t-u+s*u) },
         { 0, 1, 0, 1, 0, 1, t, t, u,    t,   t-u + s*u,     t,             t-u+s*u },
         { 0, 0, 1, 1, 1, 1, 1, 1, 0,  1-s*u, 1-u+s*u,       1-s*u,         1-u+s*u }
        });
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

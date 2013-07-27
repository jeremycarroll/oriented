/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.om;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;

public class ExamplesHelper {
    public static OM suv14(double s, double t) {
        double M = 1;
        return FactoryFactory.fromMatrix(1000.0, new double[][]{
                { 0, M, 0, M, M, 0, M,     0, s,   2*s,   2*s+M,      -2*s, 2*s,      t - s - M },
                { M, 0, 0, M, 0, M, M,     M, s+M, 3*s+M, 2*(s+M), t-3*s-M, 3*s-t+M,     0 },
                { 0, 0, M, M, M, M, 2*M, 2*M, s+M, 4*s,   3*M+2*s,   t - 4*s, 3*s-t+M,   s -M }
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

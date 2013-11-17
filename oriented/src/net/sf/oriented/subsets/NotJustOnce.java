/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

class NotJustOnce implements MinimalSubsets {
    private final Method method;
    
    NotJustOnce(Method m) {
        method = m;
    }

    NotJustOnce(String name) {
        this(oneShotMethod(name));
    }

    private static Method oneShotMethod(String name) {
        try {
            return OneShotFactory.class.getDeclaredMethod(name);
        }
        catch (NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException(name+" not configured",e);
        }
    }

    @Override
    public List<BitSet> minimal(Collection<BitSet> full, Preparation prep) {
        try {
            return ((MinimalSubsets)method.invoke(null)).minimal(full, prep);
        }
        catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
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

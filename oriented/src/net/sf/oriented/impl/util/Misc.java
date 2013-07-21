/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.common.reflect.Invokable;

public class Misc {
    public static <T> T invoke(Constructor<T> c,  Object ... args) {
        return invoke(Invokable.from(c),null,args);
    }
    @SuppressWarnings("unchecked")
    public static <T,R> R invoke(Method m,  T receiver, Object ... args) {
        return invoke((Invokable<T,R>)Invokable.from(m),receiver,args);
    }
    public static <T,R> R invoke(Invokable<T,R> m, T receiver, Object ... args) {
        try {
            return m.invoke(receiver, args);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("internal problem", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("internal problem", e);
        } catch (InvocationTargetException e) {
            Throwable rte = e.getCause();
            if (rte instanceof RuntimeException)
                throw (RuntimeException) rte;
            throw new RuntimeException("internal problem", e);
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.sf.oriented.util.combinatorics.Permutation;

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
    
    public static Integer[] box(int primitive[]) {
        Integer boxed[] = new Integer[primitive.length];
        for (int i=0;i<primitive.length;i++) {
            boxed[i] = primitive[i];
        }
        return boxed;
    }

    public static int[] unbox(Integer boxed[]) {
        int unboxed[] = new int[boxed.length];
        for (int i=0;i<boxed.length;i++) {
            unboxed[i] = boxed[i];
        }
        return unboxed;
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

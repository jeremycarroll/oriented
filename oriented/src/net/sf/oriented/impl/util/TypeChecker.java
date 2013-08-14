/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.util;


import java.util.HashSet;
import java.util.Set;

import com.google.common.reflect.TypeToken;

/**
 * The code uses the convention that two type parameters
 * one of whom is NAME and the other of which is NAME2
 * represent the same class, and hence can be cast one to the other
 * without issue.
 * This checks the implementation of that convention at runtime.
 * 
 * <p>
 * The following idiom is the only supported use.
 * In the constructor add the following code:
 * </p>
 * <pre>
        new TypeChecker<ITEM_INTERNAL,ITEM_INTERNAL2>(){ 
            @Override
            protected TypeToken<ITEM_INTERNAL> getTypeToken(Class<?> x) {
               return new TypeToken<ITEM_INTERNAL>(x){};
           }
            @Override
            protected TypeToken<ITEM_INTERNAL2> getTypeToken2(Class<?> x) {
               return new TypeToken<ITEM_INTERNAL2>(x){};
           }
        }.check(getClass());
 </pre>
 *
 * @author jeremycarroll
 *
 * @param <T>  A type parameter
 * @param <T2> An equivalent typr parameter.
 */
public abstract class TypeChecker<T,T2> {
    protected abstract TypeToken<T> getTypeToken(Class<?> x) ;
    protected abstract TypeToken<T2> getTypeToken2(Class<?> x) ;
    
    public TypeChecker(){
    }
    

    
    private static Set<String> done = new HashSet<String>();

    public void check(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        String key = RuntimeClass.key(getClass(), clazz);
        if (done.contains(key)) {
            return;
        }
        done.add(key);

        TypeToken<T> type = getTypeToken(clazz);
        TypeToken<T2> type2 = getTypeToken2(clazz);
        if (!type.getRawType().equals(type2.getRawType())) {
            throw new UnsupportedOperationException(
                    "Runtime type check failed "+  type + " is not compatible with "+type2);
        }
//        System.err.println("Checked "+clazz.getName()+"\n\t"+type.toString()+"\n\t"+type2.toString());
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

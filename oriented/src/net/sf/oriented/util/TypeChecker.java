/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.util;

import java.lang.reflect.GenericSignatureFormatError;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashSet;
import java.util.Set;

public class TypeChecker {
    
    public static void check(Object o) {
        checkClass(o.getClass());
    }
    
    private static Set<Class<?>> done = new HashSet<Class<?>>();

    private static void checkClass(Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        if (done.contains(clazz)) {
            return;
        }
        System.err.println(clazz.getName());
        done.add(clazz);

        checkSuper(clazz,clazz.getGenericSuperclass());
        for (Type interf: clazz.getGenericInterfaces() ) {
            checkSuper(clazz,interf);
        }
    }

    private static void checkSuper(Class<?> clazz, Type t) {
        if (t instanceof ParameterizedType) {
            ParameterizedType superType = (ParameterizedType)t;
            Class<?> superClazz = (Class<?>)superType.getRawType();
            Type args[] = superType.getActualTypeArguments();
            TypeVariable<?>[] params = superClazz.getTypeParameters();
            checkTypeArguments(params,args,clazz,superClazz);
            checkClass(superClazz);
        } else if (t instanceof Class) {
            checkClass((Class<?>)t);
        } else if (t != null){
            throw new IllegalArgumentException("Probably coding error.");
        }
    }

    private static void checkTypeArguments(TypeVariable<?>[] params,
            Type[] args, Class<?> clazz, Class<?> superClazz) {
        System.err.println(args.length+" = "+params.length);
        System.err.println(clazz.getName() + " => "+superClazz.getName());
        if (args.length != params.length) {
            throw new Error(formatMessage(clazz,superClazz,"declare inconsistent number of type parameters."));
        }
        for (int i=0;i<params.length;i++) {
            String name = params[i].getName();
            System.err.println(name + " = "+ args[i].toString()+ " "+args[i].getClass().getName());
            if (name.endsWith("2")) {
                name = name.substring(0, name.length()-1);
            }
        }
        
    }

    private static String formatMessage(Class<?> clazz, Class<?> superClazz,
            String string) {
        // TODO Auto-generated method stub
        return null;
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

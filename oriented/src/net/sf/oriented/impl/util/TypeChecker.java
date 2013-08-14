/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The code uses the convention that two type parameters
 * one of whom is NAME and the other of which is NAME2
 * represent the same class, and hence can be cast one to the other
 * without issue.
 * This checks the implementation of that convention at runtime.
 * @author jeremycarroll
 *
 */
public class TypeChecker {
    
    private static boolean findSuper(Class<?> target, int ix, List<Type> superClasses) {
        Type current = superClasses.get(ix);
        if (current instanceof Class) {
            return findSuper((Class<?>)current,target,ix,superClasses);
        } else if (current instanceof ParameterizedType) {
            return findSuper((Class<?>)((ParameterizedType)current).getRawType(),target,ix,superClasses);
        } else if ( current == null ) {
            return false;
        } else {
            throw new RuntimeException("Probably a coding problem");
        }
    }

    private static boolean findSuper(Class<?> current, Class<?> target, int ix,
            List<Type> superClasses) {
        if (current.equals(target)) {
            return true;
        }
        if (findSuperNext(target,ix+1,current.getGenericSuperclass(),superClasses)) {
            return true;
        }
        for (Type interf: current.getGenericInterfaces()) {
            if (findSuperNext(target,ix+1,interf,superClasses)) {
                return true;
            }
        }
        return false;
    }

    private static boolean findSuperNext(Class<?> target, int i, Type type, List<Type> superClasses) {
        if (type == null) {
            return false;
        }
        superClasses.add(i,type);
        boolean rslt = findSuper(target,i, superClasses);
        if (!rslt) {
            superClasses.remove(i);
        }
        return rslt;
    }

    /**
     * Call this method in constructors of
     * top-level superclasses of class hierarchies
     * using the type parameter naming convention.
     * @param o
     */
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
        if (args.length != params.length) {
            throw new Error(formatMessage(clazz,superClazz,"declare inconsistent number of type parameters."));
        }
        for (int i=0;i<params.length;i++) {
            String name = params[i].getName();
            String name1 = name2ToName1(name);
            if (name1 != null)   {  
                int ix1 = findIndex(name1,params);
                checkCompatible(args[ix1],args[i],clazz,superClazz);
            }
        }
    }

    private static String name2ToName1(String name) {
        if (name.endsWith("2")) {
            return name.substring(0, name.length()-1);
        } else {
            return null;
        }
    }
        

    private static void checkCompatible(Type type1, Type type2, Class<?> clazz,
            Class<?> superClazz) {
        if (type1.equals(type2)) {
            return;
        }
        if (type1.getClass() == type2.getClass()) {
            if (type1.getClass() == Class.class) {
               // already covered
            } else if (type1 instanceof TypeVariable) {
                String name1 = ((TypeVariable<?>) type1).getName();
                String name2 = ((TypeVariable<?>) type2).getName();
                if (name1.equals(name2)) {
                    return;
                }
                if (name1.equals(name2ToName1(name2))) {
                    return;
                }
            } else if (type1 instanceof ParameterizedType) {
                // not implemented
                throw new UnsupportedOperationException("ParameterizedType support not yet written.");
            } else {
                // not even known as not implemented
                throw new UnsupportedOperationException("Unexpected case in logic.");
            }
        }
        // bad case
        throw new UnsupportedOperationException(formatMessage(clazz,superClazz,
                "Runtime type check failed: "+type1+" is not compatible with "+type2));
    }

    private static int findIndex(String name, TypeVariable<?>[] params) {
        for (int i=0;i<params.length;i++) {
            if (name.equals( params[i].getName() ) ) {
                return i;
            }
        }
        throw new IllegalArgumentException("Type param "+name+" not found.");
    }

    private static String formatMessage(Class<?> clazz, Class<?> superClazz, String msg) {
        return clazz.getName()+":"+superClazz.getName()+":"+msg;
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.reflect.TypeToken;

public abstract class RuntimeClass<ITEM> {

    private static Map<String,Class<?>> runtimeClass = new HashMap<>();
    abstract protected TypeToken<ITEM> getTypeToken(Class<?> c);

    public Class<? super ITEM> getRuntimeClass(Class<?> c) {
        String key = key(getClass(), c);
        if (!runtimeClass.containsKey(key)) {
            runtimeClass.put(key, getTypeToken(c).getRawType());
        }
        return (Class<? super ITEM>) runtimeClass.get(key);
    }

    static String key(Class<?> clazz1, Class<?> clazz2) {
        return clazz1.getName()+"^^"+clazz2.getName();
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

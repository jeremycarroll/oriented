/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.reflect.TypeToken;

public abstract class RuntimeClass<ITEM> {

    private static Map<Class<?>,Class<?>> runtimeClass = new HashMap<Class<?>,Class<?>>();
    abstract protected TypeToken<ITEM> getRawType();
    
//    private Class<? super ITEM> getRawType() {
//        return new TypeToken<ITEM>(getClass()){}.getRawType();
//    }
//    
    @SuppressWarnings("unchecked")
    public Class<? super ITEM> find() {
        Class<?> key = getClass();
        if (!runtimeClass.containsKey(key)) {
            runtimeClass.put(key, getRawType().getRawType());
        }
        return (Class<? super ITEM>) runtimeClass.get(key);
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

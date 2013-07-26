/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.impl.items;

import java.lang.reflect.Constructor;

public abstract class OptionsInternal {

    <T> Constructor<?> constructorFor(Class<T> fc) {
        if (fc.equals(LabelFactoryImpl.class)) {
            return LabelImpl.class.getConstructors()[0];
        }
    	String name = fc.getSimpleName();
    	if (!name.endsWith("Factory"))
    		throw new IllegalArgumentException("Naming conventions violated");
    	name = name.substring(0, name.length() - "Factory".length())+ "Impl";
    	return constructorFor(name);
    }

    protected Constructor<?> constructorFor(String name) {
        try {
    		Class<?> impl = Class.forName(getImplementation() + name);
    		Constructor<?> c[] = impl.getConstructors();
    		if (c.length != 1)
    			throw new RuntimeException("internal problem: more than one constructor found for "+ name);
    		return c[0];
    	} catch (ClassNotFoundException e) {
    		return null;
    	}
    }
    
    protected abstract String getImplementation();

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

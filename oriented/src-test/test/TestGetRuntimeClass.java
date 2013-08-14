/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.math.BigInteger;

import junit.framework.Assert;
import net.sf.oriented.impl.util.RuntimeClass;

import org.junit.Test;

import com.google.common.reflect.TypeToken;

public class TestGetRuntimeClass {
    private static abstract class A<T> {
        Class<?> clazz = new RuntimeClass<T>(){
            @Override
            protected TypeToken<T> getTypeToken(Class<?> c) {
               return new TypeToken<T>(c){};
           }}.getRuntimeClass(A.this.getClass());
    }
    private static class B<U> extends A<U> {
    }
    private static class C extends B<BigInteger> {
    }

    private static class D extends B<String> {
    }
    @Test
    public void testAbstract() {
        C c = new C();
        Assert.assertEquals(BigInteger.class,c.clazz);
    }

    @Test
    public void testAbstract2() {
        D c = new D();
        Assert.assertEquals(String.class,c.clazz);
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

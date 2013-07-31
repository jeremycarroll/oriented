/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.math.BigInteger;

import junit.framework.Assert;
import net.sf.oriented.impl.items.HasFactory;
import net.sf.oriented.impl.set.SignedSetInternal;
import net.sf.oriented.impl.util.TypeChecker;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.UnsignedSet;

import org.junit.Test;

public class TestGetRuntimeClass {
    @Test
    public void testUnsignedSet() {
        FactoryFactory f = new FactoryFactory();
        Assert.assertEquals(Label.class, TypeChecker.runtimeClass(f.unsignedSets().empty(), Iterable.class, "T"));
    }
    private static abstract class A<T> {
    }
    private static class B<U> extends A<U> {
    }
    private static class C extends B<BigInteger> {
    }
    @Test(expected=IllegalArgumentException.class)
    public void testAbstract() {
        Assert.assertEquals(BigInteger.class, TypeChecker.runtimeClass(new C(), A.class, "T"));
    }
    @SuppressWarnings("rawtypes")
    @Test(expected=IllegalArgumentException.class)
    public void testGeneric() {
        Assert.assertEquals(BigInteger.class, TypeChecker.runtimeClass(new A(){}, A.class, "T"));
    }
    @Test
    public void testSignedSet() {
        FactoryFactory f = new FactoryFactory();
        UnsignedSet empty = f.unsignedSets().empty();
        Assert.assertEquals(SignedSetInternal.class, TypeChecker.runtimeClass(f.signedSets().construct(empty,empty), HasFactory.class, "ITEM_INTERNAL"));
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

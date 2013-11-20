/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;


public class FindMinimalSubsetsTest extends AbsSubsetTest {
    
    @Parameters
    public static Collection<Object[]> data() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException {
        return data("tests.ser", null,
                getMethods(FindMinimalSubsetsTest.class, "dummy") ,
                new Preparation[]{Preparation.Pritchard});
    }

    public FindMinimalSubsetsTest(Method m, Preparation prep, String nme, int bits, BitSet sets[], int expected) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        super(m,prep,nme,bits,sets,expected);
    }
    
    public static  MinimalSubsets dummy() {
        return new MinimalSubsets () {
            @Override
            public <U extends BitSet> List<U> minimal(Collection<U> full, Preparation prep) {
                return FindMinimalSets.minimal(full);
            }
            
        };
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

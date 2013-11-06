/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.subsets;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized;
import test.BetterParameterized.TestName;


@RunWith(value = BetterParameterized.class)
public class SpeedTestSubsets {
    @Parameters
    public static Collection<Object[]> data() throws IOException, ClassNotFoundException {
        List<Object[]> rslt = new ArrayList<Object[]>();
        ObjectInputStream in = new ObjectInputStream(SpeedTestSubsets.class.getResourceAsStream("tests.ser"));
        int cnt = 0;
        while (true) {
            int sz;
            cnt++;
            try {
                sz = in.readInt();
            }
            catch (EOFException e) {
                break;
            }
            BitSet sets[] = new BitSet[sz];
            for (int i=0;i<sz;i++) {
                sets[i] = (BitSet) in.readObject();
            }
            rslt.add(new Object[]{cnt,sets});
        }
        return rslt;
    }
    public SpeedTestSubsets(int n, BitSet sets[]) {
        
    }

    @TestName
    static public String name(int n, BitSet sets[]) {
        return "sets-"+n+"-"+sets.length;
    }

    @Test
    public void go() {
        
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

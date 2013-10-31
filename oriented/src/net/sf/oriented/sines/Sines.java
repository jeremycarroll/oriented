/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.sines;

import java.util.Formatter;

import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class Sines {
    final OM om;
    final int index[];
    final boolean ok;
    final SignedSet ids[];
    
    public Sines(OM om, SignedSet ids[]) {
        int columnsSeen = 0;
        this.om = om;
        index = new int[om.elements().length+1];
        for (int i=0;i<ids.length;i++) {
            SignedSet id = ids[i];
          //  System.err.println(id);
            columnsSeen |= toBits(id.plus(),i) | toBits(id.minus(),i);
        }
        int twoCount = 0;
        for (int i:index) {
            if (Integer.bitCount(i)==2) {
                twoCount++;
            }
        }
//        System.err.println("Twos: "+twoCount + "/" + Integer.bitCount(columnsSeen));
        this.ids = ids;
       // ok = twoCount + 3 >= Integer.bitCount(columnsSeen);
        ok = twoCount != 4 || Integer.bitCount(columnsSeen) != 8;
    }
    
    public void dump() {
        StringBuilder sb = new StringBuilder();
        Formatter fmt = new Formatter(sb);
        fmt.format("%2s ", "");
        
        for (int i=0;i<index.length;i++) {
            if (index[i] != 0) {
                fmt.format("%2s ", om.elements()[i].label());
            }
        }
        fmt.format("\n");
        for (int j=0;j<ids.length;j++) {
            fmt.format("%2c ", 'a'+j);
            for (int i=0;i<index.length;i++) {
                if (index[i] != 0) {
                    switch (ids[j].sign(om.elements()[i])) {
                    case 1:
                        fmt.format(" + ");
                        break;
                    case -1:
                        fmt.format(" - ");
                        break;
                    case 0:
                        fmt.format("   ");
                        break;
                    }
                }
            }
            fmt.format("\n");
        }
        fmt.format("\n");
        fmt.close();
        System.err.print(sb.toString());
    }
    
    public boolean isOK() {
        return ok;
    }
    
    private int toBits(UnsignedSet ss, int i) {
        int ints[] = om.asInt(ss.toArray());
        int r = 0;
        for (int k=0; k<ints.length;k++) {
            r |= (1<<ints[k]);
            index[ints[k]] |= (1 << i);
        }
        return r;
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

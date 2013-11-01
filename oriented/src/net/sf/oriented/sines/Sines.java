/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.sines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.PlusMinusPlus;

public class Sines {
    private abstract class Count {
        int counts[] = new int[om.elements().length];
        
        abstract UnsignedSet toUnsigned(SignedSet s);
        void add(SignedSet s) {
            for (int i:om.asInt(toUnsigned(s))) {
                counts[i]++;
            }
        }
    }
    private class Analysis {
        final SignedSet threes[];
        Analysis(SignedSet[] threes) {
            this.threes = threes;
        }
        boolean isOK() {
            Count plus = new Count(){
                @Override
                UnsignedSet toUnsigned(SignedSet s) {
                    return s.plus();
                }
            };
            Count minus = new Count(){
                @Override
                UnsignedSet toUnsigned(SignedSet s) {
                    return s.minus();
                }
            };
            for (SignedSet t:threes) {
                plus.add(t);
                minus.add(t);
            }
            int pairColumns = 0;
            int bad = 0;
            for (int i=0;i<plus.counts.length;i++) {
                if (plus.counts[i] == minus.counts[i]) {
                    pairColumns += plus.counts[i]*2;
                }
                if ( (plus.counts[i] == 0 ) != (minus.counts[i] == 0) ) {
                    throw new IllegalArgumentException("deep math error");
                }
                if (plus.counts[i] != 0) {
                    bad++;
                }
            }
            // I can't think of any particular limit for bad.
            return threes.length-1 <= pairColumns;
        }
        void dump() {
            Sines.this.dump(threes);
      }
      
        
    }
    final OM om;
    final SignedSet ids[];
    final Analysis ok;
    
    public Sines(OM om, SignedSet ids[]) {
        this.om = om;
        this.ids = ids;
        ok = findAnalysis();
        
        
//        index = new int[om.elements().length];
//        for (int i=0;i<ids.length;i++) {
//            SignedSet id = ids[i];
//          //  System.err.println(id);
//            columnsSeen |= toBits(id.plus(),i) | toBits(id.minus(),i);
//        }
//        int twoCount = 0;
//        for (int i:index) {
//            if (Integer.bitCount(i)==2) {
//                twoCount++;
//            }
//        }
////        System.err.println("Twos: "+twoCount + "/" + Integer.bitCount(columnsSeen));
//       // ok = twoCount + 3 >= Integer.bitCount(columnsSeen);
//        ok = twoCount != 4 || Integer.bitCount(columnsSeen) != 8;
    }
    
    private Analysis findAnalysis() {
        List<SignedSet> threes = new ArrayList<SignedSet>();
        return findAnalysis(0,threes);
    }

    private Analysis findAnalysis(int i, List<SignedSet> threes) {
        if (i==ids.length) {
            Analysis rslt = new Analysis(threes.toArray(new SignedSet[0]));
            return rslt.isOK() ? rslt : null;
        }
        int bt = threes.size();
        for (SignedSet[] lvl : PlusMinusPlus.splitIntoThrees(om, ids[i])) {
            threes.addAll(Arrays.asList(lvl));
            Analysis rslt = findAnalysis(i+1,threes);
            if (rslt != null) {
                return rslt;
            }
            while (threes.size()>bt) {
                threes.remove(threes.size()-1);
            }
        }
        return null;
    }

    public void dump() {
        if (isOK()) {
            ok.dump();
        } else {
            dump(ids);
        }
    }
    private void dump(SignedSet sets[]) {
        if (sets.length == 0) {
            System.err.println("Nothing to dump\n");
            return;
        }
        UnsignedSet support = sets[0].support();
        for (SignedSet ss:sets) {
            support = support.union(ss.support());
        }
        StringBuilder sb = new StringBuilder();
        Label elements[] = om.elements();
        Formatter fmt = new Formatter(sb);
        fmt.format("%2s ", "");
        for (Label l:elements) {
            if (support.contains(l)) {
                fmt.format("%2s ",l.label());
            }
        }
        fmt.format("\n");
        for (int j=0;j<sets.length;j++) {
            fmt.format("%2c ", 'a'+j);
            for (Label l:elements) {
                if (support.contains(l)) {
                    switch (sets[j].sign(l)) {
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
        return ok != null;
    }
    
//    private int toBits(UnsignedSet ss, int i) {
//        int ints[] = om.asInt(ss.toArray());
//        int r = 0;
//        for (int k=0; k<ints.length;k++) {
//            r |= (1<<ints[k]);
//            index[ints[k]] |= (1 << i);
//        }
//        return r;
//    }

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

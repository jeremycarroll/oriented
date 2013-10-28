/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.List;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FaceLattice;
import net.sf.oriented.omi.Label;

public class DEdge {
    
    final Face source;
    final Face dest;
    final Label label;
    TGEdge tgEdges[];
    final List<DifficultSix> sixes = new ArrayList<DifficultSix>();
    
    DEdge(TGEdge e, FaceLattice fl) {
        label = e.label();
        source = e.source.findFaceOrPoint(label, fl, 1);
        dest = e.dest.findFaceOrPoint(label, fl, -1);
        
    }
    
    @Override
    public int hashCode() {
        return ((source.hashCode()* 17)+(dest.hashCode()*93))^(label.hashCode()*11);
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DEdge)) {
            return false;
        }
        DEdge b = (DEdge)o;
        return source.equals(b.source) && dest.equals(b.dest) && label.equals(b.label);
    }

    public void addDifficultSix(DifficultSix ds) {
        sixes.add(ds);
    }
    
    boolean increaseCount(WAM wam) {
        for (DifficultSix ds:sixes) {
            if (!ds.increaseCount(wam)) {
                return false;
            }
        }
        return true;
    }

    public Label getLabel() {
        return label;
    }

    void printCounts() {
        for (DifficultSix ds:sixes) {
            ds.printCount();
        }
    }

    public int dsCount() {
        int result = 0;
        for (DifficultSix ds:sixes) {
            int c = ds.count();
            if (c > result) {
                result = c;
            }
        }
        return result;
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

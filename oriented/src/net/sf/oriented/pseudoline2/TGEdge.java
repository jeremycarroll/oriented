/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import net.sf.oriented.omi.Label;

import com.google.common.base.Preconditions;

public class TGEdge implements Comparable<TGEdge> {

    final TGVertex source;
    final TGVertex dest;
    private final Label label;
    public final int ordinal;
    EdgeChoices inChoice;
    EdgeChoices outChoice;
    int bit;
    public TGEdge(Label l, int ordinal, TGVertex s, TGVertex d) {
        Preconditions.checkArgument(s.getId().sign(l) != -1);
        Preconditions.checkArgument(d.getId().sign(l) != 1);
        label = l;
        this.ordinal = ordinal;
        this.source = s;
        this.dest = d;
    }

    public Label label() {
        return label;
    }

    @Override
    public String toString() {
        return label+":"+source.getId()+"⇒"+dest.getId();
    }
    
    public void afterAdd(WAM wam) {
        afterAdd(inChoice, wam);
        afterAdd(outChoice, wam);
    }

    private void afterAdd(EdgeChoices c, WAM wam) {
        if (c != null) {
            c.madeChoice(this, wam);
        }
    }

    @Override
    public int compareTo(TGEdge o) {
        return toString().compareTo(o.toString());
    }

    boolean afterRemove(WAM wam) {
        return afterRemove(wam, inChoice) || afterRemove(wam,outChoice);
        
    }

    private boolean afterRemove(WAM wam, EdgeChoices choice) {
         return choice == null || choice.alreadyDone() || choice.reduceCount(wam);
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

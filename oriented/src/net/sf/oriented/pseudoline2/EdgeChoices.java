/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.oriented.omi.Label;

abstract class EdgeChoices {

    final List<TGEdge> choices = new ArrayList<TGEdge>();
    
    private boolean alreadyDone = false;
    
    private int deleteCount = 0;
    
    static EdgeChoices create(TGVertex v, ShrinkingGraph sg, Label l) {
        switch (v.getId().sign(l)) {
        case 1:
            return new EdgeChoices(sg,l,sg.getOutEdges(v)){

                @Override
                void rememberChoiceInEdge(TGEdge e) {
                    e.outChoice = this;
                }

                @Override
                void forgetChoiceInEdge(TGEdge e) {
                    e.outChoice = null;
                }
            };
        case -1:
            return new EdgeChoices(sg,l,sg.getInEdges(v)){
                @Override
                void rememberChoiceInEdge(TGEdge e) {
                    e.inChoice = this;
                }

                @Override
                void forgetChoiceInEdge(TGEdge e) {
                    e.inChoice = null;
                }
            };
        case 0:
            default:
                throw new IllegalArgumentException();
        }
    }
    private EdgeChoices(ShrinkingGraph sg, Label l,Collection<TGEdge> c) {
        // we must choose one of these with label l
        for (TGEdge e:c) {
            if (e.label().equals(l)) {
                if (sg.growing.containsEdge(e)) {
                    alreadyDone = true;
                    return;
                }
                choices.add(e);
            }
        }
    }

    public boolean alreadyDone() {
        return alreadyDone;
    }
    
    boolean makeChoiceNowOrLater(WAM wam, List<TGEdge> addNow) {
        if (alreadyDone) {
            return true;
        }
        switch (choices.size()) {
        case 0:
            return false;
        case 1:
            addNow.add(choices.get(0));
            return true;
        default:
            wam.addChoice(this);
            for (TGEdge e:choices) {
                rememberChoiceInEdge(e);
            }
            return true;
        }
    }
    abstract void rememberChoiceInEdge(TGEdge e);
    abstract void forgetChoiceInEdge(TGEdge e);
    
    void forgetChoiceInEdges() {
        for (TGEdge e:choices) {
            forgetChoiceInEdge(e);
        }
    }

    int size() {
        return choices.size() - deleteCount;
    }

  public boolean impossible() {
      return (!alreadyDone) && size() == 0 ;
  }
public void madeChoice(TGEdge tgEdge, WAM wam) {
    // TODO Auto-generated method stub
    
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

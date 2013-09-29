/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;

import net.sf.oriented.omi.Label;
import net.sf.oriented.pseudoline2.WAM.Undoable;

abstract class EdgeChoices {

    static final int NOT_FORCED = -1;

    final List<TGEdge> choices = new ArrayList<TGEdge>();
    
    private boolean alreadyDone = false;
    
    private int deleteCount = 0;

    int forcedChoice = NOT_FORCED;
    
    static EdgeChoices create(TGVertex v, ShrinkingGraph sg, Label l) {
        switch (v.getId().sign(l)) {
        case 1:
            return new EdgeChoices(sg,l,sg.getOutEdges(v)){

                @Override
                void rememberChoiceInEdge(TGEdge e) {
                    if (e.outChoice != null) {
                        throw new IllegalStateException("outChoice set "+e);
                    }
//                    System.err.println("OutChoice: "+e);
                    e.outChoice = this;
                }

                @Override
                void forgetChoiceInEdge(TGEdge e) {
//                    System.err.println("Forgetting OutChoice: "+e);
                    e.outChoice = null;
                }
            };
        case -1:
            return new EdgeChoices(sg,l,sg.getInEdges(v)){
                @Override
                void rememberChoiceInEdge(TGEdge e) {
                    if (e.inChoice != null) {
                        throw new IllegalStateException("inChoice set");
                    }
//                    System.err.println("InChoice: "+e);
                    e.inChoice = this;
                }

                @Override
                void forgetChoiceInEdge(TGEdge e) {
//                    System.err.println("Forgetting InChoice: "+e);
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
    
    boolean makeChoiceNowOrLater(WAM wam, TGVertex v, List<TGEdge> addNow) {
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
            wam.addChoice(v, this);
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
        return choices.size() ; //- deleteCount;
    }

  public boolean impossible() {
      return (!alreadyDone) && size() == 0 ;
  }
  public void madeChoice(TGEdge tgEdge, WAM wam) {
      if (!alreadyDone) {
          wam.pushReenableChoice(this);
          alreadyDone = true;
      }
  }
public void unsetAlreadyDone() {
    alreadyDone = false;
}
public boolean reduceCount(WAM wam) {
    Preconditions.checkState(!alreadyDone);
    deleteCount++;
    wam.trail.push(new Undoable(){

        @Override
        public void undo() {
           deleteCount--;
            
        }});
    
    switch (choices.size()-deleteCount) {
    case 0:
//        return false;
    case 1:
        if (forcedChoice==NOT_FORCED) {
        forcedChoice =choices.size()-deleteCount;
        wam.trail.push(new Undoable(){

            @Override
            public void undo() {
               forcedChoice = NOT_FORCED;
                
            }});
        }
        return true;
        // make forced choice
//        TGEdge theChoice = null;
//        for (TGEdge e:choices) {
//            if (wam.shrinking.containsEdge(e)) {
//                if (theChoice != null) {
//                    throw new IllegalStateException("Logic error");
//                }
//                theChoice = e;
//            }
//        }
//        return wam.add(theChoice);
    default:
            return true;
    }
    
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

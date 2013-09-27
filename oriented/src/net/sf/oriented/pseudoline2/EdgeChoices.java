/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class EdgeChoices {

    final TGVertex face;
    final List<List<TGEdge>> choices;
    
    boolean alreadyDone = false;
    final Set<TGEdge> oneChoices = new HashSet<TGEdge>();
  //  private final List<TGEdge> twoChoices = new ArrayList<TGEdge>();
    final Set<TGEdge> allChoices = new HashSet<TGEdge>();
    
    public EdgeChoices(TGVertex face, List<List<TGEdge>> choices) {
        this.face = face;
        this.choices = choices;
        for (List<TGEdge> l:choices) {
            for (TGEdge t:l) {
                allChoices.add(t);
            }
        }
    }
    
    /**
     * Remove choices requiring t (if any)
     * @param t
     * @param wam
     * @return false if the operation failed and backtracking is needed.
     */
    public boolean choiceRemoved(TGEdge t, WAM wam) {
        if (allChoices.remove(t)) {
            wam.pushAddUndoingRemove(allChoices,t);
            return !allChoices.isEmpty();
        }
        return true;
    }
    
    int size() {
        return allChoices.size();
    }
    
    public boolean alreadyDone() {
        return alreadyDone;
    }

    public void prepareChoices(GrowingGraph gg, ShrinkingGraph sg) {
        alreadyDone = false;
        oneChoices.clear();
//        twoChoices.clear();
        Collection<TGEdge> already = gg.getIncidentEdges(face);
        for (List<TGEdge> choice:choices) {
            TGEdge a = choice.get(0);
            if (!sg.containsEdge(a)) {
                continue;
            }
            switch (choice.size()) {
            case 1:
                doOne(a, already);
                break;
            case 2:
                TGEdge b = choice.get(1);
                if (!sg.containsEdge(b)) {
                    continue;
                }
                if (already.contains(a)) {
                    doOne(b,already);
                } else if (already.contains(b)) {
                    doOne(a, already);
                } else {
//                    twoChoices.add(a);
//                    twoChoices.add(b);
                }
                break;
            default:
                    throw new IllegalArgumentException();
            }
            if (alreadyDone) {
                return;
            }
        }
//        for (int i=twoChoices.size()-1;i>=0;i--) {
//            if (oneChoices.contains(twoChoices.get(i))) {
//                if (i%2 == 0) {
////                    System.err.println("even");
//                    twoChoices.remove(i+1);
//                    twoChoices.remove(i);
//                } else {
////                    System.err.println("odd");
//                    twoChoices.remove(i);
//                    twoChoices.remove(i-1);
//                    i--;
//                }
//            }
//        }
    }

    private void doOne(TGEdge a, Collection<TGEdge> already) {
        if (already.contains(a)) {
            alreadyDone = true;
        } else {
            oneChoices.add(a);
        }
    }

    public boolean impossible() {
        return (!alreadyDone) && oneChoices.isEmpty() ; //&& twoChoices.isEmpty();
    }

    public TGEdge[] singleChoices() {
        return oneChoices.toArray(new TGEdge[oneChoices.size()]);
    }

//    public TGEdge[] doubleChoices() {
//        return twoChoices.toArray(new TGEdge[twoChoices.size()]);
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

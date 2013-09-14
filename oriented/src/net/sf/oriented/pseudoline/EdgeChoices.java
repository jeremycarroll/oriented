/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.oriented.omi.Face;

class EdgeChoices {

    final Face face;
    final List<List<Tension>> choices;
    
    boolean alreadyDone = false;
    final Set<Tension> oneChoices = new HashSet<Tension>();
    final List<Tension> twoChoices = new ArrayList<Tension>();
    final Set<Tension> allChoices = new HashSet<Tension>();
    
    public EdgeChoices(Face face, List<List<Tension>> choices) {
        this.face = face;
        this.choices = choices;
        for (List<Tension> l:choices) {
            for (Tension t:l) {
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
    public boolean choiceRemoved(Tension t, WAM wam) {
        if (allChoices.remove(t)) {
            wam.pushUndoReplace(allChoices,t);
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
        twoChoices.clear();
        Collection<Tension> already = gg.getIncidentEdges(face);
        for (List<Tension> choice:choices) {
            Tension a = choice.get(0);
            if (!sg.containsEdge(a)) {
                continue;
            }
            switch (choice.size()) {
            case 1:
                doOne(a, already);
                break;
            case 2:
                Tension b = choice.get(1);
                if (!sg.containsEdge(b)) {
                    continue;
                }
                if (already.contains(a)) {
                    doOne(b,already);
                } else if (already.contains(b)) {
                    doOne(a, already);
                } else {
                    twoChoices.add(a);
                    twoChoices.add(b);
                }
                break;
            default:
                    throw new IllegalArgumentException();
            }
            if (alreadyDone) {
                return;
            }
        }
        for (int i=twoChoices.size()-1;i>=0;i--) {
            if (oneChoices.contains(twoChoices.get(i))) {
                if (i%2 == 0) {
//                    System.err.println("even");
                    twoChoices.remove(i+1);
                    twoChoices.remove(i);
                } else {
//                    System.err.println("odd");
                    twoChoices.remove(i);
                    twoChoices.remove(i-1);
                    i--;
                }
            }
        }
    }

    private void doOne(Tension a, Collection<Tension> already) {
        if (already.contains(a)) {
            alreadyDone = true;
        } else {
            oneChoices.add(a);
        }
    }

    public boolean impossible() {
        return (!alreadyDone) && oneChoices.isEmpty() && twoChoices.isEmpty();
    }

    public Tension[] singleChoices() {
        return oneChoices.toArray(new Tension[oneChoices.size()]);
    }

    public Tension[] doubleChoices() {
        return twoChoices.toArray(new Tension[twoChoices.size()]);
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.oriented.omi.Face;

class EdgeChoices {

    final Face face;
    final List<List<Tension>> choices;
    
    boolean alreadyDone = false;
    final List<Tension> oneChoices = new ArrayList<Tension>();
    final List<Tension> twoChoices = new ArrayList<Tension>();
    
    public EdgeChoices(Face face, List<List<Tension>> choices) {
        this.face = face;
        this.choices = choices;
    }
    
    public boolean alreadyDone() {
        return alreadyDone;
    }

    public void prepareChoices(GrowingGraph tg) {
        alreadyDone = false;
        oneChoices.clear();
        twoChoices.clear();
        Collection<Tension> already = tg.getIncidentEdges(face);
        for (List<Tension> choice:choices) {
            Tension a = choice.get(0);
            switch (choice.size()) {
            case 1:
                doOne(a, already);
                break;
            case 2:
                Tension b = choice.get(1);
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

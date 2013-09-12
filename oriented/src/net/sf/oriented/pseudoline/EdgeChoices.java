/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
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

    public Tension fixedChoice() {
        // TODO Auto-generated method stub
        return null;
    }

    public void prepareChoices(GrowingGraph tg) {
        alreadyDone = false;
        // TODO here
    }

    public boolean impossible() {
        // TODO Auto-generated method stub
        return false;
    }

    public Tension[] singleChoices() {
        // TODO Auto-generated method stub
        return null;
    }

    public Tension[] doubleChoices() {
        // TODO Auto-generated method stub
        return null;
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

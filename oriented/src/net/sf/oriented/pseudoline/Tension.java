/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.Label;

public class Tension {

    final Face source;
    final Face dest;
    private final Label label;
    public final int ordinal;
    public Tension(Label l, int ordinal, Face s, Face d) {
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
        return label+":"+source.covector()+"⇒"+dest.covector();
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

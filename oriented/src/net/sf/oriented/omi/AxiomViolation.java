/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.omi;

public class AxiomViolation extends Exception {



    private final Object illFormed;
    public AxiomViolation(Object illFormed, String message) {
        super(message);
        this.illFormed = illFormed;
    }
    

    @Override
    public String getMessage() {
        return illFormed + " does not satisfy its axioms. " + super.getMessage();
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

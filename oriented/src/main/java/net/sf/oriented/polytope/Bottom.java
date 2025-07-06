/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.polytope;

import net.sf.oriented.omi.AxiomViolation;


final class Bottom extends AbsFace {

    public Bottom(DualFaceLattice lattice) {
        super(lattice, -1, -1);
    }
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Bottom)) {
            return false;
        }
        return lattice == ((Bottom)o).lattice;
    }
    @Override
    public int hashCode() {
        // no particular rationale for this number.
        return 502;
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public void verify() throws AxiomViolation {
        super.verify();
        if (getLower().iterator().hasNext()) {
            throw new AxiomViolation(this,"This is bootm?");
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

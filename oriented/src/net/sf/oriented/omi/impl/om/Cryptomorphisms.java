/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

// TODO: maybe refactor some functionality into this enum
public enum Cryptomorphisms {
    CIRCUITS, VECTORS, MAXVECTORS, CHIROTOPE, REALIZED,
    // MUST be half way, those below are duals of those above. Must be even
    // number.
    COCIRCUITS(CIRCUITS), COVECTORS(VECTORS), TOPES(MAXVECTORS), DUALCHIROTOPE(CHIROTOPE), DUALREALIZED(REALIZED);

    private Cryptomorphisms dual;
    private final boolean isDualForm;

    Cryptomorphisms() {
         isDualForm = false;
    }

    Cryptomorphisms(Cryptomorphisms dual) {
	this.dual = dual;
	dual.dual = this;
	isDualForm = true;
    }

    public Cryptomorphisms getDual() {
	return dual;
    }
    public boolean isDualForm() {
	return isDualForm;
    }

}
/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * 
 * 
 * 
 * 
 * 
 * The Java Oriented Matroid Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Java Oriented Matroid Library. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

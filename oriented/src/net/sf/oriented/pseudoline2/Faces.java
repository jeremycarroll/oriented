/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import net.sf.oriented.omi.Face;

class Faces {

    final public Face faceOrPoint;
    
    final public Face face; //may be null
    public Faces(Face faceOrPoint) {
        this(null,faceOrPoint);
    }

    public Faces(Face face, Face faceOrPoint) {
        this.face = face == faceOrPoint ? null : face;
        this.faceOrPoint = faceOrPoint;
    }
    
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Faces)) {
            return false;
        }
        Faces f= (Faces)o;
        return faceOrPoint.equals(f.faceOrPoint) && (  face==null ? f.face == null : face.equals(f.face));
        
    }
    @Override
    public int hashCode() {
        if (face==null) {
            return faceOrPoint.hashCode();
        }
        return faceOrPoint.hashCode() ^ face.hashCode()*23;
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

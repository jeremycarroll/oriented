/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;

public class EuclideanPseudoLines {
    private class Point {
        
        int ring;

        public Point(Face f, int i) {
            this(f);
            setLevel(i);
        }

        public Point(Face f) {
            ss2point.put(f.covector(),this);
        }
        private void setLevel(int i) {
            ring = i;
            getRing(i).add(this);
        }

        public void findAdjacents(boolean b) {
            // TODO Auto-generated method stub
            
        }

        public boolean findLevel() {
            // TODO Auto-generated method stub
            return false;
        }


    }
    final PseudoLines projective;
    final Set<Point> points = new HashSet<Point>();
    final Map<SignedSet,Point> ss2point = new HashMap<SignedSet,Point>();
    private final List<List<Point>> rings = new ArrayList<List<Point>>();
    
    public EuclideanPseudoLines(PseudoLines pseudoLines) {
        projective = pseudoLines;
        Label inf = projective.getInfinity();
        Set<Point> todo = new HashSet<Point>();
        for (Face f: projective.getFaceLattice().withDimension(0)) {
            switch (f.covector().sign(inf)) {
            case -1:
                continue;
            case 0:
                points.add(new Point(f,0));
                break;
            case 1:
                todo.add(new Point(f));
                break;
            }
        }
        for (Point p:points) {
            p.findAdjacents(true);
        }
        for (Point p:todo) {
            p.findAdjacents(false);
        }
        while (!todo.isEmpty()) {
            Iterator<Point> it = todo.iterator();
            while (it.hasNext()) {
                Point p = it.next();
                if (p.findLevel()) {
                    points.add(p);
                    it.remove();
                }
            }
        }
    }

    public List<Point> getRing(int i) {
        if (i >= rings.size()) {
            rings.add(new ArrayList<Point>());
        }
        return rings.get(i);
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

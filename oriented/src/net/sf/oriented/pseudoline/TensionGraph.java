/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sf.oriented.omi.Face;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class TensionGraph extends DirectedSparseMultigraph<Face, Tension> {
    public void dumpEdges() {
        System.err.println("====");
        for (Tension t:getEdges()) {
            Face from = this.getSource(t);
            Face to = this.getDest(t);
            System.err.println(t.label()+": "+from.covector()+" ==> "+to.covector());
        }
    }
    public void dumpVertices() {
        System.err.println("********");
        for (Face f:getVertices()) {
            System.err.print(f.covector()+": ");
            for (Tension t:getInEdges(f)) {
                System.err.print(t.label()+", ");
            }
            System.err.print(" /// ");
            for (Tension t:getOutEdges(f)) {
                System.err.print(t.label()+", ");
            }
            System.err.println();
        }
    }
    public void prune() {
        boolean pruned = true;
        while (pruned) {
            pruned = false;
            for (Face f:new ArrayList<Face>(getVertices())) {
                if (getNeighborCount(f)<3) {
                    removeVertex(f);
                    pruned = true;
                } else {
                    Collection<Tension> out = new ArrayList<Tension>(getOutEdges(f));
                    Collection<Tension> in = new ArrayList<Tension>(getInEdges(f));
                    Set<Tension> ok = new HashSet<Tension>();
                    findPlusMinusPlus(f, in, out, ok);
                    findPlusMinusPlus(f, out, in, ok);
                    boolean keptSome = false;
                    for (Tension e:out) {
                        if (ok.contains(e)) {
                            keptSome = true;
                        } else {
                            this.removeEdge(e);
                            pruned = true;
                        }
                    }
                    if (!keptSome) {
                        removeVertex(f);
                    }
                }
            }
        }
    }
    private void findPlusMinusPlus(Face vertex, Collection<Tension> in,
            Collection<Tension> out, Set<Tension> ok) {
        for (Tension first:out) {
            Face firstV = getOpposite(vertex, first);
            for (Tension second:in) {
                if (second.ordinal > first.ordinal) {
                    Face secondV = getOpposite(vertex,second);
                    if (firstV != secondV) {
                        for (Tension third: out) {
                            if (third.ordinal > second.ordinal) {
                                Face thirdV = getOpposite(vertex,third);
                                if (firstV != thirdV && secondV != thirdV) {
                                    ok.add(first);
                                    ok.add(second);
                                    ok.add(third);
                                }
                            }
                        }
                    }
                }
            }
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

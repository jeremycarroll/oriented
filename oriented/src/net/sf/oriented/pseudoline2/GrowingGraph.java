/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.List;


/**
 * This graph is created from a TensionGraph, which should
 * remain unchanged during the initialization of the twisted graph.
 * 
 * The twisted graph is built by adding edges from the TensionGraph until
 * the {@link #isTwistedGraph()} property returns true.
 * @author jeremycarroll
 *
 */
public class GrowingGraph extends AbstractTGraph {
    
    private final ShrinkingGraph parent;
    private final WAM wam;
    public GrowingGraph(WAM wam, ShrinkingGraph parent) {
       this.parent = parent; 
       this.wam = wam;
    }

    
    /**
     * 
     * @param t
     * @return true if added, false if it cannot be added.
     */
    public boolean addWithConsequences(TGEdge t) {
        List<TGEdge> moreToAdd = new ArrayList<TGEdge>();
        if (canAdd(t.source, moreToAdd) && canAdd(t.dest, moreToAdd)) {
            rawAdd(t);
            for (TGEdge e:moreToAdd) {
                if (!maybeAdd(e)) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    

    private boolean maybeAdd(TGEdge e) {
        return containsEdge(e) || addWithConsequences(e);
    }


    private boolean canAdd(TGVertex vertex, List<TGEdge> moreToAdd) {
        if (!this.containsVertex(vertex)) {
            if (!parent.containsVertex(vertex)) {
                return false;
            }
            for (TGVertex overlap:vertex.overlapping(parent)) {
                if (!wam.maybeRemove(overlap)) {
                    return false;
                }
                if (!parent.containsVertex(vertex)) {
                    return false;
                }
            }
            return vertex.addEdgeChoices(wam,moreToAdd);
        }
        return true;
    }
    
    public boolean addWithConsequences(TGVertex vertex) {
        List<TGEdge> moreToAdd = new ArrayList<TGEdge>();
        if (canAdd(vertex, moreToAdd)) {
            addVertex(vertex);
            wam.pushRemoveUndoingAdd(this, vertex);
            for (TGEdge e:moreToAdd) {
                if (!maybeAdd(e)) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
        
    }


    private void rawAdd(TGEdge t) {
        if (!addEdge(t, t.source, t.dest)) {
            throw new IllegalArgumentException("addEdge failed!");
        }
        wam.pushRemoveUndoingAdd(this,t);
    }


    
//    public void addChoices(TGVertex face, List<List<TGEdge>> choices) {
//        EdgeChoices opt = new EdgeChoices(face,choices);
//        wam.addChoice(opt);
//    }


    public void edgeHasBeenRemoved(TGEdge t) {
        // TODO Auto-generated method stub
        
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

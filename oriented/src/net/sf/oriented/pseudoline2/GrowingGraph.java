/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.BitSet;
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
    final BitSet bits = new BitSet();
    public GrowingGraph(WAM wam, ShrinkingGraph parent) {
       this.parent = parent; 
       this.wam = wam;
    }

    
    int dbgCounter = 0;
    /**
     * 
     * @param t
     * @return true if added, false if it cannot be added.
     */
    public boolean addWithConsequences(TGEdge t) {
        List<TGEdge> moreToAdd = new ArrayList<TGEdge>();
        if (containsEdge(t)) {
            throw new IllegalStateException("addEdge failure");
        }
        if (canAdd(t.source, moreToAdd) && canAdd(t.dest, moreToAdd)) {
            if (containsEdge(t)) {
                return true;
            }
            rawAdd(t);
            
//            boolean okBefore = wam.debugLookingGood();
//            if (okBefore) {
//                System.err.print("Counts ");
//                t.dEdge.printCounts();
//                System.err.println("?");
//                if ( dbgCounter++ == 4  && t.dEdge.dsCount() == 4 ) {
//                    System.err.println("XXX");
//                    
//                }
//            }
            
            if (!t.dEdge.increaseCount(wam)) {
//                if (okBefore) {
//                    System.err.println("AGGH!");
//                }
                return false;
            }
//            if (okBefore && !wam.debugLookingGood()) {
//                System.err.println("BGGH!");
//            }
            if ( ! ( t.source.choose(t, this, parent) && t.dest.choose(t, this, parent) ) ) {
                return false;
            }
            
            if (t.unnecessary()) {
                return false;
            }
            
            for (TGEdge e:moreToAdd) {
                if (!maybeAdd(e)) {
                    return false;
                }
            }
            parent.verifyOnlyEdge(t);
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
            return containsVertex(vertex) || vertex.addEdgeChoices(wam,moreToAdd);
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
        if (bits.get(t.bit) ||  !addEdge(t, t.source, t.dest)) {
            throw new IllegalArgumentException("addEdge failed!");
        }
        bits.set(t.bit);
        wam.pushRemoveUndoingAdd(this,t);
        t.afterAdd(wam);
    }


    
//    public void addChoices(TGVertex face, List<List<TGEdge>> choices) {
//        EdgeChoices opt = new EdgeChoices(face,choices);
//        wam.addChoice(opt);
//    }


    public void edgeHasBeenRemoved(TGEdge t) {
        // TODO Auto-generated method stub
        
    }


    boolean isTheOne() {
        return wam.isTheOne(this);
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

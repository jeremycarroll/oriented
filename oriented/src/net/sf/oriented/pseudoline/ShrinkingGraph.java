/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.oriented.omi.Face;
import net.sf.oriented.pseudoline.TensionPath.EdgeInfo;

import com.google.common.collect.ImmutableList;

/**
 * This graph gets smaller as we move along the stack of the {@link WAM}
 * 
 * As we remove edges, operations to replace those edges on backtracking
 * get added to the trail of the 
 * associated {@link WAM}.
 * @author jeremycarroll
 *
 */
public class ShrinkingGraph extends PrunableGraph {
    
    final private WAM wam;
    final GrowingGraph growing;
    final EuclideanPseudoLines pseudolines;
    ShrinkingGraph(WAM wam, TensionGraph orig) {
        this.wam = wam;
        copy(orig);
        growing = new GrowingGraph(wam, this);
        pseudolines = orig.getEuclideanPseudoLines();
    }
    public EuclideanPseudoLines getEuclideanPseudoLines() {
        return pseudolines;
    }
    
    @Override
    public boolean removeEdge(Tension t) {
        boolean rslt = super.removeEdge(t);
        if (!rslt) {
            throw new IllegalArgumentException("Logic error");
        }
        wam.trailRemove(t);
        return rslt;
    }
    
    class FaceConstraints extends FaceAnalyzer {
        final int index;
        final TensionPath path;
        
        FaceConstraints(int ix, FaceConstraints faceConstraints) {
            super(faceConstraints.face);
            out = faceConstraints.out;
            in = faceConstraints.in;
            path = faceConstraints.path;
            index = ix;
        }

        public FaceConstraints(int i, TensionPath path, Face face) {
           super(face);
           out = new ArrayList<Tension>(out);
           in = new ArrayList<Tension>(in);
           index = i;
           this.path = path;
        }

        @Override
        boolean add(Tension f, Tension s, Tension t) {
            // TODO Auto-generated method stub
            return false;
        }

        public FaceConstraints copy(int i) {
            return new FaceConstraints(i,this);
        }

        public void forward(EdgeInfo edgeInfo) {
            // TODO Auto-generated method stub
            
        }

        public void backward(EdgeInfo edgeInfo) {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    class EdgeSelector extends FaceAnalyzer {

        final Tension wanted;
        final int size;
        final List<List<Tension>> options = new ArrayList<List<Tension>>();
        final Collection<Tension> already;
        final int space;
        boolean justDoIt = false;
        EdgeSelector(Face f, Tension wanted) {
            super(f);
            this.wanted = wanted;
            this.size = faceSize(f);
            // what is already at this face concerning the child
            already = growing.getIncidentEdges(f);
            space = faceSize(f) - already.size();
        }

        @Override
        boolean add(Tension f, Tension s, Tension t) {
            if (f==wanted) {
                add(s,t);
            } else  if (s==wanted) {
                add(f,t);
            } else if (t==wanted) {
                add(f,s);
            }
            return !justDoIt;
        }

        private void add(Tension s, Tension t) {
            if (already.contains(s)) {
                add(t);
            } else if (already.contains(t)) {
                add(s);
            } else {
                if (space > 1) {
                   options.add(ImmutableList.of(s,t));
                }
            }
        }

        private void add(Tension t) {
            if (already.contains(t)) {
                justDoIt = true;
            } else {
                if (space > 0) {
                   options.add(ImmutableList.of(t));
                }
            }
        }

        public boolean impossible() {
            return space < 0;
        }

        public void search() {
            this.findPlusMinusPlus();
            if (justDoIt) {
                options.clear();
                options.add(Arrays.asList(new Tension[0]));
            }
        }
        
    }

    /**
     * 
     * @param face
     * @param t Has already been added to tg
     * @param tg
     * @return true if added, false if it cannot be added.
     */
    public boolean consequences(Face face, Tension t, GrowingGraph tg) {
        if (!containsVertex(face)) {
            return false;
        }
        EdgeSelector selector = new EdgeSelector(face,t);
        if (selector.impossible()) {
            return false;
        }
        selector.search();
        if (selector.options.isEmpty()) {
            return false;
        }
        if (selector.options.size() == 1) {
            for (Tension tt:selector.options.get(0)) {
                if (!(tg.containsEdge(tt) || tg.addWithConsequences(tt))) {
                    return false;
                }
            }
        } else {
           tg.addChoices(face,selector.options);
        }
        return true;
    }

    
    Tension[] sortedEdges() {
//        System.err.println("sorting");
        Tension rslt[] = getEdges().toArray(new Tension[getEdgeCount()]);
        final Map<Face,Integer> faceScore = new HashMap<Face,Integer>();
        for (Face f:getVertices()) {
            faceScore.put(f, score(f));
        }
        Arrays.sort(rslt, new Comparator<Tension>(){

            @Override
            public int compare(Tension o1, Tension o2) {
                
                return  score(o1) - score(o2);
            }

            private int score(Tension t) {
                return faceScore.get(t.source) * faceScore.get(t.dest);
            }});
        
        
        return rslt;
    }

    private int score(Face f) {
        int factor =  faceSize(f) - 2;
        int in = this.getInEdges(f).size();
        int out = this.getOutEdges(f).size();
        if (in == 0 || out == 0) {
            // this face cannot be in a solution, put any edge involving it early
            return 0;
        }
        if (in == 1) {
            return factor*out;
        }
        if (out == 1) {
            return factor*in;
        }
        int min = in<out?in:out;
        return factor*(min + getEdgeCount());
    }

    public boolean removeParallel(Tension t) {
        Collection<Tension> s = getIncidentEdges(t.source);
        Collection<Tension> d = getIncidentEdges(t.dest);
        if (s.size()<d.size()) {
            return removeParallel(t,s,t.dest);
        } else {
            return removeParallel(t,d,t.source);
            
        }
    }

    private boolean removeParallel(Tension added, Collection<Tension> toCheck, Face opposite) {
        List<Tension> toBeRemoved = new ArrayList<Tension>();
        for (Tension t:toCheck) {
            if (t == added) {
                continue;
            }
            if (isIncident(opposite, t)) {
                toBeRemoved.add(t);
            }
        }
        for (Tension t:toBeRemoved) {
            if (!wam.maybeRemove(t)) {
                return false;
            }
        }
        return true;
    }

    public FaceConstraints faceConstaints(int i, TensionPath path, Face from) {
        return new FaceConstraints(i, path, from);
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

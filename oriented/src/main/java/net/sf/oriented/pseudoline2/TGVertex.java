/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FaceLattice;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.PlusMinusPlus;
import net.sf.oriented.pseudoline2.WAM.Undoable;

public class TGVertex implements Comparable<TGVertex> {
    final SetOfSignedSet extent;
    private final SignedSet  identity;
    
    private final String desc;
    private final Face source;
    private final UnsignedSet required;
    
    UnsignedSet chosen;
    UnsignedSet doubledUp;
    /**
     * other vertices which have two or more lines in common but on opposite sides.
     * These cannot appear in the same difficulty as this vertex. (conjectured).
     */
    final Set<TGVertex> exclude = new HashSet<>();
    
    
    // no longer using faces parameter ???
    TGVertex(SignedSet id, EuclideanPseudoLines epl, Face source, String desc ) {
        identity = id;
        Set<SignedSet> ss = new HashSet<>();
        addFace(ss,source);
        for (Face e:source.lower()) {
            for (Face v:e.lower()) {
               addFace(ss,v);
            }
        }
        extent = epl.ffactory().setsOfSignedSet().copyBackingCollection(ss);
        this.source = source;
        this.desc = desc;
        required = findRequired(id, epl);
        chosen = epl.ffactory().unsignedSets().empty();
        doubledUp = epl.ffactory().unsignedSets().empty();
    }

    private UnsignedSet findRequired(SignedSet id, EuclideanPseudoLines epl) {
        Label sorted[] = sort(id.support(),epl);
        boolean pmp[] = signs(sorted,id);
        boolean requiredX[] = PlusMinusPlus.required(pmp);
        List<Label> r = new ArrayList<>();
        for (int i=0;i<sorted.length;i++) {
            if (requiredX[i]) {
                r.add(sorted[i]);
            }
        }
        return epl.ffactory().unsignedSets().copyBackingCollection(r);
    }

    private boolean[] signs(Label[] sorted, SignedSet id) {
        boolean rslt[] = new boolean[sorted.length];
        for (int i=0;i<sorted.length;i++) {
            switch (id.sign(sorted[i])) {
            case 1:
                rslt[i] = true;
                break;
            case 0:
                throw new IllegalArgumentException("Must have sign");
            case -1:
                rslt[i] = false;
                break;
            }
        }
        return rslt;
    }

    // merge constructor
    TGVertex(TGVertex a, TGVertex b) {
        extent = a.extent.union(b.extent);
        source = a.source;
        desc = a.desc + "; "+ b.desc;
        identity = a.identity;
        required = a.required;
    }

    private void addFace(Set<SignedSet> ss, Face f) {
        ss.add(f.covector());
    }

    public Face getSource() {
        return source;
    }
    public SetOfSignedSet getExtent() {
        return extent;
    }

    public SignedSet getId() {
        return identity;
    }

    public boolean impossible(PrunableGraph g) {
        return !(checkEdgeCount(g) && checkEdgeLabels(g));
    }

    Iterable<TGVertex> overlapping(AbstractTGraph g) {
        
        List<TGVertex> rslt = new ArrayList<>();
        for (TGVertex v:exclude) {
            if (g.containsVertex(v)) {
                rslt.add(v);
            }
        }
        return rslt;
    }

    /**
     * This vertex is being added to growing, what additional edges
     * should be added, either now or later.
     * @param addLater
     * @param addNow
     */
    boolean addEdgeChoices(WAM addLater, List<TGEdge> addNow) {
        if (addLater.containsChoiceFor(this)) {
            return true;
        }
        for (Label l: this.identity.support()) {
           if (!EdgeChoices.create(this, addLater.shrinking, l).makeChoiceNowOrLater(addLater, this, addNow)) {
               return false;
           }
        }
        return true;
        
    }

    public boolean checkEdgeCount(AbstractTGraph g) {
        int eCount = g.getNeighborCount(this);
        if (eCount < identity.size()) {
            return false;
        }
        return true;
    }

    public boolean checkEdgeLabels(AbstractTGraph g) {
        UnsignedSet incident = identity.minus().minus(identity.minus());
        for (TGEdge e:g.getIncidentEdges(this)) {
            incident = incident.union(e.label());
        }
        return incident.size() == identity.size();
    }
    @Override
    public String toString() {
        StringBuffer rslt = new StringBuffer(identity +": \""+desc+"\" ");
        for (SignedSet f:extent) {
            rslt.append(f.toString()+"; ");
        }
        return rslt.toString()+ " "+source.covector().toString();
        
    }
    @SuppressWarnings("unused")
    public static void fromPoint(Face cocircuit, TensionGraph tg, UnsignedSet lines, EuclideanPseudoLines euclideanPseudoLines) {
        new TGVertexFactory(cocircuit,tg,lines,euclideanPseudoLines);
    }

    @SuppressWarnings("unused")
    public static void fromFace(Face tope, TensionGraph tg, EuclideanPseudoLines epl) {
        new TGVertexFactory(tope,tg,epl);
    }

    @Override
    public int compareTo(TGVertex o) {
        return identity.toString().compareTo(o.identity.toString());
    }

    /**
     * We are looking for the endpoint of a DEdge, which is either
     * a point or a face. The DEdge has a label, and comes into this TGVertex.
     * There are three possibilities: this TGVertex has a face which includes a corner with a third edge
     * which has that label, or this TGVeretx is a single point which lies on a line with that label,
     * or there is an edge of the face corresponding to this TGVertex with that label.
     * @param label
     * @param fl
     * @param direction
     * @return
     */
    public Face findFaceOrPoint(Label label, FaceLattice fl, int direction) {
        SignedSet first = null;;
        SignedSet second = null;
        for (SignedSet x:extent) {
            if (x.sign(label)==0) {
                if (second != null) {
                    throw new IllegalStateException("Two many zeros");
                }
                if (first == null) {
                    first = x;
                } else {
                    second = x;
                }
            }
        }
        if (first == null) {
            throw new IllegalStateException("No points found");
            
        }
        if (second == null) {
            return fl.get(first);
        }
        if (!first.conformsWith(second)) {
            throw new IllegalStateException("Non-conformist are not welcome");
        }
        Face edge = fl.get(first.compose(second));
        for (Face f:edge.higher()) {
            if (f.covector().sign(label) == direction) {
                if (!f.equals(this.source)) {
                    throw new IllegalStateException("Hmm - unexpected");
                }
                return f;
            }
        }
        throw new IllegalStateException("Face not found");
    }

    static Label[] sort(UnsignedSet someLines, final EuclideanPseudoLines epl) {
        Label labels[] = someLines.toArray();
        Arrays.sort(labels,new Comparator<Label>(){
    
            @Override
            public int compare(Label o1, Label o2) {
                return epl.getEquivalentOM().asInt(o1) - epl.getEquivalentOM().asInt(o2);
            }});
        return labels;
    }

    public boolean requires(Label lbl) {
        return required.contains(lbl);
    }

    public boolean unnecessary(TGEdge t) {
        return (!required.contains(t.label())) || doubledUp.contains(t.label());
    }

    public boolean choose(final TGEdge t, GrowingGraph gg, ShrinkingGraph sg) {
        if (requires(t.label())) {
            if (doubledUp.contains(t.label())) {
                return true; // no change
            }
            if (chosen.contains(t.label())) {
                doubledUp = doubledUp.union(t.label());
                sg.wam.trail.push(new Undoable(){
                    @Override
                    public void undo() {
                        doubledUp = doubledUp.minus(t.label());
                    }});
                for (TGEdge e:gg.getIncidentEdges(this)) {
                    if (e != t && e.label().equals(t.label()) && e.unnecessary() ) {
                        return false;
                    }
                }
            } else {
                chosen = chosen.union(t.label());
                sg.wam.trail.push(new Undoable(){
                    @Override
                    public void undo() {
                        chosen = chosen.minus(t.label());
                    }});
            }
        }
        return true;
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

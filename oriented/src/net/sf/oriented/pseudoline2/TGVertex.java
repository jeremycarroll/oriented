/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;

/**
 * This class is destined to be the class of vertices in a rejigged TensionGraph
 * class
 * @author jeremycarroll
 *
 */
public class TGVertex implements Comparable<TGVertex> {
    private final SetOfSignedSet extent;
    private final SignedSet  identity;
    
    private final String desc;
    private final Face source;
    
    TGVertex(SignedSet id, FactoryFactory fact, Face source, String desc, Face ... faces ) {
        identity = id;
        Set<SignedSet> ss = new HashSet<SignedSet>();
        for (Face f:faces) {
            addFace(ss,f);
        }
        extent = fact.setsOfSignedSet().copyBackingCollection(ss);
        this.source = source;
        this.desc = desc;
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

    public Iterable<TGVertex> overlapping(AbstractTGraph g) {
        List<TGVertex> rslt = new ArrayList<TGVertex>();
        for (TGVertex v:g.getVertices()) {
            if (!v.equals(this)) {
                if (!v.extent.intersection(extent).isEmpty()) {
                    rslt.add(v);
                }
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
        return rslt.toString();
        
    }
    public static void fromPoint(Face cocircuit, TensionGraph tg, UnsignedSet lines, FactoryFactory fact) {
        new TGVertexFactory(cocircuit,tg,lines,fact);
    }

    // TODO convert this method into a helper class
    public static void fromFace(Face tope, TensionGraph tg, EuclideanPseudoLines epl) {
        new TGVertexFactory(tope,tg,epl);
    }
           
        
        
        

    @Override
    public int compareTo(TGVertex o) {
        return identity.toString().compareTo(o.identity.toString());
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.PlusMinusPlus;

import com.google.common.base.Preconditions;

/**
 * This class is destined to be the class of vertices in a rejigged TensionGraph
 * class
 * @author jeremycarroll
 *
 */
public class TGVertex {
    private final SetOfSignedSet extent;
    private final SignedSet  identity;
    
    private final String desc;
    private final Face source;
    
    private TGVertex(SignedSet id, FactoryFactory fact, Face source, String desc, Face ... faces ) {
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
        if (ss.add(f.covector()) && f.dimension()>0) {
            for (Face ff:f.lower()) {
                addFace(ss,ff);
            }
        }
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
        Preconditions.checkArgument(cocircuit.higher().size()==lines.size()*2);
        Preconditions.checkArgument(lines.size()>=3);
        for (Face f : cocircuit.higher()) {
            Preconditions.checkArgument(lines.minus(f.covector().support()).size()==1);
        }
        List<Label> plus = new ArrayList<Label>();
        List<Label> minus = new ArrayList<Label>();
        for (int sz=3;sz<=lines.size();sz++) {
            for (UnsignedSet someLines:lines.subsetsOfSize(sz)) {
                Label labels[] = someLines.toArray();
                for (boolean pmp[]:PlusMinusPlus.get(sz)) {
                    plus.clear();
                    minus.clear();
                    for (int i=0;i<labels.length;i++) {
                        (pmp[i]?plus:minus).add(labels[i]);
                    }
                    tg.addVertex( new TGVertex(
                    fact.signedSets().construct(fact.unsignedSets().copyBackingCollection(plus), 
                            fact.unsignedSets().copyBackingCollection(minus)),
                            fact,
                            cocircuit,
                            "Point: "+someLines,
                            cocircuit ) );
                }
            }
        }
    }

    // TODO convert this method into a helper class
    public static void fromFace(Face tope, TensionGraph tg, EuclideanPseudoLines epl) {
        // TODO extend using nonUniform corners too.
        Label lines[] = new Label[tope.lower().size()];
        int ix=0;
        for (Face edge : tope.lower() ) {
              lines[ix++] = uniqueMember(epl.notLoops.minus(edge.covector().support()));
        }
        if (lines.length==3) {
            // easy case
            tg.addVertex(new TGVertex(createIdentity(epl.ffactory(),tope.covector(),lines),
                    epl.ffactory(),
                    tope,
                    "Triangle",
                    tope));
        } else {
            // find any parallel sides first
            int parallel[] = new int[lines.length*(lines.length-1)/2];
            int pCount = 0;
            for (int i=0;i<lines.length;i++) {
                for (int j=i+1;j<lines.length;j++) {
                    if (epl.areParallel(lines[i],lines[j])) {
                        parallel[pCount++] = (1<<i)|(1<<j);
                    }
                }
            }
            
            // initialize singleton sets
            FactoryFactory fact = epl.ffactory();
            UnsignedSet singletons[] = new UnsignedSet[lines.length];
            for (int i=0;i<lines.length;i++) {
                singletons[i] = fact.unsignedSets().copyBackingCollection(Arrays.asList(lines[i]));
            }
            
            sides:
            for (int sides=7;sides<(1<<lines.length);sides++) {
                Set<Face> extent = new HashSet<Face>();
                int bitCount = Integer.bitCount(sides);
                if (bitCount<3) {
                    continue; // too few sides
                }
                // check we do not include parallel sides
                for (int k=0;k<pCount;k++) {
                    if ((parallel[k] & sides) == parallel[k]) {
                        continue sides;
                    }
                }
                
                SignedSet id = createIdentity(fact,tope.covector(),sides,lines);
//                System.err.println(id);
                // check point of intersection is on the 'correct' side of at least one line
                for (int i=0;i<lines.length;i++) {
                    if ( ( (1<<i) & sides ) != 0 ) {
                        for (int j=i+1;j<lines.length;j++) {
                            if ( ( (1<<j) & sides ) != 0 ) {
                                SignedSet inter = epl.lineIntersection(lines[i],lines[j]);
                                if (inter.conformsWith(tope.covector())) {
                                    continue;
                                }
                                SignedSet otherLines = inter.intersection(id);
                                if (otherLines.support().isEmpty() ) {
                                    continue sides;
                                }
                                // NOT TODO: if this is a nonUniform point then we need to save for later
                                // hmmm - but we would need to check that one for its intersections etc.
                                // hunch - don't need this.
                                
                                for (UnsignedSet other : otherLines.support().subsetsOfSize(1)) {
                                    UnsignedSet triangleSupport = other.union(singletons[i]).union(singletons[j]);
                                    SignedSet triangleId = tope.covector().restriction(triangleSupport);
                                    // TODO: make this more efficient - we are doing the same collection
                                    // several times over.
                                    epl.collectConformingTopes(triangleId,extent);
                                }
                            }
                        }
                    }
                }
                
                // TODO: later, nonUniform points
                
                tg.addVertex(new TGVertex(id,epl.ffactory(),
                        tope,
                        "Polygon: "+bitCount,
                              extent.toArray(new Face[0])));
            }
        }
    }
           
        
        
        
        
        
//        Preconditions.checkArgument(cocircuit.higher().size()==lines.size()*2);
//        Preconditions.checkArgument(lines.size()>=3);
//        for (Face f : cocircuit.higher()) {
//            Preconditions.checkArgument(lines.minus(f.covector().support()).size()==1);
//        }
//        List<Label> plus = new ArrayList<Label>();
//        List<Label> minus = new ArrayList<Label>();
//        for (int sz=3;sz<=lines.size();sz++) {
//            for (UnsignedSet someLines:lines.subsetsOfSize(sz)) {
//                Label labels[] = someLines.toArray();
//                for (boolean pmp[]:PlusMinusPlus.get(sz)) {
//                    plus.clear();
//                    minus.clear();
//                    for (int i=0;i<labels.length;i++) {
//                        (pmp[i]?plus:minus).add(labels[i]);
//                    }
//                    result.add( new TGVertex(
//                    fact.signedSets().construct(fact.unsignedSets().copyBackingCollection(plus), 
//                            fact.unsignedSets().copyBackingCollection(minus)),
//                            cocircuit ) );
//                }
//            }
//        }
    private static SignedSet createIdentity(FactoryFactory ffactory, SignedSet tope, int sides,
            Label[] lines) {
        List<Label> us = new ArrayList<Label>(lines.length);
        for (int i=0;i<lines.length;i++) {
            if ( ( (1<<i) & sides ) != 0 ) {
                us.add(lines[i]);
            }
        }
        return createIdentity(ffactory, tope, us);
    }
    private static SignedSet createIdentity(FactoryFactory ffactory,
            SignedSet tope, Label ... us) {
        return tope.restriction(ffactory.unsignedSets().copyBackingCollection(Arrays.asList(us)));
    }
    private static SignedSet createIdentity(FactoryFactory ffactory,
            SignedSet tope, List<Label> us) {
        return tope.restriction(ffactory.unsignedSets().copyBackingCollection(us));
    }
    private static Label uniqueMember(UnsignedSet singleton) {
        Iterator<Label> it = singleton.iterator();
        try {
           return it.next();
        }
        finally {
            if (it.hasNext()) {
                throw new IllegalArgumentException("Non-singletone set: "+singleton);
            }
        }
    }

    public SetOfSignedSet getExtent() {
        return extent;
    }

    public SignedSet getId() {
        return identity;
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

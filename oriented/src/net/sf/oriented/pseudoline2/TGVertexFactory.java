/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.PlusMinusPlus;

public class TGVertexFactory {
    abstract class MaskHelper {

        protected int masks[];
        protected int mCount;
        
        MaskHelper(int sz) {
            masks = new int[sz];
            mCount = 0;
        }

        public boolean matchesAny(int sides) {
            for (int k = 0; k < mCount; k++) {
                int saved = masks[k];
                if (matches(sides, saved)) {
                    return true;
                }
            }
            return false;
        }
        
        abstract boolean matches(int sides, int savedValue);

        protected int save(int mask) {
            return masks[mCount++] = mask;
        }

    }

    private class NonUniformHelper extends MaskHelper {

        public NonUniformHelper(Face tope) {
            super(tope.lower().size());
            Map<Face, Face> nonUniform2oneEdge = new HashMap<Face, Face>();
            for (Face e : tope.lower()) {
                for (Face v : e.lower()) {
                    if (v.higher().size() != 4) {
                        if (nonUniform2oneEdge.containsKey(v)) {
                            found(v, e, nonUniform2oneEdge.get(v));
                        } else {
                            nonUniform2oneEdge.put(v, e);
                        }
                    }
                }
            }

        }

        private void found(Face v, Face e1, Face e2) {
            Label lines[] = new Label[v.higher().size()/2 - 2];
            int ix = 0;
            UnsignedSet line1 = e1.covector().support();
            UnsignedSet line2 = e2.covector().support();
            edges:
            for (Face e : v.higher()) {
                UnsignedSet line = e.covector().support();
                if (!(line.equals(line1) || line.equals(line2))) {
                    Label lbl = uniqueMember(e);
                    for (int j=0;j<ix;j++) {
                        if (lines[j].equals(lbl)) {
                            continue edges;
                        }
                    }
                    lines[ix++] = lbl;
                }
            }
            int mask = saveLineLabels(Arrays.asList(lines));
            save(mask|bitFor(line1)|bitFor(line2));

        }

        @Override
        boolean matches(int sides, int savedValue) {
            return (sides & ~savedValue) == 0;
        }
    }

    private List<Label> lines = new ArrayList<Label>();

    TGVertexFactory(Face cocircuit, TensionGraph tg, UnsignedSet lines,
            FactoryFactory fact) {
        Preconditions
                .checkArgument(cocircuit.higher().size() == lines.size() * 2);
        Preconditions.checkArgument(lines.size() >= 3);
        for (Face f : cocircuit.higher()) {
            Preconditions.checkArgument(lines.minus(f.covector().support())
                    .size() == 1);
        }
        List<Label> plus = new ArrayList<Label>();
        List<Label> minus = new ArrayList<Label>();
        for (int sz = 3; sz <= lines.size(); sz++) {
            for (UnsignedSet someLines : lines.subsetsOfSize(sz)) {
                Label labels[] = someLines.toArray();
                for (boolean pmp[] : PlusMinusPlus.get(sz)) {
                    plus.clear();
                    minus.clear();
                    for (int i = 0; i < labels.length; i++) {
                        (pmp[i] ? plus : minus).add(labels[i]);
                    }
                    tg.addVertex(new TGVertex(fact.signedSets().construct(
                            fact.unsignedSets().copyBackingCollection(plus),
                            fact.unsignedSets().copyBackingCollection(minus)),
                            fact, cocircuit, "Point: " + someLines, cocircuit));
                }
            }
        }
    }

    public int bitFor(UnsignedSet line) {
        int bitPosition = lines.indexOf(uniqueMember(line));
        if (bitPosition == -1) {
            throw new IllegalStateException("Did not find line");
        }
        return (1<<bitPosition);
    }

    class ParallelHelper extends MaskHelper {
        ParallelHelper(EuclideanPseudoLines epl) {
            super(lines.size() * (lines.size() - 1) / 2);
            for (int i = 0; i < lines.size() ; i++) {
                for (int j = i + 1; j < lines.size() ; j++) {
                    if (epl.areParallel(lines.get(i), lines.get(j))) {
                        int mask = (1 << i) | (1 << j);
                        save(mask);
                    }
                }
            }
        }

        @Override
        protected boolean matches(int sides, int saved) {
            return (saved & sides) == saved;
        }
    }

    EuclideanPseudoLines epl;

    TGVertexFactory(Face tope, TensionGraph tg, EuclideanPseudoLines epl) {
        this.epl = epl;
        int initialTope = saveLines(tope.lower());
        NonUniformHelper nonUniform = new NonUniformHelper(tope);
        if (lines.size() == 3) {
            // easy case
            tg.addVertex(new TGVertex(createIdentity(epl.ffactory(),
                    tope.covector(), lines), epl.ffactory(), tope, "Triangle",
                    tope));
        } else {
            // find any parallel sides first
            ParallelHelper parallel = new ParallelHelper(epl);
            // initialize singleton sets
            FactoryFactory fact = epl.ffactory();
            UnsignedSet singletons[] = new UnsignedSet[lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                singletons[i] = fact.unsignedSets().copyBackingCollection(
                        Arrays.asList(lines.get(i)));
            }

            sides: for (int sides = 7; sides < (1 << lines.size()); sides++) {
                Set<Face> extent = new HashSet<Face>();
                int bitCount = Integer.bitCount(sides);
                if (bitCount < 3) {
                    continue; // too few sides
                }
//                if (Integer.bitCount(sides&initialTope)<2) {
//                    continue; // guessing wildly
//                }
                // check we do not include parallel sides
                if (parallel.matchesAny(sides)) {
                    continue;
                }
                
                // check we are not just using stuff from a vertex of size 5 or more!
                if (nonUniform.matchesAny(sides)) {
                    continue;
                }

                SignedSet id = createIdentity(fact, tope.covector(), sides);
                // System.err.println(id);
                // check point of intersection is on the 'correct' side of at
                // least one line
                for (int i = 0; i < lines.size(); i++) {
                    if (((1 << i) & sides) != 0) {
                        for (int j = i + 1; j < lines.size(); j++) {
                            if (((1 << j) & sides) != 0) {
                                SignedSet inter = epl.lineIntersection(lines.get(i), lines.get(j));
                                if (inter.conformsWith(tope.covector())) {
                                    continue;
                                }
                                SignedSet otherLines = inter.intersection(id);
                                if (otherLines.support().isEmpty()) {
                                    continue sides;
                                }
                                // NOT TODO: if this is a nonUniform point then
                                // we need to save for later, I think not.
                                // hmmm - but we would need to check that one
                                // for its intersections etc.
                                // hunch - don't need this.

                                for (UnsignedSet other : otherLines.support()
                                        .subsetsOfSize(1)) {
                                    UnsignedSet triangleSupport = other.union(
                                            singletons[i]).union(singletons[j]);
                                    SignedSet triangleId = tope.covector()
                                            .restriction(triangleSupport);
                                    // TODO: make this more efficient - we are
                                    // doing the same collection
                                    // several times over.
                                    epl.collectConformingTopes(triangleId,
                                            extent);
                                }
                            }
                        }
                    }
                }

                // TODO: later, nonUniform points : I think not.

                tg.maybeAddVertex(new TGVertex(id, epl.ffactory(), tope, "Polygon: "
                        + bitCount, extent.toArray(new Face[0])));
            }
        }
    }

    protected int saveLines(Collection<? extends Face> edges) {
        int oldSize = lines.size();
        for (Face edge : edges) {
            lines.add(uniqueMember(edge));
        }
        return maskFrom(oldSize);
    }

    private int maskFrom(int oldSize) {
        return ((1<<lines.size())-1) & ~((1<<oldSize)-1);
    }

    protected int saveLineLabels(Collection<Label> edges) {
        int oldSize = lines.size();
        for (Label edge : edges) {
            lines.add(edge);
        }
        return maskFrom(oldSize);
    }

    private Label uniqueMember(Face edge) {
        return uniqueMember(edge.covector().support());
    }

    protected Label uniqueMember(UnsignedSet support) {
        UnsignedSet singleton = epl.notLoops.minus(support);
        Iterator<Label> it = singleton.iterator();
        try {
            return it.next();
        }
        finally {
            if (it.hasNext()) {
                throw new IllegalArgumentException("Non-singletone set: "
                        + singleton);
            }
        }
    }

    private SignedSet createIdentity(FactoryFactory ffactory,
            SignedSet tope, int sides) {
        List<Label> us = new ArrayList<Label>(lines.size());
        for (int i = 0; i < lines.size(); i++) {
            if (((1 << i) & sides) != 0) {
                us.add(lines.get(i));
            }
        }
        return createIdentity(ffactory, tope, us);
    }


    private static SignedSet createIdentity(FactoryFactory ffactory,
            SignedSet tope, List<Label> us) {
        return tope.restriction(ffactory.unsignedSets().copyBackingCollection(us));
    }

}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.PlusMinusPlus;

import com.google.common.base.Preconditions;

/**
 * This class is destined to be the class of vertices in a rejigged TensionGraph
 * class
 * @author jeremycarroll
 *
 */
public class TGVertex {
    private final List<Face> extent;
    private final SignedSet  identity;
    
    private TGVertex(SignedSet id, Face ... faces ) {
        identity = id;
        extent = Arrays.asList(faces);
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
                            cocircuit ) );
                }
            }
        }
    }

    public static void fromFace(Face tope, TensionGraph tg, FactoryFactory fact) {
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

/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.util.BitSet;

import net.sf.oriented.pseudoline2.WAM.Undoable;

class DifficultSix extends Difficulty {

    private int count = 0;
    DifficultSix(AbstractTGraph gg,  TensionGraph base) {
        super(gg, base.totalBits());
        int bit = 1;
        while (true) {
            bit = bits.nextSetBit(bit);
            if (bit == -1) {
                return;
            }
            DEdge d = base.getDEdge(bit);
            d.addDifficultSix(this);
            bit++;
        }
    }
    boolean increaseCount(WAM wam) {
        if (count >= 5) {
            return false;
     //       throw new IllegalStateException();
        }
        count++;
        wam.trail.push(new Undoable(){

            @Override
            public void undo() {
                count--;
            }});
        if (count<5) {
            return true;
        }
        BitSet left = (BitSet) bits.clone();
        left.andNot(wam.shrinking.growing.bits);
        int bit = 1;
        bit = bits.nextSetBit(bit);
        if (bit == -1) {
            throw new IllegalStateException();
        }
        for (TGEdge e: wam.base.getDEdge(bit).tgEdges) {
            if (! wam.maybeRemove(e) ) {
                return false;
            }
        }
        return true;
    }

}

/*
 * To keep track of these during operation of WAM
 * 
 * Whenever a TGEdge is added to GG 
 * then increment counts in its corresponding DEdge 
 *   
 * Each DEdge has a list of DIfficultSixes
 * and the count is increment in each of these.
 * If the count in an DifficultSix reaches 5 then we
 * identify the 6th DEdge and remove corresponding TGEdges
 * from SG
 * 
 * 
 */


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

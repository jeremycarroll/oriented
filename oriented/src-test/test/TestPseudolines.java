/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.pseudoline.PseudoLines;

import org.junit.Test;

public class TestPseudolines {
    /*
     * chapter1 along line 1 ....
    1:6(35)(24)
    6:1(54)(32)
    3:(15)4(62)
    5:(13)(64)2
    2:(14)(36)5
    4:(12)3(56)
    reorient: []
    Circuits: ( 163524, {1'24',12'4,6'54',3'524',13'5,16'52',1'35',6'32',1'6'34',35'2'4,65'4,1'65'2,63'2,163'4} )
    CoCircuits: ( 163524, {1'3'2',3'5'2'4',6'3'5',1'6'3'4,6'24,15'4',16'5'2,1'54,132,3524,62'4',1'652',1634',635} )
    Topes: ( 163524, {1'6'3'5'2'4',1'6'3'5'2'4,1'6'3'5'24,16'3'5'2'4',16'3'5'24',16'3'5'24,1'6'3'52'4,1'6'3'524,16'35'24',16'35'24,1'6'3524,16'3524,1'63'5'2'4',163'5'2'4',1'63'52'4',1'63'52'4,1635'2'4',1635'24',1'6352'4',1'6352'4,1'63524,16352'4',163524',163524} )
    */
    
    @Test
    public void testFollowLine() {
//        1:6,5,3,4,2,6,3,5,2,4,
        // read off in this order, starting with second lot.
//        6:1,5,4,3,2,1,4,5,2,3,
//        3:5,1,4,2,6,1,5,4,6,2,
//        5:1,3,4,6,2,3,1,6,4,2,
//        2:4,1,6,3,5,1,4,3,6,5,
//        4:1,2,3,6,5,2,1,3,5,6,

        OM om = Examples.chapter1();
        System.err.println("X(1,5,6) = "+om.getChirotope().chi(om.asInt(om.ffactory().labels().parse("1","5","6"))));
        follow("1'23456","1","6");
        follow("1'23456'","6","1");
        follow("1'23'456'","3","1");
        follow("1'23'45'6'","5","1");
        follow("1'2'3'45'6'","2","1");
        follow("1'2'3'4'5'6'","4","1");
    }

    private void follow(String tope, String line, String first) {
        OM om = Examples.chapter1();
        SignedSet start = om.ffactory().signedSets().parse(tope);
        Label  along = om.ffactory().labels().parse(line);
        Label cross = om.ffactory().labels().parse(first);
        System.err.print(line+":");
        for (Face pe[]: PseudoLines.followLine(om,start,along,cross)) {
            System.err.print(PseudoLines.getLineLabel(start.support(), pe[1].covector()).label()+",");
        }
        System.err.println();
        
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

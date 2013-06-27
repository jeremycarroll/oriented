/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/


package net.sf.oriented.omi;

/**
 * Here are some 'interesting' oriented matroids.
 * Of course, all we mean by interesting is
 * interesting to me, and my projects of pseudoline stretching.
 * 
 * @author jeremycarroll
 *
 */
public class Examples {
    /**
     * See the "Sharpness of Circular Saws (2000)"
     */
   public final static OM circularSaw3  = fromChirotope(7,3,
      // 0
        "+++++ ++++ +++ ++ +" + 
      // 1 
        "---- --- -- +" +
      // 2
        "--- -- +" +
      // 3
        "++ +" +
      // 4
        "+"
    );
           

   private static OM fromChirotope(int n, int r, String chi) {
       Options opt = new Options();
       String universe[] = new String[n];
       char u[] = new char[n];
       for (int i=0;i<n;i++) {
           char c = (char) ('A'+i);
           universe[i] = new String(new char[]{c});
           u[i] = c;
       }
       opt.setUniverse(universe);
       opt.setShortLabels();
       
       return new FactoryFactory(opt).chirotope().parse(
               "("+new String(u)+", "+n+", "+chi.replaceAll(" ","")+")"
               );
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

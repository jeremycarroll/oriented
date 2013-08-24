/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.awt.Color;
import net.sf.oriented.omi.Label;

public class ImageOptions {
    public boolean showLabels;
    public boolean showOrigin;
    public double height, width;
    
    public double border;
    
    public double fontSizeRatio;
    
    public double lineThickness;
    
    public double vertexSize;
    
    public Color background;
    public Color fill;
    
    public double originArrowLength;
    public double originArrowSize;

    

    public void setColorOfInfinity(Color c) {
        
    }
    public void setStrokeOfInfinity(double ... dashes) {
        
    }
    public void setColor(Label lbl, Color c) {
        
    }
    public void setStroke(Label lbl, double ...dashes ) {
        
    }

    public static ImageOptions defaultBlackAndWhite() {
        return null;
    }
    
    public static ImageOptions defaultColor() {
        return null;
    }
    
    private Label infinity;
    void setInfinity(Label inf) {
        infinity = inf;
    }
    Color getColor(Label lbl) { return null; }
    double[] getStroke(Label lbl){ return null; }
    
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

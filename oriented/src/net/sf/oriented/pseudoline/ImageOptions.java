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
    
    public double labelPosition;
    
    public double fontSizeRatio;
    
    public double lineThickness;
    
    public double vertexSize;
    
    public Color background;
    public Color fill;
    public Color foreground;
    
    public double originArrowLength;
    public double originArrowSize;

    private double internalFontSizeRatio = 5;

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
    
    Color someColors[];
    double someDashes[][] = new double[][]{
            { 10, 3 },
            { 3, 3 },
            { 5, 3, 2, 3, 2, 3 },
            { 7, 3, 2, 3 },
            { 5, 3, 5, 3, 2, 3},
            { 5, 3, 5, 3, 2, 3, 2, 3 },
            { 10, 3, 2, 3 },
            { 10, 3, 2, 3 , 2, 3 },
            { 10, 3, 2, 3 , 5, 3, 2, 3 },
            { 10, 3, 5, 3, 2, 3 },
            { 10, 3, 2, 3, 2, 3, 2, 3 },      
    };
    
    public static ImageOptions defaultColor() {
        ImageOptions rslt = new ImageOptions();
        rslt.showOrigin = true;
        rslt.showLabels = true;
        rslt.height = rslt.width = 3000;
        rslt.border = 100;
        rslt.labelPosition = 50;
        rslt.fill = new Color(255, 255, 204);
        rslt.background = Color.WHITE;
        rslt.foreground = Color.BLACK;
        rslt.vertexSize = 4.0;
        rslt.fontSizeRatio = 1.0;
        rslt.lineThickness = 2.0;
        rslt.originArrowLength = 50;
        rslt.originArrowSize = 15;
        return rslt;
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

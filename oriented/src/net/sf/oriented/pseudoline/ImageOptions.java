/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.awt.Color;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import net.sf.oriented.omi.Label;

public class ImageOptions {
    public boolean showLabels;
    public boolean showOrigin;
    public int height, width;
    
    public double border;
    
    public double labelPosition;
    
    public double fontSizeRatio;
    
    public double lineThickness;
    
    public int vertexSize;
    
    public Color background;
    public Color fill;
    public Color foreground;
    
    public int originArrowLength;
    public int originArrowSize;

    private double internalFontSizeRatio = 5;
    
    private Color infinityColor;
    private double infinityStroke[];
    
    private Map<Label,Color> colors = new HashMap<Label,Color>();
    private Map<Label,double[]> strokes = new HashMap<Label,double[]>();
    private Label infinity;
    private int nextColor = 0;
    private int nextStroke = 0;
    
    private static Color someColors[] = new Color[22];

    /**
     * Can be null
     * @param c
     */
    public void setColorOfInfinity(Color c) {
        infinityColor = c;
    }
    /**
     * Can be null
     * @param dashes
     */
    public void setStrokeOfInfinity(double ... dashes) {
        infinityStroke = dashes;
    }
    public void setColor(Label lbl, Color c) {
        colors.put(lbl,c);
        
    }
    public void setStroke(Label lbl, double ...dashes ) {
        strokes.put(lbl,dashes);
    }

    public static ImageOptions defaultBlackAndWhite() {
        return null;
    }
    static {
        int ix = 0;
        for (int i=0;i<3;i++)
            for (int j=0;j<3;j++)
                for (int k=0;k<3;k++) {
                    if (i==j && j==k)
                        continue;
                    if (i==2 && j==2) // too close to our background color
                        continue;
                    someColors[ix++] = new Color((3+6*i)*17,(3+6*j)*17,(3+6*k)*17);
                }
        Arrays.sort(someColors,new Comparator<Color>(){
            @Override
            public int compare(Color o1, Color o2) {
                // pseudorandom order
                return System.identityHashCode(o1) - System.identityHashCode(o2);
            }});
    }
    private static double someStrokes[][] = new double[][]{
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
        rslt.infinityColor = Color.BLACK;
        rslt.height = rslt.width = 3000;
        rslt.border = 100;
        rslt.labelPosition = 50;
        rslt.fill = new Color(255, 255, 204);
        rslt.background = Color.WHITE;
        rslt.foreground = Color.BLACK;
        rslt.vertexSize = 3;
        rslt.fontSizeRatio = 1.0;
        rslt.lineThickness = 2.0;
        rslt.originArrowLength = 50;
        rslt.originArrowSize = 15;
        return rslt;
    }
    
    double fontSizeRatio() {
        return this.fontSizeRatio * this.internalFontSizeRatio;
    }
    
    void setInfinity(Label inf) {
        infinity = inf;
    }
    Color getColor(Label lbl) { 
        if ( lbl == infinity && infinityColor != null) {
            return infinityColor;
        }
        if (colors.get(lbl)==null) {
            Color color = someColors[nextColor++];
            System.err.println(lbl+" "+color);
            colors.put(lbl, color);
        }
        return colors.get(lbl);
    }
    double[] getStroke(Label lbl){ 
        if ( lbl == infinity && infinityStroke != null) {
            return infinityStroke;
        }
        if (strokes.get(lbl)==null) {
            strokes.put(lbl, someStrokes[nextStroke++]);
        }
        return strokes.get(lbl);
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

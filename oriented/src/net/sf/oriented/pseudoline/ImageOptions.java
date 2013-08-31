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
    public boolean showVertices;
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
    public int labelBorder;
    public float lineWidth;

    private ImageOptions() {

        showOrigin = true;
        showLabels = true;
        showVertices = true;
        infinityColor = Color.BLACK;
        infinityStroke = new float[]{ 100 , 0};
        height = 1000;
        width = 1500;
        border = 30;
        labelPosition = 18;
        fill = new Color(255, 255, 204);
        background = Color.WHITE;
        foreground = Color.BLACK;
        vertexSize = 10;
        fontSizeRatio = 1.0;
        lineThickness = 1.0;
        originArrowLength = 35;
        originArrowSize = 8;
        labelBorder = 1;
        lineWidth = 1.5f;
    }
    
    private double internalFontSizeRatio = 2;
    
    private Color infinityColor;
    private float infinityStroke[];
    
    private Map<Label,Color> colors = new HashMap<Label,Color>();
    private Map<Label,float[]> strokes = new HashMap<Label,float[]>();
    private Label infinity;
    private int nextColor = 0;
    private int nextStroke = 0;
    
    private static Color someColors[] = new Color[18];

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
    public void setStrokeOfInfinity(float ... dashes) {
        infinityStroke = dashes;
    }
    public void setColor(Label lbl, Color c) {
        colors.put(lbl,c);
        
    }
    public void setStroke(Label lbl, float ...dashes ) {
        strokes.put(lbl,dashes);
    }

    public static ImageOptions defaultBlackAndWhite() {
        ImageOptions rslt = new ImageOptions(){
            @Override
            Color getColor(Label lbl) { 
                return Color.BLACK;
            }
        };
        rslt.background = rslt.fill = Color.white;
        return  rslt;
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
                    if (i+j+k==5) {
                        continue;
                    }
                    if (i+j+k==4 && i*j*k==2 && k != 2) {
                        continue;
                    }
                    someColors[ix++] = new Color((3+6*i)*17,(3+6*j)*17,(3+6*k)*17);
                }
        Arrays.sort(someColors,new Comparator<Color>(){
            @Override
            public int compare(Color o1, Color o2) {
                // pseudorandom order
                return System.identityHashCode(o1) - System.identityHashCode(o2);
            }});
    }
    private static float someStrokes[][] = new float[][]{
            { 100, 10 },
            { 30, 10 },
            { 50, 10, 20, 10, 20, 10 },
            { 70, 10, 20, 10 },
            { 50, 10, 50, 10, 20, 10},
            { 50, 10, 50, 10, 20, 10, 20, 10 },
            { 100, 10, 20, 10 },
            { 100, 10, 20, 10 , 20, 10 },
            { 100, 10, 20, 10 , 50, 10, 20, 10 },
            { 100, 10, 50, 10, 20, 10 },
            { 100, 10, 20, 10, 20, 10, 20, 10 },      
    };
    static {
        for (float [] stroke:someStrokes) {
            for (int i=0;i<stroke.length;i++) {
                stroke[i] /= 2;
            }
        }
    }
    
    public static ImageOptions defaultColor() {
        return  new ImageOptions();
    }
    
    float fontSizeRatio() {
        return (float)(fontSizeRatio * internalFontSizeRatio);
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
            if (nextColor == someColors.length) {
                nextColor = 0;
            }
//            System.err.println(lbl+" "+color);
            colors.put(lbl, color);
        }
        return colors.get(lbl);
    }
    float[] getStroke(Label lbl){ 
        if ( lbl == infinity && infinityStroke != null) {
            return infinityStroke;
        }
        if (strokes.get(lbl)==null) {
            strokes.put(lbl, someStrokes[nextStroke++]);
            if (nextStroke == someStrokes.length) {
                nextStroke = 0;
            }
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

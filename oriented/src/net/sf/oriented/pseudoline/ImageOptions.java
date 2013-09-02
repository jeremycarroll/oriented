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

/**
 * Settings for drawing pseudoline diagrams.
 * Many of the settings are public fields,
 * this should just be set to the desired values.
 * Some of the settings are hidden and should be set with the public methods.
 * To construct a new settings object use either {@link #defaultColor()}
 * or {@link #defaultBlackAndWhite()} and then modify.
 * Most of the numeric fields will scale with the width and the height, and do not need
 * to be independently modified.
 * @author jeremycarroll
 *
 */
public class ImageOptions {
    boolean showLabels;
    /**
     * Whether to show the origin or not.
     */
    public boolean showOrigin;
    /**
     * Whether to high light vertices with a circle around them
     */
    public boolean showVertices;
    /**
     * The width and height of the intended image. Values of around 1000 are suggested.
     */
    public int height, width;
    
    /**
     * The width of the border in which line labels are shown.
     * Note this may be modified as a side effect of {@link #setShowLabels(boolean)}
     */
    public double border;
    
    /**
     * The position of the labels within the border.
     * This value should be less than, and approxiamtely half of, {@link #border}
     */
    public double labelPosition;
    
    /**
     * Any scaling that should be applied to the font size.
     */
    public double fontSizeRatio;
    
    /**
     * The size of the vertex circle if shown.
     */
    public int vertexSize;
    
    /**
     * The color of the background to the rectangle, outside the picture
     */
    public Color background;
    /**
     * The color of the faces inside the picture
     */
    public Color fill;
    /**
     * The foregound color that is used for text, vertices, and the origin.
     */
    public Color foreground;
    
    /**
     * The length of the arrow used to show the origin.
     */
    public int originArrowLength;
    /**
     * The size of the arrowhead used to show the origin.
     */
    public int originArrowSize;
    /**
     * An additional border around labels.
     */
    public int labelBorder;
    /**
     * The width of lines in the diagram.
     */
    public float lineWidth;

    private ImageOptions() {

        showOrigin = true;
        showLabels = true;
        showVertices = true;
        infinityColor = Color.BLACK;
        infinityStroke = new float[]{ 100 , 0};
        height = 1000;
        width = 1618;
        border = 30;
        labelPosition = 18;
        fill = new Color(255, 255, 204);
        background = Color.WHITE;
        foreground = Color.BLACK;
        vertexSize = 10;
        fontSizeRatio = 1.0;
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
     * Provides the color for the line at infinity, if null then
     * the color is chosen like any other line.
     * @param c The color or null.
     */
    public void setColorOfInfinity(Color c) {
        infinityColor = c;
    }
    /**
     * Provides the dashes for the line at infinity, if null then
     * the dashes are chosen like any other line.
     * @param dashes The dashes or null.
     * @see java.awt.BasicStroke#getDashArray()
     */
    public void setStrokeOfInfinity(float ... dashes) {
        infinityStroke = dashes;
    }
    /**
     * Provides the color for a line.
     * This has no effect for black and white images.
     * For color images there are 18 default colors which are used.
     * 
     * @param lbl The line label
     * @param c A color, non-null.
     */
    public void setColor(Label lbl, Color c) {
        colors.put(lbl,c);
    }
    /**
     * Provides the dash array for a line.
     * 
     * @param lbl The line label
     * @param dashes The dash array, non-null.
     * @see java.awt.BasicStroke#getDashArray()
     */
    public void setStroke(Label lbl, float ...dashes ) {
        strokes.put(lbl,dashes);
    }

    /**
     * Get image options that generate a black and white image.
     * @return A fresh instance of suggested image options
     */
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

    /**
     * Get image options that generate a color image.
     * @return A fresh instance of suggested image options
     */
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
    /**
     * Either show labels or not.
     * @param val true for showing labels,
     */
    public void setShowLabels(boolean val) {
        showLabels = val;
        if (!showLabels) {
            border = 1;
        } else {
            if (border == 1) {
                border = 30;
            }
        }
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

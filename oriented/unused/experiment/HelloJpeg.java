/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package experiment;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

public class HelloJpeg {

    public static void main(String args[]) throws IOException {
        for (String fmt: ImageIO.getWriterMIMETypes()) {
            System.err.println(fmt);
        }
        ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
        
        iw.setOutput(ImageIO.createImageOutputStream(new File("/Users/jeremycarroll/tmp/foo.jpeg")));
        BufferedImage image = new BufferedImage(1000,1000,BufferedImage.TYPE_INT_RGB);
            
        Graphics2D graphics = image.createGraphics();
        graphics.setBackground(Color.WHITE);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        
        Shape circ = new Ellipse2D.Double(-400, -400, 800, 800);
        graphics.setColor( new Color(255, 255, 200));
        graphics.translate(500, 500);
        graphics.scale(1,1);
        graphics.fill(circ);
        Stroke stroke = graphics.getStroke();
        if (stroke instanceof BasicStroke) {
            System.err.println(((BasicStroke)stroke).getLineWidth());
        }
        graphics.setStroke(new BasicStroke(2.0f));
        graphics.setColor(Color.RED);
        graphics.draw(circ);
        
        
        
        
//        graphics.clearRect(5, 5, 140, 240);
//        graphics.setColor(Color.YELLOW);
//        graphics.fillRect(5, 5, 140, 240);
        graphics.setColor(Color.BLUE);
Font f = graphics.getFont();
float size = 2.0f*f.getSize2D();
System.err.println(size);
graphics.setFont(f.deriveFont(size));
        graphics.drawString("hello world", 0, 0);
        iw.write(image);
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

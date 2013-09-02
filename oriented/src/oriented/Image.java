/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package oriented;

import static net.sourceforge.argparse4j.impl.Arguments.*;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.CoLoopUnrepresentableException;
import net.sf.oriented.pseudoline.PseudoLineDrawing;
import net.sf.oriented.pseudoline.ImageOptions;
import net.sf.oriented.pseudoline.PseudoLines;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * 
Create a pseudoline picture corresponding to one of the example oriented matroids.

Usage:
<pre>
Image [-h] [-W WIDTH] [-L LINE-WIDTH] [-H HEIGHT] [-o OUTPUT] [-m] [--nolabels] [--novertex]
             [--noorigin] [-∞ INFINITY] (--wheel12 | --tsukamoto13.-1 | --chapter1 | --πάππος |
             --omega14.-1 | --Ω14.0 | --omega14.0 | --circularsaw3 | --suvorov14 | --tsukamoto13.+1 |
             --ceva | --tsukamoto13.0 | --pappus | --Ω14.+1 | --omega14.+1 | --Ω14.-1 | --uniform4 |
             --uniform3 | --ringel) [--png | --jpeg | --x-png | --vnd.wap.wbmp | --gif | --bmp]
</pre>
<p>
optional arguments:
</p>
<dl>
<dt>
  -h, --help
<dt><dd>show this help message and exit</dd>
<dt>-W WIDTH, --width WIDTH</dt><dd>
                         set the width of the output.</dd>
<dt>-L LINE-WIDTH, --line-width LINE-WIDTH</dt><dd>
                         set the width of the lines.</dd>
<dt>-H HEIGHT, --height HEIGHT</dt><dd>
                         set the height of the output.</dd>
<dt>-o OUTPUT, --output OUTPUT</dt><dd>
                         sets the output file.</dd>
<dt>-m, --monochrome</dt><dd>       black and white.</dd>
<dt>--nolabels</dt><dd>             omit labels.</dd>
<dt>--novertex</dt><dd>              omit vertex indicators.</dd>
<dt>--noorigin</dt><dd>              omit origin marker.</dd>
<dt>-∞ INFINITY, -8 INFINITY, --infinity INFINITY</dt><dd> 
                         The label of the line at infinity</dd>
</dl>
<p>
Select exactly one of the following to choose the oriented matroid. The list in this documentation may be out of date,
use the <code>--help</code> option to get the up-to-date list.
</p>
<ul>
<li>
  --wheel12 </li><li>
  --tsukamoto13.-1</li><li>
  --chapter1             </li><li>
  --πάππος               </li><li>
  --omega14.-1           </li><li>
  --Ω14.0                </li><li>
  --omega14.0            </li><li>
  --circularsaw3         </li><li>
  --suvorov14            </li><li>
  --tsukamoto13.+1       </li><li>
  --ceva                 </li><li>
  --tsukamoto13.0        </li><li>
  --pappus               </li><li>
  --Ω14.+1               </li><li>
  --omega14.+1           </li><li>
  --Ω14.-1               </li><li>
  --uniform4             </li><li>
  --uniform3             </li><li>
  --ringel               </li>
</ul>
<p>
The image format defaults as jpeg, the following list of alternatives is system dependent.
The list in this documentation reflects those available on a Mac 10.8.4, with Java 1.7.0.21.
Use the <code>--help</code> option to see the options available to you.
</p>
<dl>
<dt>
  --png</dt><dd>                    Output in image/png format.</dd>
<dt>
  --jpeg   </dt><dd>                Output in image/jpeg format.</dd>
<dt>
  --x-png   </dt><dd>               Output in image/x-png format.</dd>
<dt>
  --vnd.wap.wbmp   </dt><dd>        Output in image/vnd.wap.wbmp format.</dd>
<dt>
  --gif           </dt><dd>         Output in image/gif format.</dd>
<dt>
  --bmp          </dt><dd>          Output in image/bmp format.</dd>
</dl>
 */
public class Image {
    
    private Image(){}

    public static void main(String args[]) throws IOException, CoLoopUnrepresentableException {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Image")
                .description("Create jpeg of oriented matrorid.");
        MutuallyExclusiveGroup omGroup = parser.addMutuallyExclusiveGroup("Oriented Matroid");
        omGroup.required(true);
        for (String name : Examples.all().keySet()) {
           omGroup.addArgument("--" + name).dest("om").setConst(name).action(storeConst())
                .help("select oriented matroid.");
        }
        MutuallyExclusiveGroup mimeGroup = parser.addMutuallyExclusiveGroup("Image Format");
        mimeGroup.required(false);
        for (String mime : ImageIO.getWriterMIMETypes()) {
            if (mime.startsWith("image/")) {
                String subType = mime.substring(6);
                mimeGroup.addArgument("--" + subType).dest("mimeType").setConst(mime).setDefault("image/jpeg").action(storeConst())
                .help("Output in "+mime+" format.");
            }
        }
        parser.addArgument("-W","--width").action(store()).dest("width").type(Integer.class).setDefault(1618).help("set the width of the output.");
        parser.addArgument("-L","--line-width").action(store()).dest("line-width").type(Float.class).setDefault(1.0).help("set the width of the lines.");
        parser.addArgument("-H","--height").action(store()).dest("height").type(Integer.class).setDefault(1000).help("set the height of the output.");
        parser.addArgument("-o","--output").action(store()).dest("output").help("sets the output file.");
        parser.addArgument("-m","--monochrome").action(storeTrue()).dest("monochrome").setDefault(false).help("black and white."); 
        parser.addArgument("--nolabels").action(storeFalse()).dest("labels").setDefault(true).help("omit labels.");     
        parser.addArgument("--novertex").action(storeFalse()).dest("vertices").setDefault(true).help("omit vertex indicators.");     
        parser.addArgument("--noorigin").action(storeFalse()).dest("origin").setDefault(true).help("omit origin marker.");     
        parser.addArgument("-∞","-8","--infinity").help("The label of the line at infinity");
        Namespace settings;
        try {
            settings = parser.parseArgs(args);
        }
        catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
            return;
        }
        OM om = Examples.all().get(settings.getString("om"));
        ImageOptions options = settings.getBoolean("monochrome") ? ImageOptions.defaultBlackAndWhite() : ImageOptions.defaultColor(); 
        options.width = settings.getInt("width");
        options.height = settings.getInt("height");
        options.showOrigin = settings.getBoolean("origin");
        options.showVertices = settings.getBoolean("vertices");
        options.lineWidth = settings.getFloat("line-width");
        options.setShowLabels(settings.getBoolean("labels"));
        String infinity = om.elements()[0].label();
        if ( settings.get("infinity") != null) {
            infinity = settings.getString("infinity");
        }
        PseudoLines pseudoLines = new PseudoLines(om, infinity);
        PseudoLineDrawing euclid = pseudoLines.asDrawing();
        ImageWriter iw = ImageIO.getImageWritersByMIMEType(settings.getString("mimeType")).next();
        String fname = settings.getString("output");
        Object output;
        if (fname == null) {
            output = System.out;
        } else {
            output = new File(fname);
        }
        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(output);
        iw.setOutput(imageOutput);
        iw.write(euclid.image(options));
        euclid.checkForOverlappingEdge();
        imageOutput.close();
        iw.dispose();
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

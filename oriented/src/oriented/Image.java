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
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.ImageOptions;
import net.sf.oriented.pseudoline.PseudoLines;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;

public class Image {

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
        options.setShowLabels(settings.getBoolean("labels"));
        String infinity = om.elements()[0].label();
        if ( settings.get("infinity") != null) {
            infinity = settings.getString("infinity");
        }
        PseudoLines pseudoLines = new PseudoLines(om, infinity);
        EuclideanPseudoLines euclid = pseudoLines.asEuclideanPseudoLines();
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

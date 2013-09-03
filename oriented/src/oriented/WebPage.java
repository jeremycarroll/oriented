/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package oriented;

import static net.sourceforge.argparse4j.impl.Arguments.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.OM;
import net.sf.oriented.pseudoline.CoLoopCannotBeDrawnException;
import net.sf.oriented.pseudoline.PseudoLineDrawing;
import net.sf.oriented.pseudoline.ImageOptions;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.util.combinatorics.CoLexicographic;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;


public class WebPage {
    
    static String prefix = "doc/";
    
    private WebPage(){}

    public static void main(String args[]) throws IOException, CoLoopCannotBeDrawnException {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Image")
                .description("Create web pages about oriented matrorid.");
        MutuallyExclusiveGroup omGroup = parser.addMutuallyExclusiveGroup("Oriented Matroid");
        omGroup.required(true);
        for (String name : Examples.all().keySet()) {
           omGroup.addArgument("--" + name).dest("om").setConst(name).action(storeConst())
                .help("select oriented matroid.");
        }
        parser.addArgument("-∞","-8","--infinity").action(append()).help("The label of the line at infinity");
        Namespace settings;
        try {
            settings = parser.parseArgs(args);
        }
        catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
            return;
        }
        String name = settings.getString("om");
        OM om = Examples.all().get(name);
        if (settings.get("infinity") != null) {
            List<String> inf = settings.getList("infinity");
            for (String in:inf) {
                preparePage(om,name, in);
            }
        } else {
            preparePage(om, name, om.elements()[0].label());
        }
        
//        ImageOptions options = settings.getBoolean("monochrome") ? ImageOptions.defaultBlackAndWhite() : ImageOptions.defaultColor(); 
//        options.width = settings.getInt("width");
//        options.height = settings.getInt("height");
//        options.showOrigin = settings.getBoolean("origin");
//        options.showVertices = settings.getBoolean("vertices");
//        options.lineWidth = settings.getFloat("line-width");
//        options.setShowLabels(settings.getBoolean("labels"));
//        if ( settings.get("infinity") != null) {
//            infinity = settings.getString("infinity");
//        }
//        EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om, infinity);
//        PseudoLineDrawing euclid = pseudoLines.asDrawing();
//        ImageWriter iw = ImageIO.getImageWritersByMIMEType(settings.getString("mimeType")).next();
//        String fname = settings.getString("output");
//        Object output;
//        if (fname == null) {
//            output = System.out;
//        } else {
//            output = new File(fname);
//        }
//        ImageOutputStream imageOutput = ImageIO.createImageOutputStream(output);
//        iw.setOutput(imageOutput);
//        iw.write(euclid.image(options));
//      //  euclid.checkForOverlappingEdge();
//        imageOutput.close();
//        iw.dispose();
    }

    private static void preparePage(OM om, String name, String in) throws IOException, CoLoopCannotBeDrawnException {
      ImageOptions options = ImageOptions.defaultBlackAndWhite();
      options.width = 809;
      options.height = 500;
      options.showVertices = false;
      options.showOrigin = false;
      options.setShowLabels(false);
      EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om, in);
      PseudoLineDrawing euclid = pseudoLines.asDrawing();
      ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
      ImageOutputStream imageOutput = ImageIO.createImageOutputStream(new File(prefix+"images/small-" + name + "-" + in + ".jpg"));
      iw.setOutput(imageOutput);
      iw.write(euclid.image(options));
      imageOutput.close();
      imageOutput = ImageIO.createImageOutputStream(new File(prefix+"images/" + name + "-" + in + ".jpg"));
      iw.setOutput(imageOutput);
      iw.write(euclid.image());
      imageOutput.close();
      iw.dispose();
      File description = new File(prefix+"detail/"+name+"-"+in+".html");
      Writer out = new OutputStreamWriter(new FileOutputStream(description),"utf-8");
      out.write("<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html\" charset=\"UTF-8\"/>\n<link type=\"text/css\" rel=\"stylesheet\" href=\"../jjc.css\"/>\n</head>\n<body>\n");
      out.write("<h2>"+name+" with "+in+" at infinity</h2>\n");
      if (pseudoLines.getReorientation().length!=0) {
          out.write("<h3>Reoriented</h3>\n");
          out.write("<p>"+pseudoLines.getReorientation()[0].label());
          for (int i=1;i<pseudoLines.getReorientation().length;i++) {
              out.write(", "+pseudoLines.getReorientation()[i].label());
          }
          out.write("</p>\n");
      }
      out.write("<h3>Bases</h3>\n");
      
      writeBases(out,"Positively Oriented",1,pseudoLines.getEquivalentOM());
      writeBases(out,"Negatively Oriented",-1,pseudoLines.getEquivalentOM());
      writeBases(out,"Not Bases",0,pseudoLines.getEquivalentOM());
      
      out.write("<h2>The Pseudolines</h2>\n");
      out.write("<img src=\"../images/"+name+"-"+in+".jpg\" width=\"100%\"/>\n");
      out.write("</body>\n</html>");
      out.close();
    }

    private static void writeBases(Writer out, String heading, int orientation, OM om) throws IOException {
        out.write("<h3>"+heading+"</h3>\n");
        for (int ix[]:new CoLexicographic(om.n(),3)) {
            if ( om.getChirotope().chi(ix)== orientation) {
                out.write(om.asSet(ix).toString()+", ");
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

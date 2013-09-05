/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package oriented;

import static net.sourceforge.argparse4j.impl.Arguments.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.Options;
import net.sf.oriented.omi.SetFactory;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.CoLoopCannotBeDrawnException;
import net.sf.oriented.pseudoline.PseudoLineDrawing;
import net.sf.oriented.pseudoline.ImageOptions;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.util.combinatorics.Lexicographic;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;

public class WebPage {

    static String prefix = "doc/";
    private final OM om;
    private final String infinity;
    private final String name;
    private final String htmlName;
    private FactoryFactory factory;
    private static ImageOptions colors = ImageOptions.defaultColor();

    private WebPage(OM om, String htmlName, String name, String in) {
        this.om = om;
        this.name = name;
        this.infinity = in;
        this.htmlName = htmlName;
    }

    private void setFactory(OM om) {
        Options opt = new Options();
        opt.setShortLabels();
        opt.setUniverse(getElements(om).toArray(new String[0]));
        factory = new FactoryFactory(opt);
    }

    private static List<String> getElements(OM om) {
        return Lists.transform(Arrays.asList(om.elements()),
                new Function<Label, String>() {
                    @Override
                    public String apply(Label l) {
                        return l.label();
                    }
                });
    }

    static Writer indexPage;
    private static int entryCount = 0;

    public static void main(String args[]) throws IOException,
            CoLoopCannotBeDrawnException {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Image")
                .description("Create web pages about oriented matrorid.");
        MutuallyExclusiveGroup omGroup = parser
                .addMutuallyExclusiveGroup("Oriented Matroid");
        omGroup.required(true);
        Set<String> done = new HashSet<String>();
        for (String name : Examples.all().keySet()) {
            if (name.contains(".")) {
                name = name.substring(0,name.indexOf('.'));
                if (!done.add(name)) {
                    continue;
                }
            }
            omGroup.addArgument("--" + name).dest("om").setConst(name)
                    .action(storeConst()).help("select oriented matroid.");
        }
        String plusGroupTitle = "Sign for ";
        Iterator<String> it = done.iterator();
        while (it.hasNext()) {
            plusGroupTitle = plusGroupTitle + " " + it.next() + (it.hasNext()?",":"");
        }
        MutuallyExclusiveGroup plusGroup = parser
                .addMutuallyExclusiveGroup(plusGroupTitle);
        plusGroup.required(false);
        plusGroup.addArgument("-0","--zero").dest("sign").action(storeConst()).setConst(0).setDefault(-2).type(Integer.class).help("Set variable value to 0");
        plusGroup.addArgument("--plus").dest("sign").action(storeConst()).setConst(1).setDefault(-2).type(Integer.class).help("Set variable value to +1");
        plusGroup.addArgument("--minus").dest("sign").action(storeConst()).setConst(-1).setDefault(-2).type(Integer.class).help("Set variable value to -1");
        parser.addArgument("-∞", "-8", "--infinity").action(append())
                .help("The label of the line at infinity");
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
        int signInt = settings.getInt("sign");
        String sign = new String[]{"",".-1",".0",".+1"}[signInt+2];
        String extName = name + sign;
        String htmlName = name + (signInt == -2 ? "" : ("<sup>" + new String[]{"&minus;",".0","&plus;"}[signInt+1] + "</sup>") );
        
        OM om = Examples.all().get(extName);
        if (om == null) {
            System.err.println("Must"+(signInt == -2?"":" not")+" give the (--plus|--zero|--minus) option with the "
                    + settings.getString("om")+ " oriented matroid.");
            System.exit(1);
        }
        indexPage = startHtmlPage(extName+".html");
        indexPage.write("<h2>The "+htmlName+" oriented matroid</h2>\n");
        indexPage.write("<table border='0'>\n");
        List<String> inf;
        if (settings.get("infinity") != null) {
            inf = settings.getList("infinity");
        } else {
            inf = getElements(om);
        }
        for (String in : inf) {
            new WebPage(om, htmlName, extName, in).preparePage();
        }
        endHtmlPage(indexPage);
    }

    private void preparePage() throws IOException, CoLoopCannotBeDrawnException {
        ImageOptions options = ImageOptions.defaultBlackAndWhite();
        options.width = 809;
        options.height = 500;
        options.showVertices = false;
        options.showOrigin = false;
        options.setShowLabels(false);
        EuclideanPseudoLines pseudoLines = new EuclideanPseudoLines(om,
                infinity);
        OMasChirotope eOM = pseudoLines.getEquivalentOM().getChirotope();
        setFactory(eOM);
        PseudoLineDrawing euclid = pseudoLines.asDrawing();
        ImageWriter iw = ImageIO.getImageWritersByMIMEType("image/jpeg").next();
        String smallImage = "images/small-" + name + "-" + infinity + ".jpg";
        String largeImage = "images/" + name + "-" + infinity + ".jpg";
        String detailPage = "detail/" + name + "-" + infinity + ".html";
        ImageOutputStream imageOutput = ImageIO
                .createImageOutputStream(new File(prefix + smallImage));
        maybeStartTableRow();
        indexPage.write("<td ><a href=\"" +detailPage + "\"><center><img width=\"80%\" src=\""+ smallImage + "\"/>\n" );
        indexPage.write("<br/>\n");
        indexPage.write("With line "+infinity+" projected to infinity.</center></a>");
        indexPage.write("</td>\n");
        maybeEndTableRow();
        iw.setOutput(imageOutput);
        iw.write(euclid.image(options));
        imageOutput.close();
        imageOutput = ImageIO.createImageOutputStream(new File(prefix + largeImage));
        iw.setOutput(imageOutput);
        iw.write(euclid.image(colors));
        imageOutput.close();
        iw.dispose();
        Writer out = startHtmlPage(detailPage);
        out.write("<h2>" + htmlName + " with line " + infinity
                + " projected to infinity</h2>\n");
        if (pseudoLines.getReorientation().length != 0) {
            out.write("<h3>Reoriented</h3>\n");
            out.write("<p>" + pseudoLines.getReorientation()[0].label());
            for (int i = 1; i < pseudoLines.getReorientation().length; i++) {
                out.write(", " + pseudoLines.getReorientation()[i].label());
            }
            out.write("</p>\n");
        }
        out.write("<h3>Line Order At Infinity</h3>\n<p>");
        Label e[] = eOM.elements();
        out.write(e[1].label());
        for (int i = 2; i < e.length; i++) {
            switch (eOM.chi(0, i - 1, i)) {
            case 1:
                out.write("&lt; ");
                break;
            case 0:
                out.write(", ");
                break;
            case -1:
            default:
                throw new IllegalStateException("Shouldn't happen");
            }
            out.write(e[i].label());
        }
        out.write("</p>\n");
        out.write("<h3>Bases</h3>\n");

        writeBases(out, "Positively Oriented", 1, eOM);
        writeBases(out, "Negatively Oriented", -1, eOM);
        writeBases(out, "Not Bases", 0, eOM);

        out.write("<h2>The Pseudolines</h2>\n");
        out.write("<img src=\"../" + largeImage + "\" width=\"100%\"/>\n");
        endHtmlPage(out);
    }

    private static void maybeEndTableRow() throws IOException {
        if (++entryCount % 3 == 0) {
            indexPage.write("</tr>");
         }
    }

    private static void maybeStartTableRow() throws IOException {
        if (entryCount % 3 == 0) {
           indexPage.write("<tr>");
        }
    }

    private static void endHtmlPage(Writer out) throws IOException {
        if ( entryCount % 3 != 0) {
            out.write("</tr>");
        }
        out.write("</table></body>\n</html>");
        out.close();
    }

    private static Writer startHtmlPage(String pageName)
            throws UnsupportedEncodingException, FileNotFoundException,
            IOException {
        String dots = pageName.contains("/") ? "../" : "";
        File description = new File(prefix + pageName);
        Writer out = new OutputStreamWriter(new FileOutputStream(description),
                "utf-8");
        out.write("<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html\" charset=\"UTF-8\"/>\n<link type=\"text/css\" rel=\"stylesheet\" href=\""
                + dots + "jjc.css\"/>\n</head>\n<body>\n");
        return out;
    }

    private void writeBases(Writer out, String heading, int orientation, OM om)
            throws IOException {
        SetFactory<Label, UnsignedSet> f = factory.unsignedSets();
        out.write("<h3>" + heading + "</h3>\n");
        for (int ix[] : new Lexicographic(om.n(), 3)) {
            if (ix[0] == 0) {
                continue;
            }
            if (om.getChirotope().chi(ix) == orientation) {
                out.write(f.remake(om.asSet(ix)).toString() + ", ");
            }
        }
    }

}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * The Java Oriented Matroid Library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * the Java Oriented Matroid Library. If not, see
 * <http://www.gnu.org/licenses/>.
 **************************************************************************/

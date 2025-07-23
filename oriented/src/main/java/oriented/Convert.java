/************************************************************************
 (c) Copyright 2022 Jeremy J. Carroll
 
************************************************************************/
package oriented;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.MutuallyExclusiveGroup;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.Factory;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.OMasRealized;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.Options;

import static net.sourceforge.argparse4j.impl.Arguments.storeConst;

/**
 * A command-line program to convert between different oriented matroid representations.
 */
public class Convert {

    /**
     * Represents a supported oriented matroid representation format
     */
    private enum Representation {
        CHIROTOPE("chirotope", om -> om.getChirotope(), factory -> factory.chirotope(), false),
        CIRCUITS("circuits", om -> om.getCircuits(), factory -> factory.circuits(), false),
        COCIRCUITS("cocircuits", om -> om.dual().getCircuits(), factory -> factory.circuits(), true),
        VECTORS("vectors", om -> om.getVectors(), factory -> factory.vectors(), false),
        COVECTORS("covectors", om -> om.dual().getVectors(), factory -> factory.vectors(), true),
        MAXVECTORS("maxvectors", om -> om.getMaxVectors(), factory -> factory.maxVectors(), false),
        TOPES("topes", om -> om.dual().getMaxVectors(), factory -> factory.maxVectors(), true),
        MATRIX("matrix", om -> {
            try {
                return om.getRealized();
            } catch (UnsupportedOperationException e) {
                throw new IllegalArgumentException("This oriented matroid is not realizable");
            }
        }, factory -> factory.realized(), false);

        private final String name;
        private final Function<OM, Object> getter;
        private final Function<FactoryFactory, Object> factoryGetter;
        private final boolean isDual;
        
        Representation(String name, Function<OM, Object> getter, 
                      Function<FactoryFactory, Object> factoryGetter, boolean isDual) {
            this.name = name;
            this.getter = getter;
            this.factoryGetter = factoryGetter;
            this.isDual = isDual;
        }
        
        public String getName() {
            return name;
        }
        
        public boolean isDual() {
            return isDual;
        }
        
        public String outputOM(OM om) {
            Object representation = getter.apply(om);
            return representation.toString();
        }
        
        public Factory<?> getFactory(FactoryFactory factory) {
            return (Factory<?>) factoryGetter.apply(factory);
        }
        
        public static Representation fromString(String name) {
            for (Representation rep : values()) {
                if (rep.name.equalsIgnoreCase(name)) {
                    return rep;
                }
            }
            throw new IllegalArgumentException("Unknown representation: " + name);
        }
    }

    /**
     * Runs the convert operation with the provided arguments.
     * This method is used internally and by tests to avoid System.exit() calls.
     *
     * @param args Command line arguments
     * @return 0 if successful, non-zero on error
     */

    public static int runConvert(String[] args) {
        ArgumentParser parser = setupArgumentParser();
        
        try {
            Namespace settings = parser.parseArgs(args);
            
            OM om = loadOrientedMatroid(settings);
            if (om == null) {
                System.err.println("ERROR: loadOrientedMatroid returned null");
                return 3;
            }

            String output = convertToOutputFormat(om, settings);
            writeOutput(output, settings);
            return 0;
        } catch (ArgumentParserException e) {
            System.err.println("Argument parsing error: " + e.getMessage());
            parser.handleError(e);
            return 1;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return 2;
        }
    }

    /**
     * Main entry point for the Convert CLI tool.
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        int exitCode = runConvert(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    // Cache to avoid repeatedly calling Examples.all() which can cause threading issues
    private static ArgumentParser cachedParser;
    
    private static ArgumentParser setupArgumentParser() {
        // Return the cached parser if it's available
        if (cachedParser != null) {
            return cachedParser;
        }
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Convert")
                .description("Convert oriented matroids between representations");
        
        // Input source group (mutually exclusive)
        MutuallyExclusiveGroup inputGroup = parser.addMutuallyExclusiveGroup("Input Source");
        
        // Add example oriented matroids
        Set<String> exampleBaseNames = new HashSet<>();
        for (String name : Examples.all().keySet()) {
            if (name.contains(".")) {
                String baseName = name.substring(0, name.indexOf('.'));
                if (exampleBaseNames.contains(baseName)) {
                    continue;
                }
                exampleBaseNames.add(baseName);
                inputGroup.addArgument("--" + baseName).dest("inputSource").setConst(baseName)
                        .action(storeConst()).help("Use oriented matroid from Examples");
            } else {
                inputGroup.addArgument("--" + name).dest("inputSource").setConst(name)
                        .action(storeConst()).help("Use oriented matroid from Examples");
            }
        }
        
        // Add input file option
        inputGroup.addArgument("-i", "--input").dest("inputSource")
                .help("Input file path (use - for stdin)");
        
        // Input format (required for file input)
        MutuallyExclusiveGroup inputFormatGroup = parser.addMutuallyExclusiveGroup("Input Format");
        
        // Add all supported representations for input
        for (Representation rep : Representation.values()) {
            inputFormatGroup.addArgument("--from-" + rep.getName()).dest("inputFormat")
                    .action(storeConst()).setConst(rep.getName())
                    .help("Input is in " + rep.getName() + " format");
        }
        
        // Add special input formats
        inputFormatGroup.addArgument("--from-crossings").dest("inputFormat")
                .action(storeConst()).setConst("crossings")
                .help("Input is in pseudoline crossings format");
        
        // Output format (required)
        MutuallyExclusiveGroup outputFormatGroup = parser.addMutuallyExclusiveGroup("Output Format")
                .required(true);
        
        // Add all supported representations for output
        for (Representation rep : Representation.values()) {
            outputFormatGroup.addArgument("--to-" + rep.getName()).dest("outputFormat")
                    .action(storeConst()).setConst(rep.getName())
                    .help("Output in " + rep.getName() + " format");
        }
        
        // Output destination
        parser.addArgument("-o", "--output").dest("output")
                .help("Output file path (default: stdout)");
        
        // Add sign parameter for parameterized oriented matroids from Examples
        MutuallyExclusiveGroup signGroup = parser.addMutuallyExclusiveGroup("Sign Parameter");
        signGroup.addArgument("--plus").dest("sign").action(storeConst()).setConst(1)
                .type(Integer.class).help("Set variable value to +1");
        signGroup.addArgument("--zero").dest("sign").action(storeConst()).setConst(0)
                .type(Integer.class).help("Set variable value to 0");
        signGroup.addArgument("--minus").dest("sign").action(storeConst()).setConst(-1)
                .type(Integer.class).help("Set variable value to -1");
        
        // Matrix precision for double matrix inputs
        parser.addArgument("--precision").dest("precision").type(Double.class)
                .setDefault(1000.0).help("Precision threshold for matrix realization (default: 1000.0)");
        
        return parser;
    }

    private static OM loadOrientedMatroid(Namespace settings) throws Exception {
        String inputSource = settings.getString("inputSource");
        String inputFormat = settings.getString("inputFormat");

        
        // If input format is specified but no source, read from stdin
        if (inputSource == null && inputFormat != null) {
            inputSource = "-";
        } else if (inputSource == null && inputFormat == null) {
            throw new IllegalArgumentException("Either an input source or an input format must be specified");
        }
        
        // Check if file exists first to prevent confusing files with examples
        File inputFile = new File(inputSource);
        if (inputFile.exists() && inputFile.isFile()) {
            
            // Read from file
            String content = readFromFile(inputSource);
            
            // Parse based on input format
            if (inputFormat == null) {
                throw new IllegalArgumentException("Input format must be specified for file input");
            }
            
            return parseFromContent(content, inputFormat);
        }
        
        // If not a file, check if it's from Examples
        if (Examples.all().containsKey(inputSource)) {
            return Examples.all().get(inputSource);
        } else if (inputSource.contains(".") && !inputSource.contains("/") && !inputSource.contains("\\")) {
            // Only treat as an Examples name if it doesn't look like a file path
            if (Examples.all().containsKey(inputSource)) {
                return Examples.all().get(inputSource);
            }
        } else if (settings.getInt("sign") != null) {
            // Need to add sign suffix based on parameter
            int sign = settings.getInt("sign");
            String suffix = sign == -1 ? ".-1" : sign == 0 ? ".0" : ".+1";
            String fullName = inputSource + suffix;

            
            if (Examples.all().containsKey(fullName)) {
                return Examples.all().get(fullName);
            } else {
                // Not a parameterized example, try without suffix
                if (Examples.all().containsKey(inputSource)) {
                    return Examples.all().get(inputSource);
                }
            }
        } else if (Examples.all().containsKey(inputSource)) {
            return Examples.all().get(inputSource);
        }
        
        // Input from stdin
        if ("-".equals(inputSource)) {
            // Read from stdin
            String content = readFromStdin();
            
            // Parse based on input format
            if (inputFormat == null) {
                throw new IllegalArgumentException("Input format must be specified for stdin input");
            }
            
            return parseFromContent(content, inputFormat);
        }
        
        // If we get here, the input source is not valid
        throw new IllegalArgumentException("Invalid input source: " + inputSource + ". File does not exist and not a known example.");
    }
    
    private static OM parseFromContent(String content, String inputFormat) throws Exception {
        Options options = new Options();
        FactoryFactory factory = new FactoryFactory(options);
        
        if ("crossings".equals(inputFormat)) {
            // Special handling for crossing format
            return FactoryFactory.fromCrossings(content.split("\\n"));
        } else {
            try {
                // Standard representation formats
                Representation rep = Representation.fromString(inputFormat);
                Factory<?> parser = rep.getFactory(factory);

                
                // Parse the content to create the oriented matroid
                OM om = (OM) parser.parse(content);
                
                if (om == null) {
                    throw new IllegalArgumentException("Parser returned null for " + inputFormat);
                }

                
                // If this is a dual representation, we need to take the dual of the parsed OM
                return rep.isDual() ? om.dual() : om;
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Error parsing " + inputFormat + " input: " + e.getMessage(), e);
            }
        }
    }

    private static String convertToOutputFormat(OM om, Namespace settings) {
        String formatName = settings.getString("outputFormat");
        try {
            Representation outputFormat = Representation.fromString(formatName);
            return outputFormat.outputOM(om);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported output format: " + formatName);
        } catch (UnsupportedOperationException e) {
            throw new IllegalArgumentException("Cannot convert to " + formatName + ": " + e.getMessage());
        }
    }

    private static void writeOutput(String output, Namespace settings) throws IOException {
        String outputPath = settings.getString("output");
        if (outputPath == null) {
            // Write to stdout
            System.out.println(output);
        } else {
            // Write to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
                writer.println(output);
            }
        }
    }

    private static String readFromStdin() throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }

    private static String readFromFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString();
    }
}

/************************************************************************
* This file is part of the Java Oriented Matroid Library.
* 
* 
* 
* 
* 
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
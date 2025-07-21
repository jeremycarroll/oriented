/************************************************************************
 (c) Copyright 2025 Jeremy J. Carroll
 
************************************************************************/
package test;

import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.Examples;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.OMasRealized;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.Options;
import oriented.Convert;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Tests for the Convert CLI tool.
 * These tests verify that each representation format can be correctly written and read back in.
 */
public class TestConvertCLI extends TestWithTempDir {
    
    // Small oriented matroid (k < 10)
    private static OM smallOM;
    
    // Large oriented matroid (k > 10)
    private static OM largeOM;
    
    @BeforeClass
    public static void setupOMs() {
        // Chapter1 example has 6 elements
        smallOM = Examples.chapter1();
        
        // Tsukamoto13 has 13 elements
        largeOM = Examples.tsukamoto13(1);
    }
    
    /**
     * Test converting a small oriented matroid to all formats and back.
     * This tests the round-trip conversion for all formats using chapter1 example.
     */
    @Test
    public void testSmallOMRoundTrip() throws Exception {
        testRoundTrip(smallOM, "SmallOM");
    }
    
    /**
     * Test converting a large oriented matroid to all formats and back.
     * This tests the round-trip conversion for all formats using tsukamoto13 example.
     */
    @Test
    public void testLargeOMRoundTrip() throws Exception {
        testRoundTrip(largeOM, "LargeOM");
    }
    
    /**
     * Helper method to test round-trip conversion for all formats.
     * @param om The oriented matroid to test
     * @param prefix Prefix for temporary files
     */
    private void testRoundTrip(OM om, String prefix) throws Exception {
        // Test all representation formats
        testFormatRoundTrip(om, prefix, "chirotope");
        testFormatRoundTrip(om, prefix, "circuits");
        testFormatRoundTrip(om, prefix, "cocircuits");
        testFormatRoundTrip(om, prefix, "vectors");
        testFormatRoundTrip(om, prefix, "covectors");
        testFormatRoundTrip(om, prefix, "maxvectors");
        testFormatRoundTrip(om, prefix, "topes");
        
        // Matrix format requires realizable oriented matroids
        if (isRealizable(om)) {
            testFormatRoundTrip(om, prefix, "matrix");
        }
    }
    
    /**
     * Tests that a specific format can be written and read back in correctly.
     * @param om The oriented matroid to test
     * @param prefix Prefix for temporary files
     * @param format The format to test (chirotope, circuits, etc.)
     */
    private void testFormatRoundTrip(OM om, String prefix, String format) throws Exception {
        // First, write the oriented matroid to a file in the specified format
        String outputPath = tmp + "/" + prefix + "_" + format + ".txt";
        String[] writeArgs = {"--chapter1", "--to-" + format, "-o", outputPath};
        
        if (om == largeOM) {
            writeArgs[0] = "--tsukamoto13";
            writeArgs = new String[]{"--tsukamoto13", "--plus", "--to-" + format, "-o", outputPath};
        }
        
        // Run convert to write the file
        runConvert(writeArgs);
        
        // Verify the file was created
        File outputFile = new File(outputPath);
        Assert.assertTrue("Output file was not created", outputFile.exists());
        
        // Now read the file back in
        String[] readArgs = {"-i", outputPath, "--from-" + format, "--to-chirotope"};
        
        // Capture the standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        
        // Run convert to read the file
        runConvert(readArgs);
        
        // Restore the standard output
        System.setOut(originalOut);
        
        // Get the output (chirotope representation)
        String output = outContent.toString().trim();
        
        // Compare with the original oriented matroid's chirotope
        String expectedOutput = ((OMasChirotope)om.getChirotope()).toString();
        
        // Normalize the outputs for comparison (remove whitespace variations)
        String normalizedOutput = normalizeOutput(output);
        String normalizedExpected = normalizeOutput(expectedOutput);
        
        Assert.assertEquals("Round-trip conversion failed for format: " + format, 
                normalizedExpected, normalizedOutput);
    }
    
    /**
     * Check if an oriented matroid is realizable.
     * @param om The oriented matroid to check
     * @return true if the oriented matroid is realizable
     */
    private boolean isRealizable(OM om) {
        try {
            om.getRealized();
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }
    
    /**
     * Normalize output string for comparison by removing extra whitespace.
     * @param output The output string to normalize
     * @return The normalized output string
     */
    private String normalizeOutput(String output) {
        return output.replaceAll("\\s+", " ").trim();
    }
    
    /**
     * Run the Convert CLI tool with the given arguments.
     * @param args Command-line arguments
     */
    private void runConvert(String[] args) {
        // Save the original System.out and System.err
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        
        try {
            // Since we can't intercept System.exit() calls easily in newer Java versions,
            // we'll just run the method directly and catch any exceptions
            Convert.main(args);
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException)) {
                // Rethrow unexpected exceptions
                Assert.fail("Convert execution failed: " + e.getMessage());
            }
            // Otherwise, it's an expected IllegalArgumentException which is fine
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
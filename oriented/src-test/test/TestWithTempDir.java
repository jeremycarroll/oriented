/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package test;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TestWithTempDir {
    protected static String tmp;
    private static boolean fixedDir = true;
    
    @BeforeClass
    public static void createTmpDir() throws IOException {
        if (fixedDir) {
            // change this directory as appropriate
            tmp = "/Users/jeremycarroll/tmp";
        } else {
            File tFile = File.createTempFile("oriented", ".d");
            tmp = tFile.getAbsolutePath();
            tFile.delete();
            tFile.mkdir();
        }
    }
    
    @AfterClass
    public static void deleteTmpDir() {
        if (!fixedDir) {
            File dir = new File(tmp);
            for (File f : dir.listFiles() ) {
                f.delete();
            }
            dir.delete();
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

/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  
 ************************************************************************/

package test;

import net.sf.oriented.impl.om.OMAll;
import net.sf.oriented.omi.Options;


public class TestDualRealizedBits32 extends AbsTestDualRealized {
	private final OMAll all = TestConversions.asAll(TestRealization.testDatum(Options.Impl.bits32));

	@Override
    OMAll getAll() {
        return all;
    }
}

/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * The Java Oriented Matroid Library is free software: you can
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

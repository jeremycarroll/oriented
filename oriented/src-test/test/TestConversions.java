/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.oriented.omi.Chirotope;
import net.sf.oriented.omi.ChirotopeFactory;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMChirotope;
import net.sf.oriented.omi.OMRealized;
import net.sf.oriented.omi.OMS;
import net.sf.oriented.omi.OMSFactory;
import net.sf.oriented.omi.Options;
import net.sf.oriented.omi.RealizedFactory;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.impl.om.Cryptomorphisms;
import net.sf.oriented.omi.impl.om.OMAll;
import net.sf.oriented.omi.impl.om.OMInternal;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized.TestName;

@RunWith(value = BetterParameterized.class)
public class TestConversions {

    static FactoryFactory special;

    static String names[] = { "chapter1", "loop" };

    @Parameters
    public static Collection<Object[]> data() {

	FactoryFactory factories[] = new FactoryFactory[4];
	int n = 0;
	for (int i = 0; i < 4; i++) {
	    Options opt = new Options();
	    opt.setUniverse(new String[] { "1", "2", "3", "4", "5", "6", "7" });
	    switch (i) {
	    case 0:
		opt.setLongLabels();
		break;
	    case 1:
		opt.setShortLabels();
		break;
	    case 2:
		opt.setPlusMinus(true);
		break;
	    case 3:
		break;
	    }
	    factories[i] = new FactoryFactory(opt);
	}
	special = factories[3];

	List<Object[]> r = new ArrayList<Object[]>();
	for (int source = 0; source < 2; source++) {
	    int ff = 0;
	    for (FactoryFactory f : factories) {
		for (Cryptomorphisms from : Cryptomorphisms.values())
		    for (Cryptomorphisms to : Cryptomorphisms.values()) {
			if ((Cryptomorphisms.REALIZED.equals(to) || Cryptomorphisms.DUALREALIZED
				.equals(to))
				&& !Cryptomorphisms.REALIZED.equals(from)
				&& !Cryptomorphisms.DUALREALIZED.equals(from)) {
			    continue;
			}
			r.add(new Object[] { name(source, ff, from, to),
				source, f, from, to, n++ });
		    }
		ff++;
	    }
	}
	return r;
    }

    public static String name(int source, int ff, Cryptomorphisms from,
	    Cryptomorphisms to) {

	return "{" + sName(source) + "," + fName(ff) + "," + from + "," + to
		+ "}";
    }

    @TestName
    public static String name(String name, int s, FactoryFactory f,
	    Cryptomorphisms from, Cryptomorphisms to, int n) {
	return name;
    }

    private static String fName(int ff) {
	switch (ff) {
	case 0:
	    return "long";
	case 1:
	    return "short";
	case 2:
	    return "+/-";
	case 3:
	    return "special";
	default:
	    return "!";
	}
    }

    private static String sName(int s) {
	return names[s];
    }

    private int source;
    private FactoryFactory factory;
    private Cryptomorphisms from;
    private Cryptomorphisms to;

    static public int TEST_NUMBER;

    public TestConversions(String name, int s, FactoryFactory f,
	    Cryptomorphisms from, Cryptomorphisms to, int n) {
	source = s;
	factory = f;
	this.from = from;
	this.to = to;
	TEST_NUMBER = n;
    }

    @Test
    public void convert() {
	OMAll om = create(from);
	assertTrue(om.verify());
	OMInternal first = om.get(from);
	assertTrue(first.verify());
	checkEquals(from, om, first);
	OMInternal clean = (OMInternal) inOut(first);
	OMInternal mod = clean.asAll().get(to);
	assertNotNull(mod);
	if (!mod.verify()) {
	    System.err.println(mod);
	    fail(" verification failed");
	}
	assertEquals(first, clean);
	checkEquals(to, mod, first);
	assertNotSame(first, first.dual());
    }

    private void checkEquals(Cryptomorphisms f, OM x, OM y) {
	if (f.ordinal() < Cryptomorphisms.values().length / 2)
	    assertEquals(x, y);
	else assertEquals(x.dual(), y);
    }

    private OM inOut(OMInternal first) {
	switch (from) {
	case CHIROTOPE:
	case DUALCHIROTOPE:
	    ChirotopeFactory chi = factory.chirotope();
	    if (factory == special) {
		return chi.construct((Chirotope) first);
	    }
	    return chi.parse(chi.toString((OMChirotope) first));
	case REALIZED:
	case DUALREALIZED:
	    RealizedFactory rf = factory.realized();
	    return rf.parse(rf.toString((OMRealized) first));

	default:
	    OMSFactory f = omsFactory();
	    if (factory == special) {
		return f.fromSignedSets(first.ground(), (SetOfSignedSet) first);
	    }
	    return f.parse(f.toString((OMS) first));
	}
    }

    private OMSFactory omsFactory() {
	switch (from) {
	case CIRCUITS:
	case COCIRCUITS:
	    return factory.circuits();
	case VECTORS:
	case COVECTORS:
	    return factory.vectors();
	case MAXVECTORS:
	case TOPES:
	    return factory.maxVectors();
	}
	return null;
    }

    private OMAll create(Cryptomorphisms f) {
	Options options = new Options();
	options.setShortLabels();
	switch (f) {
	case REALIZED:
	case DUALREALIZED:
	    return asAll(TestRealization.testDatum());
	default:
	    OM circuits = new FactoryFactory(options).circuits().parse(
		    "( 123456" + (source == 1 ? "7" : "")
			    + ", {12'4,13'5,23'6,45'6,12'56',13'46,23'4'5} )");
	    return asAll(circuits);
	}

    }

    public static OMAll asAll(OM circuits) {
	return ((OMInternal) circuits).asAll();
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

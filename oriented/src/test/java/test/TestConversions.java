/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.sf.oriented.impl.om.Cryptomorphisms;
import net.sf.oriented.impl.om.DelegatingFaceLattice;
import net.sf.oriented.impl.om.OMAll;
import net.sf.oriented.impl.om.OMInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Chirotope;
import net.sf.oriented.omi.ChirotopeFactory;
import net.sf.oriented.omi.FaceLattice;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.OM;
import net.sf.oriented.omi.OMSFactory;
import net.sf.oriented.omi.OMasChirotope;
import net.sf.oriented.omi.OMasRealized;
import net.sf.oriented.omi.OMasSignedSet;
import net.sf.oriented.omi.Options;
import net.sf.oriented.omi.RealizedFactory;
import net.sf.oriented.omi.SetOfSignedSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import test.BetterParameterized.TestName;

@RunWith(value = Parameterized.class)
public class TestConversions {
    

	static FactoryFactory special;

	static String onlyThisTest = null; //"{loop,special,MAXVECTORS,TOPES}";
	static String names[] = { "chapter1", "loop" };

	@Parameters(name = "{0}")
	public static Collection<Object[]> data() {
	    
	    Random rand = new Random();

		FactoryFactory factories[] = new FactoryFactory[5];
		int n = 0;
		for (int i = 0; i < 5; i++) {
			Options opt = new Options();
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
			case 4:
			    opt = new Options(Options.Impl.hash);
			    break;
			}
            opt.setUniverse(new String[] { "1", "2", "3", "4", "5", "6", "7" });
			factories[i] = new FactoryFactory(opt);
		}
		special = factories[3];

		List<Object[]> r = new ArrayList<>();
		for (int source = 0; source < 2; source++) {
			int ff = 0;
			for (FactoryFactory f : factories) {
				for (Cryptomorphisms from : Cryptomorphisms.values()) {
					for (Cryptomorphisms to : Cryptomorphisms.values()) {
						if ((Cryptomorphisms.REALIZED.equals(to) || Cryptomorphisms.DUALREALIZED
								.equals(to))
								&& !Cryptomorphisms.REALIZED.equals(from)
								&& !Cryptomorphisms.DUALREALIZED.equals(from)) {
							continue;
						}
						if (includeTest(rand, source, ff, from, to)) {
						        r.add(new Object[] { name(source, ff, from, to),
						                source, f, from, to, n++ });
						} 
					}
				}
				ff++;
			}
		}
		return r;
	}

    private static boolean includeTest(Random rand, int source, int ff,
            Cryptomorphisms from, Cryptomorphisms to) {
        return onlyThisTest == null?rand.nextInt(100)>=TestAll.skipPerCent:onlyThisTest.equals(name(source,ff,from,to));
    }

	public static String name(int source, int ff, Cryptomorphisms from,
			Cryptomorphisms to) {
		return "{" + sName(source) + "," + fName(ff) + "," + from + "," + to
				+ "}";
	}

	private static String fName(int ff) {
        return switch (ff) {
            case 0 -> "long";
            case 1 -> "short";
            case 2 -> "+/-";
            case 3 -> "special";
            case 4 -> "hash";
            default -> "!";
        };
	}

	private static String sName(int s) {
		return names[s];
	}

	private final int source;
	private final FactoryFactory factory;
	private final Cryptomorphisms from;
	private final Cryptomorphisms to;

	public static int TEST_NUMBER;

	public TestConversions(String name, int s, FactoryFactory f,
			Cryptomorphisms from, Cryptomorphisms to, int n) {
		source = s;
		factory = f;
		this.from = from;
		this.to = to;
		TEST_NUMBER = n;
	}

	@Test
	public void convert() throws AxiomViolation {
		OMAll om = create(from);
		om.verify();
		OMInternal first = om.get(from);
		first.verify();
		checkEquals(from, om, first);
		OMInternal clean = (OMInternal) inOut(first);
		OMInternal mod = clean.asAll().get(to);
		assertNotNull(mod);
		mod.verify();
		assertEquals(first, clean);
		checkEquals(to, mod, first);
		assertNotSame(first, first.dual());
        first.asAll().verify();
        mod.asAll().verify();
	}

	private void checkEquals(Cryptomorphisms f, OM x, OM y) {
		if (f.ordinal() < Cryptomorphisms.values().length / 2) {
			assertEquals(x, y);
		} else {
			assertEquals(x.dual(), y);
		}
	}

	private OM inOut(OMInternal first) {
		switch (from) {
		case CHIROTOPE:
		case DUALCHIROTOPE:
			ChirotopeFactory chi = factory.chirotope();
			if (factory == special)
				return chi.construct(Arrays.asList(first.elements()),(Chirotope) first);
			return chi.parse(chi.toString((OMasChirotope) first));
		case REALIZED:
		case DUALREALIZED:
			RealizedFactory rf = factory.realized();
			return rf.parse(rf.toString((OMasRealized) first));
		case FACELATTICE:
		case DUALFACELATTICE:
		    return new DelegatingFaceLattice(new OMAll(first.elements(),factory),(FaceLattice)first);
		default:
			OMSFactory f = omsFactory();
			if (factory == special)
				return f.fromSignedSets(first.elements(), (SetOfSignedSet) first);
			return f.parse(f.toString((OMasSignedSet) first));
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
		default:
			throw new IllegalArgumentException();
		}
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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.Options;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

import org.junit.Test;

public class TestSignedSet {

    static FactoryFactory f;
    static {
	Options options = new Options();
	options.setShortLabels();
	f = new FactoryFactory(options);
    }

    private SignedSet parse(String s) {
	return f.signedSets().parse(s);
    }

    private UnsignedSet unsigned(String string) {
	return f.unsignedSets().parse(string);
    }

    @Test
    public void testHashCode() {
	assertEquals(parse("ab'cd'e'f").hashCode(), parse("b'cd'e'fa")
		.hashCode());
    }

    @Test
    public void testParse() {
	SignedSet ss = parse("ab'cd'e'f");
	assertEquals(ss.plus(), unsigned("acf"));
	assertEquals(ss.minus(), unsigned("bde"));

    }

    @Test
    public void testSignedSetMSetMSet() {
	try {
	    parse("abda'ef'");
	    fail("exception required");
	}
	catch (IllegalArgumentException e) {
	    // OK
	}
    }

    @Test
    public void testOpposite() {
	assertEquals(parse("abc'").opposite(), parse("a'b'c"));
    }

    @Test
    public void testPlus() {
	assertEquals(parse("abc'").plus(), unsigned("ab"));
    }

    @Test
    public void testMinus() {
	assertEquals(parse("abc'").minus(), unsigned("c"));
    }

    @Test
    public void testEqualsObject() {
	assertEquals(parse("ab'cd'e'f"), parse("b'cd'e'fa"));
    }

    @Test
    public void testEqualsIgnoreSign() {
	assertTrue(parse("ab'cd'e'f").equalsIgnoreSign(parse("b'cd'e'fa")));
	assertTrue(parse("ab'cd'e'f").equalsIgnoreSign(parse("bc'def'a'")));
    }

    @Test
    public void testSeparation() {
	assertEquals(parse("ab'de").separation(parse("bcdf'")), unsigned("b"));
    }

    @Test
    public void testCompose() {
	assertEquals(parse("ab'de").compose(parse("bcdf'")), parse("ab'decf'"));

    }

    @Test
    public void testSize() {
	assertEquals(parse("ab'cd'e'f").size(), 6);
    }

    @Test
    public void testConformsTo() {
	SignedSet ab_c = parse("abc'");
	SignedSet _c = parse("c'");
	SignedSet ac = parse("ac");
	SignedSet b_c = parse("bc'");
	assertTrue(b_c.conformsWith(ab_c));
	assertTrue(ab_c.conformsWith(ab_c));
	assertTrue(_c.conformsWith(ab_c));
	assertFalse(ac.conformsWith(ab_c));
	assertTrue(ab_c.conformsWith(b_c));
    }

    @Test
    public void testSupport() {
	assertEquals(parse("ab'cd'e'f").support(), unsigned("abcdef"));
    }

    // @Test
    // public void testtoString() {
    // UnsignedSet e = base();
    // assertEquals(parse("afb'e'").toString(e),
    // "ab'e'f");
    // }
    //
    // private UnsignedSet base() {
    // return new UnsignedSet(Arrays.asList(new String[]{
    // "a","b","c","d","e","f"
    // }),true);
    // }
    // @Test
    // public void testtoPlusMinus() {
    // UnsignedSet e = base();
    // assertEquals(parse("afb'e'").toPlusMinus(e),
    // "+-00-+");
    // }

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

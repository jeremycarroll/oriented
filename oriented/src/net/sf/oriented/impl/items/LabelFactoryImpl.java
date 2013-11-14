/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.items;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.oriented.impl.util.Misc;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.LabelFactory;
import net.sf.oriented.omi.Options;
import net.sf.oriented.util.combinatorics.Group;
import net.sf.oriented.util.combinatorics.Permutation;

public class LabelFactoryImpl extends FactoryImpl<LabelImpl, Label, LabelImpl> implements LabelFactory {

	public LabelFactoryImpl(Options o) {
		super(o);
	}

	private final Map<String, LabelImpl> all = new HashMap<>();
	private final List<Label> ordered = new ArrayList<>();
	int hashCodes[][] = new int[8][256];
	int count = 0;

	LabelImpl get(String s) {
		LabelImpl rslt = all.get(s);
		if (rslt == null) {
			rslt = new LabelImpl(s, this, count++);
			all.put(s, rslt);
			ordered.add(rslt);
			if (ordered.get(rslt.ordinal()) != rslt)
				throw new IllegalStateException("logic error.");
			addHashCode(rslt);
		}
		return rslt;
	}

	private void addHashCode(LabelImpl rslt) {
		int ix = rslt.ordinal();
		int hc = rslt.hashCode();
		addHashCode(ix / 8, ix % 8, hc);

	}

	private void addHashCode(int i, int mod, int hc) {
		int bit = (1 << mod);
		for (int j = 0; j < 256; j++)
			if ((j & bit) != 0) {
				hashCodes[i][j] += hc;
			}

	}

	@Override
	public LabelImpl parse(ParseContext pc) {
		if (getOptions().getSingleChar()) {
			String s = pc.string.substring(pc.index, pc.index + 1);
			pc.index++;
			return get(s);
		}
		String l = uptoSeparator(pc);
		return get(l);
	}

	public void intern(Collection<Label> labels) {
		for (Label l : labels) {
			remake(l);
		}
	}

	public List<Label> get(Collection<String> a) {
		Label r[] = new Label[a.size()];
		Iterator<String> it = a.iterator();
		for (int i = 0; it.hasNext(); i++) {
			r[i] = parse(it.next());
		}
		return Arrays.asList(r);
	}

	@Override
	public String toString(Label s) {
		return s.label();
	}

	@Override
    public List<Label> getUniverse() {
		return ordered;
	}

	public int hashCode(int members) {
		int r = 0;
		for (int i = 0; i < 4; i++) {
			r += hashCodes[i][(members >> (8 * i)) & 255];
		}
		return r;
	}

	private Constructor<? extends Permutation> smartPermutation;
	/**
	 * Return a permutation of the universe, which
	 * permutes the items in ground by p, and leaves other
	 * things unchanged.
	 * @param ground
	 * @param p
	 * @return
	 */
    public Permutation permuteUniverse(Label[] ground, Permutation p) {
        int permutation[] = Group.identityGroup(count).identity().toArray();
        LabelImpl g[] = remake(ground);
        for (int i=0;i<g.length;i++) {
            int from = g[i].ordinal();
            int to = g[p.get(i)].ordinal();
            permutation[from] = to;
        }
        return Misc.invoke(getSmartPermutation(),permutation);
    }

    @SuppressWarnings("unchecked")
    private Constructor<? extends Permutation> getSmartPermutation() {
        if (smartPermutation == null) {
            smartPermutation = (Constructor<? extends Permutation>) ((OptionsInternal)getOptions()).constructorFor("SmartPermutation");
        }
        return smartPermutation;
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

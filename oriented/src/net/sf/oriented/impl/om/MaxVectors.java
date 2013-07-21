/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import static net.sf.oriented.impl.om.Cryptomorphisms.MAXVECTORS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.oriented.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.impl.set.SignedSetInternal;
import net.sf.oriented.impl.set.UnsignedSetInternal;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.OMSFactory;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;

public class MaxVectors extends AbsVectorsOM {

	MaxVectors(SetOfSignedSetInternal c, OMInternal a) {
		super(c, MAXVECTORS, a);
	}

	MaxVectors(Vectors vectors) {
		super(vectors.maximal(), MAXVECTORS, vectors);
	}

	@Override
	public void verify() throws AxiomViolation {
		verifySupport();
		super.verify();
		getVectors().verify();
	}

	/**
	 * We compute the set of covectors as follows: The covectors are those
	 * signed sets, which when composed with the topes, generate the topes. Each
	 * covector is a restriction of some tope. For each such restriction vv we
	 * find all the topes that are conformal with vv. We represent that tope as
	 * only the values not in vv. When we compose vv with the set of topes, we
	 * can compose it with the other (non-conforming) topes too. Each such
	 * composition must give one of the conforming topes. Thus the projection of
	 * the complement of the support of vv in the topes must be equal to the set
	 * of conforming topes.
	 * 
	 * @return
	 */
	SetOfSignedSetInternal vectors() {
		JavaSet<SignedSetInternal> r = factory().itemFactory()
				.emptyCollectionOf();
		UnsignedSetInternal support = iterator().next().support();
		Map<SignedSetInternal, SetOfSignedSetInternal> covector2tope = allTopesConformingToCovector(support);
		Map<UnsignedSet, SetOfSignedSetInternal> projections = signedProjection(support);
		Iterator<SignedSetInternal> it = covector2tope.keySet().iterator();
		while (it.hasNext()) {
			SignedSetInternal vv = it.next();
			if (covector2tope.get(vv).sameSetAs(
					projections.get(support.minus(vv.support())))) {
				r.add(vv);
			}
		}
		return factory().fromBackingCollection(r);
	}

	private Map<SignedSetInternal, SetOfSignedSetInternal> allTopesConformingToCovector(
			UnsignedSetInternal support) {
		Map<SignedSetInternal, SetOfSignedSetInternal> Tx = new HashMap<SignedSetInternal, SetOfSignedSetInternal>();
		Iterator<UnsignedSetInternal> it = support.powerSet().iterator();
		while (it.hasNext()) {
			UnsignedSetInternal x0 = it.next();
			UnsignedSetInternal x1 = support.minus(x0);
			Iterator<SignedSetInternal> topes = iterator();
			while (topes.hasNext()) {
				SignedSetInternal tope = topes.next();
				SignedSetInternal x = tope.restriction(x0);
				if (Tx.get(x0) == null) {
					Tx.put(x, vectors.conformingWith(x).restriction(x1));
				}
			}
		}
		return Tx;
	}

	private Map<UnsignedSet, SetOfSignedSetInternal> signedProjection(
			UnsignedSetInternal support) {
		Map<UnsignedSet, SetOfSignedSetInternal> Tx = new HashMap<UnsignedSet, SetOfSignedSetInternal>();
		Iterator<UnsignedSetInternal> it = support.powerSet().iterator();
		while (it.hasNext()) {
			UnsignedSet x0 = it.next();
			Tx.put(x0, vectors.restriction(x0));
		}
		return Tx;
	}

	private void verifySupport()  throws AxiomViolation {
		Iterator<? extends SignedSet> it = iterator();
		if (!it.hasNext()) {
            throw new AxiomViolation(this,"non-empty");
		}
		UnsignedSet s = it.next().support();
		while (it.hasNext())
			if (!s.equals(it.next().support())) {
                throw new AxiomViolation(this,"support");
			}
	}

	@Override
    OMSFactory omsFactory() {
        return ffactory().maxVectors();
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

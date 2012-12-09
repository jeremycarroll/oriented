/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
************************************************************************/
package net.sf.oriented.omi.impl.om;

import java.util.Iterator;

import net.sf.oriented.omi.Factory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.SetOfSignedSet;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.set.SetOfSignedSetFactory;
import net.sf.oriented.omi.impl.set.SetOfSignedSetInternal;
import net.sf.oriented.omi.impl.set.SetOfUnsignedSetInternal;
import net.sf.oriented.omi.impl.set.SignedSetInternal;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;


abstract class AbsVectors extends AbsOM implements SetOfSignedSetInternal {

	protected final SetOfSignedSetInternal vectors;

	public AbsVectors(SetOfSignedSetInternal c, OMInternal a) {
		super(a);
		vectors = c;
	}

	@Override
	public boolean equalsIsSameSetAs() {
		return false;
	}

	@Override
	public SetOfSignedSetInternal respectingEquals() {
		return vectors;
	}

	@Override
	public boolean sameSetAs(SetOfSignedSet other) {
		return vectors.sameSetAs(other);
	}
	@Override
	public boolean contains(SignedSet a) {
		return vectors.contains(a);
	}

	@Override
	public SetOfUnsignedSetInternal unsignedSets() {
		return vectors.unsignedSets();
	}

	@Override
	public SetOfSignedSetInternal intersection(SetOfSignedSet b) {
		return vectors.intersection(b);
	}

	@Override
	public boolean isSubsetOf(SetOfSignedSet b) {
		return vectors.isSubsetOf(b);
	}

	@Override
	public boolean isSupersetOf(SetOfSignedSet b) {
		return vectors.isSupersetOf(b);
	}

	@Override
	public Iterator<SignedSetInternal> iterator() {
		return vectors.iterator();
	}

	@Override
	public JavaSet<SignedSetInternal> asCollection() {
		return vectors.asCollection();
	}


	@Override
	public SetOfSignedSetInternal withSupport(UnsignedSetInternal u) {
		return vectors.withSupport(u);
	}

	@Override
	public SignedSetInternal theMember() {
		return vectors.theMember();
	}

	@Override
	public SetOfSignedSetInternal minus(SetOfSignedSet b) {
		return vectors.minus(b);
	}

	@Override
	public SetOfSignedSetInternal minus(SignedSet b) {
		return vectors.minus(b);
	}

	@Override
	public int size() {
		return vectors.size();
	}

	@Override
	public SetOfSignedSetInternal union(SetOfSignedSet b) {
		return vectors.union(b);
	}

	@Override
	abstract public String toString();

	

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o.getClass().equals(getClass()))
		   return 
		      all.sameGroundAs((AbsVectors)o) &&
		      vectors.equals(((AbsVectors)o).vectors);
		return all.equals(o);
	}

	@Override
	public SetOfSignedSetInternal union(SignedSet b) {
		return vectors.union(b);
	}

	@Override
	public boolean verify() {
		return (!vectors.isEmpty()) && verifySymmetry();
	}

	private boolean verifySymmetry() {
		Iterator<SignedSetInternal> it = iterator();
		while (it.hasNext())
			if (!contains(it.next().opposite()))
				return false;
		return true;
	}

	@Override
	public boolean isEmpty() {
		return vectors.isEmpty();
	}

	@Override
	public UnsignedSetInternal support() {
		return vectors.support();
	}

	@Override
	public JavaSet<SetOfSignedSetInternal> powerSet() {
		return vectors.powerSet();
	}

	@Override
	public JavaSet<SetOfSignedSetInternal> subsetsOfSize(int s) {
		return vectors.subsetsOfSize(s);
	}

	@Override
	public SetOfSignedSetInternal useCollection(JavaSet<SignedSetInternal> a) {
		return vectors.useCollection(a);
	}

	@Override
	public SetOfSignedSetFactory factory() {
		return (SetOfSignedSetFactory) vectors.factory();
	}
	@Override
	public SetOfSignedSetInternal conformingWith(SignedSetInternal x) {
		return vectors.conformingWith(x);
	}

	@Override
	public SetOfSignedSetInternal restriction(UnsignedSet x0) {
		return vectors.restriction(x0);
	}

	@Override
	public String toString(Factory<SetOfSignedSet> e) {
		return vectors.toString(e);
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

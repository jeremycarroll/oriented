/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
************************************************************************/
package net.sf.oriented.omi.impl.set.bits32;

import static net.sf.oriented.omi.impl.set.bits32.UnsignedSetImpl.sizex;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.omi.impl.items.HasFactoryImpl;
import net.sf.oriented.omi.impl.items.LabelFactory;
import net.sf.oriented.omi.impl.set.SignedSetFactory;
import net.sf.oriented.omi.impl.set.SignedSetInternal;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.impl.set.UnsignedSetInternal;


public class SignedSetImpl 
  extends HasFactoryImpl<SignedSetInternal, SignedSet, SignedSetInternal>
   implements SignedSetInternal{
	

	@Override
public SignedSetFactory factory() {
	return (SignedSetFactory) super.factory();
}
	final int plus;
	final int minus;


	public SignedSetImpl(UnsignedSetInternal p, UnsignedSetInternal m, SignedSetFactory f) {
		super(f);
		plus = remake(p).members;
		minus = remake(m).members;
		if ((plus&minus)!= 0)
			throw new IllegalArgumentException(
					"plus and minus must be disjoint.");
	}
	
	static int cnt = 0;
	
	SignedSetImpl(int p, int m, SignedSetFactory f) {
		super(f);
		plus = p;
		minus = m;
	}

private UnsignedSetImpl remake(UnsignedSetInternal p) {
	return (UnsignedSetImpl) unsignedSetFactory().remake(p);
}

	@Override
	public SignedSetImpl opposite() {
		return make(minus,plus);
	}

private SignedSetImpl make(int p, int m) {
	return make(p, m, factory());
}

static SignedSetImpl make(int p, int m, SignedSetFactory f) {
	return new SignedSetImpl(p, m, f);
}

private static SignedSetImpl fromCache(long l, int p, int m, SignedSetFactory f) {
	return new SignedSetImpl(p, m, f);
}

static SignedSetImpl make(long key, SignedSetFactory f) {
	return fromCache(key,SignedSetFactory.plus(key), SignedSetFactory.minus(key),f);
}
	
	@Override
	public UnsignedSetInternal plus() {
		return make(plus);
	}

	private UnsignedSetImpl make(int v) {
		return new UnsignedSetImpl(v,unsignedSetFactory());
	}

	
	private UnsignedSetFactory unsignedSetFactory() {
		return factory().unsignedF;
	}

	
	@Override
	public UnsignedSetInternal minus() {
		return make(minus);
	}

	
	@Override
	public int hashCode() {
		return 
		  labelFactory().hashCode(plus)+ 
		  35 * labelFactory().hashCode(minus);
	}

private LabelFactory labelFactory() {
	return unsignedSetFactory().itemFactory();
}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (!(o instanceof SignedSet))
			return false;
		SignedSet s = (SignedSet) o;
		SignedSetImpl ss = remake(s);
		return plus==ss.plus && minus==ss.minus;
	}


	@Override
	public boolean equalsIgnoreSign(SignedSet s) {
		SignedSetImpl ss = remake(s);
		return (plus==ss.plus && minus==ss.minus)
		||(minus==ss.plus && plus==ss.minus);
	}

	
	@Override
	public boolean equalsOpposite(SignedSet s) {
		SignedSetImpl ss = remake(s);
		return minus==ss.plus && plus==ss.minus;
	}

	
	@Override
	public UnsignedSet separation(SignedSet b) {
		SignedSetImpl bb = remake(b);
		return make((plus&bb.minus)|(minus&bb.plus));
	}

	private SignedSetImpl remake(SignedSet b) {
		return (SignedSetImpl) factory.remake(b);
	}

	
	@Override
	public SignedSetImpl compose(SignedSet b) {
		SignedSetImpl bb = remake(b);
		return 
		  make( plus | ( bb.plus & ~minus ),
				 minus | ( bb.minus & ~ plus));
	}

	
	@Override
	public int size() {
		return sizex(plus) + sizex(minus);
	}

	
	@Override
	public boolean conformsWith(SignedSet a) {
		SignedSetImpl aa = remake(a);
		return 
		   (plus & aa.minus) == 0
				 && (minus & aa.plus)==0;
	}


    
	
	@Override
	public UnsignedSetInternal support() {
		return make(plus|minus);
	}


	@Override
	public int sign(Label e) {
		if (plus().contains(e))
			return 1;
		if (minus().contains(e))
			return -1;
		return 0;
	}

	@Override
	public boolean isRestrictionOf(SignedSet x) {
		return plus().isSubsetOf(x.plus())
		      && minus().isSubsetOf(x.minus());
	}

	@Override
	public SignedSetImpl restriction(UnsignedSet x) {
		return new SignedSetImpl(plus().intersection(x),
				   minus().intersection(x), factory() );
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

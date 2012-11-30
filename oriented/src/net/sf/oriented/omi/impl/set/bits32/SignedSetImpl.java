/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
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
  extends HasFactoryImpl<SignedSetInternal,SignedSet,SignedSetInternal> 
   implements SignedSetInternal{
	final int plus;
	final int minus;

//	final static private SignedSet EMPTY = new SignedSet(UnsignedSet.EMPTY);
//	
//	static public SignedSet empty(UnsignedSet support) {
//		return EMPTY;
//	}


	public SignedSetImpl(UnsignedSetInternal p, UnsignedSetInternal m, SignedSetFactory f) {
		super(f);
		plus = remake(p).members;
		minus = remake(m).members;
		if ((plus&minus)!= 0)
			throw new IllegalArgumentException(
					"plus and minus must be disjoint.");
//		f.addToCache(plus, minus, this);
	}
	
	static int cnt = 0;
//	private static int misses;
//	private static int hits;
//	private static int busy;
	
	SignedSetImpl(int p, int m, SignedSetFactory f) {
		super(f);
		plus = p;
		minus = m;
//		if (++cnt % 10000 == 0)
//			System.err.println(cnt+ " signed sets");
	}

private UnsignedSetImpl remake(UnsignedSetInternal p) {
	return (UnsignedSetImpl) unsignedSetFactory().remake(p);
}

//	public SignedSet(UnsignedSet p) {
//		this(p, UnsignedSet.EMPTY);
//	}

	/* (non-Javadoc)
	 * @see omi.SignedSetI#opposite()
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#opposite()
	 */
	@Override
	public SignedSetImpl opposite() {
		return make(minus,plus);
	}

private SignedSetImpl make(int p, int m) {
	return make(p, m, factory());
}

static SignedSetImpl make(int p, int m, SignedSetFactory f) {
//	return fromCache( 0,//SignedSetFactory.toLong(p, m),
//			p,m,f);
//	if (r!=null)
//		return r;
	return new SignedSetImpl(p, m, f);
}

private static SignedSetImpl fromCache(long l, int p, int m, SignedSetFactory f) {
//	SignedSetImpl r = (SignedSetImpl) f.cached(l);
//	if (r!=null && r.plus==p && r.minus==m)
//		return r;
//		if (SignedSetFactory.toLong(r.plus,r.minus)!=l) {
//		r = null;
//		busy++;
//	}
//		else
//		hits++;
//	if ((misses+busy+hits)%1000000==0)
//	    System.err.println("Cache: "+hits+"/"+busy+"/"+misses);
	return new SignedSetImpl(p, m, f);
}

static SignedSetImpl make(long key, SignedSetFactory f) {
	return fromCache(key,SignedSetFactory.plus(key), SignedSetFactory.minus(key),f);
//	if (r!=null)
//		return r;
//	return new SignedSetImpl(SignedSetFactory.plus(key), SignedSetFactory.minus(key), f);
}
	/* (non-Javadoc)
	 * @see omi.SignedSetI#plus()
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#plus()
	 */
	@Override
	public UnsignedSetInternal plus() {
		return make(plus);
	}

	private UnsignedSetImpl make(int v) {
		return new UnsignedSetImpl(v,unsignedSetFactory());
	}

	
	private UnsignedSetFactory unsignedSetFactory() {
		return factory.unsignedF;
	}

	/* (non-Javadoc)
	 * @see omi.SignedSetI#minus()
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#minus()
	 */
	@Override
	public UnsignedSetInternal minus() {
		return make(minus);
	}

//	public SignedSet empty() {
//		return EMPTY;
//	}
	
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

	/* (non-Javadoc)
	 * @see omi.SignedSetI#equalsIgnoreSign(omi.SignedSetI)
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#equalsIgnoreSign(omi.SignedSet)
	 */
	@Override
	public boolean equalsIgnoreSign(SignedSet s) {
		SignedSetImpl ss = remake(s);
		return (plus==ss.plus && minus==ss.minus)
		||(minus==ss.plus && plus==ss.minus);
	}

	/* (non-Javadoc)
	 * @see omi.SignedSetI#equalsOpposite(omi.SignedSetI)
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#equalsOpposite(omi.SignedSet)
	 */
	@Override
	public boolean equalsOpposite(SignedSet s) {
		SignedSetImpl ss = remake(s);
		return minus==ss.plus && plus==ss.minus;
	}

	/* (non-Javadoc)
	 * @see omi.SignedSetI#separation(omi.SignedSet)
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#separation(omi.SignedSet)
	 */
	@Override
	public UnsignedSet separation(SignedSet b) {
		SignedSetImpl bb = remake(b);
		return make((plus&bb.minus)|(minus&bb.plus));
	}

	private SignedSetImpl remake(SignedSet b) {
		return (SignedSetImpl) factory.remake(b);
	}

	/* (non-Javadoc)
	 * @see omi.SignedSetI#compose(omi.SignedSet)
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#compose(omi.SignedSet)
	 */
	@Override
	public SignedSetImpl compose(SignedSet b) {
		SignedSetImpl bb = remake(b);
		return 
		  make( plus | ( bb.plus & ~minus ),
				 minus | ( bb.minus & ~ plus));
	}

	/* (non-Javadoc)
	 * @see omi.SignedSetI#size()
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#size()
	 */
	@Override
	public int size() {
		return sizex(plus) + sizex(minus);
	}

	/* (non-Javadoc)
	 * @see omi.SignedSetI#conformsWith(omi.SignedSet)
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#conformsWith(omi.SignedSet)
	 */
	@Override
	public boolean conformsWith(SignedSet a) {
		SignedSetImpl aa = remake(a);
		return 
		   (plus & aa.minus) == 0
				 && (minus & aa.plus)==0;
	}


    
	/* (non-Javadoc)
	 * @see omi.SignedSetI#support()
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#support()
	 */
	@Override
	public UnsignedSetInternal support() {
		return make(plus|minus);
	}

//	public String toString() {
//		return "(" + plus.toString() + "," + minus.toString() + ")";
//	}



	/* (non-Javadoc)
	 * @see omi.SignedSetI#sign(omi.Label)
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#sign(omi.Label)
	 */
	@Override
	public int sign(Label e) {
		if (plus().contains(e))
			return 1;
		if (minus().contains(e))
			return -1;
		return 0;
	}

	/* (non-Javadoc)
	 * @see omi.SignedSetI#isRestrictionOf(omi.SignedSetI)
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#isRestrictionOf(omi.SignedSet)
	 */
	@Override
	public boolean isRestrictionOf(SignedSet x) {
		return plus().isSubsetOf(x.plus())
		      && minus().isSubsetOf(x.minus());
	}

	/* (non-Javadoc)
	 * @see omi.SignedSetI#restriction(omi.UnsignedSetI)
	 */
	/* (non-Javadoc)
	 * @see omi.impl.set.hash.SignedSetInternalx#restriction(omi.UnsignedSet)
	 */
	@Override
	public SignedSetImpl restriction(UnsignedSet x) {
		return new SignedSetImpl(plus().intersection(x),
				   minus().intersection(x), factory() );
	}

}
/************************************************************************
    This file is part of the Java Oriented Matroid Library.

    The Java Oriented Matroid Library is free software: you can 
    redistribute it and/or modify it under the terms of the GNU General 
    Public License as published by the Free Software Foundation, either 
    version 3 of the License, or (at your option) any later version.

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/

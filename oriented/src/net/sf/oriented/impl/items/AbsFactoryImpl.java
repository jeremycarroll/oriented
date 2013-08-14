/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.items;

import java.lang.reflect.Array;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.oriented.impl.util.RuntimeClass;
import net.sf.oriented.omi.Factory;

import com.google.common.reflect.TypeToken;

public abstract class AbsFactoryImpl<ITEM> implements Factory<ITEM> {

	static Pattern p = Pattern.compile("([})]|\\]|\\[)|([ \t\n\r,]+)");

	protected String uptoSeparator(ParseContext pc) {
		// int i = pc.string.
		String rslt;
		Matcher m = p.matcher(pc.string);
		if (m.find(pc.index)) {
			rslt = pc.string.substring(pc.index, m.start());
			if (m.start(2) >= 0) {

				// System.err.println(rslt + ":2:"+pc.index+":"+(m.end(2)));
				pc.index = m.end(2);
			} else {
				// System.err.println(rslt + ":1:"+pc.index+":"+(m.end()-1));
				pc.index = m.end() - 1;
			}
		} else {
			rslt = pc.string.substring(pc.index);
			pc.index = pc.string.length();
		}
		return rslt;
	}

	protected void skip(ParseContext pc) {
		while (pc.index < pc.string.length()
				&& "\t\r\n ,".indexOf(pc.string.charAt(pc.index)) >= 0) {
			pc.index++;
		}
	}

	protected void expect(ParseContext pc, char expect) {
		if (pc.index >= pc.string.length()
				|| pc.string.charAt(pc.index) != expect)
			throw new IllegalArgumentException("Syntax error in: " + pc.string
					+ "Expected '" + expect + "' at position " + pc.index);
		pc.index++;
		skip(pc);
	}
    @SuppressWarnings("unchecked")
    public <T extends ITEM> T remake(ITEM t) {
        if (t instanceof HasFactory) {
            if (((HasFactory<?, ?, ?>) t).factory() == this)
                return (T) t;
        }
        // System.err.println(++cnt+":"+TestConversions.TEST_NUMBER+" remaking: "+getClass().getSimpleName());
        return fallbackRemake(t);
    }
    
    protected <T extends ITEM> T[] remake(ITEM t[]) {
        if (t.length == 0) {
            throw new IllegalArgumentException("Must be non-zero length");
        }
        T first = remake(t[0]);
        @SuppressWarnings("unchecked")
        T rslt[] = (T[]) Array.newInstance(first.getClass(), t.length);
        rslt[0] = first;
        for (int i=1;i<t.length;i++) {
            rslt[i] = remake(t[i]);
        }
        return rslt;
    }

    @SuppressWarnings("unchecked")
    protected <T extends ITEM> T fallbackRemake(ITEM t) {
        return (T)parse(toString(t));
    }


    @Override
    public ITEM[] parse(String ... many) {
        @SuppressWarnings("unchecked")
        ITEM rslt[] = (ITEM[]) Array.newInstance(new RuntimeClass<ITEM>(){ 
            @Override
            protected TypeToken<ITEM> getTypeToken(Class<?> x) {
               return new TypeToken<ITEM>(x){};
           }
        }.getRuntimeClass(getClass()), many.length );
        for (int i=0;i<many.length;i++) {
            rslt[i] = parse(many[i]);
        }
        return rslt;
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

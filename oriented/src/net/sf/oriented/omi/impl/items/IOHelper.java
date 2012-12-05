/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi.impl.items;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IOHelper {

	static Pattern p = Pattern.compile("([})]|\\]|\\[)|([ \t\n\r,]+)");

	protected String uptoSeparator(ParseContext pc) {
	//		int i = pc.string.
			String rslt;
			Matcher m = p.matcher(pc.string);
			if (m.find(pc.index)) {
				rslt = pc.string.substring(pc.index,m.start());
				if (m.start(2)>=0) {
	
	//				System.err.println(rslt + ":2:"+pc.index+":"+(m.end(2)));
					pc.index = m.end(2);
				} else {
	//				System.err.println(rslt + ":1:"+pc.index+":"+(m.end()-1));
					pc.index = m.end() -1;
				}
			} else {
				rslt = pc.string.substring(pc.index);
				pc.index = pc.string.length();
			}
			return rslt;
		}

	protected void skip(ParseContext pc) {
		while (pc.index < pc.string.length() &&
				"\t\r\n ,".indexOf(pc.string.charAt(pc.index))>=0 ) 
			pc.index++;
	}

	protected void expect(ParseContext pc, char expect) {
		if (pc.index >= pc.string.length() ||
				pc.string.charAt(pc.index) != expect) {
			throw new IllegalArgumentException("Syntax error in: "+pc.string + "Expected '" + expect +"' at position "+pc.index);
		}
		pc.index++;
		skip(pc);
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

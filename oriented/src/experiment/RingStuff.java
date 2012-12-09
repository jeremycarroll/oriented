/************************************************************************
  (c) Copyright 2012 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/

package experiment;

import com.perisic.ring.Ring;
import com.perisic.ring.RingElt;
import com.perisic.ring.UniversalPolynomialRing;

public class RingStuff {
    public static void main(String [] args) {
	// Define a polynomial ring over R
	UniversalPolynomialRing U = new UniversalPolynomialRing(Ring.Q);
	RingElt poly1 = U.map("1/2 * ((x1 - x2)^2 - a * (x1 + x2)^2)");
	System.out.println("poly1="+poly1+" of "+poly1.getRing());
	
	// 1st Example to substitute variables:
	String [] variables = { "x1", "x2" }; 
	RingElt [] values = { Ring.Q.map("23/10"), Ring.Q.map("17/10") };
	RingElt poly2 = U.evaluatePolynomial(poly1, variables, values); 
	System.out.println("Example1: poly2="+poly2+" of "+poly2.getRing());
	
//	2nd Example to substitute variables:
	RingElt poly3 = U.evaluatePolynomial(poly1, "a", Ring.Q.map(-1)); 
	System.out.println("Example2: poly3="+poly3+" of "+poly3.getRing());
	
//	3rd Example to substitute variables:
	RingElt poly4 = U.evaluatePolynomial(poly3, variables, values); 
	System.out.println("Example3: poly4="+poly4+" of "+poly4.getRing());
	

	RingElt [] valuesU = { U.map("2*b"), U.map("a/3") };
	RingElt poly5 = U.evaluatePolynomial(poly1, variables, valuesU); 
	System.out.println("Example4: poly5="+poly5+" of "+poly5.getRing());
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
/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi;

public interface Chirotope {
	/**
	 * This is only called when it has {@link rank()}
	 * arguments, and they are strictly monotonic increasing.
	 * Implementations may return correct values for arguments
	 * which are not strictly monotonic increasing but are not required to.
	 * @param i
	 * @return -1, 0 or 1
	 * @throws IllegalArgumentException If arguments are not strictly monotonic increasing, or the wrong number, or out of range.
	 */
   int chi(int... i);
   /**
    * Must be a positive integer, which is constant for each object
    * implementing this interface.
    * @return The rank associated with this chirotope.
    */
   int rank();
   int n();
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

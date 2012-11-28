/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/
package net.sf.oriented.omi;


public interface OM extends Verify {

	OMChirotope getChirotope();
	
	OMS getCircuits();

	OMS getVectors();
	
	OMS getMaxVectors();
	
	public OM dual();
	
	public Label[]  ground();
	
	public Matroid getMatroid();
	
	int rank();
	
	/**
	 * The hashCode of an Oriented Matroid is the hashCode
	 * of the circuit representation as a {@link SetOfSignedSet}s.
	 * @return the hashCode
	 */
	@Override
	int hashCode();
	
	/**
	 * An Oriented Matroid is equal to any other
	 * object implementing this interface,
	 * which represents the same underlying oriented matroid,
	 * including the {@link #ground} being equal.
	 * @param om
	 * @return true if these both represent the same Oriented Matroid
	 */
	@Override
	boolean equals(Object om);
	
	int asInt(Label l);
	
	<T extends Label> int[] asInt(T[] l);
	/**
	 * 
	 * @param u
	 * @return Sorted array of indexes.
	 */
	int[] asInt(UnsignedSet u);
	
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

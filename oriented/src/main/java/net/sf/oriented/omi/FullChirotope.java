/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

/**
 * A true chirotope with an alternating map.
 * This marker interface show that the arguments to {@link #chi(int...)}
 * do not have to be monotonic increasing.
 * 
 * @author jeremycarroll
 *
 */
public interface FullChirotope extends Chirotope {
	/**
	 * The alternating map.
	 * This is only called when it has {@link #rank()} arguments.
	 * They do not have to be monotonic increasing.
	 * 
	 * @param i an array of {@link Chirotope#rank()} values between 0 and {@link Chirotope#n()} - 1.
	 * @return -1, 0 or 1
	 * @throws IllegalArgumentException
	 *             With the wrong number of arguments or if any is out of range.
	 */
	@Override
	int chi(int... i);
}
/************************************************************************
 * This file is part of the Java Oriented Matroid Library.
 * 
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
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

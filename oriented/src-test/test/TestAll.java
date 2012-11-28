/************************************************************************
  (c) Copyright 2010 Jeremy J. Carroll
  For GPLv3 licensing information, see end of file.
************************************************************************/

package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@SuiteClasses({
	TestAlternating.class,
	TestChirotope.class,
	TestCircuits.class,
	TestConversions.class,
	TestLexicographic.class,
	TestMatroid.class,
	TestMaxVectors.class,
	TestMSet.class,
	TestSignedSet.class,
	TestVectors.class,
	
})
@RunWith(Suite.class)
public class TestAll {

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
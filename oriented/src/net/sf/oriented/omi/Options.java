/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import net.sf.oriented.impl.items.LabelFactoryImpl;
import net.sf.oriented.impl.items.OptionsInternal;
import net.sf.oriented.impl.set.AbsSetImpl;
import net.sf.oriented.impl.util.Misc;

/**
 * Provide the options for String representations. See
 * {@link FactoryFactory#FactoryFactory(Options)}.
 * 
 * @author jeremy
 * 
 */
public class Options extends OptionsInternal {
    
    /**
     * The choices of underlying implementation.
     * @author jeremycarroll
     *
     */
    public enum Impl {
        /**
         * Represent a set of labels by a 32 bit structure.
         * The universe {@link #setUniverse}, must have 32 or fewer
         * members.
         */
        bits32,
        /**
         * Use java sets to represent everything ... somewhat inefficient.
         */
        hash
    }
	LabelFactoryImpl label;
	private boolean plusMinus;
	private boolean singleChar;

	private final String implementation; 
	private Constructor<? extends JavaSet<?>> javaSet;
	/**
	 * Default options (32 bit implementation).
	 */
	public Options() {
	    this(Options.Impl.bits32);
    }
    /**
     * Allows using the non-default implementation.
     */
	public Options(Impl hash) {
        implementation = AbsSetImpl.class.getPackage().getName() + "."+ hash.name() +".";
    }

	/**
	 * Not part of the API
	 */
	@SuppressWarnings("unchecked")
	public <T> JavaSet<T> javaSetFor(Class<T> cl) {
	    if (javaSet == null) {
	        javaSet = (Constructor<? extends JavaSet<T>>) constructorFor("JavaHashSet");
	    }
	    return (JavaSet<T>) Misc.invoke(javaSet);
	}

    /**
	 * Provide all the {@link Label}s to be used, as Strings. This allows
	 * representations as sequences of {-1, 0, 1}, for instance.
	 * 
	 * @param a Converted to {@link Label}
	 */
	public void setUniverse(String a[]) {
		getLabelFactory().get(Arrays.asList(a));
	}

  
	/**
	 * Use 0 + - as the representation.
	 * 
	 * @param usePM  If true uses the +/- form.
	 * @return old value.
	 */
	public boolean setPlusMinus(boolean usePM) {
		boolean old = plusMinus;
		plusMinus = usePM;
		return old;
	}

	/**
	 * Are we using the +/- form.
	 */
	public boolean getPlusMinus() {
		return plusMinus;
	}

	/**
	 * All the {@link Label}'s used consist of a single character (unicode code
	 * point). Use short forms for everything.
	 * 
	 */
	public void setShortLabels() {
		singleChar = true;
	}

	/**
	 * Some of the {@link Label}'s used may consist of more than one character. Use
	 * longer forms for everything.
	 * 
	 */
	public void setLongLabels() {
		singleChar = false;
	}

	/**
	 * Are we using short labels or long labels?
	 * @return true if we are using short labels.
	 */
	public boolean getSingleChar() {
		return singleChar;
	}

	LabelFactoryImpl getLabelFactory() {
		if (label == null) {
			label = new LabelFactoryImpl(this);
		}
		return label;
	}

	/**
	 * Returns all the known labels.
	 * @return The universe of labels used.
	 */
	public List<Label> getUniverse() {
		return label.getUniverse();
	}

	/**
	 * Get the ith label from the universe.
	 */
	public Label getLabel(int i) {
		return getUniverse().get(i);
	}
	
    @Override
    protected String getImplementation() {
        return implementation;
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

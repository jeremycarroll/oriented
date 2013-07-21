/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import net.sf.oriented.impl.set.AbsSetImpl;
import net.sf.oriented.impl.util.Misc;

/**
 * Provide the options for String representations. See
 * {@link FactoryFactory#FactoryFactory(Options)}.
 * 
 * @author jeremy
 * 
 */
public class Options {
    
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
	LabelFactory label;
	private boolean plusMinus;
	private boolean singleChar;

	private final String implementation; 
	private Constructor<? extends JavaSet<?>> javaSet;
	public Options() {
	    this(Options.Impl.bits32);
    }
	public Options(Impl hash) {
        implementation = AbsSetImpl.class.getPackage().getName() + "."+ hash.name() +".";
    }

    // TODO: how to hide this.
	@SuppressWarnings("unchecked")
	public <T> JavaSet<T> javaSetFor(Class<T> cl) {
	    if (javaSet == null) {
	        javaSet = (Constructor<? extends JavaSet<T>>) constructorFor("JavaHashSet");
	    }
	    return (JavaSet<T>) Misc.invoke(javaSet);
	}

    // TODO: how to hide this.
	public <T> Constructor<?> constructorFor(Class<T> fc) {
		String name = fc.getSimpleName();
		if (!name.endsWith("Factory"))
			throw new IllegalArgumentException("Naming conventions violated");
		name = name.substring(0, name.length() - "Factory".length())+ "Impl";
		return constructorFor(name);
	}

    // TODO: how to hide this.
    public <T> Constructor<?> constructorFor(String name) {
        try {
            @SuppressWarnings("unchecked")
			Class<T> impl = (Class<T>) Class.forName(getImplementation() + name);
			Constructor<?> c[] = impl.getConstructors();
			if (c.length != 1)
				throw new RuntimeException("internal problem: more than one constructor found for "+ name);
			return c[0];
		} catch (ClassNotFoundException e) {
			return null;
		}
    }

	/**
	 * Provide all the {@link Label}s to be used, as Strings. This allows
	 * representations as sequences of {-1, 0, 1}, for instance.
	 * 
	 * @param a
	 *            Converted to {@link Label}
	 */
	public void setUniverse(String a[]) {
		getLabelFactory().get(Arrays.asList(a));
	}
	/**
	 * Change the implementation. This can be called at most once.
	 * Different implementations have different limitations.
	 * @param impl
	 */

  
	/**
	 * Use 0 + - as the representation.
	 * 
	 * @param usePM
	 *            If true uses the +/- form.
	 * @return old value.
	 */
	public boolean setPlusMinus(boolean usePM) {
		boolean old = plusMinus;
		plusMinus = usePM;
		return old;
	}

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
	 * Some of the {@link Label}'s used consist of more than one character. Use
	 * longer forms for everything.
	 * 
	 */
	public void setLongLabels() {
		singleChar = false;
	}

	public boolean getSingleChar() {
		return singleChar;
	}

	LabelFactory getLabelFactory() {
		if (label == null) {
			label = new LabelFactory(this);
		}
		return label;
	}

	public List<Label> getUniverse() {
		return label.getUniverse();
	}

	public Label getLabel(int i) {
		return getUniverse().get(i);
	}
	
    private String getImplementation() {
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

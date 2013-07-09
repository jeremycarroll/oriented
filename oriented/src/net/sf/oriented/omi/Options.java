/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

import net.sf.oriented.omi.impl.items.LabelFactory;

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

	private String implementation; 
	private Class<JavaSet<?>> javaSet;

	// TODO: how to hide this.
	@SuppressWarnings("unchecked")
	public <T> JavaSet<T> javaSetFor(Class<T> cl) {
		try {
			if (javaSet == null) {
				javaSet = (Class<JavaSet<?>>) Class.forName(getImplementation()
						+ "JavaHashSet");
			}
			return (JavaSet<T>) javaSet.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException("internal problem", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("internal problem", e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("internal problem", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> Constructor<?> constructorFor(Class<T> fc) {
		String name = fc.getSimpleName();
		if (!name.endsWith("Factory"))
			throw new IllegalArgumentException("Naming conventions violated");
		name = name.substring(0, name.length() - "Factory".length());
		try {
			Class<T> impl = (Class<T>) Class.forName(getImplementation() + name
					+ "Impl");
			Constructor<?> c[] = impl.getConstructors();
			if (c.length != 1)
				throw new RuntimeException(
						"internal problem: more than one constructor found for "
								+ name + "Impl");
			return c[0];
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	// /**
	// * Provide all the {@link Label}s to be used.
	// * This allows representations as sequences of {-1, 0, 1},
	// * for instance.
	// * @param a The universe of {@link Label}s.
	// */
	// public void setUniverse(Label a[]) {
	// universe = Arrays.asList(a);
	// }
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

    public void setImplementation(Impl impl) {
        if (implementation != null) {
            throw new IllegalStateException("The implementation option cannot be changed.");
        }
        implementation = Options.class.getPackage().getName()
                + ".impl.set." + impl.name() +".";
    }
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

	/**
	 * The only {@link Label}'s used are those passed in a call to
	 * {@link #setUniverse(Label[])}. Any other {@link Label} will cause an
	 * IllegalArgumentException.
	 * 
	 */
	public void setClosedUniverse() {

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
		return getUniverseInternal();
	}

	private List<Label> getUniverseInternal() {
		return label.getUniverse();
	}

	// public void extendUniverse(Collection<? extends Label> uu) {
	// Iterator<? extends Label> it = uu.iterator();
	// while (it.hasNext()){
	// Label l = it.next();
	// if (!universe.contains(l))
	// universe.add(l);
	// }
	// }

	public Label getLabel(int i) {
		return getUniverseInternal().get(i);
	}
	
    private String getImplementation() {
        if (implementation == null) {
            setImplementation(Impl.bits32);
        }
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

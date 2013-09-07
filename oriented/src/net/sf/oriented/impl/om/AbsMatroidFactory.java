/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.impl.om;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.impl.items.AbsFactoryImpl;
import net.sf.oriented.impl.items.LabelImpl;
import net.sf.oriented.impl.items.ParseContext;
import net.sf.oriented.impl.set.UnsignedSetFactory;
import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;

/**
 * A Factory for Matroid representations, possible oriented,
 * from some specific structure.
 * @author jeremycarroll
 *
 * @param <MATROID>       The class of matroid
 * @param <STRUCTURE>     The structured representation
 */
public abstract class AbsMatroidFactory<MATROID, STRUCTURE> extends AbsFactoryImpl<MATROID> {

    
    abstract class SimpleLabels extends AbstractCollection<Label> {
        private final int n;

        private SimpleLabels(int n) {
            this.n = n;
        }


        abstract String label(int j);
        @Override
        public Iterator<Label> iterator() {
            return new Iterator<Label>() {
                int i = 0;

                @Override
                public boolean hasNext() {
                    return i < n;
                }

                @Override
                public Label next() {
                    int j = i++;
                    return factory.labels().parse(label(j));
                }


                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override
        public int size() {
            return n;
        }
    }
    
    Collection<Label> simpleLabels(int n) {
        if (n>26) {
            return new SimpleLabels(n){
                @Override
                String label(int j) {
                    return "" + j;
                }
            };
        } else {
            return new SimpleLabels(n){
                @Override
                String label(int j) {
                    return new String(new char[]{(char)('A'+j)});
                }
            };

        }
    }

    final protected FactoryFactory factory;

    public AbsMatroidFactory(FactoryFactory f) {
        factory = f;
    }

    protected UnsignedSetFactory unsignedSets() {
        return (UnsignedSetFactory) factory.unsignedSets();
    }

    @Override
    public MATROID parse(String s) {
        ParseContext pc = new ParseContext(s.trim());
        MATROID r = parse(pc);
        if (pc.index != pc.string.length())
            throw new IllegalArgumentException("Syntax error");
        return r;
    }

    protected MATROID parsePair(ParseContext pc) {
        expect(pc, '(');
        List<LabelImpl> ground = unsignedSets().orderedParse(pc);
        STRUCTURE signedSets = parseMatroid(pc);
        expect(pc, ')');
        return construct(ground, signedSets);
    }

    MATROID parse(ParseContext pc) {
        if (0 == pc.string.length())
            throw new IllegalArgumentException("Syntax error - empty input");
        switch (pc.string.charAt(0)) {
        case '{':
            return construct(parseMatroid(pc));
        case '(':
            return parsePair(pc);
        default:
            throw new IllegalArgumentException(
                    "Syntax error - expected '{' or '('");
        }
    }

    abstract STRUCTURE parseMatroid(ParseContext pc);

    abstract MATROID construct(STRUCTURE sets);

    abstract MATROID construct(Collection<? extends Label> ground,
            STRUCTURE sets);

    @Override
    final public JavaSet<? extends MATROID> emptyCollectionOf() {
        return factory.options().javaSetFor(null);
    }

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

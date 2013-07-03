/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.omi.impl.om;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.oriented.omi.FactoryFactory;
import net.sf.oriented.omi.JavaSet;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.impl.items.IOHelper;
import net.sf.oriented.omi.impl.items.LabelImpl;
import net.sf.oriented.omi.impl.items.ParseContext;
import net.sf.oriented.omi.impl.set.UnsignedSetFactory;

public abstract class MoreAbsFactory<MATROID, STRUCTURE> extends IOHelper {

    final class SimpleLabels extends AbstractCollection<Label> {
        private final int n;

        SimpleLabels(int n) {
            this.n = n;
        }

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
                    return factory.labels().parse("" + ++i);
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

    final protected FactoryFactory factory;

    public MoreAbsFactory(FactoryFactory f) {
        factory = f;
    }

    protected UnsignedSetFactory unsignedSets() {
        return (UnsignedSetFactory) factory.unsignedSets();
    }

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

/************************************************************************
  (c) Copyright 2007, 2010 Jeremy J. Carroll
  
 ************************************************************************/
package net.sf.oriented.graph;

public class Edge {

	final public String label;
	final public Vertex from, to;

	public Edge(String s, Vertex f, Vertex t) {
		label = s;
		from = f;
		to = t;
	}

	@Override
	public int hashCode() {
		return label.hashCode() + 5 * from.hashCode() + 73 * to.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof Edge))
			return false;
		Edge e = (Edge) o;
		return e.label.equals(label) && e.from.equals(from) && e.to.equals(to);
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

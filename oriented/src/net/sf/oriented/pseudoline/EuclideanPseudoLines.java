/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.twod.Line;
import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.common.collect.Iterables;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;

public class EuclideanPseudoLines {
    
    private static final double RADIUS = 1400.0;
    private final static int WIDTH = 3000;
    private static class XLine extends SubLine {
        
        final Point p, q;

        public XLine(Point p, Point q) {
            super(new Vector2D(p.x, p.y), new Vector2D(q.x, q.y)); 
            this.p = p;
            this.q = q;
                
        }

    }

    private class Edge {
        
        final Face face;

        public Edge(Face e) {
            face = e;
        }
        
        boolean isOnPositiveFace() {
            for (Face tope:face.higher()) {
                if (tope.covector().minus().isEmpty()) {
                    return true;
                }
            }
            return false;
        }

    }

    class Point {
        
        int ring;
        Point adjacent[];
        final Face face;
        private final Point[][] adjacentByLevel = new Point[3][];
        private double degrees;
        @SuppressWarnings("unused")
        private double radius;
        private double x, y;
        private int countToOuter = 0;

//        public Point(Face f, int i) {
//            this(f);
//            setLevel(i);
//        }

        public Point(Face f) {
            ss2point.put(f.covector(),this);
            unclassified.add(this);
            points.add(this);
            face = f;
            ring = Integer.MAX_VALUE;
        }
        protected void setLevel(int i) {
            ring = i;
            getRing(i).add(this);
            unclassified.remove(this);
        }

        void findAdjacents() {
            int found = 0;
            boolean allowMisses = ring == 0;
            adjacent = new Point[face.higher().size()];
            for (Face e:face.higher()) {
                if (allowMisses && e.covector().sign(projective.getInfinity()) == -1) {
                        continue;
                }
                for (Face p:e.lower()) {
                    if (p != face) {
                        adjacent[found] = ss2point.get(p.covector());
                        maybeSaveEdge(e);
                        found++;
                    }
                }
            }
            if (allowMisses) {
                adjacent = resize(adjacent,found);
//                edge = resize(edge,found);
            }
            if (found != adjacent.length) {
                throw new IllegalStateException("unexpected");
            }
        }

        void maybeSaveEdge(Face e) {
            // maybe not
        }
        public void classifyAdjacents() {
            int sizes[] = new int[3];
            for (Point p:adjacent) {
                int pRing = p.ring;
                if (pRing == Integer.MAX_VALUE) {
                    p.countToOuter++;
                    pRing = ring+1;
                }
                if (pRing < ring) {
                    pRing = ring-1;
                }
                sizes[pRing - ring+1]++;
            }
            for (int i=0;i<3;i++) {
                adjacentByLevel[i] = new Point[sizes[i]];
            }
            int ix[] = new int[3];
            for (Point p:adjacent) {
                int pRing = p.ring;
                if (pRing == Integer.MAX_VALUE) {
                    pRing = ring+1;
                }
                if (pRing < ring) {
                    pRing = ring-1;
                }
                int lvl = pRing - ring+1;
                adjacentByLevel[lvl][ix[lvl]++]= p;
            }
        }
        
        Point[] getSameRing() {
            return adjacentByLevel[1];
        }
        Point[] getOuterRing() {
            return adjacentByLevel[0];
        }

        Point[] getInnerRing() {
            return adjacentByLevel[2];
        }
        public SignedSet covector() {
            return face.covector();
        }

        public void setPosition(double d) {
            degrees = d;
        }

        public void setRadius(double r) {
            radius = r;
            x = r * Math.cos(degrees*Math.PI/180.0);
            y = r * Math.sin(degrees*Math.PI/180.0);
        }
        public void computePosition() {
            double highest = 0.0;
            for (Point p:getOuterRing()) {
                if (p.degrees > highest) {
                    highest = p.degrees;
                }
            }
            double threshold = highest - 180;
            double sum = 0.0;
            double total_weight = 0.0;
            for (Point p:getOuterRing()) {
                double weight = //1.0; // /
                              Math.sqrt(ring - p.ring); //*(ring - p.ring);
                sum += p.degrees * weight;
                total_weight += weight;
                if (p.degrees < threshold) {
                    sum += 360.0 * weight;
                }
            }
            double pos =  sum / total_weight;
            if (pos > 360) {
                pos -= 360;
            }
            setPosition(pos);
        }
        public List<XLine> getLines() {
            Vector2D me = new Vector2D(x,y);
            List<XLine> rslt = new ArrayList<XLine>();
            for (Point p:this.getInnerRing()) {
                rslt.add(new XLine(this, p));
            }
            for (Point p:this.getSameRing()) {
                if (p.degrees< degrees) {
                    rslt.add(new XLine(this, p));
                }
            }
            return rslt;
        }
    }
    
    private class PointAtInfinity extends Point {
        Edge  edge[] = new Edge[2];

        public PointAtInfinity(Face f) {
            super(f);
            setLevel(0);
        }
        
        @Override
        void maybeSaveEdge(Face e) {
            if (e.covector().sign(projective.getInfinity())==0) {
                edge[edge[0]==null?0:1] = new Edge(e);
            }
        }
        
        boolean isOnPositiveFace() {
            return edge[0].isOnPositiveFace() || edge[1].isOnPositiveFace();
        }

        public PointAtInfinity next(PointAtInfinity previous) {
            Point p[] = getSameRing();
            return (PointAtInfinity) p[p[0]==previous?1:0];
        }

    }
    final PseudoLines projective;
    final Set<Point> points = new HashSet<Point>();
    final Set<Point> unclassified = new HashSet<Point>();
    final Map<SignedSet,Point> ss2point = new HashMap<SignedSet,Point>();
    private final List<List<Point>> rings = new ArrayList<List<Point>>();
    private boolean arranged = false;
    
    public EuclideanPseudoLines(PseudoLines pseudoLines) {
        projective = pseudoLines;
        Label inf = projective.getInfinity();
        for (Face f: projective.getFaceLattice().withDimension(0)) {
            switch (f.covector().sign(inf)) {
            case -1:
                continue;
            case 0:
                @SuppressWarnings("unused")
                PointAtInfinity p1 = new PointAtInfinity(f);
                break;
            case 1:
                @SuppressWarnings("unused")
                Point p2 = new Point(f);
                break;
            }
        }
        for (Point p:points) {
            p.findAdjacents();
        }
        createRings();
    }

    private void createRings() {
        for (int level = 0; level < rings.size(); level++) {
            int maxCountToOuterRing = 0;
            for (Point p:rings.get(level)) {
                p.classifyAdjacents();
            }
            for (Point p:unclassified) {
                if (p.countToOuter > maxCountToOuterRing) {
                    maxCountToOuterRing = p.countToOuter;
                }
            }
            List<Point> toSet = new ArrayList<Point>();
            for (Point p:unclassified) {
                if (p.countToOuter == maxCountToOuterRing) {
                    toSet.add(p);
                }
            }
            for (Point p:toSet) {
                p.setLevel(level+1);
            }
        }
    }

    public static <T> T[] resize(T[] old, int newLength) {
        @SuppressWarnings("unchecked")
        T rslt[] = (T[]) Array.newInstance(old.getClass().getComponentType(), newLength);
        System.arraycopy(old, 0, rslt, 0, newLength<old.length?newLength:old.length);
        return rslt;
    }

    public List<Point> getRing(int i) {
        if (i >= rings.size()) {
            rings.add(new ArrayList<Point>());
        }
        return rings.get(i);
    }
    
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("R[");
        for (List<Point>p:rings) {
            s.append(p.size());
            s.append(",");
        }
        s.append("]");
        return s.toString();
    }
    
    
    public void arrangePoints() {
        if (arranged)
            return;
        arranged = true;
        PointAtInfinity first = firstPointAtInfinity();
        PointAtInfinity last = lastPointAtInfinity();
        PointAtInfinity current = first;
        PointAtInfinity previous = last;
        double separation = 360.0 / getRing(0).size();
        int pos = 0;
        do {
            current.setPosition(separation*pos);
            current.setRadius(RADIUS);
            PointAtInfinity current1 = current;
            current = current.next(previous);
            previous = current1;
            pos++;
        } while (current != first);
        
        int innerRing = rings.size() - 1;
        double step;
        boolean deadCenter = rings.get(innerRing).size()==1;
        if (deadCenter) {
            step = RADIUS / innerRing;
        } else {
            step = RADIUS / (0.5 + innerRing);
        }
        double radius = RADIUS;
        if (deadCenter) {
            rings.get(innerRing).get(0).setPosition(0.0);
            rings.get(innerRing).get(0).setRadius(0.0);
            innerRing--;
        }
        for (int i=1;i<=innerRing;i++) {
            radius -= step;
            double degrees[] = new double[rings.get(i).size()];
            int pix = 0;
            for (Point p: rings.get(i)) {
                p.computePosition();
                degrees[pix++] = p.degrees;
                p.setRadius(radius);
            }
            Arrays.sort(degrees);
            for (int ii=1;ii<degrees.length;ii++) {
                System.err.println(degrees[ii] - degrees[ii-1]);
                if (degrees[ii] - degrees[ii-1] < 0.01) {
                    throw new IllegalStateException("Drawing failed.");
                }
            }
        }
        
    }

    private PointAtInfinity firstPointAtInfinity() {
        Label first = projective.getEquivalentOM().elements()[1];
        return pointOnPositiveFaceAndLine(first);
    }

    private PointAtInfinity lastPointAtInfinity() {
        Label[] elements = projective.getEquivalentOM().elements();
        Label first = elements[elements.length-1];
        return pointOnPositiveFaceAndLine(first);
    }
    
    @SuppressWarnings("unchecked")
    private PointAtInfinity pointOnPositiveFaceAndLine(Label first) {
        for (PointAtInfinity p:(List<PointAtInfinity>)(List<?>)getRing(0)) {
            if (p.isOnPositiveFace() && p.covector().sign(first) == 0 ) {
                return p;
            }
        }
        throw new IllegalStateException("Failed to find first point.");
    }

    static Color colors[] = new Color[23];
    static {
        int ix = 0;
        for (int i=0;i<3;i++)
            for (int j=0;j<3;j++)
                for (int k=0;k<3;k++) {
                    if (i==j && j==k)
                        continue;
                    if (i==2 && j==2 && k == 1) // too close to our background color
                        continue;
                    colors[ix++] = new Color((3+6*i)*17,(3+6*j)*17,(3+6*k)*17);
                }
        Arrays.sort(colors,new Comparator<Color>(){
            @Override
            public int compare(Color o1, Color o2) {
                // pseudorandom order
                return System.identityHashCode(o1) - System.identityHashCode(o2);
            }});
    }
    
    public void checkForOverlappingEdge() {
        Set<SubLine> allLines = new HashSet<SubLine>();
        for (Point p:points) {
            List<XLine> ee = p.getLines();
            for (SubLine ll:ee) {
                for (SubLine l:allLines) {
                    try {
                    if (ll.intersection(l, false) != null) {
                        throw new IllegalStateException("overlapping lines");
                    }
                    }
                    catch (NullPointerException parallelLinesBug) {}
                }
                allLines.add(ll);
            }
        }
    }
    
    public RenderedImage image() {
        this.arrangePoints();
        BufferedImage image = new BufferedImage(WIDTH,WIDTH,BufferedImage.TYPE_INT_RGB);
       
       Graphics2D graphics = image.createGraphics();
       graphics.setBackground(Color.WHITE);
       graphics.setColor(Color.WHITE);
       graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
       
       Shape circ = new Ellipse2D.Double(-RADIUS, -RADIUS, 2*RADIUS, 2*RADIUS);
       graphics.setColor( new Color(255, 255, 204));
       graphics.translate(WIDTH/2, WIDTH/2);
       graphics.fill(circ);
       graphics.setStroke(new BasicStroke(2.0f));
       graphics.setColor(Color.BLACK);
       graphics.draw(circ);

       for (Point p:points) {
           if (p.ring==0) {
               continue;
           }
           for (Point q:Iterables.concat(Arrays.asList(p.getSameRing()),Arrays.asList(p.getOuterRing()))) {
               Label line = projective.noCoLoops().minus(p.covector().support().union(q.covector().support())).iterator().next();
               Color c = colors[projective.getEquivalentOM().asInt(line)%colors.length];
               graphics.setColor(c);
               graphics.drawLine((int)p.x, (int)p.y, (int)q.x, (int)q.y);
           }
       }

       graphics.setColor(Color.BLACK);
       for (Point p:points) {
           graphics.fillRect((int)p.x-1,(int) p.y-1, 3, 3);
       }
       
       return image;
    }
}


/************************************************************************
    This file is part of the Java Oriented Matroid Library.  

    The Java Oriented Matroid Library is distributed in the hope that it 
    will be useful, but WITHOUT ANY WARRANTY; without even the implied 
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
    See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with the Java Oriented Matroid Library.  
    If not, see <http://www.gnu.org/licenses/>.

**************************************************************************/

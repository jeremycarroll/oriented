/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.oriented.impl.util.Misc;
import net.sf.oriented.omi.AxiomViolation;
import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.Label;
import net.sf.oriented.omi.SignedSet;
import net.sf.oriented.omi.Verify;

import org.apache.commons.math3.geometry.euclidean.twod.SubLine;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class PseudoLineDrawing implements Verify {
    
    private class Path {

        private final Label label;
        private final IPoint[] points;
        public Path(PointAtInfinity first, Point second) {
            label = lineLabel(first, second);
            this.points = followLine(first, second);
        }
        private IPoint[] followLine(PointAtInfinity first, Point second) {
            List<IPoint> points = new ArrayList<IPoint>();
            if (options.showLabels) {
                points.add(first.getLabelPosition(label));
            }
            points.add(first);
            Point prev = first;
            Point current = second;
   extendingLine:
            while ( true ) {
                points.add(current);
                for (Point p:current.adjacent) {
                    if (p == prev) {
                        continue;
                    }
                    if (p.covector().sign(label)==0) {
                        // this is on the same line.
                        prev = current;
                        current = p;
                        continue extendingLine;
                    }
                }
                if (options.showLabels) {
                    points.add(((PointAtInfinity)current).getLabelPosition(label));
                }
                // no more points, we crossed the Euclidean plane
                return points.toArray(new IPoint[points.size()]);
            }
            
        }
        
        public Path2D getPath2D() {
            Path2D result = new Path2D.Double();
            
            result.moveTo(points[0].getX(), points[0].getY());
            IPoint control[] = controlPoints(points[0], points[1], points[2]);
            result.quadTo(control[0].getX(), control[0].getY(), points[1].getX(), points[1].getY());
            for (int i=2;i<points.length-1;i++) {
                IPoint lastControl = control[1];
                control = controlPoints(points[i-1], points[i], points[i+1]);
                result.curveTo(lastControl.getX(),lastControl.getY(),control[0].getX(),control[0].getY(),
                        points[i].getX(), points[i].getY());
            }
            
            IPoint last = points[points.length-1];
            result.quadTo(control[1].getX(), control[1].getY(), last.getX(), last.getY());
            
            return result;
        }
        private IPoint[] controlPoints(IPoint point1, IPoint point2, IPoint point3) {
            double xDiff = (point3.getX() - point1.getX());
            double yDiff = (point3.getY() - point1.getY());
            double diffNorm = Math.sqrt(xDiff*xDiff+yDiff*yDiff);
            double p1dx = point2.getX() - point1.getX();
            double p1dy = point2.getY() - point1.getY();
//            double p1dnorm = Math.sqrt(p1dx*p1dx+p1dy*p1dy);
            double p1DotDiff = p1dx * xDiff + p1dy * yDiff;
            double p3dx = point3.getX() - point2.getX();
            double p3dy = point3.getY() - point2.getY();
//            double p3dnorm = Math.sqrt(p3dx*p3dx+p3dy*p3dy);
            double p3DotDiff = p3dx * xDiff + p3dy * yDiff;
            double x1 = point2.getX() -  xDiff*p1DotDiff/diffNorm/3/diffNorm,
                    y1 = point2.getY() - yDiff*p1DotDiff/diffNorm/3/diffNorm, 
                    x2 = point2.getX()  + xDiff*p3DotDiff/diffNorm/3/diffNorm, 
                    y2 = point2.getY() + yDiff*p3DotDiff/diffNorm/3/diffNorm;
            
            return new IPoint[]{new Point2DDouble(x1,y1),new Point2DDouble(x2,y2)};
        }
        public Label label() {
            return label;
        }

    }
    private static class XLine extends SubLine {
        
        @SuppressWarnings("unused")
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
    interface IPoint {
        double getX();
        double getY();
    }

    static class Point2DDouble extends Point2D.Double implements IPoint {

        public Point2DDouble(double x, double y) {
            super(x,y);
        }
        
    }
    
    class Point implements IPoint {
        
        int ring;
        Point adjacent[];
        final Face face;
        private final Point[][] adjacentByLevel = new Point[3][];
        private double degrees;
        @SuppressWarnings("unused")
        private double radius;
        private int countToOuter = 0;
        double x;
        double y;

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
                adjacent = Misc.resize(adjacent,found);
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
            y = -r * Math.sin(degrees*Math.PI/180.0);
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
        public void addToJung(Graph<IPoint, SignedSet> graph) {
            for (Point p:this.getInnerRing()) {
                graph.addEdge(p.covector().compose(covector()), this, p);
            }
            for (Point p:this.getSameRing()) {
                if (p.degrees< degrees) {
                    graph.addEdge(p.covector().compose(covector()), this, p);
                }
            }
            
        }
        public void setLocation(Layout<IPoint, SignedSet> layout) {
            layout.setLocation(this,new Point2D.Double(x+getRadius(),y+getRadius()));
        }
        public void getLocationFromLayout(AbstractLayout<IPoint, SignedSet> layout) {
            x =  layout.getX(this)-getRadius();
            y =  layout.getY(this)-getRadius();
        }
        @Override
        public double getX() {
            return x;
        }
        @Override
        public double getY() {
            return y;
        }
        
        double getDegrees() {
            return degrees;
        }
    }
    
    private class PointAtInfinity extends Point {
        Edge  edge[] = new Edge[2];
        double labelPositions[];
        Label lineLabels[];

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
        

        @Override
        public void setLocation(Layout<IPoint, SignedSet> layout) {
            super.setLocation(layout);
            layout.lock(this, true);
        }
        
        @Override
        public void getLocationFromLayout(AbstractLayout<IPoint, SignedSet> layout) {
            
        }

        public void computeLabelPositions() {
            Point first = this.getSameRing()[0];
            Point last = this.getSameRing()[1];
            setLabelPositions(first, last);
            computeLabelOrder(last);
        }

        private void computeLabelOrder(Point last) {
            lineLabels = new Label[getInnerRing().length];
            Set<Point> remaining = new HashSet<Point>(Arrays.asList(getInnerRing()));
            int ix = 0;
            while (!remaining.isEmpty()) {
                for (Point p:remaining) {
                    if (p.covector().conformsWith(last.covector())) {
                        lineLabels[ix++] = lineLabel(this,p);
                        last = p;
                        remaining.remove(p);
                        break;
                    }
                }
            }
        }
        
        IPoint getLabelPosition(Label lbl) {
           int ix = Arrays.asList(lineLabels).indexOf(lbl);
           double degrees = labelPositions[ix];
           double radians = Math.PI* degrees/180.0;
           double r = getRadius() + options.labelPosition;
//           System.err.println(lbl.label()+ " "+ degrees);
           return new Point2DDouble(r*Math.cos(radians), -r*Math.sin(radians));
        }

        private void setLabelPositions(Point first, Point last) {
            labelPositions = new double[getInnerRing().length];
            double ratio;
            switch ( getInnerRing().length) {
            case 1:
                ratio = 0.0;
                break;
            case 2:
                ratio = 0.25;
                break;
            case 3:
                ratio = 0.30;
                break;
            default:
                ratio = 0.35;
                break;
            }
            double start = first.degrees;
            double end = last.degrees;
            if ( start + 180 < end) {
                start += 360;
            } else if ( end + 180 < start ) {
                end += 360;
            }
            double diff = end - start;
            double skip = (1.0 - ratio) / 2;
            labelPositions[0] = start + skip*diff;
            if ( getInnerRing().length > 1 ) {
                double step = ratio * diff / (getInnerRing().length - 1);
                for (int i=1; i< getInnerRing().length; i++) {
                    labelPositions[i] = labelPositions[i-1] + step;
                }
            }
        }

        public void writeLineLabels(Graphics2D graphics) {
            for (int i=0;i<lineLabels.length;i++) {
                IPoint pos = getLabelPosition(lineLabels[i]);
                writeCenteredString(graphics, lineLabels[i].label(), pos.getX(), pos.getY(), Color.WHITE);
            }
        }


    }
    final EuclideanPseudoLines projective;
    final Set<Point> points = new HashSet<Point>();
    final Set<Point> unclassified = new HashSet<Point>();
    final Map<SignedSet,Point> ss2point = new HashMap<SignedSet,Point>();
    private final List<List<Point>> rings = new ArrayList<List<Point>>();
    private ImageOptions options;
    
    public PseudoLineDrawing(EuclideanPseudoLines pseudoLines) throws CoLoopUnrepresentableException {
        projective = pseudoLines;
        Label inf = projective.getInfinity();
        for (SignedSet ss:projective.getEquivalentOM().dual().getCircuits()) {
            if (ss.minus().isEmpty() && ss.plus().size()==1) {
                if (!ss.plus().minus(inf).isEmpty()) {
                    throw new CoLoopUnrepresentableException("CAGGGH");
                }
            }
        }
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

    private List<Point> getRing(int i) {
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
    
    
    private void approximatelyArrange() {
        PointAtInfinity first = firstPointAtInfinity();
        PointAtInfinity last = lastPointAtInfinity();
        PointAtInfinity current = first;
        PointAtInfinity previous = last;
        double separation = 360.0 / getOuterRing().size();
        int pos = 0;
        do {
            current.setPosition(separation*pos);
            current.setRadius(getRadius());
            PointAtInfinity current1 = current;
            current = current.next(previous);
            previous = current1;
            pos++;
        } while (current != first);
        
        int innerRing = rings.size() - 1;
        double step;
        boolean deadCenter = rings.get(innerRing).size()==1;
        if (deadCenter) {
            step = getRadius() / innerRing;
        } else {
            step = getRadius() / (0.5 + innerRing);
        }
        double radius = getRadius();
        if (deadCenter) {
            rings.get(innerRing).get(0).setPosition(0.0);
            rings.get(innerRing).get(0).setRadius(0.0);
            innerRing--;
        }
        for (int i=1;i<=innerRing;i++) {
            radius -= step;
            for (Point p: rings.get(i)) {
                p.computePosition();
                p.setRadius(radius);
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
    
    private PointAtInfinity pointOnPositiveFaceAndLine(Label first) {
        for (PointAtInfinity p:getOuterRing()) {
            if (p.isOnPositiveFace() && p.covector().sign(first) == 0 ) {
                return p;
            }
        }
        throw new IllegalStateException("Failed to find first point.");
    }

    @SuppressWarnings("unchecked")
    private List<PointAtInfinity> getOuterRing() {
        return (List<PointAtInfinity>)(List<?>)getRing(0);
    }

    
    
    @Override
    public void verify() throws AxiomViolation {
        Set<SubLine> allLines = new HashSet<SubLine>();
        for (Point p:points) {
            List<XLine> ee = p.getLines();
            for (SubLine ll:ee) {
                for (SubLine l:allLines) {
                    try {
                    if (ll.intersection(l, false) != null) {
                        throw new AxiomViolation(this,"overlapping lines");
                    }
                    }
                    catch (NullPointerException parallelLinesBug) {}
                }
                allLines.add(ll);
            }
        }
    }
    
    
    private void arrange() {
        approximatelyArrange();
        Dimension dim = new Dimension();
        dim.setSize( 2*getRadius(), 2*getRadius());
        Graph<IPoint, SignedSet> graph = new SparseGraph<IPoint, SignedSet>();
        // load up all points
        for (Point p:points) {
            p.addToJung(graph);
        }
        
        // set initial locations ...
        SpringLayout<IPoint, SignedSet> spring = new SpringLayout<IPoint, SignedSet>(graph);
        spring.setSize(dim);
        spring.initialize();
        for (Point p:points) {
            p.setLocation(spring);
        }
        for (int i=0;i<150;i++) {
            spring.step();
        }
        for (Point p:points) {
            p.getLocationFromLayout(spring);
        }
        
    }

    public RenderedImage image() {
        return image(ImageOptions.defaultColor());
    }
    public RenderedImage image(ImageOptions opt) {
        options = opt;
        arrange();
        computeLabelPositions();
        BufferedImage image = new BufferedImage(options.width,options.height,BufferedImage.TYPE_INT_RGB);
       
       Graphics2D graphics = image.createGraphics();
       graphics.setFont(graphics.getFont().deriveFont(options.fontSizeRatio()*graphics.getFont().getSize2D()));
       graphics.setBackground(getBackgroundColor());
       graphics.setColor(getBackgroundColor());
       graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
       
       Shape circ = new Ellipse2D.Double(-getRadius(), -getRadius(), 2*getRadius(), 2*getRadius());
       graphics.setColor( getFillColor());
       graphics.translate(options.width/2, options.height/2);
       graphics.scale(options.width/1000.0, options.height/1000.0);
       graphics.fill(circ);
       options.setInfinity(projective.getInfinity());
       setLineColorAndStroke(graphics, projective.getInfinity());
       graphics.draw(circ);
       
       Set<SignedSet> done = new HashSet<SignedSet>();
       for (PointAtInfinity p:getOuterRing()) {
           if (!done.contains(p.covector().opposite())) {
               done.add(p.covector());
               for (Point q:p.getInnerRing()) {
                   Path path = new Path(p, q);
                   setLineColorAndStroke(graphics, path.label());
                   graphics.draw(path.getPath2D());
               }
           }
           if (options.showLabels) {
               graphics.setColor(getForegroundColor());
               p.writeLineLabels(graphics);
           }
       }

       graphics.setColor(getForegroundColor());
       graphics.setStroke(new BasicStroke(1));
       if (options.showVertices) {
           for (Point p:points) {
               int vertexSize = options.vertexSize;
               graphics.drawOval((int)p.x-vertexSize/2,(int) p.y-vertexSize/2, vertexSize, vertexSize);
           }
       }

       if (options.showLabels) {
           labelLineAtInfinity(graphics);
       }
       IPoint p = centerOfPositiveFace();
       drawArrow(graphics,p.getX(),p.getY(),p.getX()+options.originArrowLength,p.getY());
       
       options = null;
       return image;
    }

    private void setLineColorAndStroke(Graphics2D graphics, Label lbl) {
      graphics.setStroke(new BasicStroke(options.lineWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, options.getStroke(lbl), 0.0f));
      graphics.setColor(options.getColor(lbl));
    }

    private void labelLineAtInfinity(Graphics2D graphics) {
        PointAtInfinity before = null, after = null;
        for (PointAtInfinity p:getOuterRing()) {
            double d = p.getDegrees();
            if (d>135) {
                if (after == null || d<after.getDegrees()) {
                    after = p;
                }
            } else {
                if (before == null || d>before.getDegrees()) {
                    before = p;
                }
            }
        }
        @SuppressWarnings("null")
        double degrees = (after.getDegrees() + before.getDegrees()) / 2;
        double radians = Math.PI * degrees / 180;
        double x = (getRadius() - options.labelPosition*0.8) * Math.cos(radians);
        double y = -(getRadius() - options.labelPosition*0.8) * Math.sin(radians);
        writeCenteredString(graphics,lineLabel(before,after).label(),x,y,null);
    }

    private IPoint centerOfPositiveFace() {
        int count = 0;
        double sumx = 0.0;
        double sumy = 0.0;
        for (Point p:points) {
            if (p.covector().minus().isEmpty()) {
                count++;
                sumx += p.getX();
                sumy += p.getY();
            }
        }
        return new Point2DDouble(sumx/count,sumy/count);
    }

    private void computeLabelPositions() {
        for (PointAtInfinity p: getOuterRing()) {
            p.computeLabelPositions();
        }
    }

    private Label lineLabel(Point first, Point second) {
        return projective.noCoLoops().minus(first.covector().support().union(second.covector().support())).iterator().next();
    }

    private void writeCenteredString(Graphics2D graphics, String lbl,
            double xx, double yy, Color bgColor) {
        graphics = (Graphics2D) graphics.create();
        graphics.translate(xx,yy);
        if (options.width != options.height) {
            if (options.height>options.width) {
                graphics.scale(1.0, 1.0*options.width/options.height);
            } else {
                graphics.scale(1.0/options.width*options.height,1.0);
            }
        }
        Rectangle2D bounds = graphics.getFont().getStringBounds(lbl, graphics.getFontRenderContext());
        double x =  - bounds.getWidth()/2;
        double y = + (bounds.getHeight()-1)/2 - 2;
        if (bgColor != null) {
            graphics.setColor(bgColor);
            graphics.fillRect(
                    (int)x-options.labelBorder, (int)y+options.labelBorder-(int)bounds.getHeight(), 
                    (int)bounds.getWidth()+options.labelBorder*2, (int)bounds.getHeight()+options.labelBorder*2);
        }
        graphics.setColor(Color.BLACK);
        graphics.drawString(lbl, (float) x, (float)y);
    }
    
    private void drawArrow(Graphics2D g1, double x1, double y1, double x2, double y2) {
        if (!options.showOrigin) {
            return;
        }
        Graphics2D g =  (Graphics2D) g1.create();

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int arr_size = options.originArrowSize;
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g.transform(at);
        g.setStroke(new BasicStroke(1));

        // Draw horizontal arrow starting in (0, 0)
        g.drawLine(0, 0, len, 0);
        g.fillOval((int)(-arr_size*0.6), (int)(0-arr_size*0.6), (int)(arr_size*1.2), (int)(arr_size*1.2));
        g.fillPolygon(new int[] {len, len-arr_size, len-arr_size, len},
                      new int[] {0, -arr_size, arr_size, 0}, 4);
    }

    private double getRadius() {
        return 500 - options.border;
    }

    private Color getBackgroundColor() {
        return options.background;
    }

    private Color getFillColor() {
        return options.fill;
    }
    private Color getForegroundColor() {
        return options.foreground;
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

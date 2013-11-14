/************************************************************************
  (c) Copyright 2013 Jeremy J. Carroll
 ************************************************************************/
package net.sf.oriented.pseudoline2;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;

import net.sf.oriented.omi.Face;
import net.sf.oriented.omi.UnsignedSet;
import net.sf.oriented.pseudoline.CoLoopCannotBeDrawnException;
import net.sf.oriented.pseudoline.EuclideanPseudoLines;
import net.sf.oriented.pseudoline.PseudoLineDrawing;


public class DifficultyDrawing extends PseudoLineDrawing {
    
    final Difficulty difficulty;
    final TensionGraph tg;
    final Map<Face,IPoint> positions = new HashMap<>();

    public DifficultyDrawing(EuclideanPseudoLines pseudoLines, TensionGraph ten, Difficulty d)
            throws CoLoopCannotBeDrawnException {
        super(pseudoLines);
        difficulty = d;
        tg = ten;
    }

    @Override
    protected void drawUnderlay(Graphics2D graphics) {
        // for each vertex, work out 
        // a) its coordinates
        // b) whether it is tope(s) or a point
        // c) draw it
        
        for (Faces v:difficulty.getRslt(tg).getVertices()) {
            graphics.setColor(options.getNextTwistedGraphColor());
            highlightPointOrFace(graphics, v.faceOrPoint);
            if (v.face != null) {
                highlightPointOrFace(graphics, v.face);
            }
        }
    }

    protected void highlightPointOrFace(Graphics2D graphics, Face source) {
        IPoint pos;
        switch (source.dimension()) {
        case 0:
            pos = this.getPoint(source.covector());
            highlightVertex(graphics,pos);
            break;
        case 2:
            pos = this.centerOfFace(source);
            highlightExtent(graphics,source);
            break;
        default:
            throw new IllegalStateException("Bad TGVertex dimension: "+source.dimension());
        }
        positions.put(source, pos);
    }

    @Override
    protected void drawOverlay(Graphics2D graphics) {
        

        graphics.setColor(options.twistedGraphColor);
        graphics.setStroke(new BasicStroke(options.twistedGraphLineWidth));
        // for each edge
        // draw it
        Graph<Faces, DEdge> g = difficulty.getRslt(tg);
        for (DEdge e:g.getEdges()) {
            Faces from = g.getSource(e);
            Faces to = g.getDest(e);
            IPoint fromPt = positions.get(from.faceOrPoint);
            IPoint toPt = positions.get(to.faceOrPoint);
            IPoint mid = new Point2DDouble((fromPt.getX()+toPt.getX())/2,(fromPt.getY()+toPt.getY())/2);
            drawArrowHead(graphics,fromPt.getX(),fromPt.getY(),mid.getX(),mid.getY(), options.twistedGraphArrowSize);
            drawLine(graphics,from.face,fromPt,mid);
            drawLine(graphics,to.face,toPt,mid);
            
        }
    }

    private void drawLine(Graphics2D graphics, Face face, IPoint fromPt,
            IPoint toPt) {
        if (face == null) {
            graphics.drawLine((int)fromPt.getX(),(int)fromPt.getY(),(int)toPt.getX(),(int)toPt.getY());
        } else {
            IPoint realFromPt = positions.get(face);
            Path2D curve = new Path2D.Double();
            curve.moveTo(realFromPt.getX(), realFromPt.getY());
            curve.quadTo(fromPt.getX(),fromPt.getY(),toPt.getX(),toPt.getY());
            graphics.draw(curve);
            
        }
        
    }

    private void highlightExtent(Graphics2D graphics, Face f) {
//        for (SignedSet ss:extent) {
//            IPoint vPos = getPoint(ss);
//            if (vPos != null) {
//                highlightVertex(graphics, vPos);
//            } else {
//                Face f = projective.getFaceLattice().get(ss);
                if (f.dimension()==2) {
                    Map<Face,Face> oneWay = new HashMap<>();
                    Map<Face,Face> theOther = new HashMap<>();
                    Face pts[] = new Face[2];
                    for (Face e:f.lower()) {
                        int ix=0;
                        for (Face p:e.lower()) {
                            pts[ix++] = p;
                        }
                        for (int i=0;i<2;i++) {
                            if (oneWay.containsKey(pts[i])) {
                                theOther.put(pts[i], pts[1-i]);
                            } else {
                                oneWay.put(pts[i], pts[1-i]);
                            }
                        }
                    }
                    Face firstPoint = oneWay.keySet().iterator().next();
                    Face current = firstPoint;
                    Face prev = null;
                    IPoint cPoint = getPoint(current.covector());
                    Path2D fillFace = new Path2D.Double();
                    fillFace.moveTo(cPoint.getX(), cPoint.getY());
                    do {
                        Face nextPoint = oneWay.get(current);
                        if (nextPoint.equals(prev)) {
                            nextPoint = theOther.get(current);
                        }
                        curveFromTo(fillFace,current,nextPoint);
//                        IPoint nPoint = getPoint(nextPoint.covector());

//                        cPoint = nPoint;
                        prev = current;
                        current = nextPoint;
//                        fillFace.lineTo(cPoint.getX(), cPoint.getY());

                    } while (!current.equals(firstPoint));
                    fillFace.closePath();
                    graphics.fill(fillFace);
                }

//            }
//        }

    }
    
    /**
     * The two points have an edge on an interior face in common.
     * We draw along the pseudoline that joins them with a {@link Path2D#curveTo(double, double, double, double, double, double)} call.
     *
     * @param fillFace
     * @param from
     * @param to
     */

    private void curveFromTo(Path2D fillFace, Face from, Face to) {
        Set<Face> edgeSet = new HashSet<>(from.higher());
        edgeSet.retainAll(to.higher());
        Face edge = edgeSet.iterator().next();
        Face prev = findOtherPoint(from,edge);
        Face next = findOtherPoint(to,edge);
        IPoint control0[] = controlPoints(getPoint(prev.covector()), getPoint(from.covector()), getPoint(to.covector()));
        IPoint control1[] = controlPoints(getPoint(from.covector()), getPoint(to.covector()), getPoint(next.covector()));
        IPoint toPoint = getPoint(to.covector());
        fillFace.curveTo(control0[1].getX(), control0[1].getY(), control1[0].getX(), control1[0].getY(), toPoint.getX(), toPoint.getY());
        
    }

    private Face findOtherPoint(Face onePoint, Face edge) {
        UnsignedSet line = edge.covector().support();
        for (Face otherEdge:onePoint.higher()) {
            if ( (!otherEdge.equals(edge))
                    && otherEdge.covector().support().equals(line) ) {
                for (Face otherPoint:otherEdge.lower()) {
                    if (!onePoint.equals(otherPoint)) {
                        return otherPoint;
                    }
                }
            }
        }
        throw new IllegalArgumentException("findOtherPoint failed");
    }

    private void highlightVertex(Graphics2D graphics, IPoint pos) {
        int sz = options.twistedGraphVertexSize;
        graphics.fillOval((int)pos.getX()-sz/2, (int)pos.getY()-sz/2, sz, sz);
    }


    private IPoint centerOfFace(Face f) {
        int count = 0;
        double sumx = 0.0;
        double sumy = 0.0;
        for (Face e:f.lower()) {
            for (Face v:e.lower()) {
                IPoint p = getPoint(v.covector());
                count++;
                sumx += p.getX();
                sumy += p.getY();
            }
        }
        return new Point2DDouble(sumx/count,sumy/count);
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

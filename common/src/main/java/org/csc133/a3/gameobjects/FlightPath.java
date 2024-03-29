package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;

import java.util.ArrayList;

public class FlightPath extends GameObject {
    private final Dimension worldSize;
    private final Transform startLocation;
    private final BezierCurve helipadToRiver;
    private final BezierCurve riverToFire;
    private final BezierCurve fireToRiver;
    private ArrayList<Point2D> controlPoints;
    private Transform selectedFire;
    private Transform firstFire;
    private final int DOT_OFFSET = 15;

    public FlightPath(Transform startLocation, Dimension worldSize) {
        this.worldSize = worldSize;
        this.startLocation = startLocation;
        firstFire = Transform.makeIdentity();
        firstFire.translate(worldSize.getWidth() * 0.8f,
                worldSize.getHeight() * 0.2f);

        this.selectedFire = firstFire;
        helipadToRiver = new BezierCurve(helipadToRiver());
        riverToFire = new BezierCurve(riverToFire());
        fireToRiver = new BezierCurve(fireToRiver());
    }

    private ArrayList helipadToRiver() {
        controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(  startLocation.getTranslateX() + DOT_OFFSET,
                                        startLocation.getTranslateY() + DOT_OFFSET));
        controlPoints.add(new Point2D(worldSize.getWidth()/2 + DOT_OFFSET,
                                      worldSize.getHeight() * 0.5 + DOT_OFFSET));
        controlPoints.add(new Point2D(-100 + DOT_OFFSET,
                                      worldSize.getHeight() * 0.7 + DOT_OFFSET));
        controlPoints.add(new Point2D(worldSize.getWidth()/2 + DOT_OFFSET,
                                      worldSize.getHeight() * 0.7 + DOT_OFFSET));
        return controlPoints;
    }

    private ArrayList riverToFire() {
        controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(worldSize.getWidth()/2 + DOT_OFFSET,
                                      worldSize.getHeight() * 0.7 + DOT_OFFSET));
        controlPoints.add(new Point2D(worldSize.getWidth() + 100 + DOT_OFFSET,
                                      worldSize.getHeight() * 0.7 + DOT_OFFSET));
        controlPoints.add(new Point2D(worldSize.getWidth() + 100 + DOT_OFFSET,
                                      0 + DOT_OFFSET));
        controlPoints.add(new Point2D(  selectedFire.getTranslateX() + DOT_OFFSET,
                                        selectedFire.getTranslateY() + DOT_OFFSET));
        return controlPoints;
    }

    private ArrayList<Point2D> fireToRiver() {
        controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(  selectedFire.getTranslateX() + DOT_OFFSET,
                                        selectedFire.getTranslateY() + DOT_OFFSET));
        controlPoints.add(new Point2D(-100 + DOT_OFFSET,0 + DOT_OFFSET));
        controlPoints.add(new Point2D(-100 + DOT_OFFSET,
                                      worldSize.getHeight() * 0.7 + DOT_OFFSET));
        controlPoints.add(new Point2D(worldSize.getWidth()/2 + DOT_OFFSET,
                                      worldSize.getHeight() * 0.7 + DOT_OFFSET));
        return controlPoints;
    }

    public boolean selectFirstFire() {
        return selectedFire != firstFire;
    }

    public BezierCurve getHelipadToRiver() {
        return helipadToRiver;
    }

    public BezierCurve getRiverToFire() {
        updateSelectedFire(selectedFire);
        return riverToFire;
    }

    public BezierCurve getFireToRiver() {
        updateSelectedFire(selectedFire);
        return fireToRiver;
    }

    public void updateSelectedFire(Transform selectedFire) {
        this.selectedFire = selectedFire;
        riverToFire.updateControlPoints(riverToFire());
        fireToRiver.updateControlPoints(fireToRiver());
    }

    @Override
    protected void localDraw(Graphics g,
                             Point containerOrigin,
                             Point screenOrigin) {
    }

    class BezierCurve extends GameObject {
        private ArrayList<Point2D> controlPoints;
        final static int POINT_SIZE = 30;

        public BezierCurve(ArrayList<Point2D> controlPoints) {
            this.dimension = new Dimension(30, 30);
            this.controlPoints = controlPoints;
        }

        void updateControlPoints(ArrayList<Point2D> controlPoints) {
            this.controlPoints = controlPoints;
        }

        Point2D evaluateCurve(double t) {
            Point2D p = new Point2D(0, 0);

            int d = controlPoints.size()-1;
            for(int i=0; i<controlPoints.size(); i++) {
                double b = bernsteinD(d, i, t);
                double mx = b * controlPoints.get(i).getX();
                double my = b * controlPoints.get(i).getY();
                p.setX(p.getX() + mx);
                p.setY(p.getY() + my);
            }
            return p;
        }

        private void drawBezierCurve(Graphics g,
                                     ArrayList<Point2D> controlPoints) {
            final double smallFloatIncrement = 0.06;
            g.setColor(ColorUtil.GREEN);
            for (Point2D p : controlPoints)
                g.fillArc(  (int)p.getX()-POINT_SIZE/2,
                            (int)p.getY()-POINT_SIZE/2,
                            POINT_SIZE, POINT_SIZE, 0, 360);

            g.setColor(ColorUtil.GREEN);
            Point2D currentPoint = controlPoints.get(0);
            Point2D nextPoint;
            double t = 0;
            while(t < 1) {
                nextPoint = evaluateCurve(t);

                g.drawLine( (int)currentPoint.getX(), (int)currentPoint.getY(),
                        (int)nextPoint.getX(), (int)nextPoint.getY());

                currentPoint = nextPoint;
                t = t + smallFloatIncrement;
            }
            nextPoint = controlPoints.get(controlPoints.size()-1);
            g.drawLine( (int)currentPoint.getX(), (int)currentPoint.getY(),
                    (int)nextPoint.getX(), (int)nextPoint.getY());
        }

        private double bernsteinD(int d, int i, double t) {
            // Bernstein polynomial
            //
            return choose(d,i) * MathUtil.pow(t, i) * MathUtil.pow(1-t, d-i);
        }

        private double choose(int n, int k) {
            // base case
            //
            if (k <= 0 || k >= n) {
                return 1;
            }

            // recurse using pascal's triangle
            //
            return choose(n-1, k-1) + choose(n-1, k);
        }

        @Override
        protected void localDraw(Graphics g,
                                 Point containerOrigin,
                                 Point screenOrigin) {
            drawBezierCurve(g, controlPoints);
        }

        public void setTail(Point2D lastControlPoint) {
            controlPoints.set(controlPoints.size()-1, lastControlPoint);
        }

        ArrayList<Point2D> getControlPoints() {
            return controlPoints;
        }

        public Point2D getLastPointOfPath() {
            return controlPoints.get(controlPoints.size() - 1);
        }
    }
}


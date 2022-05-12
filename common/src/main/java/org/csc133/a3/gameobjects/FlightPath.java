package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;

import java.util.ArrayList;

public class FlightPath extends GameObject{
    private final BezierCurve helipadToRiver;
    private final BezierCurve riverToFire;
    private final BezierCurve fireToRiver;
    private ArrayList<Point2D> controlPoints;
//    private final Dimension worldSize;

    public FlightPath() {
        // Init path from helipad to river
//        controlPoints = new ArrayList<>();
//        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.1));
//        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.5));
//        controlPoints.add(new Point2D(-100,worldSize.getHeight() * 0.7));
//        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.7));
        //controlPoints = new ArrayList<>();
        helipadToRiver = new BezierCurve(helipadToRiver());
        riverToFire = new BezierCurve(riverToFire());
        fireToRiver = new BezierCurve();
    }

    private ArrayList helipadToRiver() {
        controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(worldSize.getWidth() *0.5,worldSize.getHeight() * 0.1));
        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.5));
        controlPoints.add(new Point2D(-100,worldSize.getHeight() * 0.7));
        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.7));
        return controlPoints;
    }
    private ArrayList riverToFire() {
        controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.7));
        controlPoints.add(new Point2D(+100,worldSize.getHeight() * 0.7));
        // first default fire
        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.7));
        return controlPoints;
    }
    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {

    }
}

class BezierCurve extends GameObject{
    private ArrayList<Point2D> controlPoints;
    final static int POINT_SIZE = 30;

    public void setControlPoints(ArrayList<Point2D> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public BezierCurve(ArrayList<Point2D> controlPoints) {
        this.dimension = new Dimension(30, 30);
        this.controlPoints = controlPoints;
    }

    public BezierCurve() {
        this.dimension = new Dimension(30, 30);
        pathToRiver();
    }

    private void pathToRiver() {
        controlPoints = new ArrayList<>();
        controlPoints.add(new Point2D(worldSize.getWidth() *0.5,worldSize.getHeight() * 0.1));
        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.5));
        controlPoints.add(new Point2D(-100,worldSize.getHeight() * 0.7));
        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.7));
    }

    public Point2D getStartControlPoint() {
        return controlPoints.get(0);
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

    private void drawBezierCurve(Graphics g, ArrayList<Point2D> controlPoints) {
        final double smallFloatIncrement = 0.06;
        g.setColor(ColorUtil.GREEN);
        for (Point2D p : controlPoints)
            g.fillArc((int)p.getX()-POINT_SIZE/2, (int)p.getY()-POINT_SIZE/2, POINT_SIZE, POINT_SIZE, 0, 360);

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
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        drawBezierCurve(g, controlPoints);
    }

    public void setTail(Point2D lastControlPoint) {
        controlPoints.set(controlPoints.size()-1, lastControlPoint);
    }
}

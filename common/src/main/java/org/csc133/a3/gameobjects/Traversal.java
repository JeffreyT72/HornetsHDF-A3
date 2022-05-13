/*
package org.csc133.a3.gameobjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;

public class Traversal extends FlightPath.BezierCurve {
    boolean active;
    boolean changed;
    double t;
    Helicopter heli;

    public Traversal(Helicopter heli) {
        active = false;
        this.heli = heli;
        changed = false;
    }

    public void activate() {
        active = true;
    }

    public void deactivate() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public boolean hasChanged() {
        return changed;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void moveAlongPath(Point2D currentPoint) {
        Point2D nextPoint = evaluateCurve(t);

        // Translate from current to next point.
        //
        double tx = nextPoint.getX() - currentPoint.getX();
        double ty = nextPoint.getY() - currentPoint.getY();
        heli.translate(tx, ty);

        // Direction
        //
        double theta = 90 - Math.toDegrees(MathUtil.atan2(ty, tx));

        if(t <= 1) {
            t = t + 1 * 0.003;
            heli.rotate((float)(heli.getHeading() - theta));
            heli.setHeading(theta);
        }
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        if (active)
            super.localDraw(g, containerOrigin, screenOrigin);
    }

    @Override
    public void setTail(Point2D lastControlPoint) {
        super.setTail(lastControlPoint);
        changed = true;
    }
}
*/

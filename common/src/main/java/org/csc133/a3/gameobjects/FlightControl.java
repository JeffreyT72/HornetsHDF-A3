/*
package org.csc133.a3.gameobjects;

import com.codename1.ui.geom.Point2D;

import java.util.ArrayList;

public class FlightControl {
    private Traversal primary;
    private Traversal correction;

    public FlightControl(Helicopter heli) {
        primary = new Traversal(heli);
        correction = new Traversal(heli);
        primary.activate();
        correction.deactivate();
    }

    public void moveAlongPath(Point2D currentPoint) {
        if (!primary.hasChanged() && !correction.isActive()) {
            primary.moveAlongPath(currentPoint);
            if (primary.getT() >= 1)
                primary.setT(0);
        }
        // update correction curve
        if (primary.hasChanged()) {
            ArrayList<Point2D> cPoints = new ArrayList<>();
            cPoints.add(currentPoint);
            cPoints.add(primary.evaluateCurve(primary.getT()));
            correction.setControlPoints(cPoints);
            correction.setT(0);
            correction.activate();
            primary.setChanged(false);
        }
        // then, move along with correction
        if (correction.isActive()) {
            correction.moveAlongPath(currentPoint);
            if (correction.getT() >= 1) {
                correction.deactivate();
            }
        }
    }

    // State pattern
    public Traversal getPrimary() {return primary;}
    public Traversal getCorrection() {return correction;}
}
*/

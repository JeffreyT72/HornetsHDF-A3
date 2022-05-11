package org.csc133.a3.gameobjects;

import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;

import java.util.ArrayList;

public class FlightPath {
    private final BezierCurve helipadToRiver;
//    private final BezierCurve riverToFire;
//    private final BezierCurve fireToRiver;
//    private final Dimension worldSize;

    public FlightPath(BezierCurve helipadToRiver) {
        this.helipadToRiver = helipadToRiver;

//        controlPoints = new ArrayList<>();
//        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.1));
//        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.5));
//        controlPoints.add(new Point2D(-100,worldSize.getHeight() * 0.7));
//        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight() * 0.7));
    }
}

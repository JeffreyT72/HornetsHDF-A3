package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;

import java.util.ArrayList;

public class BezierCurve extends GameObject{
    ArrayList<Point2D> controlPoints;

    public BezierCurve(ArrayList<Point2D> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public BezierCurve(Dimension worldSize) {
/*        Transform defaultLocation = Transform.makeIdentity();
        defaultLocation.translate(worldSize.getWidth() * 0.85f,
                worldSize.getHeight() * 0.15f);*/
        this.worldSize = worldSize;
        this.dimension = new Dimension(30, 30);

        controlPoints = new ArrayList<>();
        // point start from bottom left corner
        controlPoints.add(new Point2D(30,30));
        controlPoints.add(new Point2D(30,worldSize.getHeight()/2));
        controlPoints.add(new Point2D(30,worldSize.getHeight()-30));
        controlPoints.add(new Point2D(worldSize.getWidth()/2,30));
        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight()/2));
        controlPoints.add(new Point2D(worldSize.getWidth()/2,worldSize.getHeight()-30));
        controlPoints.add(new Point2D(worldSize.getWidth()-30,30));
        controlPoints.add(new Point2D(worldSize.getWidth()-30,worldSize.getHeight()/2));
        controlPoints.add(new Point2D(worldSize.getWidth()-30,worldSize.getHeight()-30));
    }

    private void drawBezierCurve(Graphics g, ArrayList<Point2D> controlPoints) {
        g.setColor(ColorUtil.GRAY);
        for (Point2D p : controlPoints)
            g.fillArc((int)p.getX()-15, (int)p.getY()-15, 30, 30, 0, 360);
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        drawBezierCurve(g, controlPoints);

    }

}

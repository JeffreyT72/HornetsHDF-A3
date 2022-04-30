package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;
import org.csc133.a3.GameWorld;

public class NonPlayerHelicopter extends Helicopter{
    private static final int HELICOLOR = ColorUtil.GREEN;
    private static NonPlayerHelicopter instance;

    private NonPlayerHelicopter(Dimension worldSize, int initFuel) {
        super(worldSize, HELICOLOR, initFuel);
        this.translate(worldSize.getWidth() * 0.5, worldSize.getHeight() * 0.1);
    }

    public static NonPlayerHelicopter getInstance() {
        if(instance == null) {
            Dimension worldSize    = GameWorld.getInstance().getDimension();
            int initFuel         = GameWorld.getInstance().getFuel();
            //Transform startPoint = GameWorld.getInstance().getStartingPoint();

            instance = new NonPlayerHelicopter(worldSize, initFuel);
        }
        return instance;
    }

    // testing
    private BezierCurve bc;
    public void setPath(BezierCurve bc) {
        this.bc = bc;
    }

    private double t = 0;
    private double pathSpeed = 1.5;
    public void testPath() {
        Point2D currentPoint = new Point2D(myTranslation.getTranslateX(), myTranslation.getTranslateY());
        Point2D nextPoint = bc.evaluateCurve(t);

        // Translate from current to next point.
        //
        double tx = nextPoint.getX() - currentPoint.getX();
        double ty = nextPoint.getY() - currentPoint.getY();
        this.translate(tx, ty);

        // Direction
        //
        double theta = 90 - Math.toDegrees(MathUtil.atan2(ty, tx));

        if(t < 1) {
            t = t + pathSpeed * 0.003;
            rotate((float)(getHeading() - theta));
            setHeading(theta);
        }
    }

    public class FlightPath extends GameObject {
        @Override
        protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {

        }
    }

    public class Avoid extends GameObject {
        @Override
        protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {

        }
    }
}


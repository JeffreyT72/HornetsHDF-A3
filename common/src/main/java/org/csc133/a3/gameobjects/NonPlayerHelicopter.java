package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.GameWorld;
import org.csc133.a3.interfaces.Strategy;

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
            int initFuel         = GameWorld.getInstance().getInitFuel();
            //Transform startPoint = GameWorld.getInstance().getStartingPoint();

            instance = new NonPlayerHelicopter(worldSize, initFuel);
        }
        return instance;
    }

    private BezierCurve bc;
    public void setPath(BezierCurve bc) {
        this.bc = bc;
    }

    private double t = 0;
    public void travel() {
        Point2D currentPoint = new Point2D(myTranslation.getTranslateX(), myTranslation.getTranslateY());
        Point2D nextPoint = bc.evaluateCurve(t);

        // Translate from current to next point.
        //
        double tx = nextPoint.getX() - currentPoint.getX();
        double ty = nextPoint.getY() - currentPoint.getY();

        // Angle offset accounts for which direction is 0 degrees.
        //
        //int theta = (int) (angleFix - Math.toDegrees(Math.atan2(ty, tx)));
        NonPlayerHelicopter.this.translate(tx, ty);

        if(t < 1) {
            t = t + (1 * 0.003);
            //rotate(getHeading() - theta);
            //setHeading(theta);
        }
    }

    class FlightStrategy implements Strategy {
        @Override
        public void followCurve(){
            travel();
        }
    }
}


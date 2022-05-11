package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;
import org.csc133.a3.GameWorld;
import org.csc133.a3.interfaces.Strategy;

public class NonPlayerHelicopter extends Helicopter{
    private static final int HELICOLOR = ColorUtil.GREEN;
    private static NonPlayerHelicopter instance;

    private NonPlayerHelicopter(Dimension worldSize, int initFuel, Transform helipadLocation) {
        super(worldSize, HELICOLOR, initFuel, helipadLocation);
        this.translate(worldSize.getWidth() * 0.5, worldSize.getHeight() * 0.1);
    }

    public static NonPlayerHelicopter getInstance() {
        if(instance == null) {
            Dimension worldSize         = GameWorld.getInstance().getDimension();
            int initFuel                = GameWorld.getInstance().getFuel();
            Transform helipadLocation   = GameWorld.getInstance().getHelipadLocation();

            instance = new NonPlayerHelicopter(worldSize, initFuel, helipadLocation);
        }
        return instance;
    }

    // testing
    private BezierCurve bc;
    public void setFlightControl(BezierCurve bc) {
        this.bc = bc;
    }

    private double t = 0;
    private double pathSpeed = 1.5;

    @Override
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

    private boolean arrived() {
        if (t >= 1)
            return true;
        else
            return false;
    }

    public class FlightPath implements Strategy {
        @Override
        public void followCurve() {
            if (arrived())
                action();
            else
                testPath();
        }

        private void action() {

        }

    }

    public class Avoid implements Strategy {

        @Override
        public void followCurve() {

        }
    }
}


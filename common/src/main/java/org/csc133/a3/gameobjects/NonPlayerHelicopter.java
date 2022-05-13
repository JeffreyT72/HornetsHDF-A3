package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.GameWorld;
import org.csc133.a3.interfaces.Strategy;

public class NonPlayerHelicopter extends Helicopter{
    private static final int HELICOLOR = ColorUtil.GREEN;
    private static NonPlayerHelicopter instance;
    private Strategy strategy;
    private final FlightPath flightPath;
    // testing
    //private FlightControl fc;

    private NonPlayerHelicopter(Dimension worldSize, int initFuel, Transform helipadLocation) {
        super(worldSize, HELICOLOR, initFuel, helipadLocation);
        flightPath = GameWorld.getInstance().getFlightPath();
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
//    private BezierCurve bc;
//    public void setFlightControl(BezierCurve bc) {
//        this.bc = bc;
//    }
/*    public void setFlightControl(FlightControl fc) {
        this.fc = fc;
    }*/
    private void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
    
    private double t = 0;
    private double pathSpeed = 1.5;

/*    @Override
    public void testPath() {
        fc.moveAlongPath(new Point2D(   myTranslation.getTranslateX(),
                myTranslation.getTranslateY()));
    }*/

    public void startNPH() {
        if (getSpeed() < 9) {
            accelerate();
        }
        //strategy.followCurve();
    }

    private boolean arrived() {
        if (t >= 1)
            return true;
        else
            return false;
    }

    public class FlightPathStrategy implements Strategy {
        @Override
        public void followCurve() {
            if (arrived())
                action();
            else
                moveAlongPath();
        }

        private void action() {

        }

        private void moveAlongPath() {
            Point2D currentPoint = new Point2D(getTranslation().getTranslateX(), getTranslation().getTranslateY());
            Point2D nextPoint = flightPath.getHelipadToRiver().evaluateCurve(t);

            double tx = nextPoint.getX() - currentPoint.getX();
            double ty = nextPoint.getY() - currentPoint.getY();

            int theta = (int) (90 - Math.toDegrees(Math.atan2(ty, tx)));
            NonPlayerHelicopter.this.translate(tx, ty);

            if(!arrived()) {
                t = t + 1 * 0.003; // getSpeed()
                rotate((float) (getHeading() - theta));
                setHeading(theta);
            }
        }
    }

    public class Avoid implements Strategy {

        @Override
        public void followCurve() {

        }
    }
}


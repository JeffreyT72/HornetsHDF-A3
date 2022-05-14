package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;
import org.csc133.a3.GameWorld;
import org.csc133.a3.interfaces.Strategy;

public class NonPlayerHelicopter extends Helicopter{
    private static final int HELICOLOR = ColorUtil.GREEN;
    private static NonPlayerHelicopter instance;
    private Strategy strategy;
    private final FlightPath flightPath;
    private double avoidAngle;
    // testing
    //private FlightControl fc;

    private NonPlayerHelicopter(Dimension worldSize, int initFuel, Transform helipadLocation) {
        super(worldSize, HELICOLOR, initFuel, helipadLocation);
        flightPath = GameWorld.getInstance().getFlightPath();

        setStrategy(new FlightPathStrategy());
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

    public void nphAction() {
        startOrStopEngine();
        HelicopterSpeedUp();
        strategy.followCurve();
    }

    private void HelicopterSpeedUp() {
        if (getSpeed() < 3) {
            accelerate();
        }
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

            // Translate from current to next point.
            //
            double tx = nextPoint.getX() - currentPoint.getX();
            double ty = nextPoint.getY() - currentPoint.getY();
            NonPlayerHelicopter.this.translate(tx, ty);

            // Direction
            //
            int theta = (int) (90 - Math.toDegrees(MathUtil.atan2(ty, tx)));

            if(!arrived()) {
                t = t + getSpeed() * 0.003; // getSpeed()
                rotate((float) (getHeading() - theta));
                setHeading(theta);
            }
        }
    }

    public class Avoid implements Strategy {

        @Override
        public void followCurve() {
            avoid();
        }

        private void avoid() {
            int speedMultiplier = 2;
            double tx = getSpeed() * speedMultiplier * Math.cos(avoidAngle);
            double ty = getSpeed() * speedMultiplier * Math.sin(avoidAngle);
            translate(tx, ty);

            int theta = (int) (90 - Math.toDegrees(MathUtil.atan2(ty, tx)));
            rotate((float)getHeading() - theta);
            setHeading(theta);
        }
    }
}


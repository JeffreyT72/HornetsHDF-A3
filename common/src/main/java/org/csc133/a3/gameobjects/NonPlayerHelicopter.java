package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;
import org.csc133.a3.GameWorld;
import org.csc133.a3.interfaces.Strategy;

import java.util.ArrayList;

public class NonPlayerHelicopter extends Helicopter{
    private static NonPlayerHelicopter instance;
    private Strategy strategy;
    private double t;
    private double avoidAngle;
    private FlightPath.BezierCurve currentPath;
    private Point2D lastPointOfPath;
    private final FlightPath flightPath;
    private static final int HELICOPTER_COLOR = ColorUtil.GREEN;

    private NonPlayerHelicopter(Dimension worldSize, int initFuel, Transform helipadLocation) {
        super(worldSize, HELICOPTER_COLOR, initFuel, helipadLocation);
        initTValue();
        flightPath = GameWorld.getInstance().getFlightPath();
        currentPath = flightPath.getHelipadToRiver();
        lastPointOfPath = currentPath.getLastPointOfPath();

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

    private void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void nphAction() {
        if(isInOffState() && flightPath.selectFirstFire()) {
            GameWorld.getInstance().spawnNPH();
            startOrStopEngine();
        } else if(isInReadyState()) {
            helicopterSpeedUp();
            updateStrategy();
            strategy.followCurve();
        }
    }

    private boolean isInOffState() {
        return currentState().equals("Off");
    }

    private boolean isInReadyState() {
        return currentState().equals("Ready");
    }

    private void updateStrategy() {
        if(isFlightPathStrategy()) {
            collisionDetection();
        }
        if(needCorrection()) {
            setStrategy(new PathCorrectionStrategy());
        }
        if (canExitAvoidStrategy()) {
            createCorrectionPath();
        }
    }

    private boolean isFlightPathStrategy() {
        return strategy.getClass().getSimpleName().equals("FlightPathStrategy");
    }

    private boolean needCorrection() {
        return (t > 0 && t < 1) && goalChanged() && isFlightPathStrategy();
    }

    private boolean canExitAvoidStrategy() {
        return strategy.getClass().getSimpleName().equals("AvoidStrategy") &&
                !checkCollision(1);
    }

    private boolean goalChanged() {
        return lastPointOfPath != currentPath.getLastPointOfPath();
    }

    private void helicopterSpeedUp() {
        if (getSpeed() < 9) {
            accelerate();
        }
    }

    private boolean arrived() {
        return t >= 1;
    }

    private void createCorrectionPath() {
        ArrayList<Point2D> correctionPath = new ArrayList<>();

        correctionPath.add(new Point2D(getTranslation().getTranslateX(), getTranslation().getTranslateY()));
        correctionPath.add(currentPath.getControlPoints().get(1));
        correctionPath.add(currentPath.getLastPointOfPath());

        currentPath.updateControlPoints(correctionPath);
        setStrategy(new FlightPathStrategy());
        initTValue();
    }

    private void initTValue() {
        t = 0;
    }
    private void updateLastPoint() {
        lastPointOfPath = currentPath.getLastPointOfPath();
    }

    public void reset() {
        instance = null;
    }

    private void collisionDetection() {
        if (checkCollision(0.5)) {
            setStrategy(new AvoidStrategy());
            setAvoidanceAngle();
        }
    }

    private void setAvoidanceAngle() {
        int heading;
        Transform playerHelicopter = PlayerHelicopter.getInstance().getTranslation();

        // Use four quadrant to determine the opposite angle
        //
        if (getTranslation().getTranslateX() > playerHelicopter.getTranslateX()              // top left
                && getTranslation().getTranslateY() > playerHelicopter.getTranslateY()) {
            heading = 45;   // 45 degree because 0 is east
        } else if (getTranslation().getTranslateX() < playerHelicopter.getTranslateX()       // top right
                && getTranslation().getTranslateY() > playerHelicopter.getTranslateY()) {
            heading = 135;
        } else if (getTranslation().getTranslateX() < playerHelicopter.getTranslateX()       // bottom right
                && getTranslation().getTranslateY() < playerHelicopter.getTranslateY()) {
            heading = 225;
        } else {
            heading = 315;
        }
        avoidAngle = Math.toRadians(heading);
    }

    private boolean checkCollision(double circleFactor) {
        double playerRadius = PlayerHelicopter.getInstance().getBladeLength() * circleFactor;
        double nphRadius = NonPlayerHelicopter.getInstance().getBladeLength() * circleFactor;
        float playerX = PlayerHelicopter.getInstance().getTranslation().getTranslateX();
        float playerY = PlayerHelicopter.getInstance().getTranslation().getTranslateY();
        float nphX = getTranslation().getTranslateX();
        float nphY = getTranslation().getTranslateY();

        double distance = MathUtil.pow(nphY - playerY, 2) + MathUtil.pow(nphX - playerX, 2);
        return distance <= MathUtil.pow(playerRadius + nphRadius, 2);
    }

    public boolean crashed() {
        if(isInOffState()) {
            return false;
        }
        return checkCollision(0.3);
    }

    public class FlightPathStrategy implements Strategy {
        @Override
        public void followCurve() {
            if (arrived())
                action();
            else {
                moveAlongPath();
            }
        }

        private void action() {
            if (getWater() < 1000) {
                attemptToDrink();

                // after drink water
                if (getWater() >= 1000) {
                    currentPath = flightPath.getRiverToFire();
                    t=0;
                    updateLastPoint();
                }
            } else if (getWater() >= 1000) {
                gw.fight(NonPlayerHelicopter.getInstance());
                currentPath = flightPath.getFireToRiver();
                initTValue();
                updateLastPoint();
            }
        }

        private void attemptToDrink() {
            setCurrentSpeed(0);
            drink();
        }

        private void moveAlongPath() {
            Point2D currentPoint = new Point2D(getTranslation().getTranslateX(), getTranslation().getTranslateY());
            Point2D nextPoint = currentPath.evaluateCurve(t);

            // Translate from current to next point.
            //
            double tx = nextPoint.getX() - currentPoint.getX();
            double ty = nextPoint.getY() - currentPoint.getY();
            NonPlayerHelicopter.this.translate(tx, ty);

            // Direction
            //
            int theta = (int) (90 - Math.toDegrees(MathUtil.atan2(ty, tx)));

            if(!arrived()) {
                t += getSpeed() * 0.003;
                rotate((float) (getHeading() - theta));
                setHeading(theta);
            }
        }
    }

    public class PathCorrectionStrategy implements Strategy {
        @Override
        public void followCurve() {
            updateLastPoint();
            createCorrectionPath();
        }
    }

    public class AvoidStrategy implements Strategy {
        @Override
        public void followCurve() {
            avoid();
        }

        private void avoid() {
            int avoidSpeed = 2;
            double tx = getSpeed() * avoidSpeed * Math.cos(avoidAngle);
            double ty = getSpeed() * avoidSpeed * Math.sin(avoidAngle);
            translate(tx, ty);

            int theta = (int) (90 - Math.toDegrees(MathUtil.atan2(ty, tx)));
            rotate((float)getHeading() - theta);
            setHeading(theta);
        }
    }
}


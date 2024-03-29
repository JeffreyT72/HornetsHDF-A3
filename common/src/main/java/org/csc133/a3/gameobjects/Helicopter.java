package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.parts.Arc;
import org.csc133.a3.gameobjects.parts.Rectangle;
import org.csc133.a3.gameobjects.parts.Trapezoid;
import org.csc133.a3.interfaces.Steerable;

import java.util.ArrayList;

import static com.codename1.ui.CN.*;

//-----------------------------------------------------------------------------
public class Helicopter extends Movable implements Steerable {
    // Variables
    private int water;
    private boolean isOverRiver;

    // Constants
    private final int SIZE = 30;
    private final int SANGLE = -90;
    private final int MAX_SPEED = 10;
    private final int MIN_SPEED = 0;
    private final int MAX_WATER = 1000;
    private final int MIN_WATER = 0;

    // Helicopter parts
    static double rotationSpeed = 0d;
    final static int BUBBLE_RADIUS = 125;
    final static int ENGINE_BLOCK_WIDTH = 250;
    final static int ENGINE_BLOCK_HEIGHT = 100;
    final static int BLADE_LENGTH = BUBBLE_RADIUS * 5;
    final static int BLADE_WIDTH = 25;
    final static int BLADE_STARTING_ANGLE = 45;
    final static int JOINT_WIDTH = 30;
    final static int JOINT_HEIGHT = 10;
    final static int SKID_WIDTH = 30;
    final static int SKID_HEIGHT = 400;

    public void checkIsOnRiver(Transform riverTransform,
                               Dimension riverDimension) {
        int riverStartX = (int)riverTransform.getTranslateX()
                - riverDimension.getWidth()/2;
        int riverStartY = (int)riverTransform.getTranslateY()
                - riverDimension.getHeight()/2;
        int riverEndX = (int)riverTransform.getTranslateX()
                + riverDimension.getWidth()/2;
        int riverEndY = (int)riverTransform.getTranslateY()
                + riverDimension.getHeight()/2;

        isOverRiver = getTranslation().getTranslateX() <= riverEndX &&
                getTranslation().getTranslateX() >= riverStartX &&
                getTranslation().getTranslateY() <= riverEndY &&
                getTranslation().getTranslateY() >= riverStartY;
    }

    public void drink() {
        helicopterState.drink();
    }

    public void miss() {
        if (water > MIN_WATER)
            water = 0;
    }

    public void fuel() {
        helicopterState.fuelConsume();
    }

    public boolean hasLandedAt(Helipad helipad) {
        return helicopterState.hasLandedAt(helipad);
    }

    public void accelerate() {
        helicopterState.accelerate();
    }

    public void decelerate() {
        helicopterState.decelerate();
    }

    @Override
    public void steerLeft() {
        helicopterState.steerLeft();
    }

    @Override
    public void steerRight() {
        helicopterState.steerRight();
    }

    public String currentState() {
        return helicopterState.getClass().getSimpleName();
    }

    public double getBladeLength() {
        return BLADE_LENGTH/2;
    }

    public int getWater() {
        return water;
    }

    public int getFuel() {
        return fuel;
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloBubble extends Arc {
        public HeloBubble(int color) {
            super(color,
                  2*Helicopter.BUBBLE_RADIUS,
                  2*Helicopter.BUBBLE_RADIUS,
                    0,(float) (Helicopter.BUBBLE_RADIUS * 0.80),
                    1,1,
                    0,
                    135, 270);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloEngineBlock extends Rectangle {
        public HeloEngineBlock(int color) {
            super(color,
                    Helicopter.ENGINE_BLOCK_WIDTH,
                    Helicopter.ENGINE_BLOCK_HEIGHT,
                    0, (float) (-Helicopter.ENGINE_BLOCK_HEIGHT/2),
                    1, 1, 0);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloBlade extends Rectangle {
        public HeloBlade() {
            super(ColorUtil.GRAY,
                    BLADE_LENGTH,
                    BLADE_WIDTH,
                    0, -ENGINE_BLOCK_HEIGHT / 2,
                    1, 1,
                    BLADE_STARTING_ANGLE);
        }
        public void updateLocalTransforms(double rotationSpeed) {
            this.rotate((float)rotationSpeed);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloBladeShaft extends Arc {
        public HeloBladeShaft() {
            super(ColorUtil.BLACK,
                    2 * BLADE_WIDTH / 3,
                    2 * BLADE_WIDTH / 3,
                    0, -ENGINE_BLOCK_HEIGHT / 2,
                    1, 1,
                    0,
                    0, 360);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloFrontLeftJoint extends Rectangle {
        public HeloFrontLeftJoint() {
            super(ColorUtil.GRAY,
                    JOINT_WIDTH, JOINT_HEIGHT,
                    Helicopter.BUBBLE_RADIUS + 10,
                    Helicopter.BUBBLE_RADIUS + 10,
                    1, 1, 0);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloFrontRightJoint extends Rectangle {
        public HeloFrontRightJoint() {
            super(ColorUtil.GRAY,
                    JOINT_WIDTH, JOINT_HEIGHT,
                    -Helicopter.BUBBLE_RADIUS-10,
                    Helicopter.BUBBLE_RADIUS + 10,
                    1, 1, 0);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloBackLeftJoint extends Rectangle {
        public HeloBackLeftJoint() {
            super(ColorUtil.GRAY,
                    JOINT_WIDTH, JOINT_HEIGHT,
                    -ENGINE_BLOCK_WIDTH/2-10, -ENGINE_BLOCK_HEIGHT/2,
                    1, 1, 0);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloBackRightJoint extends Rectangle {
        public HeloBackRightJoint() {
            super(ColorUtil.GRAY,
                    JOINT_WIDTH, JOINT_HEIGHT,
                    ENGINE_BLOCK_WIDTH/2+10, -ENGINE_BLOCK_HEIGHT/2,
                    1, 1, 0);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloLeftLandingSkid extends Rectangle {
        public HeloLeftLandingSkid(int color) {
            super(color,
                    SKID_WIDTH, SKID_HEIGHT,
                    ENGINE_BLOCK_WIDTH/2+40, ENGINE_BLOCK_HEIGHT/2-30,
                    1, 1, 0);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloRightLandingSkid extends Rectangle {
        public HeloRightLandingSkid(int color) {
            super(color,
                    SKID_WIDTH, SKID_HEIGHT,
                    -ENGINE_BLOCK_WIDTH/2-40, ENGINE_BLOCK_HEIGHT/2-30,
                    1, 1, 0);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloTailCone extends Trapezoid {
        public HeloTailCone(int color) {
            super(color,
                    SKID_WIDTH, SKID_HEIGHT,
                    10, -Helicopter.ENGINE_BLOCK_HEIGHT,
                    1, 1, 0);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloTailFin extends Rectangle {
        public HeloTailFin(int color) {
            super(color,
                    60, 20,
                    -5, (float)(-Helicopter.ENGINE_BLOCK_HEIGHT*5.1),
                    1, 1, 0);
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloTailRotor extends Rectangle {
        public HeloTailRotor() {
            super(ColorUtil.GRAY,
                    15, 100,
                    35, (float)(-Helicopter.ENGINE_BLOCK_HEIGHT*5.1),
                    1, 1, 0);
        }
    }

    //```````````````````````````````````````````````````````````````````````````````````````
    // Helicopter State Pattern
    //
    private HelicopterState helicopterState;

    private void changeState(HelicopterState helicopterState) {
        this.helicopterState = helicopterState;
    }

    //```````````````````````````````````````````````````````````````````````````````````````
    private abstract class HelicopterState {
        protected Helicopter getHelicopter() {
            return Helicopter.this;
        }

        public abstract void startOrStopEngine();

        public void accelerate() {}

        public void decelerate() {}

        public void steerLeft() {}

        public void steerRight() {}

        public boolean hasLandedAt(Helipad helipad) { return false;}

        public void fuelConsume() {}

        public void drink() {}

        public void updateLocalTransforms() {}
    }

    //```````````````````````````````````````````````````````````````````````````````````````
    private class Off extends HelicopterState {

        @Override
        public void startOrStopEngine() {
            getHelicopter().changeState(new Starting());
        }

        @Override
        public boolean hasLandedAt(Helipad helipad) {
            boolean inHelipad =
                    getHelicopter().getTranslation().getTranslateX() >=
                        helipad.getTranslation().getTranslateX() -
                        helipad.getDimension().getWidth() / 2 &&
                    getHelicopter().getTranslation().getTranslateX() <=
                        helipad.getTranslation().getTranslateX() +
                        helipad.getDimension().getWidth() / 2 &&
                    getHelicopter().getTranslation().getTranslateY() >=
                        helipad.getTranslation().getTranslateY() -
                        helipad.getDimension().getHeight() / 2 &&
                    getHelicopter().getTranslation().getTranslateY() <=
                        helipad.getTranslation().getTranslateY() +
                        helipad.getDimension().getHeight() / 2;
            return inHelipad;
        }
    }

    //```````````````````````````````````````````````````````````````````````````````````````
    private class Starting extends HelicopterState {
        @Override
        public void startOrStopEngine() {
            getHelicopter().changeState(new Stopping());
        }

        @Override
        public void fuelConsume() {
            fuel -= (int) (Math.sqrt(currentSpeed) + 3);
        }

        @Override
        public void updateLocalTransforms() {
            heloBlade.updateLocalTransforms(rotationSpeed += 1);

            if (rotationSpeed >= 30) {
                getHelicopter().changeState(new Ready());
            }
        }
    }

    //```````````````````````````````````````````````````````````````````````````````````````
    private class Stopping extends HelicopterState {
        @Override
        public void startOrStopEngine() {
            getHelicopter().changeState(new Starting());
        }

        @Override
        public void updateLocalTransforms() {
            rotationSpeed -= 2;
            heloBlade.updateLocalTransforms(rotationSpeed);
            if (rotationSpeed <= 0) {
                // prevent the blade go to other direction
                rotationSpeed = 0;
                getHelicopter().changeState(new Off());
            }
        }
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private class Ready extends HelicopterState {
        @Override
        public void startOrStopEngine() {
            // conditions to stop engine
            // completely stop and press startStop button
            if (getSpeed() == 0)
                getHelicopter().changeState(new Stopping());
        }

        @Override
        public void accelerate() {
            if (currentSpeed < MAX_SPEED)
                currentSpeed++;
        }

        @Override
        public void decelerate() {
            if (currentSpeed > MIN_SPEED)
                currentSpeed--;
        }

        @Override
        public void steerLeft() {
            displayAngle -= 15;
            heading += 15;
            getHelicopter().rotate(15);
        }

        @Override
        public void steerRight() {
            displayAngle += 15;
            heading -= 15;
            getHelicopter().rotate(-15);
        }

        @Override
        public void drink() {
            if (isOverRiver && water < MAX_WATER)
                water += 100;
        }

        @Override
        public void fuelConsume() {
            fuel -= (int) (Math.sqrt(currentSpeed) + 5);
        }

        @Override
        public void updateLocalTransforms() {
            rotationSpeed = 30d;
            heloBlade.updateLocalTransforms(rotationSpeed);
        }
    }

    public void startOrStopEngine() {
        helicopterState.startOrStopEngine();
    }
    //```````````````````````````````````````````````````````````````````````````````````````
    private ArrayList<GameObject> heloParts;

    private HeloBlade heloBlade;
    private int color;
    private int fuel;

    public Helicopter(Dimension worldSize,
                      int HeliColor, int initFuel,
                      Transform helipadLocation) {
        this.worldSize = worldSize;
        this.color = HeliColor;
        setColor(HeliColor);
        this.fuel = initFuel;
        this.water = MIN_WATER;
        this.isOverRiver = false;
        this.currentSpeed = 0;
        this.dimension = new Dimension(SIZE, SIZE);
        this.heading = Math.toRadians(SANGLE);
        this.displayAngle = 0;

        this.translate( helipadLocation.getTranslateX(),
                        helipadLocation.getTranslateY());
        this.scale(0.3,0.3);

        helicopterState = new Off();
        heloParts = new ArrayList<>();
        heloParts = buildHeli();
    }

    private ArrayList<GameObject> buildHeli() {
        ArrayList<GameObject> buildHeliArr = new ArrayList<>();
        buildHeliArr.add(new HeloBubble(color));
        buildHeliArr.add(new HeloEngineBlock(color));
        heloBlade = new HeloBlade();
        buildHeliArr.add(heloBlade);
        buildHeliArr.add(new HeloBladeShaft());
        buildHeliArr.add(new HeloFrontLeftJoint());
        buildHeliArr.add(new HeloFrontRightJoint());
        buildHeliArr.add(new HeloBackLeftJoint());
        buildHeliArr.add(new HeloBackRightJoint());
        buildHeliArr.add(new HeloLeftLandingSkid(color));
        buildHeliArr.add(new HeloRightLandingSkid(color));
        buildHeliArr.add(new HeloTailCone(color));
        buildHeliArr.add(new HeloTailFin(color));
        buildHeliArr.add(new HeloTailRotor());

        return buildHeliArr;
    }

    @Override
    protected void localDraw(Graphics g,
                             Point containerOrigin,
                             Point screenOrigin) {
        cn1ReversePrimitiveTranslate(g, getDimension());
        cn1ReverseContainerTranslate(g, containerOrigin);

        for (GameObject go : heloParts)
            go.draw(g, containerOrigin, screenOrigin);

        textTranslate(g, containerOrigin, screenOrigin);
        g.setColor(ColorUtil.YELLOW);
        g.setFont(Font.createSystemFont(FACE_MONOSPACE,
                STYLE_BOLD, SIZE_MEDIUM));
        g.drawString("F : " + fuel, getWidth() +90, getHeight() + 40);
        g.drawString("W : " + water, getWidth() +90,getHeight() + 40 * 2);
    }

    public void updateLocalTransforms() {
        helicopterState.updateLocalTransforms();
    }
}
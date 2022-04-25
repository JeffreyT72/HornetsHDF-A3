package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.interfaces.Steerable;

import java.util.ArrayList;

import static com.codename1.ui.CN.*;

//-----------------------------------------------------------------------------
public class Helicopter extends Movable implements Steerable {
    // Variables
    private Point location;
    private int x, y, headingX, headingY;
    private int water;
    private boolean isOverRiver;    // Indicate the helicopter is in river area
    private int drawX, drawY;

    // Constants
    private final int SIZE = 30;
    private final int SANGLE = -90;         // Start angle, heading front
    private final int Heli_LENGTH = 50;     // Length of the helicopter line
    private final int MAX_SPEED = 10;
    private final int MIN_SPEED = 0;
    private final int MAX_WATER = 1000;
    private final int MIN_WATER = 0;

    public Helicopter(Dimension worldSize, Helipad helipad) {
        this.worldSize = worldSize;
        setColor(ColorUtil.YELLOW);
        this.water = MIN_WATER;
        this.isOverRiver = false;
        this.currentSpeed = 0;
        heading = Math.toRadians(SANGLE);
        //x = (int)helipad.getLocation().getX(); // centerX
        //y = (int)helipad.getLocation().getY(); // centerY
        location = new Point(x, y);
        headingX = (int) (location.getX() + (currentSpeed + Heli_LENGTH)
                * Math.cos(heading));
        headingY = (int) (location.getY() + (currentSpeed + Heli_LENGTH)
                * Math.sin(heading));
        this.dimension = new Dimension(SIZE, SIZE);
    }

    // Getter
    public Point getLocation() {
        return this.location;
    }

    public void accelerate() {
        if (currentSpeed < MAX_SPEED)
            currentSpeed++;
    }

    public void decelerate() {
        if (currentSpeed > MIN_SPEED)
            currentSpeed--;
    }

/*    public void move() {
        headingX = (int) (location.getX() + (currentSpeed + Heli_LENGTH)
                * Math.cos(heading));
        headingY = (int) (location.getY() + (currentSpeed + Heli_LENGTH)
                * Math.sin(heading));
        x = (int) (location.getX() + currentSpeed * Math.cos(heading));
        y = (int) (location.getY() + currentSpeed * Math.sin(heading));
        location = new Point(x, y);
    }*/

 /*   public void checkDrinkable(River river) {
        int riverStartX = (int)river.getLocation().getX()
                - river.getWidth()/2;
        int riverStartY = (int)river.getLocation().getY()
                - river.getHeight()/2;
        int riverEndX = (int)river.getLocation().getX()
                + river.getWidth()/2;
        int riverEndY = (int)river.getLocation().getY()
                + river.getHeight()/2;

        isOverRiver = getLocation().getX() <= riverEndX &&
                getLocation().getX() >= riverStartX &&
                getLocation().getY() <= riverEndY &&
                getLocation().getY() >= riverStartY;
    }*/

    public void drink() {
        if (isOverRiver && water < MAX_WATER)
            water += 100;
    }

    public void fight(Fire f) {
        int tempSize = f.getSize();
        if (f.getIsOverFire() && water > MIN_WATER) {
            tempSize -= water / 3;
            if (tempSize <= 0) {
                f.setWasExtinguished(true);
            } else {
                f.setFireSize(water / 3);
            }
            water = 0;
        }
    }

    public void miss() {
        if (water > MIN_WATER)
            water = 0;
    }

    public void fuel() {
        gw.setFuel((int)(Math.sqrt(currentSpeed) + 5));
    }
    /*
    @Override
    public void draw(Graphics g, Point containerOrigin) {
        // Helicopter
        g.setColor(getColor());

        drawX = containerOrigin.getX() + location.getX()
                - dimension.getWidth()/2;
        drawY = containerOrigin.getY() + location.getY()
                - dimension.getHeight()/2;
        headingX += containerOrigin.getX();
        headingY += containerOrigin.getY();

        g.fillArc(drawX,drawY, dimension.getWidth(), dimension.getHeight(),
                0, 360);
        g.drawLine(containerOrigin.getX() + location.getX(),
                containerOrigin.getY() + location.getY(),
                headingX, headingY);

        // Texts
        int textX = containerOrigin.getX() + (int)location.getX()
                + dimension.getWidth()/2 + 10;
        int textY = containerOrigin.getY() + (int)location.getY()
                + dimension.getHeight()/2 - 30;
        g.setFont(Font.createSystemFont(FACE_MONOSPACE,
                STYLE_BOLD, SIZE_MEDIUM));
        g.drawString("F  : " + gw.getFuel(), textX, textY);
        textY += 30;
        g.drawString("W : " + water, textX, textY);
    }
    */

    @Override
    public void steerLeft() {
        heading -= Math.toRadians(15);
    }

    @Override
    public void steerRight() {
        heading += Math.toRadians(15);
    }

    final static int BUBBLE_RADIUS = 125;

    //```````````````````````````````````````````````````````````````````````````````````````
    private static class HeloBubble extends GameObject {

        public HeloBubble() {
            setColor(ColorUtil.YELLOW);
            setDimension(new Dimension(2*Helicopter.BUBBLE_RADIUS,
                                        2*Helicopter.BUBBLE_RADIUS));
            translate(0, Helicopter.BUBBLE_RADIUS * 0.80);
        }

        @Override
        protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
            g.setColor(getColor());
            containerTranslate(g, containerOrigin);
            cn1ForwardPrimitiveTranslate(g, getDimension());
            g.drawArc(0, 0,
                        getDimension().getWidth(), getDimension().getHeight(),
                    135, 270);
        }

        public void rotate(float degrees) {
            myRotation.rotate((float)Math.toRadians(degrees), 0, 0);
        }

        public void scale(double sx, double sy) {
            myScale.scale((float)sx, (float)sy);
        }

        public void translate(double tx, double ty) {
            myTranslation.translate((float)tx, (float)ty);
        }
    }

    //```````````````````````````````````````````````````````````````````````````````````````
    private ArrayList<GameObject> heloParts;

    public Helicopter(Dimension worldSize) {
        this.worldSize = worldSize;
        setColor(ColorUtil.YELLOW);
        this.water = MIN_WATER;
        this.isOverRiver = false;
        this.currentSpeed = 0;
        heading = Math.toRadians(SANGLE);
        this.dimension = new Dimension(SIZE, SIZE);

        this.translate(worldSize.getWidth() * 0.5, worldSize.getHeight() * 0.5);
        this.scale(1,-1);
        this.rotate(180);

        heloParts = new ArrayList<>();

        heloParts.add(new HeloBubble());
    }

    public void rotate(float degrees) {
        myRotation.rotate((float)Math.toRadians(degrees), 0, 0);
    }

    public void scale(double sx, double sy) {
        myScale.scale((float)sx, (float)sy);
    }

    public void translate(double tx, double ty) {
        myTranslation.translate((float)tx, (float)ty);
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        // draw axis for debugging
        g.setColor(ColorUtil.LTGRAY);
        g.drawLine(-worldSize.getWidth()/2, 0, worldSize.getWidth()/2, 0);
        g.drawLine(0, -worldSize.getHeight()/2, 0, worldSize.getHeight()/2);

        //cn1ReversePrimitiveTranslate(g, getDimension());
        //cn1ReverseContainerTranslate(g, containerOrigin);

        for (GameObject go : heloParts)
            go.draw(g, containerOrigin, screenOrigin);

    }
}

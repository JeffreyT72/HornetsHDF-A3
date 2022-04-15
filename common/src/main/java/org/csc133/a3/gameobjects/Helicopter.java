package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.interfaces.Steerable;

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
        x = (int)helipad.getLocation().getX(); // centerX
        y = (int)helipad.getLocation().getY(); // centerY
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

    public void move() {
        headingX = (int) (location.getX() + (currentSpeed + Heli_LENGTH)
                * Math.cos(heading));
        headingY = (int) (location.getY() + (currentSpeed + Heli_LENGTH)
                * Math.sin(heading));
        x = (int) (location.getX() + currentSpeed * Math.cos(heading));
        y = (int) (location.getY() + currentSpeed * Math.sin(heading));
        location = new Point(x, y);
    }

    public void checkDrinkable(River river) {
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
    }

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

    @Override
    public void steerLeft() {
        heading -= Math.toRadians(15);
    }

    @Override
    public void steerRight() {
        heading += Math.toRadians(15);
    }
}

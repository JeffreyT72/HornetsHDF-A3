package org.csc133.a3.gameobjects.parts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.GameObject;

public class Trapezoid extends GameObject {
    private int topLeftX;
    private int topLeftY;
    private int topRightX;
    private int topRightY;
    private int bottomLeftX;
    private int bottomLeftY;
    private int bottomRightX;
    private int bottomRightY;
    private int midLeftX;
    private int midLeftY;
    private int midRightX;
    private int midRightY;
    private int x[] = new int[4];
    private int y[] = new int[4];
    final static int TRAPEZOID_POINTS = 4;
    private Trapezoid() {}

    public Trapezoid (int color, int width, int height,
                      float tx, float ty,
                      float sx, float sy,
                      float degreesRotation) {

        this.topLeftX = (int)(-width/2-20);
        this.topLeftY = (int)(height/2);
        this.topRightX = (int)(+width/2+20);
        this.topRightY = (int)(height/2);
        this.bottomLeftX = (int)(-width/2);
        this.bottomLeftY = (int)(-height/2);
        this.bottomRightX = (int)(width/2);
        this.bottomRightY = (int)(-height/2);
        this.midLeftX = -width/2-10;
        this.midLeftY = 0;
        this.midRightX = width/2+10;
        this.midRightY = 0;
        int tempX[] = {topLeftX, topRightX, bottomRightX, bottomLeftX};
        int tempY[] = {topLeftY, topRightY, bottomRightY, bottomLeftY};
        for (int i=0;i<4;i++) {
            x[i] = tempX[i];
            y[i] = tempY[i];
        }
        setColor(color);
        setDimension(new Dimension(width, height));

        translate(tx, ty);
        scale(sx, sy);
        rotate(degreesRotation);
    }
    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.setColor(getColor());
        g.drawPolygon(x, y, TRAPEZOID_POINTS);
        g.drawLine(topRightX, topRightY, midLeftX, midLeftY);
        g.drawLine(topLeftX, topLeftY, midRightX, midRightY);
        g.drawLine(bottomRightX, bottomRightY, midLeftX, midLeftY);
        g.drawLine(bottomLeftX, bottomLeftY, midRightX, midRightY);
    }
}

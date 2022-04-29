package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

//-----------------------------------------------------------------------------
public class Helipad extends Fixed{
    // Constants
    private final int DISTANCE = 10;
    private final int SIZE = 200;
    private final int THICKNESS = 2;

    public Helipad(Dimension worldSize) {
        this.worldSize = worldSize;
        setColor(ColorUtil.GRAY);
        this.dimension = new Dimension(SIZE, SIZE);

        translate(worldSize.getWidth() * 0.5, worldSize.getHeight() * 0.1);
    }

    /*
    @Override
    public void draw(Graphics g, Point containerOrigin) {
        g.setColor(getColor());

        int x = containerOrigin.getX() + (int)getLocation().getX()
                - dimension.getWidth()/2;
        int y = containerOrigin.getY() + (int)getLocation().getY()
                - dimension.getHeight()/2;
        int w = dimension.getWidth();
        int h = dimension.getHeight();
        int circleX = x + DISTANCE;
        int circleY = y + DISTANCE;
        int circleW = w - DISTANCE*2;
        int circleH = h - DISTANCE*2;

        g.drawRect(x, y, w, h, 2);
        g.drawArc(circleX, circleY, circleW, circleH, 0, 360);
    }
    */
    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.drawRect(0, 0, getWidth(), getHeight(), THICKNESS);
        g.drawArc(DISTANCE, DISTANCE, getWidth()-DISTANCE*2, getHeight()-DISTANCE*2, 0, 360);
    }
}

package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

//-----------------------------------------------------------------------------
public class Helipad extends Fixed {
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

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.drawRect(0, 0, getWidth(), getHeight(), THICKNESS);
        g.drawArc(DISTANCE, DISTANCE,
                getWidth()-DISTANCE*2, getHeight()-DISTANCE*2,
                0, 360);
    }
}

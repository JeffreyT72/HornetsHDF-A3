package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

//-----------------------------------------------------------------------------
public class River extends Fixed {
    private final int THICKNESS = 3;

    public River(Dimension worldSize) {
        this.worldSize = worldSize;
        setColor(ColorUtil.BLUE);
        this.dimension = new Dimension(worldSize.getWidth(), 200);
        translate(worldSize.getWidth() * 0.5, worldSize.getHeight() * 0.7);
        scale(1,-1);
        rotate(0);
    }

    @Override
    public void localDraw(Graphics g,
                          Point containerOrigin,
                          Point screenOrigin) {
        g.drawRect(0, 0, getWidth(), getHeight(), THICKNESS);
    }
}

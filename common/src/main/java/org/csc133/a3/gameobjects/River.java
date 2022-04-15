package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

//-----------------------------------------------------------------------------
public class River extends Fixed {
    public River(Dimension worldSize) {
        this.worldSize = worldSize;
        setColor(ColorUtil.BLUE);
        setLocation(new Point(worldSize.getWidth()/2,
                worldSize.getHeight()/4));
        this.dimension = new Dimension(worldSize.getWidth(), 200);
    }

    @Override
    public void draw(Graphics g, Point containerOrigin) {
        g.setColor(getColor());
        int x = containerOrigin.getX() + (int)getLocation().getX()
                - dimension.getWidth()/2;
        int y = containerOrigin.getY() + (int)getLocation().getY()
                - dimension.getHeight()/2;
        int w = dimension.getWidth();
        int h = dimension.getHeight();

        g.drawRect(x, y, w, h);
    }

    // Getter
    //
    public int getWidth() {
        return dimension.getWidth();
    }

    public int getHeight() {
        return dimension.getHeight();
    }
}

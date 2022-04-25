package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

//-----------------------------------------------------------------------------
public class River extends Fixed {
    //private Transform riverTranslation, riverRotation, riverScale;
    private final int THICKNESS = 3;

    public River(Dimension worldSize) {
        //this.worldSize = worldSize;
        setColor(ColorUtil.BLUE);
        //setLocation(new Point(worldSize.getWidth()/2,
        //        worldSize.getHeight()/4));
        this.dimension = new Dimension(worldSize.getWidth(), 200);

        this.translate(worldSize.getWidth() * 0.5, worldSize.getHeight() * 0.7);
        this.scale(1,-1);
        this.rotate(0);
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

        g.drawRect(x, y, w, h);
    }
    */
    @Override
    public void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.drawRect(0, 0, getWidth(), getHeight(), THICKNESS);
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

    // Getter
    //
    public int getWidth() {
        return dimension.getWidth();
    }

    public int getHeight() {
        return dimension.getHeight();
    }
}

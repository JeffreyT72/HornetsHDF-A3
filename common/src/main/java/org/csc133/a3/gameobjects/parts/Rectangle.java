package org.csc133.a3.gameobjects.parts;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.gameobjects.GameObject;

public class Rectangle extends GameObject {
    private Rectangle() {}

    public Rectangle (int color, int width, int height,
                      float tx, float ty,
                      float sx, float sy,
                      float degreesRotation) {

        setColor(color);
        setDimension(new Dimension(width, height));

        translate(tx, ty);
        scale(sx, sy);
        rotate(degreesRotation);
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.setColor(getColor());
        //containerTranslate(g, containerOrigin);
        //cn1ForwardPrimitiveTranslate(g, getDimension());
        g.drawRect(0, 0, getWidth(), getHeight());
    }
}

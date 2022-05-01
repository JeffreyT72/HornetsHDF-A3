package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.util.MathUtil;
import org.csc133.a3.interfaces.Selectable;

public class SpacePortal extends GameObject implements Selectable {
    private boolean selected;
    Point lowerLeft;
    private int myRadius;

    public SpacePortal() {
        this.dimension = new Dimension(30, 30);
        selected = false;
        myRadius = 100;
        lowerLeft = new Point(-myRadius, -myRadius);
        setColor(ColorUtil.BLUE);
    }

    private double distanceBetween(Point2D a, Point2D b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public boolean contains(Point2D p) {
        return distanceBetween(getLocation(), p) <= myRadius;
    }

    @Override
    public void select(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.setColor(getColor());

        for (int i = 0; i < 15; i++)
            g.drawArc(containerOrigin.getX() + lowerLeft.getX() + i,
                    containerOrigin.getY() + lowerLeft.getY() + i - 100,
                    2 * (myRadius - i), 2 * (myRadius - i),
                    0, 360);
    }
}

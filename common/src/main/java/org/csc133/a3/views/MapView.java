package org.csc133.a3.views;

import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.GameWorld;
import org.csc133.a3.gameobjects.GameObject;

public class MapView extends Container {
    private GameWorld gw;

    public MapView(GameWorld gw) {
        this.gw = gw;
    }

    @Override
    public void laidOut(){
        gw.setDimension(new Dimension(this.getWidth(), this.getHeight()));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Point containerOrigin = new Point(getX(), getY());  // originRelativeToParent
        Point screenOrigin = new Point(getAbsoluteX(), getAbsoluteY());  // originRelativeToScreen

        // draw all objects in the gameworld relative to this container object
        //
        for(GameObject go:gw.getGameObjectCollection())
            go.draw(g, containerOrigin, screenOrigin);
    }
}

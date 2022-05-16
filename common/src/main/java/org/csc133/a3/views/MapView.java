package org.csc133.a3.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.ui.layouts.BorderLayout;
import org.csc133.a3.GameWorld;
import org.csc133.a3.gameobjects.Fire;
import org.csc133.a3.gameobjects.GameObject;

public class MapView extends Container {
    private GameWorld gw;

    private float winLeft, winRight, winTop, winBottom;
    private float zoomFactor;
    // 0 = zoom out x0.5, 1 = normal x1
    private int zoomScale;

    public MapView(GameWorld gw) {
        this.gw = gw;
        setLayout(new BorderLayout());
        this.getAllStyles().setBgTransparency(255);
        this.getAllStyles().setBgColor(ColorUtil.BLACK);
        zoomFactor = 1f;
        zoomScale = 0;
    }

    // Set up the world to ND transform
    //
    private Transform buildWorldToNDXform(float winWidth, float winHeight,
                                          float winLeft, float winBottom) {
        Transform tmpXform = Transform.makeIdentity();
        tmpXform.scale( zoomFactor / winWidth,  zoomFactor / winHeight);
        tmpXform.translate(-winLeft, -winBottom);
        return tmpXform;
    }

    // Set up the ND to Screen transform
    //
    private Transform buildNDToDisplayXform(float displayWidth,
                                            float displayHeight) {
        Transform tmpXform = Transform.makeIdentity();
        tmpXform.translate(0, displayHeight);
        tmpXform.scale(displayWidth, -displayHeight);
        return tmpXform;
    }

    // Set up the Viewing Transformation Matrix.
    //
    Transform theVTM;
    private void setupVTM(Graphics g) {
        Transform worldToND, ndToDisplay;
        winLeft = winBottom = 0;
        winRight = this.getWidth();
        winTop = this.getHeight();

        float winHeight = winTop - winBottom;
        float winWidth = winRight - winLeft;

        worldToND = buildWorldToNDXform(winWidth, winHeight,
                winLeft, winBottom);
        ndToDisplay = buildNDToDisplayXform(getWidth(), getHeight());
        theVTM = ndToDisplay.copy();
        theVTM.concatenate(worldToND);

        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        gXform.translate(getAbsoluteX(), getAbsoluteY());
        gXform.concatenate(theVTM);
        gXform.translate(-getAbsoluteX(), -getAbsoluteY());
        g.setTransform(gXform);
    }

    private Transform getInverseVTM() {
        Transform inverseVTM = Transform.makeIdentity();
        try {
            theVTM.getInverse(inverseVTM);
        } catch (Transform.NotInvertibleException e) {
            e.printStackTrace();
        }
        return inverseVTM;
    }

    private Point2D transformPoint2D(Transform t, Point2D p) {
        float[] in = new float[2];
        float[] out = new float[2];
        in[0] = (float)p.getX();
        in[1] = (float)p.getY();
        t.transformPoint(in, out);
        return new Point2D(out[0], out[1]);
    }

    @Override
    public void laidOut(){
        gw.setDimension(new Dimension(this.getWidth(), this.getHeight()));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        setupVTM(g);

        Point containerOrigin = new Point(getX(), getY());  // originRelativeToParent
        Point screenOrigin = new Point(getAbsoluteX(), getAbsoluteY());  // originRelativeToScreen

        // draw all objects in the gameWorld relative to this container object
        //
        for(GameObject go:gw.getGameObjectCollection())
            go.draw(g, containerOrigin, screenOrigin);

        g.resetAffine();
    }

    @Override
    public void pointerPressed(int x, int y) {
        x = x - getAbsoluteX();
        y = y - getAbsoluteY();
        Point2D invertedPoint = transformPoint2D(getInverseVTM(),
                                                new Point2D(x, y));

        for(GameObject go : gw.getGameObjectCollection()) {
            if (go instanceof Fire) {
                ((Fire) go).checkIfSelected(invertedPoint);
            }
        }
    }

    public void zoom() {
        if (zoomScale == 0) {
            zoomFactor = 0.5f;
            zoomScale++;
        } else if (zoomScale == 1) {
            zoomFactor = 1f;
            zoomScale = 0;
        }
    }
}

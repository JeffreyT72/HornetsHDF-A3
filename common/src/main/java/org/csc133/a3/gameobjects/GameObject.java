package org.csc133.a3.gameobjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.GameWorld;

public abstract class GameObject {
    private int color;
    //Point location;
    Dimension dimension;
    Dimension worldSize;
    Transform gXform;
    Transform gOrigXform;
    Transform myTranslation;
    Transform myRotation;
    Transform myScale;
    // For getting the fuel value from GameWorld
    //
    GameWorld gw;

    public GameObject() {
        gw = GameWorld.getInstance();
        myTranslation = Transform.makeIdentity();
        myRotation = Transform.makeIdentity();
        myScale = Transform.makeIdentity();
    }

    void setColor(int color) {
        this.color = color;
    }

    void setDimension(Dimension d) {
        dimension = new Dimension(d.getWidth(), d.getHeight());
    }
/*    void setLocation(Point location) {
        this.location = location;
    }*/

    int getColor() {
        return this.color;
    }

    Dimension getDimension() {
        return this.dimension;
    }
/*    Point getLocation() {
        return this.location;
    }*/

    public void draw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.setColor(color);

        //Transform gXform = preLTransform(g, screenOrigin);
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        Transform gOrigXform = gXform.copy();

        // move drawing coordinates back
        //
        gXform.translate(screenOrigin.getX(), screenOrigin.getY());

        localTransforms(gXform);
        //postLTransform(g, screenOrigin, gXform);

        // move the drawing coordinates as part of the "local origin" transformations
        //
        gXform.translate(-screenOrigin.getX(), -screenOrigin.getY());

        // set the current transform of the graphics context
        //
        g.setTransform(gXform);

        cn1ForwardPrimitiveTranslate(g, dimension);
        containerTranslate(g, containerOrigin);

        localDraw(g, containerOrigin, screenOrigin);

        // restore the original xform in g
        //
        //g.resetAffine();
        g.setTransform(gOrigXform);
    }

    protected abstract void localDraw(Graphics g, Point containerOrigin, Point screenOrigin);

    protected void localTransforms(Transform gxForm) {
        gxForm.translate(myTranslation.getTranslateX(), myTranslation.getTranslateY());
        gxForm.concatenate(myRotation);
        gxForm.scale(myScale.getScaleX(), myScale.getScaleY());
    }

    protected void containerTranslate(Graphics g, Point parentOrigin) {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(parentOrigin.getX(), parentOrigin.getY());
        g.setTransform(gxForm);
    }

    protected void cn1ReverseContainerTranslate(Graphics g, Point parentOrigin) {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(-parentOrigin.getX(), -parentOrigin.getY());
        g.setTransform(gxForm);
    }

    protected void cn1ForwardPrimitiveTranslate(Graphics g, Dimension pDimension) {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(-pDimension.getWidth()/2, -pDimension.getHeight()/2);
        g.setTransform(gxForm);
    }

    protected void cn1ReversePrimitiveTranslate(Graphics g, Dimension pDimension) {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(pDimension.getWidth()/2, pDimension.getHeight()/2);
        g.setTransform(gxForm);
    }
}

abstract class Fixed extends GameObject {
    boolean fixed;

    public Fixed() {
        fixed = false;
    }

    // Once object's location is initialized,
    // it won't be able to set it again.
    //
/*    @Override
    void setLocation(Point location) {
        if (!fixed) {
            this.location = location;
        }
        fixed = true;
    }*/
}

abstract class Movable extends GameObject {
    int currentSpeed;
    double heading;

    public Movable() {
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public double getHeading() {
        return heading;
    }
}

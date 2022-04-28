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
        this.worldSize = worldSize;
        myTranslation = Transform.makeIdentity();
        myRotation = Transform.makeIdentity();
        myScale = Transform.makeIdentity();
    }

    protected void setColor(int color) {
        this.color = color;
    }

    protected void setDimension(Dimension d) {
        dimension = new Dimension(d.getWidth(), d.getHeight());
    }
/*    void setLocation(Point location) {
        this.location = location;
    }*/

    // Getter
    //
    public int getWidth() {
    return dimension.getWidth();
}

    public int getHeight() {
        return dimension.getHeight();
    }

    protected int getColor() {
        return this.color;
    }

    protected Dimension getDimension() {
        return this.dimension;
    }
/*    Point getLocation() {
        return this.location;
    }*/
    public void rotate(float degrees) {
    myRotation.rotate((float)Math.toRadians(degrees), 0, 0);
}

    public void scale(double sx, double sy) {
        myScale.scale((float)sx, (float)sy);
    }

    public void translate(double tx, double ty) {
        myTranslation.translate((float)tx, (float)ty);
    }

    public void draw(Graphics g, Point containerOrigin, Point screenOrigin) {
        Transform gXform = Transform.makeIdentity();
        g.getTransform(gXform);
        Transform gOrigXform = gXform.copy();

        // move drawing coordinates back
        //
        gXform.translate(screenOrigin.getX(), screenOrigin.getY());

        localTransforms(gXform);

        // move the drawing coordinates as part of the "local origin" transformations
        //
        gXform.translate(-screenOrigin.getX(), -screenOrigin.getY());

        // set the current transform of the graphics context
        //
        g.setTransform(gXform);

        cn1ForwardPrimitiveTranslate(g, dimension);
        containerTranslate(g, containerOrigin);

        g.setColor(color);
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
    double displayAngle;

    public Movable() {
    }

    public void move(long elapsedTimeInMillis) {
        double speedMultiplier = calcSpeedMultiplier(elapsedTimeInMillis);
        double angle = Math.toRadians(heading + 90);

        // The speed is multiplied by SPEED_MULTIPLIER to indirectly reduce
        // the fuel cost of the helicopter's movement.
        //
        this.translate(currentSpeed * speedMultiplier * Math.cos(angle),
                currentSpeed * speedMultiplier * Math.sin(angle));
    }

    private double calcSpeedMultiplier(long elapsedTime) {
        return (elapsedTime / 100f) * 4;
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public double getDisplayAngle() {
        return displayAngle;
    }
}

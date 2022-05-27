package org.csc133.a3.gameobjects;

import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.GameWorld;

public abstract class GameObject {
    private int color;
    protected int objectId;
    protected Dimension dimension;
    protected Dimension worldSize;
    private Transform gXform;
    private Transform gOrigXform;
    private Transform myTranslation;
    private Transform myRotation;
    private Transform myScale;
    // For getting the fuel value from GameWorld
    //
    private GameWorld gw;

    public GameObject() {
        gw = GameWorld.getInstance();
        this.worldSize = gw.getDimension();

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

    // Getter
    //
    public GameWorld getGameWorld() {
        return gw;
    }

    public int getWidth() {
        return getDimension().getWidth();
    }

    public int getHeight() {
        return getDimension().getHeight();
    }

    protected int getColor() {
        return color;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public Transform getTranslation() {
        return myTranslation;
    }

    public int getId() {
        return this.objectId;
    }

    public Dimension getWorldSize() {
        return this.worldSize;
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

    protected abstract void localDraw(Graphics g,
                                      Point containerOrigin,
                                      Point screenOrigin);

    protected Transform preLTTransform(Graphics g, Point screenOrigin) {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gOrigXform = gxForm.copy();

        // Move drawing coordinates back
        //
        gxForm.translate(screenOrigin.getX(), screenOrigin.getY());
        return gxForm;
    }

    protected void localTransforms(Transform gxForm) {
        gxForm.translate(   myTranslation.getTranslateX(),
                            myTranslation.getTranslateY());
        gxForm.concatenate(myRotation);
        gxForm.scale(myScale.getScaleX(), myScale.getScaleY());
    }

    void postLTransform(Graphics g, Point screenOrigin, Transform gXform) {
        // Move the drawing coordinates so that the local origin coincides with
        // the screen origin post local transforms.
        //
        gXform.translate(-screenOrigin.getX(), -screenOrigin.getY());
        // set the current transform of the graphics context
        //
        g.setTransform(gXform);
    }

    protected void containerTranslate(Graphics g, Point parentOrigin) {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(parentOrigin.getX(), parentOrigin.getY());
        g.setTransform(gxForm);
    }

    protected void cn1ReverseContainerTranslate(Graphics g,
                                                Point parentOrigin) {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(-parentOrigin.getX(), -parentOrigin.getY());
        g.setTransform(gxForm);
    }

    protected void cn1ForwardPrimitiveTranslate(Graphics g,
                                                Dimension pDimension) {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(-pDimension.getWidth()/2, -pDimension.getHeight()/2);
        g.setTransform(gxForm);
    }

    protected void cn1ReversePrimitiveTranslate(Graphics g,
                                                Dimension pDimension) {
        Transform gxForm = Transform.makeIdentity();
        g.getTransform(gxForm);
        gxForm.translate(pDimension.getWidth()/2, pDimension.getHeight()/2);
        g.setTransform(gxForm);
    }

    protected void textTranslate(Graphics g,
                                 Point containerOrigin,
                                 Point screenOrigin) {
        g.setTransform(gOrigXform);
        Transform gXform = preLTTransform(g, screenOrigin);
        gXform.translate(   getTranslation().getTranslateX(),
                            getTranslation().getTranslateY());
        gXform.scale(1, -1);
        postLTransform(g, screenOrigin, gXform);
        cn1ForwardPrimitiveTranslate(g, dimension);
        containerTranslate(g, containerOrigin);
    }

    public void draw(Graphics g, Point containerOrigin, Point screenOrigin) {
        Transform gXform = preLTTransform(g, screenOrigin);
        localTransforms(gXform);
        postLTransform(g, screenOrigin, gXform);

        cn1ForwardPrimitiveTranslate(g, dimension);
        containerTranslate(g, containerOrigin);

        g.setColor(color);
        localDraw(g, containerOrigin, screenOrigin);

        // restore the original xform in g
        //
        g.setTransform(gOrigXform);
    }
}

abstract class Fixed extends GameObject {
    private boolean fixed;

    public Fixed() {
        this.fixed = false;
    }

    // Once object's location is initialized,
    // it won't be able to set it again.
    //
    @Override
    public void translate(double tx, double ty) {
        if (!fixed) {
            super.translate(tx, ty);
            fixed = true;
        }
    }
}

abstract class Movable extends GameObject {
    protected int currentSpeed;
    protected double speedFactor;
    protected double heading;
    protected double displayAngle;
    protected double angle;

    public Movable() {
        this.currentSpeed = 0;
        this.heading = 0;
    }

    public void move(long elapsedTimeInMillis) {
        speedFactor = calcSpeedMultiplier(elapsedTimeInMillis);
        angle = Math.toRadians(heading + 90);

        this.translate(currentSpeed * speedFactor * Math.cos(angle),
                currentSpeed * speedFactor * Math.sin(angle));
    }

    private double calcSpeedMultiplier(long elapsedTime) {
        return (elapsedTime / 100f) * 4;
    }

    void setHeading(double theta) {
        this.heading = theta;
    }

    void setCurrentSpeed(int speed) {
        this.currentSpeed = speed;
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public double getDisplayAngle() {
        return displayAngle;
    }

    public double getHeading() {
        return this.heading;
    }
}
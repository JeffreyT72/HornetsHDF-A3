package org.csc133.a3.gameobjects;

import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.Transform;
import org.csc133.a3.GameWorld;
import org.csc133.a3.interfaces.Drawable;

public abstract class GameObject implements Drawable {
    private int color;
    Point location;
    Dimension dimension;
    Dimension worldSize;
    // For getting the fuel value from GameWorld
    //
    GameWorld gw;

    public GameObject() {
        gw = GameWorld.getInstance();
    }

    void setColor(int color) {
        this.color = color;
    }

    void setLocation(Point location) {
        this.location = location;
    }

    int getColor() {
        return this.color;
    }

    Point getLocation() {
        return this.location;
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
    @Override
    void setLocation(Point location) {
        if (!fixed) {
            this.location = location;
        }
        fixed = true;
    }
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

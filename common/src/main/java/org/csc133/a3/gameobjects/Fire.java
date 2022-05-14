package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.interfaces.Observer;
import org.csc133.a3.interfaces.Subject;

import java.util.List;
import java.util.Random;

import static com.codename1.ui.CN.*;

//-----------------------------------------------------------------------------
public class Fire extends GameObject implements Observer {
    private Random  r;  // for grow() function

    // Variable
    private int     buildingId;
    private int     size;
    private int     increaseRate;
    private double  scaleRate;
    private boolean wasExtinguished;  // set true when fire was extinguished
    private boolean startBurning;
    private boolean isOverFire;       // set true when helicopter is over fire

    private boolean selected;
    //private int myRadius;

    // Constant
    private final int MIN_SIZE = 8;


    private final FireDispatch subject;

    public Fire(Dimension worldSize, int buildingId, FireDispatch subject) {
        this.buildingId = buildingId;
        this.worldSize = worldSize;
        setColor(ColorUtil.MAGENTA);
        this.wasExtinguished = false;
        this.startBurning = false;
        this.isOverFire = false;
        this.r = new Random();
        this.size = MIN_SIZE + r.nextInt(10);
        this.dimension = new Dimension(size, size);

        //selected = false;
        //myRadius = dimension.getWidth()/2;
        this.scale(1,-1);

        this.subject = subject;
        this.subject.attach(this);

        // set fire object into unStarted state
        //
        fireState = new UnStarted();
    }

    void start() {
        fireState.start();
    }

    public void grow() {
        fireState.grow();
    }

    public void fight(int water) {
        fireState.fight(water);
    }

    public void checkIfSelected(Point2D invertedPoint) {
        fireState.checkIfSelected(invertedPoint);
    }

    // Getter
    //
    public int getFireSize() {
        return this.size;
    }

    public boolean getIsOverFire() {
        return this.isOverFire;
    }

    public boolean getWasExtinguished() {
        return this.wasExtinguished;
    }

    public boolean getIsBurning() {
        return this.startBurning;
    }

    public int getBuildingId() {
        return this.buildingId;
    }

    // Setter
    void setWasExtinguished(boolean wasExtinguished) {
        this.wasExtinguished = wasExtinguished;
    }

    void setFireSize(int size) {
        this.size -= size;
    }

/*    public void grow() {
        increaseRate = 1 + r.nextInt(2);
        size += increaseRate;
        this.dimension = new Dimension(size, size);
        myRadius = dimension.getWidth()/2;
        //scale(increaseRate, increaseRate);
    }*/

/*        if (this.contains(sp) && !this.isSelected()) {
            this.select(true);
            //gw.getFc().getPrimary().setTail(this.getLocation());
            //gw.getSpL().select(false);
        }
        for (Fire f: gw.getFireCollection()) {
            if (!this.contains(sp) && this.isSelected())
                f.select(false);
        }*/

    public boolean checkIsOverFire(Helicopter helicopter) {
        if  (helicopter.getTranslation().getTranslateX() >=
                myTranslation.getTranslateX() - size / 2 &&
                helicopter.getTranslation().getTranslateX() <=
                        myTranslation.getTranslateX() + size / 2 &&
                helicopter.getTranslation().getTranslateY() >=
                        myTranslation.getTranslateY() - size / 2 &&
                helicopter.getTranslation().getTranslateY() <=
                        myTranslation.getTranslateY() + size / 2) {
            return true;
        } else
            return false;
    }

    @Override
    public void update(Observer object) {
        fireState.update(object);
    }

/*    private double distanceBetween(Point2D a, Point2D b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }*/

    //```````````````````````````````````````````````````````````````````````````````````````
    // Fire State Pattern
    //
    private Fire.FireState fireState;

    private void changeState(Fire.FireState fireState) {
        this.fireState = fireState;
    }

    private abstract class FireState {
        protected Fire getFire() {
            return Fire.this;
        }

        void start() {}

        void grow() {}

        void update(Observer object) {}

        void checkIfSelected(Point2D invertedPoint) {}

        void fight(int water) {}

        void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {}

    }

    public class UnStarted extends FireState {
        @Override
        void start() {
            startBurning = true;
            getFire().changeState(new Burning());
        }
    }

    public class Burning extends FireState {
        @Override
        void grow() {
            increaseRate = 1 + r.nextInt(3);
            scaleRate = 1 + r.nextInt(increaseRate) / 10f;
            scale(scaleRate, scaleRate);
            size *= scaleRate;
//            dimension = new Dimension(size, size);
//            myRadius = dimension.getWidth()/2;
        }

        @Override
        void checkIfSelected(Point2D invertedPoint) {
            if (invertedPoint.getY() >= getTranslation().getTranslateY() - getHeight() * 2 &&
                    invertedPoint.getY() <= getTranslation().getTranslateY() + getHeight() * 2 &&
                    invertedPoint.getX() >= getTranslation().getTranslateX() - getWidth() * 2 &&
                    invertedPoint.getX() <= getTranslation().getTranslateX() + getWidth() * 2) {
                subject.setSelectedFire(Fire.this);
            }
        }

        @Override
        void fight(int water) {
            int tempSize = size;
            tempSize -= water/3f;
            if (tempSize <= 0) {
                setDimension(new Dimension(0, 0));
                size = 0;
                setWasExtinguished(true);
                changeState(new Extinguished());
            } else {
                scale(30f/water, 30f/water);
                size *= 30f/water;
            }
        }

        @Override
        void update(Observer object) {
            if(object != Fire.this) {
                selected = false;
            } else {
                selected = true;
            }
        }

        @Override
        protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
            g.fillArc(0, 0, getWidth(), getHeight(), 0, 360);

            // Text
            g.setFont(Font.createSystemFont(FACE_MONOSPACE,
                    STYLE_BOLD, SIZE_MEDIUM));
            g.drawString(String.valueOf(size), getWidth()+10, getHeight()+10);
        }
    }

    public class Extinguished extends FireState {
        // Do nothing
    }

    public void setup(int buildingX, int buildingY, int w, int h) {
        this.translate(buildingX + r.nextInt(w), buildingY + r.nextInt(h));
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        fireState.localDraw(g, containerOrigin, screenOrigin);
    }
}

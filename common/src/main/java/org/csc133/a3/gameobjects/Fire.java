package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import org.csc133.a3.interfaces.FireState;
import org.csc133.a3.StateContext;
import org.csc133.a3.interfaces.Selectable;

import java.util.Random;

import static com.codename1.ui.CN.*;

//-----------------------------------------------------------------------------
public class Fire extends GameObject implements Selectable {
    private Random  r;  // for grow() function

    // Variable
    private int     buildingId;
    private int     size;
    private int     increaseRate;
    private boolean wasExtinguished;  // set true when fire was extinguished
    private boolean startBurning;
    private boolean isOverFire;       // set true when helicopter is over fire

    private boolean selected;
    private int myRadius;

    // Constant
    private final int MIN_SIZE = 8;

    // States
    private StateContext context        = new StateContext();
    private UnStarted    unStarted      = new UnStarted();
    private Burning      burning        = new Burning();
    private Extinguished extinguished   = new Extinguished();

    public Fire(Dimension worldSize, int buildingId) {
        this.buildingId = buildingId;
        this.worldSize = worldSize;
        setColor(ColorUtil.MAGENTA);
        this.wasExtinguished = false;
        this.startBurning = false;
        this.isOverFire = false;
        this.r = new Random();
        this.size = MIN_SIZE + r.nextInt(10);
        this.dimension = new Dimension(size, size);

        selected = false;
        myRadius = dimension.getWidth()/2;

        scale(1,-1);

        // set fire object into unStarted state
        //
        unStarted.fireAction(context);
    }

    // Getter
    //
    public int getFireSize() {
        return this.size;
    }

    boolean getIsOverFire() {
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

    public void grow() {
        increaseRate = 1 + r.nextInt(2);
        size += increaseRate;
        this.dimension = new Dimension(size, size);
        myRadius = dimension.getWidth()/2;
        //scale(increaseRate, increaseRate);
    }

    public void checkIsSelected(Point2D sp) {
        if (this.contains(sp) && !this.isSelected()) {
            this.select(true);
            gw.getFc().getPrimary().setTail(this.getLocation());
            //gw.getSpL().select(false);
        }
        for (Fire f: gw.getFireCollection()) {
            if (!this.contains(sp) && this.isSelected())
                f.select(false);
        }
    }

/*    public void isOverFire(Helicopter helicopter) {
        isOverFire = helicopter.getLocation().getX() >=
                this.location.getX() - this.size / 2 &&
                helicopter.getLocation().getX() <=
                        this.location.getX() + this.size / 2 &&
                helicopter.getLocation().getY() >=
                        this.location.getY() - this.size / 2 &&
                helicopter.getLocation().getY() <=
                        this.location.getY() + this.size / 2;
    }*/

    public class UnStarted implements FireState {
        private int size;
        public void fireAction(StateContext context) {
            this.size = 0;
            context.setState(this);
        }
    }

    public class Burning implements FireState {
        private int size;

        public void fireAction(StateContext context) {
            startBurning = true;
            this.size = MIN_SIZE + r.nextInt(10);
            context.setState(this);
        }
    }

    public class Extinguished implements FireState {
        public void fireAction(StateContext context) {
            context.setState(this);
        }
    }

    public void start() {
        burning.fireAction(context);
    }

    public void Extinguished() {
        extinguished.fireAction(context);
    }

    public void setup(int buildingX, int buildingY, int w, int h) {
        translate(buildingX + r.nextInt(w), buildingY + r.nextInt(h));
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
        g.fillArc(0, 0, getWidth(), getHeight(), 0, 360);

        // Text
        g.setFont(Font.createSystemFont(FACE_MONOSPACE,
                STYLE_BOLD, SIZE_MEDIUM));
        g.drawString(String.valueOf(size), getWidth()+10, getHeight()+10);
    }
}

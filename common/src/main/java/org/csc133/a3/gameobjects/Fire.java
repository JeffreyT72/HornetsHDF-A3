package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;
import org.csc133.a3.interfaces.FireState;
import org.csc133.a3.StateContext;

import java.util.Random;

import static com.codename1.ui.CN.*;

//-----------------------------------------------------------------------------
public class Fire extends GameObject{
    private Random  r;  // for grow() function

    // Variable
    private int     buildingId;
    private int     size;
    private int     increaseRate;
    private boolean wasExtinguished;  // set true when fire was extinguished
    private boolean startBurning;
    private boolean isOverFire;       // set true when helicopter is over fire

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

        //this.translate(worldSize.getWidth(), worldSize.getHeight());
        this.scale(1,-1);
        this.rotate(0);

        // set fire object into unStarted state
        //
        unStarted.fireAction(context);
    }

    // Getter
    //
    public int getWidth() {
        return dimension.getWidth();
    }

    public int getHeight() {
        return dimension.getHeight();
    }

    boolean getIsOverFire() {
        return this.isOverFire;
    }

    public boolean getWasExtinguished() {
        return this.wasExtinguished;
    }

    public int getSize() {
        return this.size;
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
        //scale(increaseRate, increaseRate);
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
    /*
    @Override
    public void draw(Graphics g, Point containerOrigin) {
        g.setColor(getColor());
        int x = containerOrigin.getX() + (int)location.getX()
                - dimension.getWidth()/2;
        int y = containerOrigin.getY() + (int)location.getY()
                - dimension.getHeight()/2;
        int w = dimension.getWidth();
        int h = dimension.getHeight();

        g.fillArc(x, y, w, h, 0, 360);
        g.setFont(Font.createSystemFont(FACE_MONOSPACE,
                STYLE_BOLD, SIZE_MEDIUM));
        int strX = x + dimension.getWidth();
        int strY = y + dimension.getHeight();
        g.drawString(String.valueOf(size), strX, strY);
    }
    */
    public void setup(int x, int y, int w, int h) {
        //this.location = new Point(x + r.nextInt(w), y + r.nextInt(h));
        this.translate(x + r.nextInt(w), y + r.nextInt(h));
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.fillArc(0, 0, getWidth(), getHeight(), 0, 360);

        // Text (need to transforms)
        g.setFont(Font.createSystemFont(FACE_MONOSPACE,
                STYLE_BOLD, SIZE_MEDIUM));
        g.drawString(String.valueOf(size), size/2 + 10, size/2 + 10);
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
}

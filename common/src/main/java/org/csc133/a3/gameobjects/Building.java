package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

import java.util.Random;

import static com.codename1.ui.CN.*;

public class Building extends Fixed {
    private Random r;
    private int id;
    private int damage;
    private int value;
    private int buildingArea;
    private int currentDamage;
    private int previousDamage;
    private final int THICKNESS = 4;

    public Building (Dimension worldSize, int id) {
        this.r = new Random();
        this.id = id;
        this.worldSize = worldSize;
        setColor(ColorUtil.rgb(255, 0, 0));
        this.damage = 0;
        this.currentDamage = 0;
        this.previousDamage = 0;
        this.scale(1,-1);
        buildingSetup(id);
        this.buildingArea = dimension.getHeight() * dimension.getWidth();
    }

    private void buildingSetup(int id) {
        if (id == 0) {              // Top building
            this.dimension = new Dimension(1000, 150);
            this.value = (r.nextInt(10) + 1)* 100;
            this.translate(worldSize.getWidth() * 0.5, worldSize.getHeight() * 0.9);
        } else if (id == 1) {       // Left building
            this.dimension = new Dimension(200, 600);
            this.value = (r.nextInt(10) + 1)* 100;
            this.translate(worldSize.getWidth() * 0.2, worldSize.getHeight() * 0.3);
        } else if (id == 2) {       // Right building
            this.dimension = new Dimension(250, 400);
            this.value = (r.nextInt(10) + 1)* 100;
            this.translate(worldSize.getWidth() * 0.8, worldSize.getHeight() * 0.3);
        }
    }
    /*
    @Override
    public void draw(Graphics g, Point containerOrigin) {
        g.setColor(getColor());

        // building
        int x = containerOrigin.getX() + (int)getLocation().getX()
                - dimension.getWidth()/2;
        int y = containerOrigin.getY() + (int)getLocation().getY()
                - dimension.getHeight()/2;
        int w = dimension.getWidth();
        int h = dimension.getHeight();
        g.drawRect(x, y, w, h, 4);
        // text
        int textX = containerOrigin.getX() + (int)getLocation().getX()
                + dimension.getWidth()/2 + 10;
        int textY = containerOrigin.getY() + (int)getLocation().getY()
                + dimension.getHeight()/2 - 30;
        // If building damage over 100%, then keep display 100%.
        // But only for visual, the actual damage still increasing
        // until total building damage is 100% then player loss.
        // (observe from demo playthrough)
        if (damage <= 100)
            g.drawString("D: " + damage + "%", textX, textY);
        else
            g.drawString("D: 100%", textX, textY);
        textY -= 30;
        g.drawString("V: " + value, textX, textY);
    }
    */
/*    public int getId() {
        return id;
    }*/

    public int getSize() {
        return buildingArea;
    }

    public int getValue() {
        return value;
    }

    public void setFireInBuilding(Fire fire) {
        // Passing building location
        //
        int x = (int) (myTranslation.getTranslateX() - getWidth()/2);
        int y = (int) (myTranslation.getTranslateY() - getHeight()/2);
        int w = getWidth();
        int h = getHeight();

        fire.setup(x, y, w, h);
        fire.start();
    }

    public void updateBuildingDmg(int totalFireSizeInBuilding) {
        currentDamage = totalFireSizeInBuilding*100/buildingArea;
        if (currentDamage > previousDamage) {
            damage = currentDamage;
            previousDamage = currentDamage;
        }
    }
    // Getter
    //
    public int getWidth() {
        return dimension.getWidth();
    }

    public int getHeight() {
        return dimension.getHeight();
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.drawRect(0, 0, getWidth(), getHeight(), THICKNESS);

        // Text
        g.setFont(Font.createSystemFont(FACE_MONOSPACE,
                STYLE_BOLD, SIZE_MEDIUM));
        if (damage <= 100)
            g.drawString("D: " + damage + "%", getWidth(), getHeight());
        else
            g.drawString("D: 100%", getWidth(), getHeight());

        g.drawString("V: " + value, getWidth(), getHeight()-30);
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

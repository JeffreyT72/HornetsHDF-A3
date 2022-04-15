package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.geom.Point;

import java.util.Random;


public class Building extends Fixed {
    private Random r;
    private int id;
    private int damage;
    private int value;
    private int buildingArea;
    private int currentDamage;
    private int previousDamage;

    public Building (Dimension worldSize, int id) {
        this.r = new Random();
        this.id = id;
        this.worldSize = worldSize;
        setColor(ColorUtil.rgb(255, 0, 0));
        this.damage = 0;
        this.currentDamage = 0;
        this.previousDamage = 0;
        buildingSetup(id);
        this.buildingArea = dimension.getHeight() * dimension.getWidth();
    }

    private void buildingSetup(int id) {
        if (id == 0) {              // Top building
            setLocation(new Point(worldSize.getWidth()/2, 150));
            this.dimension = new Dimension(1000, 150);
            this.value = (r.nextInt(10) + 1)* 100;
        } else if (id == 1) {       // Left building
            setLocation(new Point((int) (worldSize.getWidth()*0.2),
                    (int) (worldSize.getHeight()*0.6)));
            this.dimension = new Dimension(200, 600);
            this.value = (r.nextInt(10) + 1)* 100;
        } else if (id == 2) {       // Right building
            setLocation(new Point((int) (worldSize.getWidth()*0.8),
                    (int) (worldSize.getHeight()*0.7)));
            this.dimension = new Dimension(250, 400);
            this.value = (r.nextInt(10) + 1)* 100;
        }
    }

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

    public int getId() {
        return id;
    }

    public int getSize() {
        return buildingArea;
    }

    public int getValue() {
        return value;
    }

    public void setFireInBuilding(Fire fire) {
        // Passing building location
        //
        int x = (int)getLocation().getX() - dimension.getWidth()/2;
        int y = (int)getLocation().getY() - dimension.getHeight()/2;
        int w = dimension.getWidth();
        int h = dimension.getHeight();

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
}

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

    public void setFireInBuilding(Fire fire) {
        int x = (int) (getTranslation().getTranslateX() - getWidth()/2);
        int y = (int) (getTranslation().getTranslateY() - getHeight()/2);
        int w = getWidth();
        int h = getHeight();

        fire.setup(x, y, w, h);
        fire.start();
    }

    public void updateBuildingDmg(int totalFireSizeInBuilding) {
        currentDamage = totalFireSizeInBuilding * 100 / buildingArea;
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

    public int getSize() {
        return buildingArea;
    }

    public int getValue() {
        return value;
    }

    @Override
    protected void localDraw(Graphics g, Point containerOrigin, Point screenOrigin) {
        g.drawRect(0, 0, getWidth(), getHeight(), THICKNESS);

        // Text
        g.setFont(Font.createSystemFont(FACE_MONOSPACE,
                STYLE_BOLD, SIZE_MEDIUM));
        g.drawString("V: " + value, getWidth() + 20, getHeight());

        if (damage <= 100)
            g.drawString("D: " + damage + "%", getWidth() + 20, getHeight() + 20 * 2);
        else
            g.drawString("D: 100%", getWidth() + 20, getHeight() + 20 * 2);
    }
}

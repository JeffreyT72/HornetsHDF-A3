package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.geom.Dimension;
import org.csc133.a3.GameWorld;

public class PlayerHelicopter extends Helicopter{
    private static final int HELICOLOR = ColorUtil.YELLOW;
    private static PlayerHelicopter instance;

    private PlayerHelicopter(Dimension worldSize, int initFuel){
        super(worldSize, HELICOLOR, initFuel);
    }

    public static PlayerHelicopter getInstance() {
        if(instance == null) {
            Dimension worldSize    = GameWorld.getInstance().getDimension();
            int initFuel         = GameWorld.getInstance().getInitFuel();
            //Transform startPoint = GameWorld.getInstance().getStartingPoint();

            instance = new PlayerHelicopter(worldSize, initFuel);
        }
        return instance;
    }
}

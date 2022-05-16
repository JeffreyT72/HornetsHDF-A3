package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import org.csc133.a3.GameWorld;

public class PlayerHelicopter extends Helicopter{
    private static final int HELICOPTER_COLOR = ColorUtil.YELLOW;
    private static PlayerHelicopter instance;

    private PlayerHelicopter(Dimension worldSize, int initFuel, Transform helipadLocation){
        super(worldSize, HELICOPTER_COLOR, initFuel, helipadLocation);
    }

    public static PlayerHelicopter getInstance() {
        if(instance == null) {
            Dimension worldSize         = GameWorld.getInstance().getDimension();
            int initFuel                = GameWorld.getInstance().getFuel();
            Transform helipadLocation   = GameWorld.getInstance().getHelipadLocation();

            instance = new PlayerHelicopter(worldSize, initFuel, helipadLocation);
        }
        return instance;
    }

    public void reset() {
        instance = null;
    }
}

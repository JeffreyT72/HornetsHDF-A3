package org.csc133.a3.gameobjects;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Dimension;
import org.csc133.a3.GameWorld;

public class PlayerHelicopter extends Helicopter{
    private static int HeliColor = ColorUtil.YELLOW;
    private static PlayerHelicopter instance;

    private PlayerHelicopter(Dimension worldSize){
        super(worldSize);
    }

    public static PlayerHelicopter getInstance() {
        if(instance == null) {
            Dimension worldSize    = GameWorld.getInstance().getDimension();
/*            int initFuel         = GameWorld.getInstance().getFuel();
            Transform startPoint = GameWorld.getInstance().getStartingPoint();*/

            instance = new PlayerHelicopter(worldSize);
        }
        return instance;
    }
}

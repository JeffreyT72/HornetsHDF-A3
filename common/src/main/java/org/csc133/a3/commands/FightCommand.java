package org.csc133.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.GameWorld;
import org.csc133.a3.gameobjects.PlayerHelicopter;

public class FightCommand extends Command {
    public FightCommand(GameWorld gw) {
        super("Fight");
    }

    @Override
    public void actionPerformed (ActionEvent event) {
        GameWorld.getInstance().fight(PlayerHelicopter.getInstance());
    }
}

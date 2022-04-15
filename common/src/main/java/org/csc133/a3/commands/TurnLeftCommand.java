package org.csc133.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.GameWorld;

public class TurnLeftCommand extends Command {
    public TurnLeftCommand(GameWorld gw) {
        super("Left");
    }

    @Override
    public void actionPerformed (ActionEvent event) {
        GameWorld.getInstance().changeHeadingLeft();
    }
}


package org.csc133.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.GameWorld;

public class EngineStartStopCommand extends Command {
    public EngineStartStopCommand(GameWorld gw) {
        super("Start/Stop Engine");
    }

    @Override
    public void actionPerformed (ActionEvent event) {
        GameWorld.getInstance().engineStartStop();
    }
}

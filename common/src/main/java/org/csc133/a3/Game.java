package org.csc133.a3;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Form;
import com.codename1.ui.Graphics;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.util.UITimer;
import org.csc133.a3.commands.*;
import org.csc133.a3.views.ControlCluster;
import org.csc133.a3.views.GlassCockpit;
import org.csc133.a3.views.MapView;

//-----------------------------------------------------------------------------
public class Game extends Form implements Runnable {
    private GameWorld gw;
    private MapView mapView;
    private GlassCockpit glassCockpit;
    private ControlCluster controlCluster;

    public Game() {
        gw = GameWorld.getInstance();

        mapView = new MapView(gw);
        glassCockpit = new GlassCockpit(gw);
        controlCluster = new ControlCluster();

        // Hide the title bar
        //
        getToolbar().hideToolbar();

        setupLayout();

        ActionListener AccelerateCommand    = new AccelerateCommand(gw);
        ActionListener BrakeCommand         = new BrakeCommand(gw);
        ActionListener TurnLeftCommand      = new TurnLeftCommand(gw);
        ActionListener TurnRightCommand     = new TurnRightCommand(gw);
        ActionListener DrinkCommand         = new DrinkCommand(gw);
        ActionListener FightCommand         = new FightCommand(gw);
        ActionListener ExitCommand          = new ExitCommand(gw);

        addKeyListener(-91, AccelerateCommand);
        addKeyListener(-92, BrakeCommand);
        addKeyListener(-93, TurnLeftCommand);
        addKeyListener(-94, TurnRightCommand);
        addKeyListener('d', DrinkCommand);
        addKeyListener('f', FightCommand);
        addKeyListener('Q', ExitCommand);

        // Timer, 50ms for each tick
        //
        UITimer timer = new UITimer(this);
        timer.schedule(50, true, this);

        this.getAllStyles().setBgColor(ColorUtil.BLACK);
        this.show();
        gw.init();
    }

    private void setupLayout() {
        this.setLayout(new BorderLayout());
        this.add(BorderLayout.NORTH, glassCockpit);
        this.add(BorderLayout.SOUTH, controlCluster);
        this.add(BorderLayout.CENTER, mapView);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }

    @Override
    public void run() {
        glassCockpit.update();
        gw.updateLocalTransforms();
        gw.tick();
        repaint();
    }
}

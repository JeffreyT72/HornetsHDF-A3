package org.csc133.a3.views;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Font;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Style;
import org.csc133.a3.GameWorld;
import org.csc133.a3.commands.*;

public class ControlCluster extends Container {
    private GameWorld gw;
    private Button accelBtn;
    private Button brakeBtn;
    private Button drinkBtn;
    private Button leftBtn;
    private Button rightBtn;
    private Button fightBtn;
    private Button exitBtn;

    public ControlCluster() {
        this.gw = GameWorld.getInstance();

        accelBtn = this.buttonMaker(new AccelerateCommand(gw), "Accel");
        brakeBtn = this.buttonMaker(new BrakeCommand(gw), "Break");
        drinkBtn = this.buttonMaker(new DrinkCommand(gw), "Drink");
        leftBtn = this.buttonMaker(new TurnLeftCommand(gw), "Left");
        rightBtn = this.buttonMaker(new TurnRightCommand(gw), "Right");
        fightBtn = this.buttonMaker(new FightCommand(gw), "Fight");
        exitBtn = this.buttonMaker(new ExitCommand(gw), "Exit");

        this.setLayout(new BorderLayout());
        //this.getAllStyles().setBgColor(ColorUtil.WHITE);
        //this.getAllStyles().setBgTransparency(255);
        createEastBtn();
        createWestBtn();
        this.add(BorderLayout.CENTER, exitBtn);
    }

    private void createEastBtn() {
        Container eastBtn = new Container(new BorderLayout());
        eastBtn.add(BorderLayout.EAST, accelBtn);
        eastBtn.add(BorderLayout.CENTER, brakeBtn);
        eastBtn.add(BorderLayout.WEST,drinkBtn);
        this.add(BorderLayout.EAST, eastBtn);
    }

    private void createWestBtn() {
        Container westBtn = new Container(new BorderLayout());
        westBtn.add(BorderLayout.EAST, fightBtn);
        westBtn.add(BorderLayout.CENTER, rightBtn);
        westBtn.add(BorderLayout.WEST, leftBtn);
        this.add(BorderLayout.WEST, westBtn);
    }

    private Button buttonMaker(Command command, String text) {
        // Create button and set button command
        //
        Button button = new Button(text);
        button.setCommand(command);

        // Button Style
        //
        Style btnStyle = button.getAllStyles();
        btnStyle.setFont(Font.createSystemFont(
                Font.FACE_PROPORTIONAL,
                Font.STYLE_BOLD,
                Font.SIZE_MEDIUM));
        //btnStyle.setFgColor(ColorUtil.BLUE);
        //btnStyle.setBgColor(ColorUtil.WHITE);
        btnStyle.setBgTransparency(255);
        return button;
    }
}


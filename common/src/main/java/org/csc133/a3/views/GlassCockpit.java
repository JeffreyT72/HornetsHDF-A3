package org.csc133.a3.views;

import com.codename1.ui.Container;
import com.codename1.ui.Font;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.plaf.Style;
import org.csc133.a3.GameWorld;

public class GlassCockpit extends Container {
    private GameWorld gw;
    private Label heading;
    private Label speed;
    private Label fuel;
    private Label fireCount;
    private Label fireSize;
    private Label damage;
    private Label loss;

    public GlassCockpit(GameWorld gw) {
        this.gw = gw;

        // Making a 2 by 7 tables
        this.setLayout(new GridLayout(2,7));
        // Adding all label into first row
        addDescribeLabel();

        // Initializes all display label
        heading     = new Label("0");
        speed       = new Label("0");
        fuel        = new Label("0");
        fireCount   = new Label("0");
        fireSize    = new Label("0");
        damage      = new Label("0");
        loss        = new Label("0");

        // Add display label into second row
        this.add(heading);
        this.add(speed);
        this.add(fuel);
        this.add(fireCount);
        this.add(fireSize);
        this.add(damage);
        this.add(loss);

        // Set label style
        Style labelStyle = this.getAllStyles();
        labelStyle.setFont(Font.createSystemFont(
                Font.FACE_PROPORTIONAL,
                Font.STYLE_BOLD,
                Font.SIZE_MEDIUM));
        labelStyle.setBgTransparency(255);
    }

    private void addDescribeLabel() {
        this.add("HEADING");
        this.add("SPEED");
        this.add("FUEL");
        this.add("FIRES");
        this.add("FIRE SIZE");
        this.add("DAMAGE");
        this.add("LOSS");
    }

    public void update() {
        heading.setText(String.valueOf(gw.getDisplayHeading()));
        speed.setText(String.valueOf(gw.getCurrentSpeed()));
        fuel.setText(String.valueOf(gw.getFuel()));
        fireCount.setText(String.valueOf(gw.getCurrentFireNo()));
        fireSize.setText(String.valueOf(gw.getTotalFireSize()));
        damage.setText(gw.getTotalDmg() + " %");
        loss.setText(String.valueOf(gw.getLoss()));
    }
}
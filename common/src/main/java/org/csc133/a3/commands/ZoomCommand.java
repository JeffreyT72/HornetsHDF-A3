package org.csc133.a3.commands;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import org.csc133.a3.views.MapView;

public class ZoomCommand extends Command {
    private final MapView mapView;

    public ZoomCommand(MapView mapView) {
        super("Zoom");
        this.mapView = mapView;
    }

    @Override
    public void actionPerformed (ActionEvent event) {
        mapView.zoom();
    }
}

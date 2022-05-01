package org.csc133.a3.interfaces;

import com.codename1.ui.geom.Point2D;

public interface Selectable {
    public boolean contains(Point2D p);
    public void select(boolean selected);
    public boolean isSelected();
}

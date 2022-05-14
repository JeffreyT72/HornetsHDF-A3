package org.csc133.a3.interfaces;

import com.codename1.ui.geom.Point2D;

public interface Subject {
    public void attach(Observer o);
    public void detach(Observer o);
    public void notifyUpdate();

//    public boolean contains(Point2D p);
//    public void select(boolean selected);
//    public boolean isSelected();
}

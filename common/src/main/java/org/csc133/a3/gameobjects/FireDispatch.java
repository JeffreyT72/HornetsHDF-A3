package org.csc133.a3.gameobjects;

import com.codename1.ui.Transform;
import org.csc133.a3.GameWorld;
import org.csc133.a3.interfaces.Observer;
import org.csc133.a3.interfaces.Subject;

import java.util.ArrayList;

public class FireDispatch implements Subject {
    private final ArrayList<Observer> observers;
    private Fire selected;

    public FireDispatch() {
        observers = new ArrayList<>();
    }

    @Override
    public void attach(Observer object) {
        observers.add(object);
    }

    @Override
    public void detach(Observer object) {
        observers.remove(object);
    }

    @Override
    public void notifyUpdate() {
        for(Observer object : observers) {
            object.update(selected);
        }
    }

    void setSelectedFire(Fire selected) {
        this.selected = selected;
        notifyUpdate();
        updateSelectedFire();
    }

    private void updateSelectedFire() {
        Transform fire = Transform.makeIdentity();
        fire.translate( selected.getTranslation().getTranslateX(),
                        selected.getTranslation().getTranslateY());
        GameWorld.getInstance().updateSelectedFire(fire);
    }
}

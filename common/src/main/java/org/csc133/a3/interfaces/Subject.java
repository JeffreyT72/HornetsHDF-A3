package org.csc133.a3.interfaces;

public interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyUpdate();
}

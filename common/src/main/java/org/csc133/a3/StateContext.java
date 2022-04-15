package org.csc133.a3;

import org.csc133.a3.interfaces.FireState;

public class StateContext {
    private FireState state;

    public StateContext() {
        state = null;
    }

    public void setState(FireState state){
        this.state = state;
    }

    public void fireAction() {
        state.fireAction(this);
    }
}

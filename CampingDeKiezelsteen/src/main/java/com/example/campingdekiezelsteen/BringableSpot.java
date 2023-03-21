package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.State;

public class BringableSpot extends Spot{
    private State state;

    public BringableSpot(State state) {
        this.state = state;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }
}

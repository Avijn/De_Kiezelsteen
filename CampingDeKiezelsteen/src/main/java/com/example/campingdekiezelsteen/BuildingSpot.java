package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.State;

public class BuildingSpot extends Spot {
    private State state;

    public BuildingSpot(State state) {
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

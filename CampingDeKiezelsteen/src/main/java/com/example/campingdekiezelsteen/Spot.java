package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.Free;
import com.example.campingdekiezelsteen.State.State;

public abstract class Spot {
    private State state;

    public void changeState(){

    }



// Getters en setters

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}

package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.State;

public abstract class Spot {
    private State state;
    private Placeable placeable;
    private int spotNr;

    public void changeState(State state){
        this.state = state;
//        TODO: Iets van een callback zodat de user interface aangepast wordt
    }

    public void createPlaceable(Placeable placeable){
        this.placeable = placeable;
    }



// Getters en setters

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Placeable getPlaceable() {
        return placeable;
    }

    public void setPlaceable(Placeable placeable) {
        this.placeable = placeable;
    }

    public int getSpotNr() {
        return spotNr;
    }

    public void setSpotNr(int spotNr) {
        this.spotNr = spotNr;
    }
}

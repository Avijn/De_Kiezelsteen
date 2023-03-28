package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.State;

public class BuildingSpot extends Spot {
    private State state;

    public BuildingSpot(State state) {
        this.state = state;
    }

    @Override
    public void changeState(State state) {
        this.state = state;
    }

    @Override
    public Placeable createPlaceable2(String placeableType) {
        Placeable placeable = null;
        switch(placeableType) {
            case "Vakantiehuis":
                placeable = new Tent();
                break;
            case "Tiki-tent":
                placeable = new Camper();
                break;
            case "Sanitair":
                placeable = new Caravan();
                break;
            case "Laundry":
                placeable = new Laundry();
                break;
        }
        return placeable;
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

package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.Free;
import com.example.campingdekiezelsteen.State.State;

public class BuildingSpot extends Spot {
    private State state;

    public BuildingSpot() {
        this.state = new Free();
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
                placeable = new House(placeableType+" ");
                break;
            case "Tiki-tent":
                placeable = new TikiTent(placeableType+" ");
                break;
            case "Sanitair":
                placeable = new Sanitair(placeableType+" ");
                break;
            case "Laundry":
                placeable = new Laundry(placeableType+" ");
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

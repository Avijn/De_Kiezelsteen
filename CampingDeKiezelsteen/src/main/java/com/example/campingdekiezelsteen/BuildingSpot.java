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
    public Placeable createPlaceable(String placeableType) {
        Placeable placeable = null;
        switch (placeableType) {
            case "house" -> placeable = new House("Vakantiehuis");
            case "tikitent" -> placeable = new TikiTent("Tiki-tent");
            case "sanitair" -> placeable = new Sanitair("Sanitair");
            case "laundry" -> placeable = new Laundry("Washok");
            default -> System.out.println("No such placeable exists.");
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

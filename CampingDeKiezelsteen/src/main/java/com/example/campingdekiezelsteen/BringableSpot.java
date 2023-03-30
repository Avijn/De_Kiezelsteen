package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.Free;
import com.example.campingdekiezelsteen.State.State;

public class BringableSpot extends Spot implements Reservable {
    private String style = "";
    private State state;

    public BringableSpot() {
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
            case "tent" -> placeable = new Tent();
            case "camper" -> placeable = new Camper();
            case "caravan" -> placeable = new Caravan();
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

    @Override
    public void reserve() {

    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}

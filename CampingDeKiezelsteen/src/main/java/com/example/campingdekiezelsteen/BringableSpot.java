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
    public Placeable createPlaceable2(String placeableType) {
        Placeable placeable = null;
        switch(placeableType) {
            case "Tent":
                placeable = new Tent();
                break;
            case "Camper":
                placeable = new Camper();
                break;
            case "Caravan":
                placeable = new Caravan();
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

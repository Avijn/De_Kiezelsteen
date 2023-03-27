package com.example.campingdekiezelsteen.State;

import com.example.campingdekiezelsteen.BuildingSpot;
import com.example.campingdekiezelsteen.Spot;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class UnderMaintenance implements State {
    private String color = "orange";

    @Override
    public VBox buttonClicked(Spot spot) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        String spotname = "Spot " + spot.getSpotNr();
        if (spot.getPlaceable() != null && spot.getClass().isAssignableFrom(BuildingSpot.class)) {
            spotname = spot.getPlaceable().getName();
            
        }
        Label title = new Label(spotname);
        title.getStyleClass().add("infoTitle");
        Label subtitle = new Label("Status: Onderhoud");
        subtitle.getStyleClass().add("text-label");
        vBox.getChildren().add(title);
        vBox.getChildren().add(subtitle);
        return vBox;
    }

    private void getChecklist(){

    }

    @Override
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

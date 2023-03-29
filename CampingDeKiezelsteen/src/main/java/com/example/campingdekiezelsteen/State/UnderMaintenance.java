package com.example.campingdekiezelsteen.State;

import com.example.campingdekiezelsteen.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class UnderMaintenance implements State {
    private String color = "orange";

    @Override
    public VBox buttonClicked(Spot spot) {
        // Sets up infoblock.
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        // Sets spotnumber as variable.
        String spotname = "Spot " + spot.getSpotNr();

        // Adds change status button and disables it.
        Button submit = new Button("Verander status");
        submit.getStyleClass().add("submit-button");
        submit.setDisable(true);

        // List for checklist items.
        ArrayList<CheckBox> list = new ArrayList<>();

        if (spot.getPlaceable() != null && spot.getClass().isAssignableFrom(BuildingSpot.class)) {
            // If spot is a buildingspot -> set spotname instead of spotnumber as title.
            spotname = spot.getPlaceable().getName();

            // Maintenance only for instances of class Building
            if (spot.getPlaceable() instanceof Building b) {

                // Gets arraylist 'checklist' from placeable and creates checkbox for every item.
                for (String item : b.getChecklist()) {
                    CheckBox listitem = getChecklistItem(item);

                    listitem.setOnMouseClicked(e -> {
                        // When item is clicked code below will check if all items of checklist have been checked.
                        // When this is the case the submit button will be changed to active.
                        ArrayList<CheckBox> checked = new ArrayList<>();
                        for (CheckBox box : list) {
                            if (box.isSelected()){
                                checked.add(box);
                            }
                        }
                        submit.setDisable(!(checked.size() == list.size()));
                    });

                    list.add(listitem);
                }
            }
        }

        // Creates title and subtitle.
        Label title = new Label(spotname);
        title.getStyleClass().add("infoTitle");
        Label subtitle = new Label("Status: Onderhoud");
        subtitle.getStyleClass().add("text-label");

        // Adds all children to container.
        vBox.getChildren().add(title);
        vBox.getChildren().add(subtitle);
        for (CheckBox l : list) {
            vBox.getChildren().add(l);
        }
        vBox.getChildren().add(submit);
        // Return container (VBox).
        return vBox;
    }

    private CheckBox getChecklistItem(String item) {
        CheckBox box = new CheckBox(item);
        box.getStyleClass().add("info-text");
        return box;
    }

    @Override
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

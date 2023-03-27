package com.example.campingdekiezelsteen.State;

import com.example.campingdekiezelsteen.BuildingSpot;
import com.example.campingdekiezelsteen.Laundry;
import com.example.campingdekiezelsteen.Sanitair;
import com.example.campingdekiezelsteen.Spot;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


public class Free implements State {
    private String color = "green";

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
        Label subtitle = new Label("Status: Vrij");
        subtitle.getStyleClass().add("text-label");
        Label text = new Label("Reserveer hieronder: ");
        text.getStyleClass().add("info-text");
        Button submit = new Button("Reserveer");
        submit.getStyleClass().add("submit-button");
        vBox.getChildren().add(title);
        vBox.getChildren().add(subtitle);
        vBox.getChildren().add(text);
        vBox.getChildren().add(getTextfield("Volledige naam"));
        vBox.getChildren().add(getDatePicker("Aankomstdatum"));
        vBox.getChildren().add(getDatePicker("Vertrekdatum"));
        vBox.getChildren().add(submit);
        return vBox;
    }

    private VBox getDatePicker(String label){
        Label fieldTitle = new Label(label + ": ");
        fieldTitle.getStyleClass().add("text-field-label");
        DatePicker field = new DatePicker();
        field.getStyleClass().add("text-field");
        VBox textfield = new VBox();
        textfield.getChildren().add(fieldTitle);
        textfield.getChildren().add(field);
        return textfield;
    }

    private VBox getTextfield(String label){
        Label fieldTitle = new Label(label + ": ");
        fieldTitle.getStyleClass().add("text-field-label");
        TextField field = new TextField();
        field.getStyleClass().add("text-field");
        VBox textfield = new VBox();
        textfield.getChildren().add(fieldTitle);
        textfield.getChildren().add(field);
        return textfield;
    }

    @Override
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

package com.example.campingdekiezelsteen.State;

import com.example.campingdekiezelsteen.BuildingSpot;
import com.example.campingdekiezelsteen.Spot;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public class Free implements State {
    private String color = "green";
    private ArrayList<Object> fields = new ArrayList<>();
    private Button submit = new Button("Reserveer");

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
        submit.getStyleClass().add("submit-button");
        submit.setDisable(true);
        vBox.getChildren().add(title);
        vBox.getChildren().add(subtitle);
        vBox.getChildren().add(text);
        vBox.getChildren().add(getTextfield("Volledige naam hoofdboeker"));
        vBox.getChildren().add(getDatePicker("Aankomstdatum"));
        vBox.getChildren().add(getDatePicker("Vertrekdatum"));
        vBox.getChildren().add(submit);
        return vBox;
    }

    private VBox getDatePicker(String label) {
        Label fieldTitle = new Label(label + ": ");
        fieldTitle.getStyleClass().add("text-field-label");
        DatePicker field = new DatePicker();
        field.getEditor().setDisable(true);
        field.getEditor().setOpacity(1);
        field.getStyleClass().add("text-field");
        fields.add(field);
        field.valueProperty().addListener(getListener());
        VBox textfield = new VBox();
        textfield.getChildren().add(fieldTitle);
        textfield.getChildren().add(field);
        return textfield;
    }

    private VBox getTextfield(String label) {
        Label fieldTitle = new Label(label + ": ");
        fieldTitle.getStyleClass().add("text-field-label");
        TextField field = new TextField();
        field.getStyleClass().add("text-field");
        fields.add(field);
        field.textProperty().addListener(getListener());
        VBox textfield = new VBox();
        textfield.getChildren().add(fieldTitle);
        textfield.getChildren().add(field);
        return textfield;
    }

    private ChangeListener getListener() {
        // Listener makes sure that all fields are filled out before making button active.
        return (observable, oldValue, newValue) -> {
            ArrayList<Object> filled = new ArrayList<>();
            for (Object o : fields) {
                if (o.getClass().isAssignableFrom(TextField.class)) {
                    TextField field1 = (TextField) o;
                    if (!field1.getText().trim().isEmpty() || !field1.getText().trim().equals("")) {
                        filled.add(field1);
                    }
                } else if (o.getClass().isAssignableFrom(DatePicker.class)) {
                    DatePicker picker = (DatePicker) o;
                    if (picker.getValue() != null) {
                        filled.add(picker);
                    }
                }
            }
            submit.setDisable(!(filled.size() == fields.size()));
        };
    }


    @Override
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

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
        // Sets up infoblock.
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));

        // Sets spotnumber as variable.
        String spotname = "Spot " + spot.getSpotNr();

        if (spot.getPlaceable() != null && spot.getClass().isAssignableFrom(BuildingSpot.class)) {
            // If spot is a buildingspot -> set spotname instead of spotnumber as title.
            spotname = spot.getPlaceable().getName();
        }

        // Creates title, subtitle and text.
        Label title = new Label(spotname);
        title.getStyleClass().add("infoTitle");
        Label subtitle = new Label("Status: Vrij");
        subtitle.getStyleClass().add("text-label");
        Label text = new Label("Reserveer hieronder: ");
        text.getStyleClass().add("info-text");

        // Gets make reservation button and disables it.
        submit.getStyleClass().add("submit-button");
        submit.setDisable(true);

        // Adds all children to container.
        vBox.getChildren().add(title);
        vBox.getChildren().add(subtitle);
        vBox.getChildren().add(text);
        vBox.getChildren().add(getTextfield("Volledige naam hoofdboeker"));
        vBox.getChildren().add(getDatePicker("Aankomstdatum"));
        vBox.getChildren().add(getDatePicker("Vertrekdatum"));
        vBox.getChildren().add(submit);
        // Return container (VBox).
        return vBox;
    }

    private VBox getDatePicker(String label) {
        // Creates label and datepicker item.
        Label fieldTitle = new Label(label + ": ");
        fieldTitle.getStyleClass().add("text-field-label");
        DatePicker field = new DatePicker();
        field.getStyleClass().add("text-field");

        // Disables typing inside datepicker.
        field.getEditor().setDisable(true);
        field.getEditor().setOpacity(1);

        // Adds datepicker field to arraylist with all fields.
        fields.add(field);

        // Adds changelistener to datepicker.
        field.valueProperty().addListener(getListener());

        // Creates container and adds all children.
        VBox textfield = new VBox();
        textfield.getChildren().add(fieldTitle);
        textfield.getChildren().add(field);
        // Returns datepicker as VBox.
        return textfield;
    }

    private VBox getTextfield(String label) {
        // Creates label and textfield item.
        Label fieldTitle = new Label(label + ": ");
        fieldTitle.getStyleClass().add("text-field-label");
        TextField field = new TextField();
        field.getStyleClass().add("text-field");

        // Adds textfield to arraylist with all fields.
        fields.add(field);

        // Adds changelistener to textfield.
        field.textProperty().addListener(getListener());

        // Creates container and adds all children.
        VBox textfield = new VBox();
        textfield.getChildren().add(fieldTitle);
        textfield.getChildren().add(field);
        // Returns textfield as VBox.
        return textfield;
    }

    private ChangeListener getListener() {
        // Listener makes sure that all fields are filled out before making button active.
        return (observable, oldValue, newValue) -> {
            // Create arraylist for fields that have been filled.
            ArrayList<Object> filled = new ArrayList<>();
            // Loops through all fields.
            for (Object o : fields) {
                if (o.getClass().isAssignableFrom(TextField.class)) {
                    // If field is textfield 'merge' back to textfield.
                    TextField field1 = (TextField) o;
                    if (!field1.getText().trim().isEmpty() || !field1.getText().trim().equals("")) {
                        // If field is not empty, add to filled arraylist.
                        filled.add(field1);
                    }
                } else if (o.getClass().isAssignableFrom(DatePicker.class)) {
                    // If field is datepicker 'merge' back to datepicker.
                    DatePicker picker = (DatePicker) o;
                    if (picker.getValue() != null) {
                        // If datepicker is not empty, add to filled arraylist.
                        filled.add(picker);
                    }
                }
            }
            // Change button disabled to false if all fields have been filled in. Else set button disabled to true.
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

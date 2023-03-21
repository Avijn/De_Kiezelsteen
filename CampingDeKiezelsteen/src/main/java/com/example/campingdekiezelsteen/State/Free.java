package com.example.campingdekiezelsteen.State;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class Free implements State {
    private String color = "green";

    @Override
    public VBox buttonClicked() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        Label title = new Label("Vrij");
        title.getStyleClass().add("infoTitle");
        vBox.getChildren().add(title);
        return vBox;
    }

    @Override
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

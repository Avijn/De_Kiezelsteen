package com.example.campingdekiezelsteen.State;

import com.example.campingdekiezelsteen.HelloApplication;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Window;


public class Free implements State {
    private String color;

    @Override
    public void buttonClicked() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        Label title = new Label("Vrij");
        title.getStyleClass().add("infoTitle");
        vBox.getChildren().add(title);
//        setRightPanel(vBox);
    }
}

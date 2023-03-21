package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.Free;
import com.example.campingdekiezelsteen.State.Reserved;
import com.example.campingdekiezelsteen.State.UnderMaintenance;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HelloApplication extends Application {
    ArrayList<Spot> spotsList = new ArrayList<>();
    private final Pane pane = new Pane();

    @Override
    public void start(Stage stage) {
        // Adds spots to spotsList.
        addSpots();
        // Sets up scene.
        Scene scene = new Scene(setupDesign(), 1000, 600);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private HBox setupDesign() {
        HBox hBox = new HBox();
        hBox.setFillHeight(true);
        hBox.getChildren().add(setupGrid());
        pane.getStyleClass().add("infoBlock");
        hBox.getChildren().add(pane);
        return hBox;
    }

    private GridPane setupGrid() {
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("bluePrint");
        gridPane.setPrefWidth(700);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        // Gets square root of amount of spots to divide in even rows and columns.
        double square = Math.sqrt(spotsList.size());
        // current is used to keep track of the amount of buttons that have been created in the loops.
        int current = 1;
        // Loop for rows.
        for (int y = 0; y < square; y++) {
            // Loop for columns.
            for (int x = 0; x < square; x++) {
                // Checks if current is not greater than the amount of spots.
                if (current <= spotsList.size()) {
                    gridPane.add(addButton(current), x, y);
                    current++;
                }
            }
        }
        return gridPane;
    }

    private Button addButton(int spotnr) {
        Button button = new Button(String.valueOf(spotnr));
        button.setPrefSize(200, 200);
        // Gets color from state and gets right style for button:
        switch (spotsList.get(spotnr-1).getState().getColor()) {
            case "orange" -> button.getStyleClass().add("state-orange");
            case "red" -> button.getStyleClass().add("state-red");
            default -> button.getStyleClass().add("state-green");
        }
        button.setOnMouseClicked( e -> {
            // Clears infoblock
            pane.getChildren().clear();
            // Adds right info in infoblock
            pane.getChildren().add(spotsList.get(spotnr - 1).getState().buttonClicked());
        });
        return button;
    }

    private void addSpots(){
//        TODO: JSON File hierop toepassen. Nu nog hardcoded.
        for (int i = 0; i < 60; i++){
            switch (i+1) {
                case 2 -> spotsList.add(new BringableSpot(new UnderMaintenance()));
                case 12 -> spotsList.add(new BringableSpot(new Reserved()));
                default -> spotsList.add(new BringableSpot(new Free()));
            }
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
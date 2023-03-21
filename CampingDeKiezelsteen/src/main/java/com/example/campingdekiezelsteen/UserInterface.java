package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.Free;
import com.example.campingdekiezelsteen.State.Reserved;
import com.example.campingdekiezelsteen.State.UnderMaintenance;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UserInterface extends Application {
    ArrayList<Spot> spotsList = new ArrayList<>();
    private final Pane pane = new Pane();

    @Override
    public void start(Stage stage) {
        // Adds spots to spotsList.
        addSpots();
        // Sets up scene.
        Scene scene = new Scene(setupDesign(), 1000, 600);
        scene.getStylesheets().add(UserInterface.class.getResource("style.css").toExternalForm());
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
        gridPane.setMaxHeight(600);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(15);
        gridPane.setVgap(15);
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
        Spot spot = spotsList.get(spotnr-1);
        System.out.println(spot.getClass().isAssignableFrom(BringableSpot.class));
        Button button = new Button(String.valueOf(spotnr));
        button.setPrefSize(200, 200);
        setButtonStyle(spot, button);
        button.setOnMouseClicked( e -> {
            // Clears infoblock
            pane.getChildren().clear();
            // Adds right info in infoblock
            pane.getChildren().add(spotsList.get(spotnr - 1).getState().buttonClicked());
        });
        return button;
    }

    private void setButtonStyle(Spot spot, Button button){
        if (spot.getClass().isAssignableFrom(BringableSpot.class)) {

        }

        // Gets color from state and gets right style for button:
        switch (spot.getState().getColor()) {
            case "orange" -> button.getStyleClass().add("state-orange");
            case "red" -> button.getStyleClass().add("state-red");
            default -> button.getStyleClass().add("state-green");
        }
    }

    private void addSpots(){
//        TODO: JSON File hierop toepassen. Nu is het nog hardcoded.
        for (int i = 0; i < 60; i++){
            switch (i+1) {
//                TODO: Nu nog standaard een bringable spot, kan uiteraard ook een buildingspot zijn. Moet ook uit de JSON komen.
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
package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.State.Free;
import com.example.campingdekiezelsteen.State.Reserved;
import com.example.campingdekiezelsteen.State.State;
import com.example.campingdekiezelsteen.State.UnderMaintenance;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class UserInterface extends Application {
    private final Pane pane = new Pane();
    private final GridPane gridPane = new GridPane();
    private ArrayList<Spot> spotsList = new ArrayList<>();

    public static void main(String[] args) {
        launch();
    }

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
        Spot spot = spotsList.get(spotnr - 1);
        Button button = new Button(String.valueOf(spotnr));
        button.setPrefSize(200, 200);
        setButtonStyle(spot, button);
        // Shows infoblock when button is clicked
        button.setOnMouseClicked(e -> {
            loadPane(getPaneInfo(spot, button));
        });
        return button;
    }

    private VBox getPaneInfo(Spot spot, Button button){
        // Creates info block
        VBox vBox = spot.getState().buttonClicked(spot);
        // Sets listener for button inside info block
        vBox.getChildren().get(vBox.getChildren().size() - 1).setOnMouseClicked(event -> {
            // If state is under maintenance, then make button change state.
            if (spot.getState().getClass().isAssignableFrom(UnderMaintenance.class)) {
                State newState = new Free();
                if (spot.getPlaceable() != null && (spot.getPlaceable().getClass().isAssignableFrom(Laundry.class) || spot.getPlaceable().getClass().isAssignableFrom(Sanitair.class))) {
                    newState = new Reserved();
                }
                // Changes state
                spot.changeState(newState);
                // Reloads button and info block
                setButtonStyle(spot, button);
                loadPane(spot.getState().buttonClicked(spot));
            }
            // Else if state is free make reservation.
            else if (spot.getState().getClass().isAssignableFrom(Free.class)) {
                // TODO: make reservation.

            }
        });
        return vBox;
    }

    private void loadPane(VBox vBox){
        // Clears infoblock
        pane.getChildren().clear();
        // Adds right info in infoblock
        pane.getChildren().add(vBox);
    }

    private void setButtonStyle(Spot spot, Button button) {
        // Clears all styles
        button.getStyleClass().clear();
        // Adds standard style to all buttons
        button.getStyleClass().add("transparent");
        if (spot.getPlaceable() != null && spot.getPlaceable().getStyle() != "") {
            // Adds background style -> png image of placeable
            button.getStyleClass().add(spot.getPlaceable().getStyle());
        }

        // Gets color from state and gets right style for button:
        switch (spot.getState().getColor()) {
            case "orange" -> button.getStyleClass().add("state-orange");
            case "red" -> button.getStyleClass().add("state-red");
            default -> button.getStyleClass().add("state-green");
        }
    }

    private void addSpots() {
//        TODO: JSON File hierop toepassen. Nu is het nog hardcoded.
        BuildingSpot buildingSpot = new BuildingSpot(new UnderMaintenance());
        buildingSpot.createPlaceable(new House());
        BuildingSpot sanitair = new BuildingSpot(new UnderMaintenance());
        sanitair.createPlaceable(new Sanitair());
        BuildingSpot laundry = new BuildingSpot(new Reserved());
        laundry.createPlaceable(new Laundry());
        BuildingSpot tiki = new BuildingSpot(new UnderMaintenance());
        tiki.createPlaceable(new TikiTent());
        for (int i = 0; i < 60; i++) {
            switch (i + 1) {
//                TODO: Nu nog standaard een bringable spot, kan uiteraard ook een buildingspot zijn. Moet ook uit de JSON komen.
                case 2 -> {
                    buildingSpot.setSpotNr(i + 1);
                    spotsList.add(buildingSpot);
                }
                case 12 -> {
                    sanitair.setSpotNr(i + 1);
                    spotsList.add(sanitair);
                }
                case 15 -> {
                    laundry.setSpotNr(i + 1);
                    spotsList.add(laundry);
                }
                case 35 -> {
                    tiki.setSpotNr(i + 1);
                    spotsList.add(tiki);
                }
                default -> {
                    Spot spot = new BringableSpot(new Free());
                    spot.setSpotNr(i + 1);
                    spotsList.add(spot);
                }
            }
        }
    }
}
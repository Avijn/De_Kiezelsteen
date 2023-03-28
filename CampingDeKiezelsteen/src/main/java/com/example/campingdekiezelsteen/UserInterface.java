package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.Adapter.Camping;
import com.example.campingdekiezelsteen.State.Free;
import com.example.campingdekiezelsteen.State.Reserved;
import com.example.campingdekiezelsteen.State.State;
import com.example.campingdekiezelsteen.State.UnderMaintenance;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;

public class UserInterface extends Application {
    private final Pane pane = new Pane();
    private final GridPane gridPane = new GridPane();
    private Camping camping;
    private ArrayList<Spot> spotsList = new ArrayList<>();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        // Adds spots to spotsList.
        camping = new Camping("De Kiezelsteen");
        addSpots();
        // Sets up scene.
        Scene scene = new Scene(setupDesign(), 1000, 600);
        scene.getStylesheets().add(UserInterface.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        // Disable resizable property.
        stage.resizableProperty().setValue(Boolean.FALSE);
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
        // Gets background css class from blueprint.
        gridPane.getStyleClass().add(camping.getBlueprint().getBackground());
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

    private VBox getPaneInfo(Spot spot, Button button) {
        // Creates info block
        VBox vBox = spot.getState().buttonClicked(spot);
        if (spot.getState().getClass().isAssignableFrom(Reserved.class)){
            if (!camping.getOrderBook().getReservations().isEmpty()) {
                for (Reservation reservation : camping.getOrderBook().getReservations().values()) {
                    if (reservation.getReservable() == spot || reservation.getReservable() == spot.getPlaceable()) {
                        // If state of spot is reserved and spot or placeable is found in reservations also send reservation to state.
                        Reserved state = (Reserved) spot.getState();
                        vBox = state.buttonClicked(spot, reservation);
                    }
                }
            }
        }
        // Sets listener for button inside info block
        if (vBox.getChildren().size() != 0) {
            vBox.getChildren().get(vBox.getChildren().size() - 1).setOnMouseClicked(event -> {
                // If state is under maintenance, then make button change state.
                if (spot.getState().getClass().isAssignableFrom(UnderMaintenance.class)) {
                    State newState = new Free();
                    if (spot.getPlaceable() != null && (spot.getPlaceable().getClass().isAssignableFrom(Laundry.class) || spot.getPlaceable().getClass().isAssignableFrom(Sanitair.class))) {
                        newState = new Reserved();
                    }
                    // Changes state
                    spot.changeState(newState);
                    showPopup("Status succesvol veranderd.", 3);
                    // Reloads button and info block
                    setButtonStyle(spot, button);
                    loadPane(getPaneInfo(spot, button));
                }
                // Else if state is free make reservation.
                else if (spot.getState().getClass().isAssignableFrom(Free.class)) {
                    // TODO: make reservation.
                    Free state = (Free) spot.getState();
                    createReservation(spot, state.getFields());
                    setButtonStyle(spot, button);
                    loadPane(getPaneInfo(spot, button));
                }
            });
        }
        return vBox;
    }

    private void loadPane(VBox vBox) {
        // Clears infoblock
        pane.getChildren().clear();
        // Adds right info in infoblock
        pane.getChildren().add(vBox);
    }

    private void setButtonStyle(Spot spot, Button button) {
        // Clears all styles
        button.getStyleClass().clear();
        // Adds standard style to all buttons
        for (Reservation reservation : camping.getOrderBook().getReservations().values()) {
            if (reservation.getReservable().equals(spot) || reservation.getReservable().equals(spot.getPlaceable())) {
                if (reservation.getArrivaldate().isBefore(LocalDate.now()) && reservation.getDeparturedate().isAfter(LocalDate.now())) {
                    spot.setState(new Reserved());
                }
            }
        }

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
        buildingSpot.createPlaceable(new House("Vakantiehuis 1"));
        BuildingSpot sanitair = new BuildingSpot(new UnderMaintenance());
        sanitair.createPlaceable(new Sanitair("Sanitair 1"));
        BuildingSpot laundry = new BuildingSpot(new Reserved());
        laundry.createPlaceable(new Laundry("Laundry 1"));
        BuildingSpot tiki = new BuildingSpot(new UnderMaintenance());
        tiki.createPlaceable(new TikiTent("Tiki-Tent 1"));
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

    private void createReservation(Spot spot, ArrayList<Object> fields) {
        // Create reservation variables.
        String name = "";
        LocalDate arrivalDate = LocalDate.now();
        LocalDate departureDate = LocalDate.now();
        Reservable reservable;
        Placeable placeable = null;

        if (spot instanceof Reservable) {
            // If spot is reservable set spot as reservable for reservation.
            reservable = (Reservable) spot;
        } else if (spot.getPlaceable() != null && spot.getPlaceable() instanceof Reservable) {
            // If spot is not and placeable is reservable set placeable as reservable for reservation.
            reservable = (Reservable) spot.getPlaceable();
        } else {
            // Else something went wrong and reservable is null.
            reservable = null;
        }

        // Loop through fields.
        for (Object item : fields) {
            if (item.getClass().isAssignableFrom(TextField.class)) {
                // If item is textfield and field id is 'name' get text for reservation variable name.
                TextField field = (TextField) item;
                if (field.getId().equals("name")) {
                    name = field.getText();
                }
            } else if (item.getClass().isAssignableFrom(DatePicker.class)) {
                // If item is datepicker
                DatePicker field = (DatePicker) item;
                if (field.getId().equals("aDate")) {
                    // If field id is 'aDate' get value for reservation variable arrivalDate.
                    arrivalDate = field.getValue();
                } else if (field.getId().equals("dDate")) {
                    // If field id is 'dDate' get value for reservation variable departureDate.
                    departureDate = field.getValue();
                }
            } else {
                // Else field is Choicebox and spot is instance of reservable.
                if (spot instanceof Reservable) {
                    ChoiceBox<String> box = (ChoiceBox<String>) item;
                    // Switch case for type of placeable.
                    switch (box.getValue()) {
                        case "Camper" -> placeable = new Camper();
                        case "Caravan" -> placeable = new Caravan();
                        case "Tent" -> placeable = new Tent();
                        default -> placeable = null;
                    }
                }
            }
        }

        if (reservable != null) {
            // If reservable is not null create reservation with created variables.
            Reservation reservation = new Reservation(reservable, name, arrivalDate, departureDate, "#" + spot.getSpotNr() + camping.getOrderBook().getReservations().size(), placeable);
            // Add reservation to orderbook of camping.
            camping.getOrderBook().addReservation(camping.getOrderBook().getReservations().size(), reservation);
            // Shows popup with success message.
            showPopup("Reservering succesvol aangemaakt.", 3);
            // If reservation is during current day then change state to reserved and add placeable to spot.
            if (reservation.getArrivaldate().isBefore(LocalDate.now()) && reservation.getDeparturedate().isAfter(LocalDate.now())) {
                spot.setState(new Reserved());
                if (placeable != null) {
                    spot.setPlaceable(placeable);
                }
            }
        }


    }

    private void showPopup(String message, int seconds) {
        Stage popup = new Stage();
        popup.initStyle(StageStyle.TRANSPARENT);

        // Create layout.
        VBox popupLayout = new VBox();
        Label popupMessage = new Label(message);
        popupMessage.getStyleClass().add("popup-text");
        popupLayout.getChildren().add(popupMessage);
        popupLayout.getStyleClass().add("popup-success");

        // Create scene.
        Scene scene = new Scene(popupLayout, 800, 50);
        scene.getStylesheets().add(UserInterface.class.getResource("style.css").toExternalForm());
        scene.setFill(Color.TRANSPARENT);

        // Add scene to popup and position stage.
        popup.setScene(scene);
        popup.setX((Screen.getPrimary().getVisualBounds().getWidth() - 800) / 2);
        popup.setY((Screen.getPrimary().getVisualBounds().getHeight() - 50) / 8);

        // hide popup after few seconds:
        PauseTransition delay = new PauseTransition(Duration.seconds(seconds));
        delay.setOnFinished(e -> {
            FadeTransition fadeout = fade(false, popupLayout);

            //playing the transition
            fadeout.play();
            fadeout.setOnFinished(event -> popup.hide());
        });

        popup.setAlwaysOnTop(true);
        popup.show();
        FadeTransition fadein = fade(true, popupLayout);
        fadein.play();
        delay.play();
    }

    private FadeTransition fade(boolean fadein, Node node) {
        FadeTransition fade = new FadeTransition();

        if (fadein) {
            //setting the duration for the Fade transition
            fade.setDuration(Duration.seconds(0.2));
            //setting the initial and the target opacity value for the transition
            fade.setFromValue(0);
            fade.setToValue(100);
        } else {
            //setting the duration for the Fade transition
            fade.setDuration(Duration.seconds(1));
            //setting the initial and the target opacity value for the transition
            fade.setFromValue(100);
            fade.setToValue(0);
        }

        //the transition will set to be auto reversed by setting this to true
        fade.setAutoReverse(false);

        //setting Circle as the node onto which the transition will be applied
        fade.setNode(node);

        return fade;
    }
}
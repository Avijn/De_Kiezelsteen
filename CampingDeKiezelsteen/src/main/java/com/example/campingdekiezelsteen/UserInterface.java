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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserInterface extends Application {
    private final Pane pane = new Pane();
    private final GridPane gridPane = new GridPane();
    private final Pane reservationsPane = new Pane();
    private Camping camping;
    private ArrayList<Spot> spotsList = new ArrayList<>();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        // Creates camping and gets spots from blueprint.
        camping = new Camping("De Kiezelsteen");
        spotsList = new ArrayList<>(camping.getSpots().values());

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
        hBox.getChildren().add(setupRightBlock());
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
            getReservationsInfo(spot);
        });

        // Change state of sanitair and laundry according to amount of current reservations.
        if (spot.getPlaceable() != null && (spot.getPlaceable() instanceof Laundry || spot.getPlaceable() instanceof Sanitair)) {
            needsCleaningCalculator(spot, button);
        }

        return button;
    }

    private void needsCleaningCalculator(Spot spot, Button button) {
        // Standard time between cleanings is 12 hours, with every reservation thirty minutes will be subtracted.
        int minutes = 720 - (30 * camping.getCurrentReservations());
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                spot.setState(new UnderMaintenance());
                setButtonStyle(spot, button);
                needsCleaningCalculator(spot, button);
            }
        };
        executorService.schedule(runnable, minutes, TimeUnit.MINUTES);
    }

    private Accordion setupRightBlock() {
        // Create accordion.
        Accordion accordion = new Accordion();
        accordion.setMinWidth(300);
        // Set styles of child panes.
        pane.getStyleClass().add("infoBlock");
        reservationsPane.getStyleClass().add("infoBlock");
        // Create titledpanes with child panes.
        TitledPane pane1 = new TitledPane("Info", pane);
        TitledPane pane2 = new TitledPane("Reserveringen", reservationsPane);
        // Add child panes to accordion.
        accordion.getPanes().add(pane1);
        accordion.getPanes().add(pane2);
        // Set top pane to expanded.
        accordion.setExpandedPane(pane1);
        return accordion;
    }

    private void loadPane(VBox vBox) {
        // Clears infoblock
        pane.getChildren().clear();
        // Adds right info in infoblock
        pane.getChildren().add(vBox);
    }

    private VBox getPaneInfo(Spot spot, Button button) {
        // Creates info block
        VBox vBox = spot.getState().buttonClicked(spot);
        if (spot.getState().getClass().isAssignableFrom(Reserved.class)) {
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
                    if (spot.getPlaceable() != null) {
                        if (spot.getPlaceable().getClass().isAssignableFrom(Laundry.class) || spot.getPlaceable().getClass().isAssignableFrom(Sanitair.class)) {
                            newState = new Reserved();
                        }
                        for (Reservation res : camping.getOrderBook().getReservations().values()) {
                            if (res.getReservable().equals(spot.getPlaceable())) {
                                if (camping.currentDay(res.getArrivaldate(), res.getDeparturedate())) {
                                    newState = new Reserved();
                                }
                            }
                        }
                    }
                    // Changes state
                    Building building = (Building) spot.getPlaceable();
                    building.clean();
                    spot.changeState(newState);
                    showPopup("Status succesvol veranderd.", 3, true);
                    // Reloads button and info block
                    setButtonStyle(spot, button);
                    loadPane(getPaneInfo(spot, button));
                }
                // Else if state is free make reservation.
                else if (spot.getState().getClass().isAssignableFrom(Free.class)) {
                    Free state = (Free) spot.getState();
                    createReservation(spot, state.getFields());
                    setButtonStyle(spot, button);
                    loadPane(getPaneInfo(spot, button));
                }
            });
        }
        return vBox;
    }

    private void getReservationsInfo(Spot spot) {
        // Clear out the pane.
        reservationsPane.getChildren().clear();
        // Create new accordion for all reservations.
        Accordion accordion = new Accordion();
        accordion.setPadding(new Insets(20));
        // Get all reservations with this spot.
        for (Reservation res : camping.getOrderBook().getReservations().values()) {
            if (res.getReservable() == spot || res.getReservable() == spot.getPlaceable()) {
                // Create pane and titledpane for each reservation.
                Pane pane = new Pane();
                pane.getStyleClass().add("infoBlock");
                // Set pane content with info of reservation.
                pane.getChildren().add(getInfo(res));
                TitledPane titledPane = new TitledPane(res.getId(), pane);
                // Add titledpane to accordion.
                accordion.getPanes().add(titledPane);
            }
        }
        // Add accordion to pane.
        reservationsPane.getChildren().add(accordion);
    }

    private VBox getInfo(Reservation reservation) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        // Formatter for dates.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        // Get name, arrivaldate and departuredate from reservation.
        vBox.getChildren().add(getSingularLine("Hoofdboeker: ", reservation.getCustomerName()));
        vBox.getChildren().add(getSingularLine("Aankomstdatum: ", reservation.getArrivaldate().format(formatter)));
        vBox.getChildren().add(getSingularLine("Vertrekdatum: ", reservation.getDeparturedate().format(formatter)));
        return vBox;
    }

    private HBox getSingularLine(String title, String info) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20, 0, 0, 0));
        // Create title and set style
        Label Title = new Label(title);
        Title.setPrefWidth(130);
        Title.getStyleClass().add("info-text");
        // Create infotext and set style
        Label Info = new Label(info);
        Info.setPrefWidth(130);
        Info.getStyleClass().add("info-text");
        // Add both to hbox.
        hBox.getChildren().add(Title);
        hBox.getChildren().add(Info);
        return hBox;
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

    private void createReservation(Spot spot, ArrayList<Object> fields) {
        // Create reservation variables.
        String name = "";
        LocalDate arrivalDate = LocalDate.now();
        LocalDate departureDate = LocalDate.now();
        Reservable reservable;
        Placeable placeable = null;
        String placeableType = "";

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
                    placeableType = box.getValue();
                    // Switch case for type of placeable.
                    switch (box.getValue()) {
                        case "Camper" -> placeable = new Camper();
                        case "Caravan" -> placeable = new Caravan();
                        case "Tent" -> placeable = new Tent();
                        default -> placeable = null;
                    }

                    assert placeable != null;
                    placeable.setName(box.getValue());
                }
            }
        }

        for (Reservation res : camping.getOrderBook().getReservations().values()) {
            if (res.getReservable() == spot || res.getReservable() == spot.getPlaceable()) {
                if ((res.getArrivaldate().isBefore(arrivalDate) && res.getDeparturedate().isAfter(arrivalDate)) || (res.getArrivaldate().isBefore(departureDate) && res.getDeparturedate().isAfter(departureDate))) {
                    showPopup("Tijdens deze data staat er al een reservering gepland.", 3, false);
                    return;
                }
            }
        }

        if (reservable != null) {
            // If reservable is not null create reservation with created variables.
            Reservation reservation = new Reservation(reservable, name, arrivalDate, departureDate, "#000" + camping.getOrderBook().getReservations().size(), placeable);
            // Add reservation to orderbook of camping.
            camping.getOrderBook().addReservation(camping.getOrderBook().getReservations().size(), reservation);
            // Shows popup with success message.
            showPopup("Reservering succesvol aangemaakt.", 3, true);
            // If reservation is during current day then change state to reserved and add placeable to spot.
            if (camping.currentDay(reservation.getArrivaldate(), reservation.getDeparturedate())) {
                spot.setState(new Reserved());
                if (!placeableType.equals("")) {
                    spot.setPlaceable(placeableType);
                }
            }
        }
    }

    private void showPopup(String message, int seconds, boolean succes) {
        Stage popup = new Stage();
        popup.initStyle(StageStyle.TRANSPARENT);

        // Create layout.
        VBox popupLayout = new VBox();
        Label popupMessage = new Label(message);
        popupMessage.getStyleClass().add("popup-text");
        popupLayout.getChildren().add(popupMessage);
        // Style -> Green or Red.
        if (succes) {
            popupLayout.getStyleClass().add("popup-success");
        } else {
            popupLayout.getStyleClass().add("popup-error");
        }

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

    // Fade transition for popup.
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

        //the transition will only play once
        fade.setAutoReverse(false);

        //setting the parameter node as the node onto which the transition will be applied
        fade.setNode(node);

        return fade;
    }
}
package com.example.campingdekiezelsteen.State;

import com.example.campingdekiezelsteen.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class Reserved implements State {
    private String color = "red";
    private Reservation reservation;

    public VBox buttonClicked(Spot spot, Reservation reservation) {
        this.reservation = reservation;
        return buttonClicked(spot);
    }

    @Override
    public VBox buttonClicked(Spot spot) {
        String spotname = "Spot " + spot.getSpotNr();
        String state = "Gereserveerd";
        if (spot.getPlaceable() != null && spot.getClass().isAssignableFrom(BuildingSpot.class)) {
            if (spot.getPlaceable().getClass().isAssignableFrom(Laundry.class) || spot.getPlaceable().getClass().isAssignableFrom(Sanitair.class)) {
                state = "Niet te reserveren";
            }
            spotname = spot.getPlaceable().getName();
        }
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20));
        Label title = new Label(spotname);
        title.getStyleClass().add("infoTitle");
        Label subtitle = new Label("Status: " + state);
        subtitle.getStyleClass().add("text-label");
        vBox.getChildren().add(title);
        vBox.getChildren().add(subtitle);

        if (reservation != null) {
            vBox.getChildren().add(getName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
                vBox.getChildren().add(getDate(reservation.getArrivaldate().format(formatter), true));
                vBox.getChildren().add(getDate(reservation.getDeparturedate().format(formatter)
                        , false));
                if (spot.getClass().isAssignableFrom(BringableSpot.class) && spot.getPlaceable() != null) {
                    vBox.getChildren().add(getPlaceableType(spot.getPlaceable().getType()));
                }
        } else if(state != "Niet te reserveren"){
            Label error = new Label("Er is iets misgegaan.");
            error.getStyleClass().add("info-text");
            vBox.getChildren().add(error);
        }
        return vBox;
    }

    private HBox getName() {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20, 0, 0, 0));
        Label title = new Label("Hoofdboeker: ");
        title.setPrefWidth(130);
        title.getStyleClass().add("info-text");
        String name = "-";
        if (reservation != null) {
            name = reservation.getCustomerName();
        }
        Label nameLabel = new Label(name);
        nameLabel.setPrefWidth(130);
        nameLabel.getStyleClass().add("info-text");
        hBox.getChildren().add(title);
        hBox.getChildren().add(nameLabel);
        return hBox;
    }

    private HBox getPlaceableType(String placeabletype){
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 0, 0, 0));
        String titleString = "Overnachtingstype: ";
        Label title = new Label(titleString);
        title.setPrefWidth(130);
        title.getStyleClass().add("info-text");
        Label date = new Label(placeabletype);
        date.setPrefWidth(130);
        date.getStyleClass().add("info-text");
        hBox.getChildren().add(title);
        hBox.getChildren().add(date);
        return hBox;
    }

    private HBox getDate(String dateString, Boolean arrival) {
        HBox hBox = new HBox();
        hBox.setPadding(new Insets(5, 0, 0, 0));
        String titleString = "Aankomstdatum: ";
        if (!arrival) {
            titleString = "Vertrekdatum: ";
        }
        Label title = new Label(titleString);
        title.setPrefWidth(130);
        title.getStyleClass().add("info-text");
        Label date = new Label(dateString);
        date.setPrefWidth(130);
        date.getStyleClass().add("info-text");
        hBox.getChildren().add(title);
        hBox.getChildren().add(date);
        return hBox;
    }

    @Override
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}

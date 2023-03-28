package com.example.campingdekiezelsteen.State;

import com.example.campingdekiezelsteen.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
                vBox.getChildren().add(getDate(String.valueOf(reservation.getArrivaldate()), true));
                vBox.getChildren().add(getDate(String.valueOf(reservation.getDeparturedate())
                        , false));
        } else {
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
        title.getStyleClass().add("info-text");
        String name = "-";
        if (reservation != null) {
            name = reservation.getCustomerName();
        }
        Label date = new Label(name);
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
        title.getStyleClass().add("info-text");
        Label date = new Label(dateString);
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

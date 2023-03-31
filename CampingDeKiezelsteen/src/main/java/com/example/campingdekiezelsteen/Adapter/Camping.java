package com.example.campingdekiezelsteen.Adapter;

import com.example.campingdekiezelsteen.*;
import com.example.campingdekiezelsteen.State.Reserved;
import com.example.campingdekiezelsteen.State.UnderMaintenance;

import java.time.LocalDate;
import java.util.Map;

public class Camping {
    private Blueprint blueprint;
    private OrderBook orderBook;
    private String name;

    public Camping(String name) {
        blueprint = new Blueprint("bluePrint");
        orderBook = new OrderBook();
        orderBook.setReservations(blueprint.getReservations());
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<Integer, Spot> getSpots() {
        getStatesSpots();
        return blueprint.getSpots();
    }

    public void getStatesSpots() {
        getMaintenanceStates();
        getReservedStates();
    }

    private void getReservedStates() {
        for (Reservation reservation : orderBook.getReservations().values()) {
            for (Spot spot : blueprint.getSpots().values()) {
                if (reservation.getReservable().equals(spot) || reservation.getReservable().equals(spot.getPlaceable())) {
                    if (reservation.getArrivaldate().isBefore(LocalDate.now()) && reservation.getDeparturedate().isAfter(LocalDate.now())) {
                        if (spot.getClass().isAssignableFrom(BringableSpot.class)) {
                            spot.setPlaceable(reservation.getPlaceable().getType());
                        }
                        spot.setState(new Reserved());
                    }
                }
            }
        }
    }


    private void getMaintenanceStates() {
        orderBook.getReservations().forEach((key, reservation) -> {
            for (Spot spot : blueprint.getSpots().values()) {
                if (reservation.getReservable().equals(spot.getPlaceable())) {
                    if (spot.getPlaceable() instanceof Building building) {
                        if (reservation.getArrivaldate().isBefore(LocalDate.now()) && reservation.getDeparturedate().isBefore(LocalDate.now())) {
                            building.used();
                            System.out.println(orderBook.getReservations());
                        }
                    }
                }
                if (spot.getPlaceable() != null && spot.getPlaceable() instanceof Building building && building.getAmountOfUsages() > 0) {
                    spot.setState(new UnderMaintenance());
                }
            }
        });
    }
}

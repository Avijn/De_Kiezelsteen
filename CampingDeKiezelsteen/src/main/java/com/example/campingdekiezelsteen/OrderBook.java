package com.example.campingdekiezelsteen;

import java.util.HashMap;
import java.util.Map;

public class OrderBook {
    private Map<Integer, Reservation> reservations = new HashMap<>();

    public OrderBook() {
    }

    public void addReservation(int id, Reservation reservation){
        reservations.put(id, reservation);
    }

    public void setReservations(Map<Integer, Reservation> reservations) {
        this.reservations = reservations;
    }

    public Map<Integer, Reservation> getReservations() {
        return reservations;
    }
}

package com.example.campingdekiezelsteen;

import java.util.Map;

public class OrderBook {
    private Map<Integer, Reservation> reservations;

    public OrderBook() {

    }

    public void addReservation(int id, Reservation reservation) {
        this.reservations.put(id, reservation);
    }

    public Map<Integer, Reservation> getReservation() {
        return reservations;
    }

    public void setReservation(Map<Integer, Reservation> reservation) {
        this.reservations = reservation;
    }
}

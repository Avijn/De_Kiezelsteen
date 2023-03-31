package com.example.campingdekiezelsteen;

import com.example.campingdekiezelsteen.Adapter.Camping;

import java.util.HashMap;
import java.util.Map;

public class OrderBook {
    private Map<Integer, Reservation> reservations = new HashMap<>();

    public OrderBook() {
    }

    public void addReservation(int id, Reservation reservation, Camping camping){
        reservations.put(id, reservation);
        camping.getBlueprint().addReservationToFile(reservation);
    }

    public void setReservations(Map<Integer, Reservation> reservations) {
        this.reservations = reservations;
    }

    public Map<Integer, Reservation> getReservations() {
        return reservations;
    }
}

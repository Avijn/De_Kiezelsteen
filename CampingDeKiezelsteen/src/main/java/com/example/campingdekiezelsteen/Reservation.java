package com.example.campingdekiezelsteen;

import java.time.LocalDate;
import java.util.Date;

public class Reservation {
    private Reservable reservable;
    private String customerName;
    private LocalDate arrivaldate;
    private LocalDate departuredate;
    private String id;

    public Reservation(Reservable reservable, String customerName, LocalDate arrivaldate, LocalDate departuredate, String id) {
        this.reservable = reservable;
        this.customerName = customerName;
        this.arrivaldate = arrivaldate;
        this.departuredate = departuredate;
        this.id = id;
    }

    //    GETTERS AND SETTERS:

    public Reservable getReservable() {
        return reservable;
    }

    public void setReservable(Reservable reservable) {
        this.reservable = reservable;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDate getArrivaldate() {
        return arrivaldate;
    }

    public void setArrivaldate(LocalDate arrivaldate) {
        this.arrivaldate = arrivaldate;
    }

    public LocalDate getDeparturedate() {
        return departuredate;
    }

    public void setDeparturedate(LocalDate departuredate) {
        this.departuredate = departuredate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

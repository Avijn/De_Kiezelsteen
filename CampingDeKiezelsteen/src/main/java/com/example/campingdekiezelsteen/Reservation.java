package com.example.campingdekiezelsteen;

import java.util.Date;

public class Reservation {
    private Reservable reservable;
    private String customerName;
    private Date arrivaldate;
    private Date departuredate;
    private int id;

    public Reservation() {
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

    public Date getArrivaldate() {
        return arrivaldate;
    }

    public void setArrivaldate(Date arrivaldate) {
        this.arrivaldate = arrivaldate;
    }

    public Date getDeparturedate() {
        return departuredate;
    }

    public void setDeparturedate(Date departuredate) {
        this.departuredate = departuredate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

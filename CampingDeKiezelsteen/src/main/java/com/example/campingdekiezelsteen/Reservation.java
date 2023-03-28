package com.example.campingdekiezelsteen;

public class Reservation {
    private Reservable reservable;
    private String customerName;
    private String date;
    private int id;

    public Reservation(Reservable reservable,
                       String customerName,
                       String date,
                       int id) {
        this.reservable = reservable;
        this.customerName = customerName;
        this.date = date;
        this.id = id;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

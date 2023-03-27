package com.example.campingdekiezelsteen;

public class Laundry extends Building {
    private String style = "laundry";

    public Laundry(){
        setName("Laundry");
    }

    @Override
    public void cleanFloors() {
        super.cleanFloors();
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}

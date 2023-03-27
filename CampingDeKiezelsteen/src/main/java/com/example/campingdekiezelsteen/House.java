package com.example.campingdekiezelsteen;

public class House extends Building implements Reservable {
    private String style = "house";

    @Override
    public void cleanWindows() {
        super.cleanWindows();
    }

    @Override
    public void cleanSheets() {
        super.cleanSheets();
    }

    @Override
    public void reserve() {

    }


    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}

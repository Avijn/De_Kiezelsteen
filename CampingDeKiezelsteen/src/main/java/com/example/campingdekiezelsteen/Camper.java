package com.example.campingdekiezelsteen;

public class Camper extends Bringable {
    private String style = "camper";

    public Camper() {
        super("Camper");
    }

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }
}

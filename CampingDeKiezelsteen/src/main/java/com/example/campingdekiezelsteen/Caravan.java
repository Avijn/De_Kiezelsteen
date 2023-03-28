package com.example.campingdekiezelsteen;

public class Caravan extends Placeable {
    private String style = "caravan";

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }
}

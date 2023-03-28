package com.example.campingdekiezelsteen;

public class Tent extends Placeable {
    private String style = "tent";

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }
}

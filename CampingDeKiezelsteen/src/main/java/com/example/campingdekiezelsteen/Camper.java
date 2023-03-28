package com.example.campingdekiezelsteen;

public class Camper extends Placeable {
    private String style = "camper";

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }
}

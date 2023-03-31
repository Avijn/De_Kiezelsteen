package com.example.campingdekiezelsteen;

public class Tent extends Bringable {
    private String style = "tent";

    public Tent() {
        super("Tent");
        setType("tent");
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

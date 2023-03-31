package com.example.campingdekiezelsteen;

public class Caravan extends Bringable {
    private String style = "caravan";

    public Caravan() {
        super("Caravan");
        setType("caravan");
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

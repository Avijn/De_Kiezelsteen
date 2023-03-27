package com.example.campingdekiezelsteen;


public class TikiTent extends Building implements Reservable {
    private String style = "tikitent";

    public TikiTent(){
        setName("Tiki-tent");
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

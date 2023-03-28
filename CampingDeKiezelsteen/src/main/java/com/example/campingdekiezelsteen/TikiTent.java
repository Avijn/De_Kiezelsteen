package com.example.campingdekiezelsteen;


import java.util.ArrayList;

public class TikiTent extends Building implements Reservable {
    private String style = "tikitent";

    public TikiTent(){
        super("Tiki-tent");
    }

    @Override
    public void cleanSheets() {
        super.cleanSheets();
    }

    @Override
    public void reserve() {

    }

    @Override
    public ArrayList<String> getChecklist() {
        ArrayList<String> checklistitems = new ArrayList<>();
        checklistitems.add("Bedden opmaken");
        return checklistitems;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}

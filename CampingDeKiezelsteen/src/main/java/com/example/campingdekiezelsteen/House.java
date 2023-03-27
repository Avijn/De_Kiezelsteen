package com.example.campingdekiezelsteen;

import java.util.ArrayList;

public class House extends Building implements Reservable {
    private String style = "house";

    public House(){
        setName("Vakantiehuis");
    }

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

    @Override
    public ArrayList<String> getChecklist() {
        ArrayList<String> checklistitems = new ArrayList<>();
        checklistitems.add("Ramen lappen");
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

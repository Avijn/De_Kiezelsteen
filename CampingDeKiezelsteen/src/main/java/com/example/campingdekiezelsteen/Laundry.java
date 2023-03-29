package com.example.campingdekiezelsteen;

import java.util.ArrayList;

public class Laundry extends Building {
    private String style = "laundry";

    public Laundry(String name){
        super(name);
    }

    @Override
    public void cleanFloors() {
        super.cleanFloors();
    }

    @Override
    public ArrayList<String> getChecklist() {
        ArrayList<String> checklistitems = new ArrayList<>();
        checklistitems.add("Vloeren stofzuigen/ dweilen");
        return checklistitems;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}

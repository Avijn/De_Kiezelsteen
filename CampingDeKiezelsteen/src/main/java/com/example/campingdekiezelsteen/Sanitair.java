package com.example.campingdekiezelsteen;

import java.util.ArrayList;

public class Sanitair extends Building {
    private String style = "sanitair";

    public Sanitair(String name){
        super(name);
    }

    @Override
    public void cleanToilets() {
        super.cleanToilets();
    }

    @Override
    public void cleanSinks() {
        super.cleanSinks();
    }

    @Override
    public ArrayList<String> getChecklist() {
        ArrayList<String> checklistitems = new ArrayList<>();
        checklistitems.add("Toiletten schoonmaken");
        checklistitems.add("Wasbakken schoonmaken");
        return checklistitems;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}

package com.example.campingdekiezelsteen;

import java.util.ArrayList;

public abstract class Building extends Placeable {

    public Building(String name) {
        super(name);
    }

    public void clean(){    }

    public void cleanWindows(){

    }

    public void cleanSheets(){

    }

    public void cleanToilets(){

    }

    public void cleanSinks(){

    }

    public void cleanFloors(){

    }

    public ArrayList<String> getChecklist(){
        ArrayList<String> checklistitems = new ArrayList<>();
        checklistitems.add("Ramen lappen");
        checklistitems.add("Bedden opmaken");
        checklistitems.add("Toiletten schoonmaken");
        checklistitems.add("Wasbakken schoonmaken");
        checklistitems.add("Vloeren stofzuigen/ dweilen");
        return checklistitems;
    }
}

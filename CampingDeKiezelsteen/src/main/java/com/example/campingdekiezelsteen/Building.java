package com.example.campingdekiezelsteen;

public abstract class Building extends Placeable {
    private String name;

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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}

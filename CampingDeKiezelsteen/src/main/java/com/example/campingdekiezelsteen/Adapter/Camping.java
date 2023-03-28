package com.example.campingdekiezelsteen.Adapter;

import com.example.campingdekiezelsteen.Adapter.Blueprint;

public class Camping {
    private Blueprint blueprint;

    public Camping(String name)
    {
        blueprint = new Blueprint("bluePrint");
        System.out.println(name);
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(Blueprint blueprint) {
        this.blueprint = blueprint;
    }
}

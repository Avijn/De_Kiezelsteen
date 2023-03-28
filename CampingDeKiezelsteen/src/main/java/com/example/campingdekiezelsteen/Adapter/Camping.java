package com.example.campingdekiezelsteen.Adapter;
import com.example.campingdekiezelsteen.OrderBook;

import com.example.campingdekiezelsteen.Adapter.Blueprint;

public class Camping {
    private Blueprint blueprint;
    private OrderBook orderbook;
    private String name;

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

    public OrderBook getOrderbook() {
        return orderbook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

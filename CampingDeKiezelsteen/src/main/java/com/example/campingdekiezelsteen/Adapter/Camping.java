package com.example.campingdekiezelsteen.Adapter;
import com.example.campingdekiezelsteen.OrderBook;

import com.example.campingdekiezelsteen.*;
import com.example.campingdekiezelsteen.Adapter.Blueprint;
import com.example.campingdekiezelsteen.State.Free;
import com.example.campingdekiezelsteen.State.Reserved;
import com.example.campingdekiezelsteen.State.UnderMaintenance;

import java.util.HashMap;
import java.util.Map;

public class Camping {
    private Blueprint blueprint;
    private OrderBook orderBook = new OrderBook();
    private String name;

    public Camping(String name)
    {
        blueprint = new Blueprint("bluePrint");
        System.out.println(name);
//        addSpots();
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

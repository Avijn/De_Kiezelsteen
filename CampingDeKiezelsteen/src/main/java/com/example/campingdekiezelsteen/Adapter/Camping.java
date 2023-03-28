package com.example.campingdekiezelsteen.Adapter;

import com.example.campingdekiezelsteen.Adapter.Blueprint;
import com.example.campingdekiezelsteen.OrderBook;

public class Camping {
    private Blueprint blueprint;
    private OrderBook orderBook = new OrderBook();

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

    public OrderBook getOrderBook() {
        return orderBook;
    }

    public void setOrderBook(OrderBook orderBook) {
        this.orderBook = orderBook;
    }
}

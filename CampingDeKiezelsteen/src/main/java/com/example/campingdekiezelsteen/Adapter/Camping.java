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
        addSpots();
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

    private void addSpots() {
        Map<Integer, Spot> spotsList = new HashMap<>();
//        TODO: JSON File hierop toepassen. Nu is het nog hardcoded.
        BuildingSpot buildingSpot = new BuildingSpot();
        buildingSpot.setState(new UnderMaintenance());
        buildingSpot.createPlaceable(new House("Vakantiehuis 1"));
        BuildingSpot sanitair = new BuildingSpot();
        sanitair.setState(new UnderMaintenance());
        sanitair.createPlaceable(new Sanitair("Sanitair 1"));
        BuildingSpot laundry = new BuildingSpot();
        laundry.setState(new Reserved());
        laundry.createPlaceable(new Laundry("Laundry 1"));
        BuildingSpot tiki = new BuildingSpot();
        tiki.setState(new UnderMaintenance());
        tiki.createPlaceable(new TikiTent("Tiki-Tent 1"));
        for (int i = 0; i < 60; i++) {
            switch (i + 1) {
//                TODO: Nu nog standaard een bringable spot, kan uiteraard ook een buildingspot zijn. Moet ook uit de JSON komen.
                case 2 -> {
                    buildingSpot.setSpotNr(i + 1);
                    spotsList.put(i + 1, buildingSpot);
                }
                case 12 -> {
                    sanitair.setSpotNr(i + 1);
                    spotsList.put(i + 1, sanitair);
                }
                case 15 -> {
                    laundry.setSpotNr(i + 1);
                    spotsList.put(i + 1, laundry);
                }
                case 35 -> {
                    tiki.setSpotNr(i + 1);
                    spotsList.put(i + 1, tiki);
                }
                default -> {
                    Spot spot = new BringableSpot();
                    spot.setSpotNr(i + 1);
                    spotsList.put(i + 1, spot);
                }
            }
        }
        blueprint.setSpots(spotsList);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

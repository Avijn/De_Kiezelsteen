package com.example.campingdekiezelsteen.Adapter;

import com.example.campingdekiezelsteen.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.example.campingdekiezelsteen.State.Reserved;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;

public class BlueprintXMLAdapter {

    public Map<Integer, Spot> createSpotsFromXML (File file) {
        try {
            Map<Integer, Spot> spots = new HashMap<Integer, Spot>();

            file = new File(UserInterface.class.getResource("camping.xml").toURI());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8")
            );
            Document xml = builder.parse(file);
            NodeList spotXML = xml.getElementsByTagName("spot");

            for(int i = 0; i < spotXML.getLength(); i++) {
                int number = i + 1;
                Node spotTypeNode = spotXML.item(i);
                Element element = (Element) spotTypeNode;

                if (element.getElementsByTagName("type").item(0).getTextContent().equals("building")) {
                    spots.put(number, new BuildingSpot());
                    switch(element.getElementsByTagName("placeabletype").item(0).getTextContent()) {
                        case "house" -> {
                            spots.get(number).setPlaceable("house");
                            spots.get(number).setSpotNr(number);
                        }
                        case "tikitent" -> {
                            spots.get(number).setPlaceable("tikitent");
                            spots.get(number).setSpotNr(number);
                        }
                        case "laundry" -> {
                            spots.get(number).setPlaceable("laundry");
                            spots.get(number).setState(new Reserved());
                            spots.get(number).setSpotNr(number);
                        }
                        case "sanitair" -> {
                            spots.get(number).setPlaceable("sanitair");
                            spots.get(number).setState(new Reserved());
                            spots.get(number).setSpotNr(number);
                        }
                    }
                    continue;
                }
                spots.put(number, new BringableSpot());
                spots.get(number).setSpotNr(number);
            }
            return spots;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Reservation> createReservationsFromXML(File file){
        Map<Integer, Reservation> spots = new HashMap<Integer, Reservation>();

        try {
            file = new File(UserInterface.class.getResource(file.getName()).toURI());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8")
            );
            Document xml = builder.parse(file);
            NodeList spotXML = xml.getElementsByTagName("reservations");
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

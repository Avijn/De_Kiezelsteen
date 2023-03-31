package com.example.campingdekiezelsteen.Adapter;

import com.example.campingdekiezelsteen.*;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import com.example.campingdekiezelsteen.State.Reserved;
import com.google.gson.JsonObject;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class BlueprintXMLAdapter {

    public File file;
    private Map<Integer, Spot> spots = new HashMap<>();

    public BlueprintXMLAdapter(File file){
        this.file = file;
    }

    public Map<Integer, Spot> createSpotsFromXML () {
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
            this.spots = spots;
            return spots;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Integer, Reservation> createReservationsFromXML(){

        Map<Integer, Reservation> reservations = new HashMap<Integer, Reservation>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            file = new File(UserInterface.class.getResource(file.getName()).toURI());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8")
            );
            Document xml = builder.parse(file);
            NodeList reservationsXML = xml.getElementsByTagName("reservations");

            for(int i = 0; i < reservationsXML.getLength(); i++) {
                int number = i + 1;
                Node spotTypeNode = reservationsXML.item(i);
                Element element = (Element) spotTypeNode;
                int reservableId = Integer.parseInt(element.getElementsByTagName("reservable").item(0).getTextContent());
                Reservable reservable = getReservable(reservableId);

                String placeableType = element.getElementsByTagName("placeable").item(i).getTextContent();
                Placeable placeable = spots.get(reservableId).createPlaceable(placeableType);
                reservations.put(number,new Reservation(
                        reservable,
                        element.getElementsByTagName("mainbooker").item(i).getTextContent(),
                        LocalDate.parse(element.getElementsByTagName("arrivaldate").item(i).getTextContent(), formatter),
                        LocalDate.parse(element.getElementsByTagName("departuredate").item(i).getTextContent(), formatter),
                        element.getElementsByTagName("id").item(i).getTextContent(),
                        placeable
                ));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private Reservable getReservable(int reservableId) {
        // Get spot from spotslist where id equals reservable id.
        Spot spot = spots.get(reservableId);
        if (spot.getClass().isAssignableFrom(BuildingSpot.class)) {
            // If spot is buildingspot then reservable should be its placeable
            if (spot.getPlaceable() instanceof Reservable) {
                return (Reservable) spot.getPlaceable();
            } else {
                System.out.println("The placeable of this spot cannot be reserved.");
                return null;
            }
        }
        // Else spot is bringablespot which makes reservable the spot itself.
        return (Reservable) spot;
    }

    public void addReservationsToXML(Reservation reservation) {
        try {
            file = new File(UserInterface.class.getResource(file.getName()).toURI());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringBuilder xmlStringBuilder = new StringBuilder();
            ByteArrayInputStream input = new ByteArrayInputStream(
                    xmlStringBuilder.toString().getBytes("UTF-8")
            );
            Document xml = builder.parse(file);
            Element reservationsXML = xml.getElementById("reservations");

            String reservable = "0";
            String placeable = "null";


            System.out.println(reservation.getCustomerName());

            for (Map.Entry<Integer, Spot> set: spots.entrySet())
            {
                if(set.getValue().getPlaceable() == reservation.getReservable())
                {
                    System.out.println(set.getKey());
                    reservable = set.getKey().toString();
                    placeable = set.getValue().getPlaceable().getStyle();
                }
            }
            System.out.println(xml.getElementById("spots"));
            //Element rootElement = xml.getElementById("");
            Element reservationElement = xml.createElement("reservation");
            Element idElement = xml.createElement("id");
            Element mainbookerElement = xml.createElement("mainbooker");
            Element arrivaldateElement = xml.createElement("arrivaldate");
            Element departuredateElement = xml.createElement("departuredate");
            Element reservableElement = xml.createElement("reservable");
            Element placeableElement = xml.createElement("placeable");

            idElement.setTextContent(reservation.getId());
            mainbookerElement.setTextContent(reservation.getCustomerName());
            arrivaldateElement.setTextContent(reservation.getArrivaldate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            departuredateElement.setTextContent(reservation.getDeparturedate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            reservableElement.setTextContent(reservable);
            placeableElement.setTextContent(placeable);

            reservationElement.appendChild(idElement);
            reservationElement.appendChild(mainbookerElement);
            reservationElement.appendChild(arrivaldateElement);
            reservationElement.appendChild(departuredateElement);
            reservationElement.appendChild(reservableElement);
            reservationElement.appendChild(placeableElement);
            //rootElement.appendChild(reservationElement);
            xml.appendChild(reservationElement);

            try (FileOutputStream output =
                         new FileOutputStream(file.getPath())) {
                writeXml(xml, output);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void writeXml(Document doc,
                                 OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }
}

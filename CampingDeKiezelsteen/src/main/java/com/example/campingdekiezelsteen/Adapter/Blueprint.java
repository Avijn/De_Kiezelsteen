package com.example.campingdekiezelsteen.Adapter;

import com.example.campingdekiezelsteen.*;
import com.example.campingdekiezelsteen.State.Reserved;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Blueprint {
    private String background;
    private Map<Integer, Spot> spots;
    private Map<Integer, Reservation> reservations;
    private File file = new File("");

    public Blueprint(String background) {
        this.background = background;
        this.spots = new HashMap<>();
        this.reservations = new HashMap<>();
        try {
            this.file = new File(UserInterface.class.getResource("camping.json").toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        createSpotsAndGetReservations();
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public Map<Integer, Spot> getSpots() {
        return spots;
    }

    public void setSpots(Map<Integer, Spot> spots) {
        this.spots = spots;
    }

    public Map<Integer, Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Map<Integer, Reservation> reservations) {
        this.reservations = reservations;
    }

    public File getFile() {
        return file;
    }

    /**
     * <summary>
     * Gets the based on the given filePath.
     * </summary>
     *
     * @param filePath the path to the "blueprint" file
     */
    public void setFile(String filePath) {
        try {
            file = new File(filePath);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        return fileName.split("\\.")[1].toLowerCase();
    }

    /**
     * <summary>
     * Checks if the uploaded file is either <b>JSON</b> or <b>XML</b> <br />
     * <b>JSON</b>: Executes the <b>createsSpotsFromJson()</b> method;<br />
     * <b>XML</b> : Creates the adapter class and executes the <b>createSpotsFromXML</b> method;<br />
     * <b>Neither</b> : Does nothing except for a SOUT("File is not a xml or json file.");
     * </summary>
     */
    public void createSpotsAndGetReservations() {
        if (file.isFile()) {
            switch (getFileExtension(file)) {
                case "json" -> {
                    createSpotsFromJson();
                    createReservationsFromJson();
                }
                case "xml" -> {
                    BlueprintXMLAdapter xmlAdapter = new BlueprintXMLAdapter();
                    xmlAdapter.createSpotsFromXML(file);
                }
                default -> System.out.println("File is not a xml or json file.");
            }
        }
    }

    //Todo : Its not possible to parse the LocalDate for some reason, its accessor in the library is private final
    // We should probably change this to some other datatype for date
    public void createReservationsFromJson() {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = null;

        try {
            JsonObject o = (JsonObject) parser.parse(new FileReader(file));
            jsonArray = o.get("reservations").getAsJsonArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        assert jsonArray != null;
        for (JsonElement element : jsonArray) {
            JsonObject elementObject = element.getAsJsonObject();
            // Formatter for date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            int id = elementObject.get("id").getAsInt();

            // Parse strings to localdate
            LocalDate arrivaldate = LocalDate.parse(elementObject.get("arrivaldate").getAsString(), formatter);
            LocalDate departuredate = LocalDate.parse(elementObject.get("departuredate").getAsString(), formatter);

            int reservableId = elementObject.get("reservable").getAsInt();
            Reservable reservable = getReservable(reservableId);
            Placeable placeable = getPlaceable(elementObject);

            JsonObject mainbooker = elementObject.get("mainbooker").getAsJsonObject();
            String name = mainbooker.get("firstname").getAsString() + " " + mainbooker.get("lastname").getAsString();

            Reservation reservation = new Reservation(reservable, name, arrivaldate, departuredate, String.valueOf(id), placeable);

            reservations.put(id, reservation);
        }
    }

    private Placeable getPlaceable(JsonObject object){
       if(object.has("placeable")) {
           String placeableType = object.get("placeable").getAsString();
           return spots.get(object.get("reservable").getAsInt()).createPlaceable(placeableType);
       }
       return null;
    }

    private Reservable getReservable(int reservableId) {
        Spot spot = spots.get(reservableId);
        if (spot.getClass().isAssignableFrom(BuildingSpot.class)) {
            if (spot.getPlaceable() instanceof Reservable) {
                return (Reservable) spot.getPlaceable();
            } else {
                System.out.println("The placeable of this spot cannot be reserved.");
                return null;
            }
        }
        return (Reservable) spot;
    }

    /**
     * <summary>
     * Creates objects out of the JsonObjects given in the file
     * and put these into the spots (Hash)Map.
     * </summary>
     */
    public void createSpotsFromJson() {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = null;

        try {
            JsonObject o = (JsonObject) parser.parse(new FileReader(file));
            jsonArray = o.get("spots").getAsJsonArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert jsonArray != null;
        for (JsonElement element : jsonArray) {
            JsonObject elementObject = element.getAsJsonObject();
            int id = elementObject.get("id").getAsInt();
            switch (elementObject.get("type").getAsString()) {
                case "bringable" -> {
                    spots.put(id, new BringableSpot());
                    spots.get(id).setSpotNr(id);
                }
                case "building" -> {
                    spots.put(id, new BuildingSpot());
                    JsonObject placeable = (JsonObject) elementObject.get("placeable");

                    // Creates and sets placeable for spot
                    String type = placeable.get("type").getAsString();
                    spots.get(id).setPlaceable(type);
                    // If type is laundry or sanitair state cannot be free
                    if ("laundry".equals(type) || "sanitair".equals(type)) {
                        spots.get(id).setState(new Reserved());
                    }

                    // Sets placeable name
                    String placeableName = placeable.get("name").getAsString();
                    spots.get(id).getPlaceable().setName(placeableName);
                }
                default -> {
                }
            }
        }
    }
}
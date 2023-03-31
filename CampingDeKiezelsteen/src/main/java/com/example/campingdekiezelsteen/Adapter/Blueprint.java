package com.example.campingdekiezelsteen.Adapter;

import com.example.campingdekiezelsteen.*;
import com.example.campingdekiezelsteen.State.Reserved;
import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarOutputStream;

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
                    BlueprintXMLAdapter xmlAdapter = new BlueprintXMLAdapter(file);
                    spots = xmlAdapter.createSpotsFromXML();
                    reservations = xmlAdapter.createReservationsFromXML();
                    xmlAdapter.addReservationsToXML(reservations.get(1));
                    break;

                }
                default -> System.out.println("File is not a xml or json file.");
            }
        }
    }

    public void createReservationsFromJson() {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = null;

        // Try to parse file to json array.
        try {
            JsonObject o = (JsonObject) parser.parse(new FileReader(file));
            jsonArray = o.get("reservations").getAsJsonArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        assert jsonArray != null;
        // Loop through all reservations in json file if json array is not null.
        for (JsonElement element : jsonArray) {
            JsonObject elementObject = element.getAsJsonObject();
            // Formatter for date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            int id = elementObject.get("id").getAsInt();

            // Parse strings to localdate
            LocalDate arrivaldate = LocalDate.parse(elementObject.get("arrivaldate").getAsString(), formatter);
            LocalDate departuredate = LocalDate.parse(elementObject.get("departuredate").getAsString(), formatter);

            // Get reservable
            int reservableId = elementObject.get("reservable").getAsInt();
            Reservable reservable = getReservable(reservableId);
            // Get placeable
            Placeable placeable = getPlaceable(elementObject);

            // Get name of main booker
            String name = elementObject.get("mainbooker").getAsString();

            // Create reservation
            Reservation reservation = new Reservation(reservable, name, arrivaldate, departuredate, String.valueOf(id), placeable);

            // Add reservation to list
            reservations.put(id, reservation);
        }
    }

    private Placeable getPlaceable(JsonObject object){
       if(object.has("placeable")) {
           // If object has a placeable then create placeable for the given placeable type.
           String placeableType = object.get("placeable").getAsString();
           return spots.get(object.get("reservable").getAsInt()).createPlaceable(placeableType);
       }
       // Else return null
       return null;
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

    /**
     * <summary>
     * Creates objects out of the JsonObjects given in the file
     * and put these into the spots (Hash)Map.
     * </summary>
     */
    public void createSpotsFromJson() {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = null;

        // Try to parse file to json array.
        try {
            JsonObject o = (JsonObject) parser.parse(new FileReader(file));
            jsonArray = o.get("spots").getAsJsonArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert jsonArray != null;
        // Loop through all reservations in json file if json array is not null.
        for (JsonElement element : jsonArray) {
            JsonObject elementObject = element.getAsJsonObject();
            // Get id of spot
            int id = elementObject.get("id").getAsInt();
            // Get type of spot
            switch (elementObject.get("type").getAsString()) {
                case "bringable" -> {
                    // If type is bringable -> create bringablespot and set spotnr to id.
                    spots.put(id, new BringableSpot());
                    spots.get(id).setSpotNr(id);
                }
                case "building" -> {
                    // If type is building -> create buildingspot
                    spots.put(id, new BuildingSpot());

                    // Creates and sets placeable for spot
                    JsonObject placeable = (JsonObject) elementObject.get("placeable");
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

    /**
     * Adds a reservation to the JSON file
     * @param reservation the reservation that needs to be added to the JSON file
     */
    public void addReservationToJson(Reservation reservation){
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = null;
        JsonObject jsonObject = null;
        String reservable = "";
        String placeable = "";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Try to parse file to json array.
        try {
            jsonObject = (JsonObject) parser.parse(new FileReader(file));
            jsonArray = jsonObject.get("reservations").getAsJsonArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assert jsonArray != null;

        for (Map.Entry<Integer, Spot> set: spots.entrySet())
        {
            if(set.getValue().getPlaceable() == reservation.getReservable())
            {
                reservable = set.getKey().toString();
                placeable = set.getValue().getPlaceable().getStyle();
            }
            if (set.getValue() == reservation.getReservable()){
                reservable = set.getKey().toString();
                placeable = reservation.getPlaceable().getType();
            }
        }

        String json = "{ \"id\" : \""+ reservation.getId().toString() +
                "\" , \"mainbooker\" : \""+ reservation.getCustomerName() +
                "\", \"arrivaldate\" : \""+ reservation.getArrivaldate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                "\" , \"departuredate\" : \""+ reservation.getDeparturedate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                "\" , \"reservable\" : \"" + reservable +
                "\" , \"placeable\" : \""+ placeable + "\" }";

        try(FileWriter writer = new FileWriter(file.getPath()))
        {
            JsonObject jsonReservation = new Gson().fromJson(json, JsonObject.class);
            jsonArray.add(jsonReservation);
            jsonObject.add("reservations", jsonArray);

            gson.toJson(jsonObject, writer);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
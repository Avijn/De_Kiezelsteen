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
import java.util.HashMap;
import java.util.Map;

public class Blueprint {
    private String background;
    private Map<Integer, Spot> spots;
    private File file = new File("");

    public Blueprint(String background) {
        this.background = background;
        this.spots = new HashMap<Integer, Spot>();
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

    private String getFileExtension(File file){
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

    public void createReservationsFromJson(){
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = null;

        try {
            JsonObject o = (JsonObject) parser.parse(new FileReader(file));
            jsonArray = o.get("reservations").getAsJsonArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int counter = 1;

        assert jsonArray != null;
        for (JsonElement element : jsonArray) {
            JsonObject elementObject = element.getAsJsonObject();

            counter++;
        }
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

        int counter = 1;

        assert jsonArray != null;
        for (JsonElement element : jsonArray) {
            JsonObject elementObject = element.getAsJsonObject();
            switch (elementObject.get("type").getAsString()) {
                case "bringable" -> {
                    spots.put(counter, new BringableSpot());
                    spots.get(counter).setSpotNr(counter);
                }
                case "building" -> {
                    spots.put(counter, new BuildingSpot());
                    JsonObject placeable = (JsonObject) elementObject.get("placeable");
                    spots.get(counter).setPlaceable(placeable.get("type").getAsString());
                    String type = placeable.get("type").getAsString();
                    if ("laundry".equals(type) || "sanitair".equals(type)) {
                        spots.get(counter).setState(new Reserved());
                    }
                }
                default -> {
                }
            }
            counter++;
        }
    }
}
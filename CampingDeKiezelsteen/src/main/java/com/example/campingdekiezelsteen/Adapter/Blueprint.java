package com.example.campingdekiezelsteen.Adapter;

import com.example.campingdekiezelsteen.Spot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.*;

public class Blueprint {
    private String background;
    //Todo This is used for testing purposes since Spot is not done yet, needs to be removed!
    //private Map<Integer, Spot> spots;
    private Map<Integer, Camping> spots;
    private File file;

    public Blueprint(String background)
    {
        this.background = background;
        //Todo This is used for testing purposes since Spot is not done yet, needs to be removed!
        this.spots = new HashMap<Integer, Camping>();
        //this.spots = new HashMap<interger, Spot>()
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    //Todo needs to be uncommented when Spot is done!
//    public Map<Integer, Spot> getSpots() {
//        return spots;
//    }
//
//    public void setSpots(Map<Integer, Spot> spots) {
//        this.spots = spots;
//    }

    public File getFile()
    {
        return file;
    }

    /**
     * <summary>
     *     Gets the based on the given filePath.
     * </summary>
     * @param filePath the path to the "blueprint" file
     */
    public void setFile(String filePath) {
        try {
            file = new File(filePath);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * <summary>
     *     Checks if the uploaded file is either <b>JSON</b> or <b>XML</b> <br />
     *     <b>JSON</b>: Executes the <b>createsSpotsFromJson()</b> method;<br />
     *     <b>XML</b> : Creates the adapter class and executes the <b>createSpotsFromXML</b> method;<br />
     *     <b>Neither</b> : Does nothing except for a SOUT("File is not a xml or json file.");
     * </summary>
     */
    public void createSpots() {
        if(file.isFile())
        {
            String fileName = file.getName();
            String extension = fileName.split(".", fileName.length()).toString().toLowerCase();
            switch(extension) {
                case "json":
                    createSpotsFromJson();
                    break;
                case "xml":
                    BlueprintXMLAdapter xmlAdapter = new BlueprintXMLAdapter();
                    xmlAdapter.createSpotsFromXML(file);
                    break;
                default:
                    System.out.println("File is not a xml or json file.");
                    break;
            }
        }
    }

    /**
     * <summary>
     * Creates objects out of the JsonObjects given in the file
     * and put these into the spots (Hash)Map.
     * </summary>
     */
    public void createSpotsFromJson() {
        String string = "[{\"name\":1}, {\"name\": 2}]";
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(string).getAsJsonArray();

        int counter = 1;
        for (JsonElement element: jsonArray) {
            JsonObject elementObject = element.getAsJsonObject();
            //Todo implement object when Spot is done {new Spot()}
            spots.put(counter,  new Camping(
                    elementObject.get("name").toString()
            ));
            counter++;
        }
    }
}
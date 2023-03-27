package com.example.campingdekiezelsteen.Adapter;

import com.example.campingdekiezelsteen.Spot;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;

public class Blueprint {
    private String background;
    private Map<Integer, Spot> spots;

    public Blueprint(String background)
    {
        this.background = background;
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

    public File getFile(String filePath)
    {
        try{
            File file = new File(filePath);
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()){
                String data = reader.nextLine();
                System.out.println(data);
            }
            reader.close();
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }


}

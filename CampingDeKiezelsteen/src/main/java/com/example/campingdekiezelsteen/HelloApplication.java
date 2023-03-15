package com.example.campingdekiezelsteen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    Color white = Color.rgb(255,255,255);
    Color lightGray = Color.rgb(245, 245, 245);
    Color black = Color.rgb(0,0,0);

    int amountOfSpots = 60;

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(setupDesign(), 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    private HBox setupDesign(){
        HBox hBox = new HBox();
        hBox.setFillHeight(true);
        hBox.setBackground(new Background(new BackgroundFill(white, null, null)));
        hBox.getChildren().add(setupGrid());
        return hBox;
    }

    private GridPane setupGrid(){
        GridPane gridPane = new GridPane();
        gridPane.setBackground(new Background(new BackgroundFill(lightGray, null, null)));
        gridPane.setPrefWidth(700);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        // Gets square root of amount of spots to divide in even rows and columns.
        double square = Math.sqrt(amountOfSpots);
        // current is used to keep track of the amount of buttons that have been created in the loops.
        int current = 1;
        // Loop for rows.
        for(int y = 0; y < square; y++) {
            // Loop for columns.
            for (int x = 0; x < square; x++) {
                // Checks if current is not greater than the amount of spots.
                if (current <= amountOfSpots) {
                    gridPane.add(addButton(current), x, y);
                    current++;
                }
            }
        }
        return gridPane;
    }

    private Button addButton(int spot){
        Button button = new Button(String.valueOf(spot));
        button.setPrefSize(200, 200);
        return button;
    }

    public static void main(String[] args) {
        launch();
    }
}
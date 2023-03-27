package com.example.campingdekiezelsteen.State;

import com.example.campingdekiezelsteen.Spot;
import javafx.scene.layout.VBox;

public interface State {
    VBox buttonClicked(Spot spot);
    String getColor();
}

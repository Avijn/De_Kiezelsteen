module com.example.campingdekiezelsteen {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.campingdekiezelsteen to javafx.fxml;
    exports com.example.campingdekiezelsteen;
    exports com.example.campingdekiezelsteen.State;
    opens com.example.campingdekiezelsteen.State to javafx.fxml;
}
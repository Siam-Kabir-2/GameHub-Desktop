module org.example.gamehubd {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphicsEmpty;


    opens com.gamehub to javafx.fxml;
    exports com.gamehub;
}
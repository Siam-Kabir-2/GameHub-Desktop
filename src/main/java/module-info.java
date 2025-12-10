module org.example.gamehubd {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.gamehubd to javafx.fxml;
    exports org.example.gamehubd;
}
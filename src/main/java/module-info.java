module org.example.gamehubd {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires io.github.cdimascio.dotenv.java;

    opens com.gamehub to javafx.fxml;

    exports com.gamehub;
}
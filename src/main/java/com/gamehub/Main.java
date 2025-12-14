package com.gamehub;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Initialize database tables (creates users and highscores tables if they don't
        // exist)
        DatabaseManager.initializeDatabase();

        // Set the primary stage in SceneManager for easy scene switching
        SceneManager.setStage(primaryStage);

        // Load the Login screen as the starting point
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gamehub/LoginView.fxml"));
        Parent root = loader.load();

        Scene loginScene = new Scene(root);

        primaryStage.setTitle("GameHub - Login");
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false); // Optional: prevent window resizing
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
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
        DatabaseManager.initializeDatabase();
        SceneManager.setStage(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gamehub/LoginView.fxml"));
        Parent root = loader.load();

        Scene loginScene = new Scene(root);

        primaryStage.setTitle("GameHub - Login");
        primaryStage.setWidth(900);
        primaryStage.setHeight(600);
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(true);
        primaryStage.setMaximized(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
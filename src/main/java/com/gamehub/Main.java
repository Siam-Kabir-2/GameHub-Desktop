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
        SceneManager.setStage(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/gamehub/HomeView.fxml"));
        Parent root = loader.load();

        Scene homeScene = new Scene(root);
        SceneManager.setHomeScene(homeScene);

        primaryStage.setTitle("GameHub - Java Lab");
        primaryStage.setScene(homeScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
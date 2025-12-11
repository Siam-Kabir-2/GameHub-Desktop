package com.gamehub;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    private static Stage primaryStage;
    private static Scene homeScene;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static void setHomeScene(Scene scene) {
        homeScene = scene;
    }

    public static void goHome() {
        if (homeScene != null) {
            primaryStage.setScene(homeScene);
        }
    }

    public static void switchTo(Scene gameScene) {
        primaryStage.setScene(gameScene);
    }
}
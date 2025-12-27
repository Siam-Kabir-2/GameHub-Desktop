package com.gamehub;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class NavController {

    public void onBackClick(){
        SceneManager.goHome();
    }

    private static NavController instance;

    @FXML
    private Label gameName;
    @FXML
    private Label scoreBox;

    @FXML
    public void initialize() {
        instance = this;
    }

    public static void setGameName(String name) {
        if (instance != null && instance.gameName != null) {
            instance.gameName.setText(name);
        }
    }
    public static void setScore(String score) {
        if (instance != null && instance.scoreBox != null) {
            instance.scoreBox.setText(score);
        }
    }
}

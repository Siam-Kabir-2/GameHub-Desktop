package com.gamehub;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public abstract class GameBase {
    protected BorderPane root;

    public GameBase(String gameTitle) {
        root = new BorderPane();
        root.setPrefSize(800, 600);
        HBox header = new HBox(20);
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle("-fx-background-color: #DDDDDD;");
        Button btnBack = new Button("⬅ Back to Menu");
        btnBack.setStyle("-fx-font-size: 14px; -fx-cursor: hand;");
        btnBack.setOnAction(e -> SceneManager.goHome());
        Label lblTitle = new Label(gameTitle);
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        header.getChildren().addAll(btnBack, lblTitle);
        root.setTop(header);

        root.setCenter(createGameContent());
    }

    public abstract Node createGameContent();

    public Scene getScene() {
        return new Scene(root);
    }
}
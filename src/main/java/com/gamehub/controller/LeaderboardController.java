package com.gamehub.controller;

import com.gamehub.DatabaseManager;
import com.gamehub.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {

    @FXML private ComboBox<String> gameSelector;
    @FXML private VBox leaderboardContainer;

    private final String[] GAMES = {"TicTacToe", "Snake", "Guess", "RPS", "Memory"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameSelector.getItems().addAll(GAMES);
        gameSelector.setValue(GAMES[0]);
        loadLeaderboard(GAMES[0]);
        NavController.setGameName("LEADERBOARD");
    }

    @FXML
    private void onGameSelected() {
        String selected = gameSelector.getValue();
        if (selected != null) {
            loadLeaderboard(selected);
        }
    }

    private void loadLeaderboard(String gameName) {
        leaderboardContainer.getChildren().removeIf(node -> node != leaderboardContainer.getChildren().get(0));
        
        String[][] data = DatabaseManager.getLeaderboard(gameName, 20);
        
        if (data.length == 0) {
            Label noData = new Label("No scores yet for " + gameName);
            noData.setTextFill(javafx.scene.paint.Color.web("#888888"));
            noData.setFont(new Font("Verdana", 16));
            leaderboardContainer.getChildren().add(noData);
            return;
        }
        
        for (int i = 0; i < data.length; i++) {
            HBox row = createRow(i + 1, data[i][0], data[i][1]);
            leaderboardContainer.getChildren().add(row);
        }
    }

    private HBox createRow(int rank, String username, String score) {
        HBox row = new HBox(20);
        row.setAlignment(Pos.CENTER);
        row.setStyle("-fx-background-color: " + (rank % 2 == 0 ? "#2A2A3A" : "#252535") + "; -fx-padding: 12; -fx-background-radius: 5;");
        
        String rankColor = switch(rank) {
            case 1 -> "#FFD700";
            case 2 -> "#C0C0C0";
            case 3 -> "#CD7F32";
            default -> "#FFFFFF";
        };
        
        String rankEmoji = switch(rank) {
            case 1 -> "🥇";
            case 2 -> "🥈";
            case 3 -> "🥉";
            default -> String.valueOf(rank);
        };
        
        Label rankLabel = new Label(rankEmoji);
        rankLabel.setPrefWidth(80);
        rankLabel.setAlignment(Pos.CENTER);
        rankLabel.setTextFill(javafx.scene.paint.Color.web(rankColor));
        rankLabel.setFont(new Font("Verdana Bold", 18));
        
        Label nameLabel = new Label(username);
        nameLabel.setPrefWidth(200);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTextFill(javafx.scene.paint.Color.WHITE);
        nameLabel.setFont(new Font("Verdana", 16));
        
        Label scoreLabel = new Label(score);
        scoreLabel.setPrefWidth(120);
        scoreLabel.setAlignment(Pos.CENTER);
        scoreLabel.setTextFill(javafx.scene.paint.Color.web("#00FF88"));
        scoreLabel.setFont(new Font("Verdana Bold", 18));
        
        row.getChildren().addAll(rankLabel, nameLabel, scoreLabel);
        return row;
    }

//    @FXML
//    private void onBackClick() {
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/HomeView.fxml"));
//            SceneManager.switchTo(new Scene(root));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}

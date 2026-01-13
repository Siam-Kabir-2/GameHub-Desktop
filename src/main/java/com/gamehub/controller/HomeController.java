package com.gamehub.controller;

import com.gamehub.SceneManager;
import com.gamehub.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label usernameLabel2;
    @FXML
    private VBox userDropdown;

    @FXML
    public void initialize() {
        if (UserSession.getInstance().isLoggedIn()) {
            String username = UserSession.getInstance().getUsername();
            usernameLabel.setText(username);
            usernameLabel2.setText(username);
            System.out.println("Welcome, " + username + "!");
        }

        userDropdown.setVisible(false);
        userDropdown.setManaged(false);
    }

    @FXML
    public void onUserClick() {
        boolean isVisible = userDropdown.isVisible();
        userDropdown.setVisible(!isVisible);
        userDropdown.setManaged(!isVisible);
    }

    @FXML
    public void onLogoutClick() {
        System.out.println("Logging out user: " + UserSession.getInstance().getUsername());

        UserSession.getInstance().logout();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/LoginView.fxml"));
            Stage stage = (Stage) usernameLabel.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GameHub - Login");
        } catch (Exception e) {
            System.err.println("Error returning to login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onLeaderboardClick() {
        System.out.println("Opening Leaderboard...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/LeaderboardView.fxml"));
            Scene scene = new Scene(root);
            SceneManager.switchTo(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onTicTacToeClick() {
        System.out.println("Launching Tic Tac Toe...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/game/TicTacToeView.fxml"));
            Scene guessScene = new Scene(root);
            SceneManager.switchTo(guessScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onSnakeClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText("Snake Game");
        alert.setContentText("This game is coming soon! Stay tuned.");
        alert.showAndWait();
    }

    @FXML
    public void onGuessClick() {
        System.out.println("Launching Guess...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/game/GuessView.fxml"));
            Scene guessScene = new Scene(root);
            SceneManager.switchTo(guessScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onRpsClick() {
        System.out.println("Launching Rock-paper-Scissor...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/game/RpsView.fxml"));
            Scene guessScene = new Scene(root);
            SceneManager.switchTo(guessScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onMemoryClick() {
        System.out.println("Launching Memory Game...");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/gamehub/game/MemoryView.fxml"));
            Scene guessScene = new Scene(root);
            SceneManager.switchTo(guessScene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void on2048Click() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText("2048 Game");
        alert.setContentText("This game is coming soon! Stay tuned.");
        alert.showAndWait();
    }
}
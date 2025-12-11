package com.gamehub;

import javafx.fxml.FXML;

public class HomeController {

    @FXML
    public void onUserClick() {
        System.out.println("Opening User...");
    }

    @FXML
    public void onSettingsClick() {
        System.out.println("Opening Settings...");
    }

    @FXML
    public void onTicTacToeClick() {
        System.out.println("Launching Tic Tac Toe...");
        // SceneManager.switchTo(new TicTacToeGame().getScene());
    }

    @FXML
    public void onSnakeClick() {
        System.out.println("Launching Snake...");
        // SceneManager.switchTo(new SnakeGame().getScene());
    }

    @FXML
    public void onGuessClick() {
        System.out.println("Launching Guess...");
        // SceneManager.switchTo(new GuessGame().getScene());
    }

    @FXML
    public void onRpsClick() {
        System.out.println("Launching Rock-paper-Scissor...");
        // SceneManager.switchTo(new RpsGame().getScene());
    }

    @FXML
    public void onMemoryClick() {
        System.out.println("Launching Memory Game...");
        // SceneManager.switchTo(new MemoryGame().getScene());
    }

    @FXML
    public void on2048Click() {
        System.out.println("Launching 2048...");
        // SceneManager.switchTo(new 2048Game().getScene());
    }
}
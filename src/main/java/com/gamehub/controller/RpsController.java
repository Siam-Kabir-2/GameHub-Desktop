package com.gamehub.controller;

import com.gamehub.DatabaseManager;
import com.gamehub.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class RpsController implements Initializable {

    @FXML
    private Button rockBtn;
    @FXML
    private Button paperBtn;
    @FXML
    private Button scissorsBtn;
    @FXML
    private Button restartBtn;
    @FXML
    private Label resultLabel;
    @FXML
    private Label playerScoreLabel;
    @FXML
    private Label computerScoreLabel;
    @FXML
    private Label turnsLabel;

    private int playerScore = 0;
    private int computerScore = 0;
    private int turnsRemaining = 10;
    private static final int MAX_TURNS = 10;
    private Random random = new Random();
    private static final String[] CHOICES = { "Rock", "Paper", "Scissors" };
    private static final String[] EMOJIS = { "✊", "✋", "✌" };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavController.setGameName("RPS");
        updateTurnsDisplay();
    }

    @FXML
    private void onRockClick() {
        playRound(0);
    }

    @FXML
    private void onPaperClick() {
        playRound(1);
    }

    @FXML
    private void onScissorsClick() {
        playRound(2);
    }

    private void playRound(int playerChoice) {
        if (turnsRemaining <= 0) {
            return;
        }

        int computerChoice = random.nextInt(3);

        String result = determineWinner(playerChoice, computerChoice);

        String playerMove = EMOJIS[playerChoice] + " " + CHOICES[playerChoice];
        String computerMove = EMOJIS[computerChoice] + " " + CHOICES[computerChoice];

        if (result.equals("WIN")) {
            playerScore += 10;
            turnsRemaining--;
            resultLabel.setText("You: " + playerMove + " | " + computerMove + ": Computer | YOU WIN!");
        } else if (result.equals("LOSE")) {
            computerScore += 10;
            turnsRemaining--;
            resultLabel.setText("You: " + playerMove + " | " + computerMove + ": Computer | YOU LOSE!");
        } else {
            resultLabel.setText("You: " + playerMove + " | " + computerMove + ": Computer | DRAW!");
        }

        updateScoreDisplay();
        updateTurnsDisplay();

        if (turnsRemaining <= 0) {
            endGame();
        }
    }

    private String determineWinner(int player, int computer) {
        if (player == computer)
            return "DRAW";

        if ((player == 0 && computer == 2) ||
                (player == 1 && computer == 0) ||
                (player == 2 && computer == 1)) {
            return "WIN";
        }

        return "LOSE";
    }

    private void updateScoreDisplay() {
        playerScoreLabel.setText(String.valueOf(playerScore));
        computerScoreLabel.setText(String.valueOf(computerScore));
    }

    private void updateTurnsDisplay() {
        turnsLabel.setText("Turns Remaining: " + turnsRemaining);
    }

    private void endGame() {
        rockBtn.setDisable(true);
        paperBtn.setDisable(true);
        scissorsBtn.setDisable(true);

        String finalResult;
        if (playerScore > computerScore) {
            finalResult = "🎉 YOU WON THE GAME! 🎉";
            NavController.setScore(String.valueOf(playerScore - computerScore));
        } else if (computerScore > playerScore) {
            finalResult = "💔 YOU LOST THE GAME! 💔";
        } else {
            finalResult = "🤝 IT'S A TIE! 🤝";
        }

        resultLabel.setText(finalResult);

        if ((playerScore - computerScore) > 0) {
            DatabaseManager.saveScore(UserSession.getInstance().getUserId(), "RPS", (playerScore - computerScore));
        }
    }

    @FXML
    private void onRestart() {
        if ((playerScore - computerScore) > 0 && turnsRemaining == 0) {
            DatabaseManager.saveScore(UserSession.getInstance().getUserId(), "RPS", (playerScore - computerScore));
        }

        playerScore = 0;
        computerScore = 0;
        turnsRemaining = MAX_TURNS;

        rockBtn.setDisable(false);
        paperBtn.setDisable(false);
        scissorsBtn.setDisable(false);

        updateScoreDisplay();
        updateTurnsDisplay();
        resultLabel.setText("Choose your move!");
    }
}

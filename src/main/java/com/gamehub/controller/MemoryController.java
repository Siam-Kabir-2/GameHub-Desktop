package com.gamehub.controller;

import com.gamehub.DatabaseManager;
import com.gamehub.UserSession;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class MemoryController implements Initializable {

    @FXML
    private Button greenBtn, redBtn, yellowBtn, blueBtn, startBtn;
    @FXML
    private Label statusLabel, scoreLabel;

    private List<Integer> sequence = new ArrayList<>();
    private List<Integer> userInput = new ArrayList<>();
    private int score = 0;
    private boolean canClick = false;
    private Random random = new Random();

    private static final int GREEN = 0;
    private static final int RED = 1;
    private static final int YELLOW = 2;
    private static final int BLUE = 3;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavController.setGameName("Memory");
        disableColorButtons();
    }

    @FXML
    private void onStart() {
        if (score > 0) {
            DatabaseManager.saveScore(UserSession.getInstance().getUserId(), "Memory", score);
        }

        sequence.clear();
        userInput.clear();
        score = 0;
        updateScore();
        startBtn.setDisable(true);
        nextRound();
    }

    private void nextRound() {
        canClick = false;
        userInput.clear();

        int nextColor = random.nextInt(4);
        sequence.add(nextColor);

        statusLabel.setText("Watch the pattern!");
        disableColorButtons();

        PauseTransition delay = new PauseTransition(Duration.millis(500));
        delay.setOnFinished(e -> playSequence());
        delay.play();
    }

    private void playSequence() {
        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int color : sequence) {
            PauseTransition flash = new PauseTransition(Duration.millis(100));
            flash.setOnFinished(e -> flashButton(color, true));

            PauseTransition unflash = new PauseTransition(Duration.millis(500));
            unflash.setOnFinished(e -> flashButton(color, false));

            PauseTransition gap = new PauseTransition(Duration.millis(300));

            sequentialTransition.getChildren().addAll(flash, unflash, gap);
        }

        sequentialTransition.setOnFinished(e -> {
            statusLabel.setText("Your turn! Repeat the pattern");
            enableColorButtons();
            canClick = true;
        });

        sequentialTransition.play();
    }

    private void flashButton(int color, boolean flash) {
        Button btn = getButton(color);
        if (flash) {
            btn.setStyle(btn.getStyle() + "-fx-opacity: 1.0; -fx-effect: dropshadow(gaussian, white, 20, 0.7, 0, 0);");
        } else {
            btn.setStyle(getOriginalStyle(color));
        }
    }

    private Button getButton(int color) {
        switch (color) {
            case GREEN:
                return greenBtn;
            case RED:
                return redBtn;
            case YELLOW:
                return yellowBtn;
            case BLUE:
                return blueBtn;
            default:
                return greenBtn;
        }
    }

    private String getOriginalStyle(int color) {
        String baseStyle = "-fx-border-color: black; -fx-border-width: 3px;";
        switch (color) {
            case GREEN:
                return "-fx-background-color: #4CAF50; " + baseStyle;
            case RED:
                return "-fx-background-color: #F44336; " + baseStyle;
            case YELLOW:
                return "-fx-background-color: #FFEB3B; " + baseStyle;
            case BLUE:
                return "-fx-background-color: #2196F3; " + baseStyle;
            default:
                return baseStyle;
        }
    }

    @FXML
    private void onGreenClick() {
        handleColorClick(GREEN);
    }

    @FXML
    private void onRedClick() {
        handleColorClick(RED);
    }

    @FXML
    private void onYellowClick() {
        handleColorClick(YELLOW);
    }

    @FXML
    private void onBlueClick() {
        handleColorClick(BLUE);
    }

    private void handleColorClick(int color) {
        if (!canClick)
            return;

        flashButton(color, true);
        PauseTransition pause = new PauseTransition(Duration.millis(300));
        pause.setOnFinished(e -> flashButton(color, false));
        pause.play();

        userInput.add(color);

        int index = userInput.size() - 1;

        if (!userInput.get(index).equals(sequence.get(index))) {
            gameOver();
            return;
        }

        if (userInput.size() == sequence.size()) {
            score++;
            updateScore();

            PauseTransition delay = new PauseTransition(Duration.millis(800));
            delay.setOnFinished(e -> nextRound());
            delay.play();
        }
    }

    private void gameOver() {
        canClick = false;
        disableColorButtons();
        statusLabel.setText("Game Over! Final Score: " + score);
        startBtn.setDisable(false);

        if (score > 0) {
            DatabaseManager.saveScore(UserSession.getInstance().getUserId(), "Memory", score);
        }
    }

    private void updateScore() {
        scoreLabel.setText(String.valueOf(score));
        NavController.setScore(String.valueOf(score));
    }

    private void enableColorButtons() {
        greenBtn.setDisable(false);
        redBtn.setDisable(false);
        yellowBtn.setDisable(false);
        blueBtn.setDisable(false);
    }

    private void disableColorButtons() {
        greenBtn.setDisable(true);
        redBtn.setDisable(true);
        yellowBtn.setDisable(true);
        blueBtn.setDisable(true);
    }
}

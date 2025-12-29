package com.gamehub.controller;

import com.gamehub.DatabaseManager;
import com.gamehub.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.*;

public class TicTacToeController implements Initializable {

    @FXML
    private Button cell00, cell01, cell02;
    @FXML
    private Button cell10, cell11, cell12;
    @FXML
    private Button cell20, cell21, cell22;
    @FXML
    private Button resetBtn;
    @FXML
    private Label statusLabel;
    @FXML
    private Label scoreLabel;

    private Button[][] cells = new Button[3][3];
    private String[][] board = new String[3][3];
    private boolean gameActive = true;
    private int score = 0;
    private Random random = new Random();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavController.setGameName("TicTacToe");

        cells[0][0] = cell00;
        cells[0][1] = cell01;
        cells[0][2] = cell02;
        cells[1][0] = cell10;
        cells[1][1] = cell11;
        cells[1][2] = cell12;
        cells[2][0] = cell20;
        cells[2][1] = cell21;
        cells[2][2] = cell22;

        resetBoard();
    }

    @FXML
    private void onCellClick(javafx.event.ActionEvent event) {
        if (!gameActive)
            return;

        Button clickedBtn = (Button) event.getSource();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j] == clickedBtn && board[i][j].isEmpty()) {
                    makeMove(i, j, "X");

                    if (gameActive) {
                        Platform.runLater(() -> {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (gameActive) {
                                computerMove();
                            }
                        });
                    }
                    return;
                }
            }
        }
    }

    private void makeMove(int row, int col, String player) {
        board[row][col] = player;
        cells[row][col].setText(player);
        cells[row][col].setDisable(true);

        if (checkWinner(player)) {
            gameActive = false;
            if (player.equals("X")) {
                statusLabel.setText("🎉 YOU WIN! 🎉");
                score += 10;
                scoreLabel.setText(String.valueOf(score));
                DatabaseManager.saveScore(UserSession.getInstance().getUserId(), "TicTacToe", score);
            } else {
                statusLabel.setText("💔 COMPUTER WINS! 💔");
            }
            disableAllCells();
        } else if (isBoardFull()) {
            gameActive = false;
            statusLabel.setText("🤝 IT'S A DRAW! 🤝");
            disableAllCells();
        } else {
            statusLabel.setText(player.equals("X") ? "Computer's turn..." : "Your turn!");
        }
    }

    private void computerMove() {
        List<int[]> emptyCells = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    emptyCells.add(new int[] { i, j });
                }
            }
        }

        if (!emptyCells.isEmpty() && gameActive) {
            int[] move = emptyCells.get(random.nextInt(emptyCells.size()));
            makeMove(move[0], move[1], "O");
        }
    }

    private boolean checkWinner(String player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(player) && board[i][1].equals(player) && board[i][2].equals(player)) {
                return true;
            }
        }

        for (int j = 0; j < 3; j++) {
            if (board[0][j].equals(player) && board[1][j].equals(player) && board[2][j].equals(player)) {
                return true;
            }
        }

        if (board[0][0].equals(player) && board[1][1].equals(player) && board[2][2].equals(player)) {
            return true;
        }
        if (board[0][2].equals(player) && board[1][1].equals(player) && board[2][0].equals(player)) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void disableAllCells() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setDisable(true);
            }
        }
    }

    @FXML
    private void onReset() {
        resetBoard();
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = "";
                cells[i][j].setText("");
                cells[i][j].setDisable(false);
            }
        }

        gameActive = true;
        statusLabel.setText("Your turn! Click a cell to play");
    }
}

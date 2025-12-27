package com.gamehub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GuessController implements Initializable {

    @FXML
    private TextField userGuess;
    @FXML
    private Button guessBtn;
    @FXML
    private Label gameName;
    @FXML
    private Label hintBox;
    @FXML
    private Label attemptRem;


    private Random random=new Random();
    private int targetNum;
    private int guessedNum;
    public int attempt;
    public int total=0;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        generateNumber();
        NavController.setGameName("GUESS");
    }

    public void onClickGuess(){
        guessedNum=Integer.parseInt(userGuess.getText());
        if(guessedNum==targetNum){
            hintBox.setText("Whoaaa o.o ! You Guessed "+guessedNum+" Correctly ! Ready For Another?");
            userGuess.clear();
            generateNumber();
            total+=attempt;
            attemptRem.setText("10");
            NavController.setScore(String.valueOf(total));
            if(total>0) DatabaseManager.saveScore(UserSession.getInstance().getUserId(), "Guess",total);
        }
        else {
            if(guessedNum<targetNum){
                hintBox.setText("Your Guess is too low! Choose Higher.");
            }
            else if(guessedNum>targetNum){
                hintBox.setText("Your Guess is too high! Choose Lower.");
            }
            attempt=Integer.parseInt(attemptRem.getText())-1;
            if (attempt!=0){
                attemptRem.setText(String.valueOf(attempt));
                userGuess.clear();
            }
            else {
                hintBox.setText("Game Over! You Are Out of attempt!");
                userGuess.setDisable(true);
                if(total>0) DatabaseManager.saveScore(UserSession.getInstance().getUserId(), "Guess",total);
            }


        }
    }

    public void onClickRestart(){

        generateNumber();
        hintBox.setText("Guess in a row within remaining attempts to increment Score");
        attemptRem.setText("10");
        userGuess.clear();
        NavController.setScore("00");
        userGuess.setDisable(false);
       if(total>0) DatabaseManager.saveScore(UserSession.getInstance().getUserId(), "Guess",total);
        total=0;

    }

    private void generateNumber(){
        targetNum=random.nextInt(0,101);
    }
}
